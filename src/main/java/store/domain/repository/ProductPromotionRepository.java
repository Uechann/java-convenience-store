package store.domain.repository;

import store.domain.model.ProductPromotion;

import java.util.*;

public class ProductPromotionRepository {

    private final Map<String, ProductPromotion> productPromotionMap = new LinkedHashMap<>();

    public void save(ProductPromotion productPromotion) {
        productPromotionMap.put(productPromotion.getProduct().getName(), productPromotion);
    }

    public Optional<ProductPromotion> findByName(String productName) {
        return Optional.ofNullable(productPromotionMap.get(productName));
    }

    public List<ProductPromotion> findAll() {
        return new ArrayList<>(productPromotionMap.values());
    }
}
