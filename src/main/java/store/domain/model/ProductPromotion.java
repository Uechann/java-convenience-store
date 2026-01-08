package store.domain.model;

public class ProductPromotion {

    private Product product;
    private Promotion promotion;

    private ProductPromotion(Product product, Promotion promotion) {
        this.product = product;
        this.promotion = promotion;
    }

    public static ProductPromotion of(Product product, Promotion promotion) {
        return new ProductPromotion(product, promotion);
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
