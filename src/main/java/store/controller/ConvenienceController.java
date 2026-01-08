package store.controller;

import com.sun.source.tree.IfTree;
import store.domain.service.ConvenienceService;
import store.domain.service.FileService;
import store.dto.ProductResponseDto;
import store.dto.PromotionLeakResponseDto;
import store.global.validator.InputValidator;
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
            // 상품과 수량 구매 입력후 주문 접수
            String productAndQuantityInput = inputView.inputProductAndQuantity();
            convenienceService.buyProduct(productAndQuantityInput);

            // 정산
            // 프로모션과 멤버십 여부 입력
            PromotionLeakResponseDto promotionButLeakQuantity = convenienceService.isPromotionButLeakQuantity();
            if (promotionButLeakQuantity != null) {
                outputView.outputAdditionalPromotionDecision(promotionButLeakQuantity);
                String additionalPromotionDecision = inputView.inputAdditionalPromotionDecision();
                InputValidator.validateYNPattern(additionalPromotionDecision);
                if (additionalPromotionDecision.equals("Y")) {
                    // service에서 order 수량 수정
                    convenienceService.increasePromotionQuantity(promotionButLeakQuantity);
                }
            }

            // 영수증 출력

            // 재구매 의사 입력
            inputView.inputAdditionalPurchase();

        }
    }
}
