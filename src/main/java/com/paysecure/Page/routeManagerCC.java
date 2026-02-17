package com.paysecure.Page;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebDriver;

public class routeManagerCC {
	// Cache: Merchant | PaymentMethod | Currency  -> PSP
    private static final Map<String, String> routeCache = new ConcurrentHashMap<>();

    private routeManagerCC() {}

    public static void clearCache() {
        routeCache.clear();
        System.out.println("✓ Route cache cleared");
    }

    public static void ensureRoute(
            WebDriver driver,
            String partialMerchant,
            String merchant,
            String partialPaymentMethod,
            String paymentMethod,
            String partialCurrency,
            String currency,
            String psp,
            String routing,
            String routingValue
    ) throws InterruptedException {

        String routeKey = merchant + "|" + paymentMethod + "|" + currency;

        // ✅ CACHE CHECK
        if (routeCache.containsKey(routeKey)
                && routeCache.get(routeKey).equalsIgnoreCase(psp)) {

            System.out.println("✓ Route CACHED: " + routeKey + " -> " + psp);
            return;
        }

        // 🔄 ROUTE CHANGE / NEW
        if (routeCache.containsKey(routeKey)) {
            System.out.println("↺ Route CHANGE: " + routeKey +
                    " (" + routeCache.get(routeKey) + " → " + psp + ")");
        } else {
            System.out.println("➕ Route NEW: " + routeKey + " -> " + psp);
        }

        // UI Routing
        MerchantRoutingPageLimitAndCharges routingPage =
                new MerchantRoutingPageLimitAndCharges(driver);

        routingPage.navigateUptoLimitAndCharges();
        routingPage.selectMerchant(partialMerchant, merchant);
        routingPage.selectCurrency(partialCurrency, currency);
        routingPage.selectPaymentMethod(partialPaymentMethod, paymentMethod);
        routingPage.getDetailsForLimitAndCharges();
        routingPage.fillAllTextboxesWithTab();
        routingPage.selectRouteTo(routing);
        routingPage.selectRoutePoint(routingValue);
        routingPage.saveButton();
        routingPage.finalsaveButton();

        // ✅ UPDATE CACHE
        routeCache.put(routeKey, psp);
        System.out.println("✓ Route CONFIGURED: " + routeKey + " -> " + psp);
    }
}
