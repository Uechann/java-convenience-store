package store.dto;

public record PromotionQuantityLeakResponseDto(
        Long orderId,
        String productName,
        int leakQuantity
) {
    public static PromotionQuantityLeakResponseDto of(Long orderId, String productName, int leakQuantity) {
        return new PromotionQuantityLeakResponseDto(orderId, productName, leakQuantity);
    }
}
