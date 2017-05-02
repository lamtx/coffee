package erika.app.coffee.application;

public class ActionType {
    public static final String UPDATE_PACKAGES_LIST = "UPDATE_PACKAGES_LIST";
    public static final String SET_COMPONENT_STATUS = "SET_COMPONENT_STATUS";

    // Main
    public static final String PUSH = "PUSH";
    public static final String POP = "POP";

    // Popup
    public static final String SHOW_POPUP = "SHOW_POPUP";
    public static final String DISMISS_POPUP = "DISMISS_POPUP";
    public static final String SET_MESSAGE_BOX = "SET_MESSAGE_BOX";
    public static final String SET_LOADING_DIALOG_MESSAGE = "SET_LOADING_DIALOG_MESSAGE";

    // Base List
    public static final String SET_LOAD_STATE = "SET_LOAD_STATE";
    public static final String SET_IS_REFRESHING = "SET_IS_REFRESHING";

    // Sign In
    public static final String SET_USER_NAME = "SET_USER_NAME";
    public static final String SET_PASSWORD = "SET_PASSWORD";
    public static final String SET_HOST = "SET_HOST";

    // Table List
    public static final String SET_CHECKABLE_TABLE_CHECKED = "SET_CHECKABLE_TABLE_CHECKED";
    public static final String SET_TABLE_LIST_RESULT = "SET_TABLE_LIST_RESULT";

    //Order
    public static final String SET_TABLE_FOR_ORDER_COMPONENT = "SET_TABLE_FOR_ORDER_COMPONENT";
    public static final String SET_MENU_CATEGORY_LIST = "SET_MENU_CATEGORY_LIST";
    public static final String SET_MENU_CATEGORY_KEYWORD = "SET_MENU_CATEGORY_KEYWORD";
}
