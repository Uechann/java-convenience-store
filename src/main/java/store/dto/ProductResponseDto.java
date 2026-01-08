package store.dto;

import store.domain.model.Product;

public record ProductResponseDto(
        String productName,
        int price,
        int quantity,
        String promotionName
) {
    public static ProductResponseDto of(String productName, int price, int quantity, String promotionName) {
        return new ProductResponseDto(productName, price, quantity, promotionName);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("- ").append(productName).append(" ");
        stringBuilder.append(String.format("%,d", price)).append("원 ");
        if (quantity != 0) {
            stringBuilder.append(quantity).append("개 ");
        }

        if (quantity == 0) {
            stringBuilder.append("재고 없음");
        }

        if (!promotionName.isEmpty()) {
            stringBuilder.append(promotionName);
        }
        return stringBuilder.toString();
    }
}
