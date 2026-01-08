package store.controller;

import store.domain.service.ConvenienceService;
import store.domain.service.FileService;
import store.dto.ProductResponseDto;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class ConvenienceController {

    private final FileService fileService;
    private final ConvenienceService convenienceService;
    private final InputView inputView;
    private final OutputView outputView;

    public ConvenienceController(
            FileService fileService,
            ConvenienceService convenienceService,
            InputView inputView,
            OutputView outputView
    ) {
        this.fileService = fileService;
        this.convenienceService = convenienceService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        fileService.initialProductAndPromotion();

        while (true) {
            // 시작 메시지 출력
            outputView.outputStartMessage();
            List<ProductResponseDto> productsStock = convenienceService.getProductsStock();
            // 상품 재고 현황 출력
            outputView.outputProductStocks(productsStock);
            // 상품과 수량 구매 입력
            inputView.inputProductAndQuantity();

            // 프로모션과 멤버십 여부 입력

            // 영수증 출력

            // 재구매 의사 입력
            inputView.inputAdditionalPurchase();

        }
    }
}
