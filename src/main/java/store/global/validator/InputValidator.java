package store.global.validator;

public class InputValidator {

    public static void validateInputFunction(String inputFunction) {
        if (!inputFunction.matches(INPUT_FUNCTION_PATTERN)) {
            throw new IllegalArgumentException(INVALID_INPUT_PATTERN.getMessage());
        }
    }

    public static void validateInputTime(String inputTime) {
        if (!inputTime.matches(INPUT_TIME_PATTERN)) {
            throw new IllegalArgumentException(INVALID_INPUT_PATTERN.getMessage());
        }
    }
}
