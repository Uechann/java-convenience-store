package store.global.validator;

import static store.global.constant.Pattern.PRODUCT_AND_QUANTITY_PATTERN;
import static store.global.constant.Pattern.Y_OR_N;
import static store.global.exception.ErrorMessage.INVALID_INPUT;
import static store.global.exception.ErrorMessage.INVALID_PRODUCT_AND_QUANTITY;

public class InputValidator {
    public static void validateProductQuantityPattern(String input) {
        if (!input.matches(PRODUCT_AND_QUANTITY_PATTERN)) {
            throw new IllegalArgumentException(INVALID_PRODUCT_AND_QUANTITY.getMessage());
        }
    }

    public static void validateYNPattern(String input) {
        if (!input.matches(Y_OR_N)) {
            throw new IllegalArgumentException(INVALID_INPUT.getMessage());
        }
    }
}
