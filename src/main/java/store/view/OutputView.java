package store.view;

import store.dto.ProductResponseDto;
import store.dto.PromotionLeakResponseDto;

import java.util.List;

public class OutputView {

    public OutputView() {}

    // 시작 메시지 출력
    public void outputStartMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    // 상품 재고 출력
    public void outputProductStocks(List<ProductResponseDto> productResponseDtos) {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (ProductResponseDto responseDto : productResponseDtos) {
            System.out.println(responseDto.toString());
        }
    }

    // 프로모션 추가 여부 안내 메시지 출력
    // 현 재 { 상 품 명 } 은 ( 는 ) 1 개 를 무 료 로 더 받 을 수 있 습 니 다 .
    public void outputAdditionalPromotionDecision(PromotionLeakResponseDto responseDto) {
        System.out.println("현재 " + responseDto.productName()+"은(는) " + responseDto.freeQuantity()+"개를 무료로 더 받을 수 있습니다.");
    }


    // 프로모션 재고 부족으로 일부수량 정거로 구매 안내 메시지
    // 현 재 { 상 품 명 } { 수 량 } 개 는 프 로 모 션 할 인 이 적 용 되 지 않 습 니 다.
}
