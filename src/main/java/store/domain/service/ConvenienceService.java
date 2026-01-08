package store.domain.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.model.Order;
import store.domain.model.Product;
import store.domain.model.ProductPromotion;
import store.domain.model.Promotion;
import store.domain.repository.OrderRepository;
import store.domain.repository.ProductPromotionRepository;
import store.domain.repository.ProductRepository;
import store.domain.repository.PromotionRepository;
import store.dto.*;
import store.global.util.Parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static store.global.exception.ErrorMessage.OVER_PRODUCT_STOCK;
import static store.global.exception.ErrorMessage.PRODUCT_NOT_FOUND;

public class ConvenienceService {

    private final ProductRepository productRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final OrderRepository orderRepository;
    private final Parser<String> stringParser;

    public ConvenienceService(
            ProductRepository productRepository,
            ProductPromotionRepository productPromotionRepository,
            OrderRepository orderRepository,
            Parser<String> stringParser
    ) {
        this.productRepository = productRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.orderRepository = orderRepository;
        this.stringParser = stringParser;
    }

    // 총 재고 현황 조회
    public List<ProductResponseDto> getProductsStock() {
        List<ProductResponseDto> responseDtos = new ArrayList<>();
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            Optional<ProductPromotion> optional = productPromotionRepository.findByName(product.getName());
            // 프로모션이 있으면
            if (optional.isPresent()) {
                ProductPromotion productPromotion = optional.get();
                responseDtos.add(ProductResponseDto.of(
                        product.getName(), product.getPrice(), productPromotion.getQuantity(), productPromotion.getPromotion().getName()));

                int leftQuantity = product.getQuantity() - productPromotion.getQuantity();
                responseDtos.add(ProductResponseDto.of(product.getName(), product.getPrice(), leftQuantity, ""));
            }
            // 프로모션 없음
            if (optional.isEmpty()) {
                responseDtos.add(ProductResponseDto.of(product.getName(), product.getPrice(), product.getQuantity(), ""));
            }
        }
        return responseDtos;
    }

    public void buyProduct(String productAndQuantities) {
        List<String> productAndQuantitySplit = stringParser.parse(productAndQuantities, ",");

        for (String productAndQuantity : productAndQuantitySplit) {
            String substring = productAndQuantity.substring(1, productAndQuantity.length() - 1);
            List<String> productQuantity = stringParser.parse(substring, "-");
            String productName = productQuantity.getFirst();
            int quantity = Integer.parseInt(productQuantity.getLast());

            // 상품 존재 여부 검증
            Product product = productRepository.findByName(productName)
                    .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND.getMessage()));

            // 수량 검증
            if (!product.isValidQuantity(quantity)) {
                throw new IllegalArgumentException(OVER_PRODUCT_STOCK.getMessage());
            }
            orderRepository.save(Order.of(product, quantity));
        }
    }

    public PromotionLeakResponseDto isPromotionButLeakQuantity() {
        List<Order> pendingOrders = orderRepository.findPendingOrders();
        for (Order order : pendingOrders) {
            Product product = order.getProduct();
            int quantity = order.getQuantity();

            // 프로모션이 존재하면 수량이 맞는지 검증
            Optional<ProductPromotion> optional = productPromotionRepository.findByName(product.getName());
            if (optional.isPresent()) {
                Promotion promotion = optional.get().getPromotion();
                if (quantity < promotion.getBuyQuantity() + promotion.getGetQuantity()) {
                    return PromotionLeakResponseDto.of(order.getId(), product.getName(), promotion.getGetQuantity());
                }
            }
        }
        return null;
    }

    // 프로모션 상품 구매 수량 추가
    public void increasePromotionQuantity(PromotionLeakResponseDto promotionLeakResponseDto) {
        Order order = orderRepository.findById(promotionLeakResponseDto.orderId()).get();
        order.increaseQuantity(promotionLeakResponseDto.freeQuantity());
    }

    // 프로모션 재고 부족으로 일부 수량을 프로모션 혜택 없이 결제 기능
    public PromotionQuantityLeakResponseDto isPromotionQuantityLeak() {
        List<Order> pendingOrders = orderRepository.findPendingOrders();

        for (Order order : pendingOrders) {
            Product product = order.getProduct();
            int orderQuantity = order.getQuantity();

            // 프로모션이 존재하면 프로모션 수량보다 초과인지 검증
            Optional<ProductPromotion> optional = productPromotionRepository.findByName(product.getName());
            if (optional.isPresent()) {
                int promotionQuantity = optional.get().getQuantity();

                if (orderQuantity > promotionQuantity) {
                    int leakQuantity = orderQuantity - promotionQuantity;
                    return PromotionQuantityLeakResponseDto.of(order.getId(), product.getName(), leakQuantity);
                }
            }
        }
        return null;
    }

    public void decreaseLeakPromotionOrderQuantity(PromotionQuantityLeakResponseDto responseDto) {
        Order order = orderRepository.findById(responseDto.orderId()).get();
        order.increaseQuantity(responseDto.leakQuantity());
    }

    // 주문 정산
    // N 멤버십 없이
    //  - 구매 상품 내역: 상품명, 수량, 가격
    //  - 증정 상품: 프로모션에 따라 무료로 제공된 증정 상품 목록
    //  - 금액 정보
    //    - 총 구매액: 구매한 상품의 총 수량과 총 금액
    //    - 행사 할인: 프로모션에 의해 할인된 금액
    //    - 멤버십 할인: 멤버십 할인: 멤버십에 의해 추가로 할인된 금액
    //    - 최종 결제 금액:
    public ProductReceiptResponse settlePendingOrder(String memberShipDecision) {

        List<Order> pendingOrders = orderRepository.findPendingOrders();
        List<BuyProductResponse> buyProductResponses = new ArrayList<>();
        List<PresentationResponse> presentationResponses = new ArrayList<>();
        int totalCount = 0;
        int totalPrice = 0;
        int promotionDiscount = 0;
        int membershipDiscount = 0;

        for (Order pendingOrder : pendingOrders) {
            buyProductResponses.add(BuyProductResponse.of(pendingOrder));
            pendingOrder.getProduct().decreaseQuantity(pendingOrder.getQuantity());

            totalCount += pendingOrder.getQuantity();
            totalPrice += pendingOrder.getProduct().getPrice() * pendingOrder.getQuantity();

            // 프로모션이 있는지 검사
            Optional<ProductPromotion> optionalProductPromotion = productPromotionRepository.findByName(pendingOrder.getProduct().getName());
            if (optionalProductPromotion.isPresent()) {
                // 프로모션 기간 검사

                ProductPromotion productPromotion = optionalProductPromotion.get();
                Product product = productPromotion.getProduct();
                Promotion promotion = productPromotion.getPromotion();
                LocalDateTime now = DateTimes.now();

                if (now.toLocalDate().isAfter(promotion.getStartDate()) && now.toLocalDate().isBefore(promotion.getEndDate())) {
                    if (pendingOrder.getQuantity() >= promotion.getGetQuantity() + promotion.getBuyQuantity()) {
                        presentationResponses.add(PresentationResponse.of(product.getName(), promotion.getGetQuantity()));
                        productPromotion.decreaseQuantity(pendingOrder.getQuantity());
                        promotionDiscount += promotion.getGetQuantity() * product.getPrice();
                    }
                }

            }

            // 프로모션이 없고 멤버십 할인이 Y일때
            if (optionalProductPromotion.isEmpty() && memberShipDecision.equals("Y")) {

            }

            // order complete 처리
            pendingOrder.orderComplete();
        }

        return ProductReceiptResponse.of(
                buyProductResponses,
                presentationResponses,
                totalCount,
                totalPrice,
                promotionDiscount,
                membershipDiscount,
                totalPrice - promotionDiscount - membershipDiscount
        );
    }
}