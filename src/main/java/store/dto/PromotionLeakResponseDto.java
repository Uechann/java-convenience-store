package store.dto;

public record PromotionLeakResponseDto(
        Long orderId,
        String productName,
        int freeQuantity
) {
    public static PromotionLeakResponseDto of(Long orderId, String productName, int freeQuantity) {
        return new PromotionLeakResponseDto(orderId, productName, freeQuantity);
    }
}
