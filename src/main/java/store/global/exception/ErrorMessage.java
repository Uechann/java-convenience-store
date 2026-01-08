package store.global.exception;


public enum ErrorMessage {

    ERROR_MESSAGE("[ERROR]"),

    // 공통
    INPUT_EMPTY_OR_BLANK("[ERROR] 입력값이 비어있거나 공백입니다."),
    INVALID_INPUT_PATTERN("[ERROR] 잘못된 형식을 입력하였습니다."),
    INVALID_INPUT("[ERROR] 잘못된 입력입니다. 다시 입력해주세요."),

    INVALID_PRODUCT_AND_QUANTITY("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해주세요."),
    OVER_PRODUCT_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");



    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}