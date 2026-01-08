package store.domain.service;

import store.domain.model.Product;
import store.domain.model.ProductPromotion;
import store.domain.model.Promotion;
import store.domain.repository.ProductPromotionRepository;
import store.domain.repository.ProductRepository;
import store.domain.repository.PromotionRepository;
import store.global.util.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FileService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final Parser<String> stringParser;

    public FileService(ProductRepository productRepository,
                       PromotionRepository promotionRepository,
                       ProductPromotionRepository productPromotionRepository, Parser<String> stringParser) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.stringParser = stringParser;
    }

    public void initialProductAndPromotion() {
        // 프로모션 초기화
        File promotions = new File("src/main/resources/promotions.md");
        try (BufferedReader reader = new BufferedReader(new FileReader(promotions))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> promotion = stringParser.parse(line, ",");
                String name = promotion.get(0);
                int buyQuantity = Integer.parseInt(promotion.get(1));
                int getQuantity = Integer.parseInt(promotion.get(2));
                LocalDate startDate = LocalDate.parse(promotion.get(3));
                LocalDate endDate = LocalDate.parse(promotion.get(4));
                promotionRepository.save(Promotion.of(name, buyQuantity, getQuantity, startDate, endDate));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 상품 초기화 및 프로모션 연결
        File products = new File("src/main/resources/product.md");
        try (BufferedReader reader = new BufferedReader(new FileReader(products))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> productSplit = stringParser.parse(line, ",");
                String name = productSplit.get(0);
                int price = Integer.parseInt(productSplit.get(1));
                int quantity = Integer.parseInt(productSplit.get(2));
                Product product = Product.of(name, price, quantity);
                productRepository.save(product);

                String promotionName = productSplit.get(3);
                if (!promotionName.equals("null")) {
                    Promotion promotion = promotionRepository.findByName(promotionName)
                            .orElseThrow(() -> new IllegalArgumentException("프로모션 없음"));
                    productPromotionRepository.save(ProductPromotion.of(product, promotion));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
