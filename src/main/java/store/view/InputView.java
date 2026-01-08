package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public InputView() {}

    public String inputProductAndQuantity() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public String inputMembershipDecision() {
        System.out.println("멤버십 할인을 받으시겠습니다? (Y/N)");
        return Console.readLine();
    }

    public String inputAdditionalPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return Console.readLine();
    }

    public String inputPurchaseNotPromotionDecision() {
        System.out.println("그래도 구매하시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public String inputAdditionalPromotionDecision() {
        System.out.println("추가하시겠습니까? (Y/N)");
        return Console.readLine();
    }
}
