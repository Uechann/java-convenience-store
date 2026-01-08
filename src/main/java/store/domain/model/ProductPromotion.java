package store.domain.model;

public class ProductPromotion {

    private Product product;
    private Promotion promotion;
    private int quantity;

    private ProductPromotion(Product product, Promotion promotion, int quantity) {
        this.product = product;
        this.promotion = promotion;
        this.quantity = quantity;
    }

    public static ProductPromotion of(Product product, Promotion promotion, int quantity) {
        return new ProductPromotion(product, promotion, quantity);
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
