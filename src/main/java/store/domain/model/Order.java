package store.domain.model;

public class Order {

    private Long id;
    private Product product;
    private int quantity;
    private OrderStatus orderStatus;

    private Order(Product product, int quantity, OrderStatus orderStatus) {
        this.product = product;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }

    public static Order of(Product product, int quantity) {
        return new Order(product, quantity, OrderStatus.PENDING);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public boolean isPending() {
        return orderStatus.equals(OrderStatus.PENDING);
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
