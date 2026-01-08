package store.domain.repository;

import store.domain.model.Promotion;

import java.util.*;

public class PromotionRepository {

    private final Map<String, Promotion> promotionMap = new LinkedHashMap<>();

    public void save(Promotion promotion) {
        promotionMap.put(promotion.getName(), promotion);
    }

    public Optional<Promotion> findByName(String promotionName) {
        return Optional.ofNullable(promotionMap.get(promotionName));
    }

    public List<Promotion> findAll() {
        return new ArrayList<>(promotionMap.values());
    }
}