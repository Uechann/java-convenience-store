package store.controller;

import store.domain.service.ConvenienceService;
import store.domain.service.FileService;
import store.dto.ProductReceiptResponse;
import store.dto.PromotionLeakResponseDto;
import store.dto.PromotionQuantityLeakResponseDto;
import store.global.validator.InputValidator;
import store.view.InputView;
import store.view.OutputView;

import static store.global.util.Retry.retry;

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
            String retry = retry(() -> {
                outputView.outputStartMessage();
                outputView.outputProductStocks(convenienceService.getProductsStock());
                // 상품과 수량 구매 입력후 주문 접수
                inputOrder();

                // 프로모션과 멤버십 여부 입력
                PromotionLeakResponseDto promotionButLeakQuantity = convenienceService.isPromotionButLeakQuantity();
                validatePromotionButLeakQuantity(promotionButLeakQuantity);

                // 프로모션 재고가 부족한지 검증
                PromotionQuantityLeakResponseDto promotionQuantityLeak = convenienceService.isPromotionQuantityLeak();
                validatePromotionQuantityLeak(promotionQuantityLeak);

                // 멤버십 할인 여부 입력
                String membershipDecision = inputView.inputMembershipDecision();
                InputValidator.validateYNPattern(membershipDecision);
                ProductReceiptResponse productReceiptResponse = convenienceService.settlePendingOrder(membershipDecision);

                // 영수증 출력
                outputView.outputProductReceipt(productReceiptResponse);

                // 재구매 의사 입력
                return inputAdditionalPurchaseDecision();
            });

            if (retry.equals("N")) {
                break;
            }
        }
    }

    private String inputAdditionalPurchaseDecision() {
        String inputAdditionalPurchase = inputView.inputAdditionalPurchase();
        InputValidator.validateYNPattern(inputAdditionalPurchase);
        return inputAdditionalPurchase;
    }

    private void inputOrder() {
        String productAndQuantityInput = inputView.inputProductAndQuantity();
        InputValidator.validateProductQuantityPattern(productAndQuantityInput);
        convenienceService.buyProduct(productAndQuantityInput);
    }

    private void validatePromotionQuantityLeak(PromotionQuantityLeakResponseDto promotionQuantityLeak) {
        if (promotionQuantityLeak != null) {
            outputView.outputPromotionQuantityLeak(promotionQuantityLeak);
            String notPromotionDecision = inputView.inputPurchaseNotPromotionDecision();
            InputValidator.validateYNPattern(notPromotionDecision);

            if (notPromotionDecision.equals("N")) {
                convenienceService.decreaseLeakPromotionOrderQuantity(promotionQuantityLeak);
            }
        }
    }

    private void validatePromotionButLeakQuantity(PromotionLeakResponseDto promotionButLeakQuantity) {
        if (promotionButLeakQuantity != null) {
            outputView.outputAdditionalPromotionDecision(promotionButLeakQuantity);
            String additionalPromotionDecision = inputView.inputAdditionalPromotionDecision();
            InputValidator.validateYNPattern(additionalPromotionDecision);
            if (additionalPromotionDecision.equals("Y")) {
                // service에서 order 수량 수정
                convenienceService.increasePromotionQuantity(promotionButLeakQuantity);
            }
        }
    }
}
