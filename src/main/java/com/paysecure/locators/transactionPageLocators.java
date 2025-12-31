package com.paysecure.locators;

public class transactionPageLocators {
    // scrolling point of view
    public static final String analytics = "//span[text()='Analytics']";
    public static final String lastStatus="//th[text()='Last Status']";


    // report
    public static final String report = "(//span[text()='Report'])[2]";
    public static final String transactions ="//span[text()='Transactions']";

    // filter transaction through select status
    public static final String selectStatus= "//select[@id='status']";

    //ui+api framework
    public static final String searchPurchaseID="//input[@id='purchaseId']";
    public static final String firstTransactionID="(//th[text()='Transaction ID'])[1]/ancestor::table//td[4]";

    public static final String status="//select[@id='status']";

    //search Button
    public static final String searchButton="//button[text()='Search']";

    //select merchant
    public static final String selectmerchant="//span[text()='Select Merchant']";
    public static final String searchmerchant="//input[@placeholder='Search Merchant']";
    public static final String selectSearchmerchantCheckbox="(//input[@type='checkbox'])[2]";

    //select bank
    public static final String selectBank = "//span[text()='Select Bank']";
    public static final String searchFieldBank = "//input[@class='select2-search__field']";
    public static final String selectBankFromSearchField = "//ul[@id='select2-bnk-results']/li";
    public static final String getBankNameFrmTxnInfoPopUp="//tbody[@id='tblduspTrans']//tr[5]/td[4]";

   //select mid
   public static final String selectMid = "//select[@id='midn']";
    public static final String date = "//div[@id='reportrange']";
    public static final String dateRanges = "//div[@class='ranges']/descendant::li";
    public static final String dateFrom = "(//td[@data-title='r1c2'])[1]";
    public static final String dateTo = "(//td[@data-title='r2c5'])[1]";
    public static final String noRecordFound = "//td[text()='No Records Found']";
    public static final String applyButton = "//button[text()='Apply']";

//select timezone
    public static final String title = "(//td[@data-title='r1c2'])[1]";
    public static final String selectTimeZone = "//select[@id='tZone']";
    public static final String timeZones = "//ul[@id='select2-tZone-results']/li";
    public static final String searchFieldTimeZones = "//input[@role='searchbox']";
    public static final String timeZoneRollBox = "//select[@id='tZone']";


    //select currency
    public static final String selectCurrency = "//select[@id='merCurr']";
    public static final String getCurrencyFromCurrencyField = "//span[@id='select2-merCurr-container']";
    public static final String getCurrencyFromTable = "//*[@id=\"filterTransStats\"]/tr[1]/td[9]";

    //Last Actions column
    public static final String lastAction = "(//button[@title='Last Action'])[1]";
    public static final String transactionId = "//span[@id='lAhistory']";
    public static final String appProperties = "(//table[@class='table table-bordered'])[5]/descendant::th";
    public static final String cancelButtonOnLastAction = "//h4[text()='Last Action']/following-sibling::button";


    //action - Download invoice
    public static final String downloadInvoice = "(//button[@title='Download invoice'])[3]";
    public static final String generate = "//button[text()='Generate']";
    public static final String errorMessageForEmailImage = "//span[text()='Please fill logo Image, company Email.']";
    public static final String addProduct = "//button[@class='btn btn-primary addnewproductbtn']";

    public static final String companyName = "//input[@id='companyName']";
    public static final String companyAddress = "//input[@id='companyAddress']";
    public static final String companyEmail = "//input[@id='companyEmail']";
    public static final String companyLogo = "//input[@id='logoImageforUpload']";
    public static final String resizeImage = "//button[text()='Resize']";
    public static final String tableErrorMessage = "//span[@id='tableError']";

    public static final String description = "//input[@class='form-control description']";
    public static final String quantity = "//input[@class='form-control quantity']";
    public static final String unitPrice = "//input[@class='form-control unitPrice']";
    public static final String amount = "//input[@class='form-control amount']";
    public static final String grandTotal="//div[@id='grandTotal']";
    
    
    //status history
    public static final String history = "(//button[@title='History'])[1]";
    public static final String statusHistory = "//h4[text()='Status History']";
    public static final String historyTransaction = "//span[@id='phistory']";
    public static final String cancelButtonOnHistory = "//h4[text()='Status History']/following-sibling::button";


    //Remarks
    public static final String remarks = "(//button[@title='Remarks'])[1]";
    public static final String remarksTransactionId = "//input[@id='Transaction_Id']";
    public static final String sendMessageButton = "(//button[@class='btn btn-primary'])[2]";
    public static final String errorMessage = "//div[@id='swal2-html-container']";
    public static final String cancelRemarks = "(//h5[@id='exampleModalLabel'])[2]/following-sibling::button";
    public static final String message = "(//label[text()='Message'])[2]";
    public static final String ok = "//button[text()='OK']";
    public static final String userType = "//select[@id='usertype-select']";
    public static final String selectUserType = "(//span[@aria-disabled='false'])[2]";
    public static final String users = "//select[@id='recipient-select']";
    public static final String selectError = "//select[@id='recipient-Error']";
    public static final String errorMessageTextarea = "//textarea[@id='message-text']";
    public static final String successMessage = "//div[text()='Message sent successfully']";                                                 

    //Next Button
    public static final String nextBtn = "//li[contains(@class,'page-item next')]";
    public static final String firsttxnBtn = "((//th[text()='Transaction ID'])[1]/ancestor::table//td[4])[1]";
    public static final String transactionTableId = "((//th[text()='Transaction ID'])[1]/ancestor::table//td[4])[1]";
    public static final String merchantNameFromTXNPage="((//th[text()='Transaction ID'])[1]/ancestor::table//td[5])[1]";
    public static final String transactionTableAmount = "((//th[text()='Transaction ID'])[1]/ancestor::table//td[8])[1]";
    public static final String transactionTableCurrency = "((//th[text()='Transaction ID'])[1]/ancestor::table//td[9])[1]";
    public static final String orderId = "//td[text()='OrderId']/following-sibling::td[@class='propValue'][1]";
    public static final String currencyLabel = "//td[text()='currency']/following-sibling::td[1]";
    public static final String amountLabel = "//td[text()='Amt']/following-sibling::td[1]";
    public static final String lastStatusFromTxnPage="((//th[text()='Transaction ID'])[1]/ancestor::table//td[11])[1]";

   //total entries 
    public static final String entries = "(//span[@class='pagingInfoLbl'])[3]";
    public static final String reportRange="//div[@id='reportrange']//span";
    public static final String multiSelectedMerchant="//span[@class='multiselect-selected-text']";


   //search transaction
    public static final String transactionSearch="//input[@id='purchaseId']";

    //get merchant name
    public static final String getMerchantNameFromTXNPage="//button[@class='multiselect dropdown-toggle btn btn-default form-control']";
    public static final String getbankNameFromBankField="//span[@id='select2-bnk-container']";

    //payment info 
    public static final String purchaseTransactionID="(//td[@class='propValue'])[6]";
    public static final String currencyFrompaymentInfo="(//td[@class='propValue'])[9]";
    public static final String currnecyPayUPaymentInfo="(//td[@class='propValue'])[10]";
    public static final String amountFrompaymentInfo="(//td[@class='propValue'])[11]";
    public static final String amountForPayUPaymentInfo="(//td[@class='propValue'])[12]";
    public static final String maskedCardOnUI="((//th[text()='Transaction ID'])[1]/ancestor::table//td[7])[1]";
    
    //psp login 
    public static final String emailPSP="//input[@name='email']";
    public static final String passwordPSP="//input[@name='password']";
    public static final String signinButtonPSP="//button[text()='Sign in']";
    public static final String transactionPSPModule="//div[text()='Transactions']";
    
    
    //search transactions in PSP - Matrix
    public static final String txID="//div[text()='Tx ID']";
    public static final String searchTXIDPSP="//input[@class='filter-search-input']";
    public static final String txinPSP="(//div[@class='multiline-cell ellipsis-cell'])[1]/a";
    public static final String amountAndCurrency="(//div[@class='multiline-cell ellipsis-cell'])[2]";
    public static final String maskCardPSP="//a[@class='item card-mask']/div";
    public static final String successStatusFromPSP="//div[@class='badge-cell active']";

   //Transaction - PAYU
    public static final String loginPayUButton="//button[@type='submit']";
    public static final String skipFlowPayU="//p[text()='Skip tour']";
    public static final String transactionPayU="//p[text()='Transactions']";

   // payhost 6406
    
    public static final String submitButton="//button[text()='Submit']";





}
