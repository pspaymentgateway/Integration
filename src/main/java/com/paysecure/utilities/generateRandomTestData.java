package com.paysecure.utilities;

import java.util.Random;
import java.util.UUID;

import org.testng.Reporter;

public class generateRandomTestData {

	
	  public static String generateRandomFirstName() {
	        String firstName = "john" + UUID.randomUUID().toString().substring(0, 3);
	        Reporter.log("Generated First Name: " + firstName, true);
	        return firstName;
	    }

	    // Generates a random last name like "Doexyz"
	    public static String generateRandomLastName() {
	        String lastName = "Doe" + UUID.randomUUID().toString().substring(0, 3);
	        Reporter.log("Generated Last Name: " + lastName, true);
	        return lastName;
	    }

	    // Generates a random email like "johnDoeab@mail.com"
	    public static String generateRandomEmail() {
	        String email = "nairobiPalermo" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
	        Reporter.log("Generated Email: " +email, true);
	        return email;
	    }

	    // Generates a random double value between 0.00 and 100.00 as a string
	    public static String generateRandomDouble() {
	        Random random = new Random();
	        double value = random.nextDouble() * 100; // range 0.00 - 100.00
	        String formattedValue = String.format("%.2f", value);
	        Reporter.log("Generated Random Double: " + formattedValue, true);
	        return formattedValue;
	    }
	    
	    public static String generateRandomDoublePrice(
	            double minAmount,
	            double maxAmount,
	            double defaultAmount) {

	        Random random = new Random();

	        Reporter.log(
	            "Input Amounts - Min: " + minAmount +
	            ", Max: " + maxAmount +
	            ", Default: " + defaultAmount,
	            true
	        );

	        /* =====================================================
	           STEP 1: Validate Range
	           ===================================================== */
	        boolean isValidRange = (minAmount > 0 && maxAmount > minAmount);

	        if (!isValidRange) {

	            Reporter.log(
	                "Invalid range detected. Min=" + minAmount +
	                ", Max=" + maxAmount,
	                true
	            );

	            if (defaultAmount > 0) {
	                Reporter.log(
	                    "Provided default amount is used because range is invalid.",
	                    true
	                );
	                return String.format("%.2f", defaultAmount);
	            }

	            Reporter.log(
	                "System fallback amount is used because both range and default are invalid.",
	                true
	            );
	            return "100.00";
	        }

	        /* =====================================================
	           STEP 2: Boundary Classification Message
	           ===================================================== */
	        String boundaryMessage;

	        if (defaultAmount == minAmount) {
	            boundaryMessage = "Input amount is equal to minimum boundary.";
	        } else if (defaultAmount == maxAmount) {
	            boundaryMessage = "Input amount is equal to maximum boundary.";
	        } else if (defaultAmount < minAmount) {
	            boundaryMessage = "Input amount is less than minimum boundary.";
	        } else if (defaultAmount > maxAmount) {
	            boundaryMessage = "Input amount is greater than maximum boundary.";
	        } else {
	            boundaryMessage = "Input amount is within the valid range.";
	        }

	        Reporter.log(boundaryMessage, true);

	        /* =====================================================
	           STEP 3: Boundary Intent Check
	           ===================================================== */
	        if (defaultAmount == minAmount || defaultAmount == maxAmount) {

	            Reporter.log(
	                "Boundary value provided by tester. Using default amount directly.",
	                true
	            );

	            return String.format("%.2f", defaultAmount);
	        }

	        /* =====================================================
	           STEP 4: Validate Default Amount
	           ===================================================== */
	        boolean isValidDefault =
	                defaultAmount > 0 &&
	                defaultAmount >= minAmount &&
	                defaultAmount <= maxAmount;

	        /* =====================================================
	           STEP 5: Decision Logic
	           ===================================================== */
	        if (isValidDefault) {

	            Reporter.log(
	                "Valid range and default detected. Generating random value.",
	                true
	            );

	            double value =
	                    minAmount + random.nextDouble() * (maxAmount - minAmount);

	            value = Math.round(value * 100.0) / 100.0;

	            if (value < minAmount) {
	                value = minAmount;
	            }

	            if (value > maxAmount) {
	                value = maxAmount;
	            }

	            return String.format("%.2f", value);
	        }

	        Reporter.log(
	            "Default amount is outside valid range. Using system fallback value.",
	            true
	        );

	        return "100.00";
	    }




	    
	 // Generates a random Indian mobile number like "9876543210"
	    public static String generateRandomIndianMobileNumber() {
	        // Indian mobile numbers start with 6, 7, 8, or 9
	        String[] startDigits = {"6", "7", "8", "9"};
	        Random random = new Random();

	        // Pick a random starting digit
	        String firstDigit = startDigits[random.nextInt(startDigits.length)];

	        // Generate remaining 9 random digits
	        StringBuilder mobileNumber = new StringBuilder(firstDigit);
	        for (int i = 0; i < 9; i++) {
	            mobileNumber.append(random.nextInt(10));
	        }

	        Reporter.log("Generated Indian Mobile Number: " + mobileNumber.toString(), true);
	        return mobileNumber.toString();
	    }
	    
	 //  Generates a random Merchant Customer ID like "D229"
	    public static String generateRandomMerchantCustomerId() {
	        Random random = new Random();
	        int number = 100 + random.nextInt(900); // Generates a number between 100–999
	        String merchantCustomerId = "Z" + number;
	        
	        Reporter.log("Generated Merchant Customer ID: " + merchantCustomerId, true);
	        return merchantCustomerId;
	    }



}
