package erika.app.coffee.application;

public class ActionType {

    // Main
    public static final String PUSH = "PUSH";
    public static final String POP = "POP";
    public static final String SET_ROOT = "SET_ROOT";
    public static final String SET_APP_TITLE = "SET_APP_TITLE";
    public static final String SIGN_OUT = "SIGN_OUT";
    public static final String SET_CLIENT_STATUS = "SET_CLIENT_STATUS";

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
    public static final String SET_TABLE_LIST_RESULT = "SET_TABLE_LIST_RESULT";
    public static final String SET_TABLE_STATUS = "SET_TABLE_STATUS";
    public static final String SET_TABLE_PRICE = "SET_TABLE_PRICE";

    // Order
    public static final String SET_CURRENT_TABLE = "SET_CURRENT_TABLE";
    public static final String SET_LEFT_PANEL_WIDTH = "SET_LEFT_PANEL_WIDTH";

    // Menu
    public static final String SET_MENU_CATEGORY_LIST = "SET_MENU_CATEGORY_LIST";
    public static final String SET_MENU_CATEGORY_KEYWORD = "SET_MENU_CATEGORY_KEYWORD";

    // Ordered
    public static final String SET_ORDERED_MENU_LIST = "SET_ORDERED_MENU_LIST";
    public static final String ADD_ORDERED_ITEM = "ADD_ORDERED_ITEM";

    // Number
    public static final String SET_NUMBER_VALUE = "SET_NUMBER_VALUE";
    public static final String SET_NUMBER_ACTION = "SET_NUMBER_ACTION";
    public static final String SET_NUMBER_STATE = "SET_NUMBER_STATE";

    // Message
    public static final String ADD_MESSAGE = "ADD_MESSAGE";
    public static final String REMOVE_MESSAGE = "REMOVE_MESSAGE";
    public static final String CHANGE_MESSAGE_STATUS = "CHANGE_MESSAGE_STATUS";

}
