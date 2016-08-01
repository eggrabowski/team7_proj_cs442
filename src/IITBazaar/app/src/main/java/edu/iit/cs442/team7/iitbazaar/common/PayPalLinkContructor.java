package edu.iit.cs442.team7.iitbazaar.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

import edu.iit.cs442.team7.iitbazaar.BuildConfig;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class PayPalLinkContructor
{

    private final boolean isSSL = BuildConfig.isSSL;
    private final String headerImageLink = BuildConfig.imageURL;
    private final String principalColor = "CF102D";
    private final String returnPage = BuildConfig.returnPage;
    private final String cancelReturnPage = BuildConfig.cancelReturnPage;
    private final String PAYPAL_PREFIX = BuildConfig.paypalscr;


    public final String CHARSET = StandardCharsets.US_ASCII.name();

    private final String PAYPAL_EQUAL = "=";
    private final String PAYPAL_CONCAT = "&";


    private final HashMap<String,String> linkOptions = new HashMap<>();

    public PayPalLinkContructor()
    {
    }

    public PayPalLinkContructor setSeller(String sellerEmail) {


        linkOptions.put(PayPalKeys.SHOPPING_CART_BUSINESS.getKey(), encode(sellerEmail));

        return this;
    }

    public PayPalLinkContructor setItemName(String itemName) {

        linkOptions.put(PayPalKeys.ITEM_NAME.getKey(), encode(itemName));
        return this;
    }




    public PayPalLinkContructor setAmount(String itemPrice) {

        linkOptions.put(PayPalKeys.ITEM_AMOUNT.getKey(),encode(itemPrice));

        return this;
    }


    public PayPalLinkContructor setBuyerFirstName(String buyerFirstName) {

        linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_FIRST_NAME.getKey(),encode(buyerFirstName));

        return this;
    }

    public PayPalLinkContructor setItemNumber(int itemNumber) {

        linkOptions.put(PayPalKeys.PAYMENT_TRANSACTION_INVOICE_NUMBER.getKey(),encode(Integer.toString(itemNumber)));

        return this;
    }

    public PayPalLinkContructor setItemNumber(String itemNumber) {

        linkOptions.put(PayPalKeys.PAYMENT_TRANSACTION_INVOICE_NUMBER.getKey(),encode(itemNumber));

        return this;
    }



    public PayPalLinkContructor setBuyerLastName(String buyerLastName)  {

        linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_LAST_NAME.getKey(), encode(buyerLastName));

        return this;
    }



    public PayPalLinkContructor setBuyEmail(String buyerEmail) {

        linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_EMAIL.getKey(),encode(buyerEmail));
        // linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NOTE_LABEL.getKey(),encode("Add special instructions for "+buyerEmail));


        return this;
    }


    private void setGeneralDefaults(){

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_PRICIPLE_COLOR.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_PRICIPLE_COLOR.getKey(), principalColor);
        }

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_LOGO_IN_UPPER_LEFT_URL.getKey()) && isSSL && null != headerImageLink) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_LOGO_IN_UPPER_LEFT_URL.getKey(), encode(headerImageLink));
        }


        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_BACKGROUND_COLOR_UNDER_HEADER.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_BACKGROUND_COLOR_UNDER_HEADER.getKey(), principalColor);
        }

        if (null == linkOptions.get(PayPalKeys.CMD.getKey())) {
            linkOptions.put(PayPalKeys.CMD.getKey(), PayPalKeys.CMD_BUYITNOW.getKey());
        }
        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NO_SHIPPING.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NO_SHIPPING.getKey(), PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NO_SHIPPING_DEFAULT.getKey());
        }

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NO_NOTE.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NO_NOTE.getKey(), PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_NO_NOTE_DEFAULT.getKey());
        }

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_RETURN_URL.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_RETURN_URL.getKey(), encode(returnPage));
        }

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_RETURN_METHOD.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_RETURN_METHOD.getKey(),  PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_RETURN_METHOD_POST.getKey());
        }

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_CANCEL_RETURN.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_CANCEL_RETURN.getKey(), encode(cancelReturnPage));
        }

        if (null == linkOptions.get(PayPalKeys.PAYMENT_TRANSACTION_CURRENCY_CODE.getKey())) {
            linkOptions.put(PayPalKeys.PAYMENT_TRANSACTION_CURRENCY_CODE.getKey(), PayPalKeys.PAYMENT_TRANSACTION_CURRENCY_CODE_UNITED_STATE_DOLLAR.getKey());
        }
        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_LOCALE_OF_LOGIN.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_LOCALE_OF_LOGIN.getKey(), PayPalKeys.PAYPAL_CHECKOUT_DISPLAY_LOCALE_OF_LOGIN_UNITED_STATES.getKey());
        }

        if (null == linkOptions.get(PayPalKeys.BUILD_NOTIFICATION.getKey())) {
            linkOptions.put(PayPalKeys.BUILD_NOTIFICATION.getKey(), PayPalKeys.BUILD_NOTIFICATION_DEFAULT.getKey());
        }

        if (null == linkOptions.get(PayPalKeys.PAYPAL_CHECKOUT_SUBMIT.getKey())) {
            linkOptions.put(PayPalKeys.PAYPAL_CHECKOUT_SUBMIT.getKey(), PayPalKeys.PAYPAL_CHECKOUT_SUBMIT_DEFAULT.getKey());
        }


    }

    public String createLink() {

        setGeneralDefaults();


        StringBuilder sb = new StringBuilder();
        sb.append(PAYPAL_PREFIX);

        Iterator<String> kiIter = linkOptions.keySet().iterator();

        while(kiIter.hasNext()){

            final String key = kiIter.next();
            final String value = linkOptions.get(key);

            sb.append(key);
            sb.append(PAYPAL_EQUAL);
            sb.append(value);

            if (kiIter.hasNext()){
                sb.append(PAYPAL_CONCAT);
            }




        }

        return sb.toString();
    }

    private String encode(String in){
        try {
            return URLEncoder.encode(in, StandardCharsets.US_ASCII.name());
        } catch (UnsupportedEncodingException e) {
            return "";
        }


    }

}