package store.dto;

import store.domain.model.Order;

public record BuyProductResponse(
        String productName,
        int quantity,
        int price
) {
    public static BuyProductResponse of(Order order) {
        return new BuyProductResponse(
                order.getProduct().getName(),
                order.getQuantity(),
                order.getProduct().getPrice() * order.getQuantity()
        );
    }

    @Override
    public String toString() {
        return String.format("%s\t\t%d\t\t%,d", productName, quantity, price);
    }
}
