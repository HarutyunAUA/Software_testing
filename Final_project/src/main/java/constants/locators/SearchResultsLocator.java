package constants.locators;

public class SearchResultsLocator {
    public static final String RESULT_LIST_XPATH = "//*[@id=\"product-wrapper\"]/ol/li[*]";
    public static final String BEFORE_PRODUCT_NAME_XPATH = "//*[@id=\"product-wrapper\"]/ol/li[";
    public static final String AFTER_PRODUCT_NAME_XPATH = "]/div[2]/h5/a";
    public static final String CATEGORY_XPATH = "//*[@class=\"items\"]/li[2]/a";
    public static final String STYLE_CLASS = "base";
    public static final String PRICE_FILTER = "//*[@id=\"narrow-by-list\"]/div[2]/div[1]";
    public static final String PRICE_FROM = "input[amshopby-fromto-id='from']";
    public static final String PRICE_TO = "input[amshopby-fromto-id='to']";
    public static final String PRICE_FILTER_BUTTON = "button[amshopby-fromto-id='go']";
    public static final String BEFORE_ELEMENT_PATH = "//*[@id=\"product-wrapper\"]/ol/li[";
    public static final String AFTER_ELEMENT_PATH = "]/div[2]/div[2]/span/span";
    public static final String MATERIAL_FILTER = "//*[@id=\"narrow-by-list\"]/div[5]/div[1]";
    public static final String MATERIAL_DROPDOWN_SECTIONS = "form[data-amshopby-filter-request-var='material']";
    public static final String FINAL_PRICE_ENDING = "/span[2]";
    public static final String DATA_AMOUNT_TAG = "data-price-amount";
    public static final String FILTER_VISIBILITY_TAG = "aria-selected";
    public static final String FILTER_VISIBILITY_TAG_VALUE = "true";
    public static final String MATERIAL_DROPDOWN_TAG = "li";
    public static final String EMPTY_PAGE_MESSAGE = "div[class='message info empty']";
    public static final String DATA_LABEL = "data-label";

}
