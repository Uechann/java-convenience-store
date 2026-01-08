package store.dto;

import java.util.List;

//  - 구매 상품 내역: 상품명, 수량, 가격
//  - 증정 상품: 프로모션에 따라 무료로 제공된 증정 상품 목록
//  - 금액 정보
//    - 총 구매액: 구매한 상품의 총 수량과 총 금액
//    - 행사 할인: 프로모션에 의해 할인된 금액
//    - 멤버십 할인: 멤버십 할인: 멤버십에 의해 추가로 할인된 금액
//    - 최종 결제 금액:
public record ProductReceiptResponse(
        List<BuyProductResponse> buyProductResponses,
        List<PresentationResponse> presentationResponse,
        int totalCount,
        int totalPrice,
        int promotionDiscount,
        int membershipDiscount,
        int payPrice
) {
    public static ProductReceiptResponse of(
            List<BuyProductResponse> buyProductResponses,
            List<PresentationResponse> presentationResponse,
            int totalCount,
            int totalPrice,
            int promotionDiscount,
            int membershipDiscount,
            int payPrice
    ) {
        return new ProductReceiptResponse(
                buyProductResponses,
                presentationResponse,
                totalCount,
                totalPrice,
                promotionDiscount,
                membershipDiscount,
                payPrice
        );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("==========W 편의점===========").append("\n");
        stringBuilder.append("상품명\t\t수량\t\t금액").append("\n");

        for (BuyProductResponse buyProductRespons : buyProductResponses) {
            stringBuilder.append(buyProductRespons.toString()).append("\n");
        }

        stringBuilder.append("==========증   정===========").append("\n");
        for (PresentationResponse response : presentationResponse) {
            stringBuilder.append(response.toString()).append("\n");
        }

        stringBuilder.append("===========================").append("\n");
        stringBuilder.append("총구매액").append("\t\t").append(totalCount).append("\t\t").append(String.format("%,d", totalPrice)).append("\n");
        stringBuilder.append("행사할인").append("\t\t\t\t").append(String.format("%,d", promotionDiscount * -1)).append("\n");
        stringBuilder.append("멤버십할인").append("\t\t\t\t").append(String.format("%,d", membershipDiscount * -1)).append("\n");
        stringBuilder.append("내실돈").append("\t\t\t\t").append(String.format("%,d", payPrice)).append("\n");

        return stringBuilder.toString();
    }
}
