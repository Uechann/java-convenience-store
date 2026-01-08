package store.global.exception;


public enum ErrorMessage {

    ERROR_MESSAGE("[ERROR]"),

    // 공통
    INPUT_EMPTY_OR_BLANK("[ERROR] 입력값이 비어있거나 공백입니다."),
    INVALID_INPUT_PATTERN("[ERROR] 잘못된 형식을 입력하였습니다."),

    // 월과 요일
    INVALID_MONTH_AND_WEEKDAY("[ERROR] 유효하지 않은 입력 값입니다. 다시 입력해 주세요."),
    INVALID_WEEKDAY_KOREA_NAME("[ERROR] 해당되는 한글 요일이 없습니다."),
    WEEKDAY_NOT_FOUND("[ERROR] 해당 요일이 없습니다."),

    // 시간
    INVALID_TIME_PATTERN("[ERROR] 잘못된 시간 형식입니다."),
    INVALID_ACCESS_DATE("[ERROR] 등교일이 아닙니다."),
    INVALID_FUTURE_TIME("[ERROR] 아직 수정할 수 없습니다."),

    // 크루
    CREW_NOT_FOUND("[ERROR] 등록되지 않은 닉네임입니다."),

    // 출석
    ATTENDANCE_NOT_FOUND("[ERROR] 수정하고자 하는 출석 기록이 없습니다."),
    ATTENDANCE_ALREADY_EXIST("[ERROR] 이미 출석을 확인하였습니다. 필요한 경우 수정 기능을 이용해주세요.");


    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}