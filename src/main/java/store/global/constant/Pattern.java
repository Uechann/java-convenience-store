package store.global.constant;

public class Pattern {

    public static final String MENU = "[가-힣]+";
    public static final String QUANTITY = "[1-9][0-9]*";
    public static final String MENU_QUANTITY = "["+ MENU + "-" + QUANTITY + "]";

    // 상품과 수량 패턴
    public static final String PRODUCT_AND_QUANTITY_PATTERN = "^" + MENU_QUANTITY + "(," + MENU_QUANTITY + ")*" + "$";

    // Y/N 패턴
    public static final String Y_OR_N= "^[Y|N]$";
}
