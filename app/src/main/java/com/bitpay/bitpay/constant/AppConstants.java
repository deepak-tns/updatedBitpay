package com.bitpay.bitpay.constant;

/**
 * Created by Codeslay-03 on 4/06/2017.
 */

public interface AppConstants {

    /* App Tag*/
    public static final String APP_NAME = "BitPay";
    public static final String VERSION_CODE = "1.0.0";
    public static final String DEVICE_TYPE = "Android";
    /* bg_splash screen*/
    public static final int SPLASH_TIME = 3000;

    /* animation type*/
    public static final int ANIMATION_SLIDE_UP = 0x01;
    public static final int ANIMATION_SLIDE_LEFT = 0x02;

    /* DashboardActivity*/
    public static final int APP_EXIT_TIME = 2000;
    public static final int MIN_ACCOUNT_NUMBER_LENGTH = 10;
    public static final int MIN_PAN_NUMBER_LENGTH = 10;
    /* font */
    public static final String FONT_ROBOTO_REGULAR = "font/Roboto-Regular.ttf";
    public static final String FONT_CORBEL_REGULAR = "font/corbel_regular.ttf";
    public static final String FONT_CORBEL_ITALIC = "font/corbel_italic.ttf";
    public static final String FONT_CORBEL_BOLD = "font/corbelb_bold.ttf";
    public static final String FONT_BITCOIN_REGULAR = "font/DejaVuSansCondensed.ttf";
    public static final String FONT_BITCOIN_BOLD = "font/DejaVuSansCondensed-Bold.ttf";

    /*req permission*/
    public static final int RECEIVE_SMS_PERMISSION_REQUEST = 0x01;
    public final int READ_CONTACTS_PERMISSION_REQUEST = 0x02;
    public static final int CAMERA_PERMISSION_REQUEST = 0x03;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 0x04;
    /* Dashboard*/
    public static final int HOME = 0;
    public static final int BIT_ADDRESS = 1;
    public static final int DOCUMENTS = 2;
    public static final int CONTACT_BOOK = 3;
    public static final int TRANSACTIONS = 4;
    public static final int SUPPORT = 5;
    public static final int ABOUT_US = 6;

    public static final String KEY_WEBVIEW_URL = "webviewUrl";
    public static String KEY_POSSITION = "position";
    public static String KEY_IMAGE = "image";

    /* otp  */
    public static final long timeafterwhichRecieverUnregister = 900000l;
    public static final long longTimeAfterWhichButtonEnable = 31000;
    public static final long longTotalVerificationTime = 31000;
    public static final long longOneSecond = 1000;
    public static final String KEY_MOBILE_NO = "mobileNumber";
    public static final String KEY_OTP = "OtpIs";
    public static final String KEY_USER_PIN = "userPin";
    public static final String EVENT_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final int PRIORITY_HIGH = 2147483647;
    public static final int RESET_LIMIT = 5;
    public static final int PIN_LENGTH = 4;

    /*alert type*/
    public static final int ALERT_TYPE_NO_NETWORK = 0x01;
    public static final int ALERT_TYPE_SEND_BIT = 0x02;
    public static final int ALERT_TYPE_CANCEL_TRANSCATION = 0x03;
    public static final int ALERT_TYPE_BUY_SELL_BITCOIN = 0x04;

    /*status*/
    public static final String KEY_PAN_STATUS = "pancardStatus";
    public static final String KEY_BANK_STATUS = "bankStatus";
    public static final String KEY_KYC_STATUS = "kycStatus";
    public static final String KEY_EMAIL_STATUS = "emailStatus";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_REJECTED = "rejected";
    public static final int STATUS_VERYFIED = 1;
    /*user key*/
    public static final String USER_ID = "id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PROFILE_IMAGE = "user_image";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_MOBILE_NO = "mobile_no";
    public static final String USER_BIT_ADDRESS = "bit_address";
    public static final String USER_BIT_USER_ID = "bit_user_id";
    public static final String USER_SECURE_PIN = "secure_pin";
    public static final String IS_USER_EXITS = "is_user_exist";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CURRENT_BALANCE = "credits";
    public static final String KEY_NETWORK_FEE = "network_fee";
    public static final String KEY_MIN_AMOUNT = "minimum_amount";
    public static final String KEY_MAX_AMOUNT = "maximum_amount";
    public static final String KEY_BUYING_RATE = "buying_rate";
    public static final String KEY_SELLING_RATE = "selling_rate";
    public static final String KEY_RATE_AMMOUNT = "rateAmmount";
    public static final String KEY_BIT_AMMOUNT = "bitAmmount";
    public static final String KEY_DATA = "data";
    public static final String KEY_TYPE = "type";
    public static final String KEY_PROVIDER_MODEL = "providerModel";
    public static final String KEY_TRANSACTION_ID = "transection_id";
    public static final String KEY_TRANSACTION_TYPE = "transactionId";
    public static final String KEY_TRANSACTION_MODEL = "transactionModel";

    public static final String STATUS_PAN = "pancard_status";
    public static final String STATUS_BANK = "bank_detail_status";
    public static final String STATUS_KYC = "kyc_status";
    public static final String STATUS_EMAIL = "email_status";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String FCM_TOKEN = "fcmToken";

    public static final int TYPE_BUY_BITCOIN = 0x01;
    public static final int TYPE_SELL_BITCOIN = 0x02;
    public static final int TYPE_SEND_BITCOIN = 0x03;
    public static final int TYPE_MOBILE_TOPUP = 0x04;
    public static final int TYPE_DTH = 0x05;
    public static final int TYPE_GAS = 0x06;
    public static final int TYPE_ELECTRICITY = 0x07;
    public static final int TYPE_RESET_PIN = 0x08;
    public static final float MIN_TRANSFER_AMT = 0;//1000;
    public static final float MAX_TRANSFER_AMT = 500000;
    public static final float MIN_RECHARGE_AMT = 10;
    /* Request Tag*/
    public static final int REQUEST_TAG_NO_RESULT = 0x01;
    public static final int REQUEST_TAG_CONTACT_ACTIVITY = 0x02;
    public static final int REQUEST_TAG_PAYU_ACTIVITY = 0x03;
    public static final int REQUEST_TAG_CONFIRM_PIN = 0x04;
    public static final int REQUEST_TAG_EDIT_PROFILE_ACTIVITY = 0x05;
    public static final int REQUEST_TAG_SCANNER_ACTIVITY = 0x05;
    /* camera req*/
    public static final int PICK_IMAGE_CAMERA = 0x01;
    public static final int PICK_IMAGE_GALLERY = 0x02;

    /* camera req*/
    public static final int RECHARGE_MOBILE = 0x01;
    public static final int RECHARGE_DTH = 0x02;
    public static final int RECHARGE_GAS = 0x03;
    public static final int RECHARGE_ELECTRICITY = 0x04;

    /* App URL*/
    public static final String URL_PAYMENT_SUCCESS = "https://payu.herokuapp.com/success";
    public static final String URL_PAYMENT_FAIL = "https://payu.herokuapp.com/failure";

    public static final String RESPONCE_ERROR = "error";
    public static final String RESPONCE_MESSAGE = "message";
    public static final String RESPONCE_STATUS = "status";
    public static final String RESPONCE_FAIL = "fail";
    public static final String RESPONCE_DATA = "data";
    public static final String RESPONCE_ERRPR_MESSAGE = "error_message";

    public static final String BASE_URL_IMAGES = "http://bitpayit.com/bitpay/images/";
    //public static final String BASE_URL_IMAGES = "http://codeslaysales.com/bitpay/images/";

    public static final String BASE_URL = "http://bitpayit.com/bitpay/bitpayApi/v2/index.php/";
    //public static final String BASE_URL = "http://codeslaysales.com/bitpay/bitpayApi/v2/index.php/";

    public static final String SIGNUP_URL = BASE_URL + "loginORSignup";
    public static final String RESET_PIN_URL = BASE_URL + "resetSecurePin";
    public static final String ADD_CONTACT_URL = BASE_URL + "addContactDetail";
    public static final String GET_CONTACT_URL = BASE_URL + "getContactDetail";
    public static final String GET_CURRENT_BALANCE_URL = BASE_URL + "getCurrentWalletBalanceAmount";
    public static final String GET_BITCOIN_RATE_URL = BASE_URL + "getBuyingAndSellingRates";
    public static final String SEND_BITCOIN_URL = BASE_URL + "transferBitCoinToUser";
    public static final String ADD_BANK_DETAILS_URL = BASE_URL + "addBankDetails";
    public static final String UPLOAD_NEW_IMAGE = BASE_URL + "uploadNewImage";
    public static final String ADD_PAN_DETAILS_URL = BASE_URL + "addPancardDetails";
    public static final String GET_BANK_DETAIL_URL = BASE_URL + "getBankDetails";
    public static final String GET_PAN_DETAIL_URL = BASE_URL + "getPancardDetails";
    public static final String BUY_BITCOINS_URL = BASE_URL + "buyBitCoin";
    public static final String SELL_BITCOINS_URL = BASE_URL + "sellBitCoin";
    public static final String ADD_SUPPORT_URL = BASE_URL + "addSupport";
    public static final String UPDATE_USER_DETAILS_URL = BASE_URL + "updateUserProfile";
    public static final String GET_USER_TRANSACTION_URL = BASE_URL + "getUserTransection";
    public static final String RECHARGE_MOBILE_URL = BASE_URL + "rechargeUserMobile";
    public static final String GET_ACTIVITY_URL = BASE_URL + "getAllActivities";
    public static final String GET_USER_ACTIVITY_URL = BASE_URL + "getUserActivities";
    public static final String GET_DOC_DETAIL_URL = BASE_URL + "getDocumentStatus";
    public static final String DEPOSIT_AMOUNT_URL = BASE_URL + "addAmountDeposite";
    public static final String ADD_KYC_DETAILS_URL = BASE_URL + "addKYCDetails";
    public static final String GET_KYC_DETAIL_URL = BASE_URL + "getKYCDetails";
    public static final String UPDATE_BANK_IMAGE_URL = BASE_URL + "addImageBankDetails";
    public static final String GET_NETWORK_FEE_URL = BASE_URL + "getNetworkFee";
    public static final String GET_STATE_LIST_URL = BASE_URL + "getAllIndianState";
    public static final String GET_CITY_LIST_URL = BASE_URL + "getIndianCityByState";
}

