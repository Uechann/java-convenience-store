package store.domain.service;

import store.domain.model.Product;
import store.domain.repository.ProductRepository;
import store.domain.repository.PromotionRepository;
import store.global.util.Parser;

import java.util.List;

import static store.global.exception.ErrorMessage.OVER_PRODUCT_STOCK;
import static store.global.exception.ErrorMessage.PRODUCT_NOT_FOUND;

public class ConvenienceService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final Parser<String> stringParser;

    public ConvenienceService(ProductRepository productRepository, PromotionRepository promotionRepository, Parser<String> stringParser) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.stringParser = stringParser;
    }

    public void buyProduct(String productAndQuantities) {
        List<String> productAndQuantitySplit = stringParser.parse(productAndQuantities, ",");

        for (String productAndQuantity : productAndQuantitySplit) {
            String substring = productAndQuantity.substring(1, productAndQuantity.length() - 1);
            List<String> productQuantity = stringParser.parse(substring, "-");
            // 상품 존재 여부 검증
            String productName = productQuantity.getFirst();
            int quantity = Integer.parseInt(productQuantity.getLast());
            Product product = productRepository.findByName(productName)
                    .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND.getMessage()));

            // 수량 검증
            if (!product.isValidQuantity(quantity)) {
                throw new IllegalArgumentException(OVER_PRODUCT_STOCK.getMessage());
            }
        }
    }
}
