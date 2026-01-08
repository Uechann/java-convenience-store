package store.domain.repository;

import store.domain.model.Product;

import java.util.*;

public class ProductRepository {

    private final Map<String, Product> productMap = new LinkedHashMap<>();

    public void save(Product product) {
        productMap.put(product.getName(), product);
    }

    public Optional<Product> findByName(String productName) {
        return Optional.ofNullable(productMap.get(productName));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }
}
