package store.domain.service;

import store.domain.model.Order;
import store.domain.model.Product;
import store.domain.model.ProductPromotion;
import store.domain.model.Promotion;
import store.domain.repository.OrderRepository;
import store.domain.repository.ProductPromotionRepository;
import store.domain.repository.ProductRepository;
import store.domain.repository.PromotionRepository;
import store.dto.ProductResponseDto;
import store.dto.PromotionLeakResponseDto;
import store.dto.PromotionQuantityLeakResponseDto;
import store.global.util.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static store.global.exception.ErrorMessage.OVER_PRODUCT_STOCK;
import static store.global.exception.ErrorMessage.PRODUCT_NOT_FOUND;

public class ConvenienceService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final OrderRepository orderRepository;
    private final Parser<String> stringParser;

    public ConvenienceService(
            ProductRepository productRepository,
            PromotionRepository promotionRepository,
            ProductPromotionRepository productPromotionRepository,
            OrderRepository orderRepository,
            Parser<String> stringParser
    ) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
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
}