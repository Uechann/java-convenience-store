package store.dto;

public record PresentationResponse(
        String productName,
        int quantity
) {
    public static PresentationResponse of(String productName, int quantity) {
        return new PresentationResponse(productName, quantity);
    }

    @Override
    public String toString() {
        return String.format("%s\t\t%d", productName, quantity);
    }
}
