package com.paysecure.Page;

import java.util.Map;
import org.openqa.selenium.WebDriver;

public class RouteManager {
    private static final Map<String, String> routeCache = new java.util.concurrent.ConcurrentHashMap<>();
    
    private RouteManager() {}
    
    public static void clearCache() {
        routeCache.clear();
        System.out.println("✓ Route cache cleared");
    }
    
    public static void ensureRoute(
            WebDriver driver,
            String partialMerchant,
            String Merchant,
            String partialPaymentMethod,
            String paymentMethod,
            String partialCurrency,
            String currency,
            String psp,
            String routing,
            String routingValue
    ) throws InterruptedException {
        
        String routeKey = Merchant + "|" + paymentMethod + "|" + currency;
        
        //  Check cache
        if (routeCache.containsKey(routeKey) && routeCache.get(routeKey).equalsIgnoreCase(psp)) {
            System.out.println(" Route CACHED: " + psp + " (Skipped configuration)");
            return;
        }
        
        //  Log what's happening
        if (routeCache.containsKey(routeKey)) {
            System.err.println(" Route CHANGE: " + routeKey + " (from " + routeCache.get(routeKey) + " to " + psp + ")");
        } else {
            System.err.println(" Route NEW: Configuring " + psp);
        }
        
        MerchantRoutingPageLimitAndCharges routingPage = new MerchantRoutingPageLimitAndCharges(driver);
        routingPage.navigateUptoLimitAndCharges();
        routingPage.selectMerchant(partialMerchant, Merchant);
        routingPage.selectCurrency(partialCurrency, currency);
        routingPage.selectPaymentMethod(partialPaymentMethod, paymentMethod);
        routingPage.getDetailsForLimitAndCharges();
        routingPage.fillAllTextboxesWithTab();
        routingPage.selectRouteTo(routing);
        routingPage.selectRoutePoint(routingValue);
        routingPage.saveButton();
        routingPage.finalsaveButton();
        
        routeCache.put(routeKey, psp);
        System.out.println("✓ Route CONFIGURED: " + psp + " is now active");
    }
}
