package com.paysecure.utilities;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/*---------------------------------------------------------------*/
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.openqa.selenium.WebDriver;
/*---------------------------------------------------------------*/
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;

import org.testng.Reporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.paysecure.base.baseClass;


import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class DBConnection {
	static MongoCollection<Document> collection;
	static MongoClient mongoClient;
	public void createConnectionForDB() {
	    try {
	       
	        String uri = baseClass.getProp().getProperty("mongo.uri", 
	                        "mongodb://PGS:PGSReady@172.31.88.8:27127/?authSource=PGS");

	        mongoClient = MongoClients.create(uri);

	        // 🔹 Read DB & collection name from config if possible
	        String dbName = baseClass.getProp().getProperty("mongo.db", "PGS");
	        String collectionName = baseClass.getProp().getProperty("mongo.collection", "purchase");

	        MongoDatabase database = mongoClient.getDatabase(dbName);
	        collection = database.getCollection(collectionName);

	        Reporter.log("✅ Connected to MongoDB: " + dbName + "." + collectionName, true);

	    } catch (Exception e) {
	        Reporter.log("❌ Failed to connect to MongoDB: " + e.getMessage(), true);
	        e.printStackTrace();
	        throw e; // rethrow so tests fail fast
	    }
	}

	
	

	
	public static long startEpoch;
	public static long endEpoch;

	public void DateToEpochSeconds(String reportTime) {
	    Reporter.log("🕒 Report time range (UI): " + reportTime, true);

	    String[] parts = reportTime.split(" - ");
	    String startStr = parts[0].trim();
	    String endStr = parts[1].trim();

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	    ZoneId zone = ZoneId.of("Asia/Kolkata"); // "Asia/Calcutta" is legacy alias

	    LocalDateTime startLdt = LocalDateTime.parse(startStr, formatter);
	    LocalDateTime endLdt = LocalDateTime.parse(endStr, formatter);

	    startEpoch = startLdt.atZone(zone).toEpochSecond();
	    endEpoch = endLdt.atZone(zone).toEpochSecond();

	    Reporter.log("📅 Start DateTime: " + startLdt + " → Epoch: " + startEpoch, true);
	    Reporter.log("📅 End DateTime  : " + endLdt + " → Epoch: " + endEpoch, true);
	}

	
	
	public static int getTodaysTransactions(String status) {
	    LocalDate today = LocalDate.now();

	    int day = today.getDayOfMonth();
	    int month = today.getMonthValue();
	    int year = today.getYear();

	    Reporter.log("📅 Today's date: " + today, true);

	    Bson query = Filters.and(
	            Filters.eq("status", status.toUpperCase()), // keep consistency with DB
	            Filters.eq("day", String.valueOf(day)),
	            Filters.eq("month", String.valueOf(month)),
	            Filters.eq("year", String.valueOf(year))
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total '" + status.toUpperCase() + 
	                 "' transactions on " + today + " = " + count, true);

	    mongoClient.close();
	    return (int) count;
	}

	
	public static int getTodaysTransactionsUsingStatusAndMerchant(String status, String merchantName) {
	    LocalDate today = LocalDate.now();

	    int day = today.getDayOfMonth();
	    int month = today.getMonthValue();
	    int year = today.getYear();

	    Reporter.log("📅 Today's date: " + today, true);

	    Bson query = Filters.and(
	            Filters.eq("status", status.toUpperCase()),  // ensure consistency
	            Filters.eq("merchantName", merchantName),
	            Filters.eq("day", String.valueOf(day)),
	            Filters.eq("month", String.valueOf(month)),
	            Filters.eq("year", String.valueOf(year))
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total '" + status.toUpperCase() + "' transactions for merchant '" 
	                 + merchantName + "' on " + today + " = " + count, true);

	    mongoClient.close();
	    return (int) count;
	}

	
	public static int getSingleMerchantTransaction(String merchantName) {
	    LocalDate today = LocalDate.now();

	    int day = today.getDayOfMonth();
	    int month = today.getMonthValue();
	    int year = today.getYear();

	    Reporter.log("📅 Today's date: " + today, true);

	    Bson query = Filters.and(
	            Filters.eq("merchantName", merchantName),
	            Filters.eq("day", String.valueOf(day)),
	            Filters.eq("month", String.valueOf(month)),
	            Filters.eq("year", String.valueOf(year))
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for merchant '" + merchantName +
	                 "' on " + today + " = " + count, true);

	    mongoClient.close();
	    return (int) count;
	}

	
	
	
	
	
	
	
	
	
	public static int getYesterdaysTransactions(String status) {
	    LocalDate yesterday = LocalDate.now().minusDays(1);

	    int day = yesterday.getDayOfMonth();
	    int month = yesterday.getMonthValue();
	    int year = yesterday.getYear();

	    Reporter.log("📅 Yesterday's date: " + yesterday, true);

	    Bson query = Filters.and(
	            Filters.eq("status", status.toUpperCase()), // keep consistency with DB values
	            Filters.eq("day", String.valueOf(day)),
	            Filters.eq("month", String.valueOf(month)),
	            Filters.eq("year", String.valueOf(year))
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total '" + status.toUpperCase() +
	                 "' transactions on " + yesterday + " = " + count, true);

	    mongoClient.close();
	    return (int) count;
	}

	
	
	public static int getYesterdaysTransactionsForSingleMerchant(String merchantName) {
	    LocalDate yesterday = LocalDate.now().minusDays(1);

	    int day = yesterday.getDayOfMonth();
	    int month = yesterday.getMonthValue();
	    int year = yesterday.getYear();

	    Reporter.log("📅 Yesterday's date: " + yesterday, true);

	    Bson query = Filters.and(
	            Filters.eq("merchantName", merchantName),
	            Filters.eq("day", String.valueOf(day)),
	            Filters.eq("month", String.valueOf(month)),
	            Filters.eq("year", String.valueOf(year))
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for merchant '" + merchantName +
	                 "' on " + yesterday + " = " + count, true);

	    mongoClient.close();
	    return (int) count;
	}

	


	public int getStatusCountForLastNDays(String status) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    // Build query: { status: "PAID", is_test: false, parentId: 0, created_on: { $gte: startEpoch, $lte: endEpoch } }
	    Bson query = Filters.and(
	            Filters.eq("status", status.toUpperCase()),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total '" + status.toUpperCase() + "' transactions in last selected days: " + count, true);

	    return (int) count;
	}

	
	public int getStatusCountForLastNDaysWithDateStatusMerchant(String status, String merchantName) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    // Build query: { status: "PAID", merchantName: "...", created_on: { $gte: startEpoch, $lte: endEpoch } }
	    Bson query = Filters.and(
	            Filters.eq("status", status.toUpperCase()),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0),
	            Filters.eq("merchantName", merchantName),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total '" + status.toUpperCase() + "' transactions for merchant [" 
	                 + merchantName + "] in last selected days: " + count, true);

	    return (int) count;
	}


	public int getStatusCountForLastNDaysWithDateStatusMultipleMerchant(String status, String merchantName, String MerchantName) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    // Build query: { status: "PAID", created_on: { $gte: startEpoch, $lte: endEpoch } }
	    Bson query = Filters.and(
	            Filters.eq("status", status.toUpperCase()),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0),
	            Filters.in("merchantName", merchantName, MerchantName),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total '" + status.toUpperCase() + "' transactions for merchants [" 
	                 + merchantName + ", " + MerchantName + "] in last selected days: " + count, true);

	    return (int) count;
	}

	    
	public int getDBDataWithRespectToDateWiseFilter() {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    Bson query = Filters.and(
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions (date-wise filter) in last selected days: " + count, true);

	    return (int) count;
	}

	

	public int bankNameBankMidAndDateAndCheckWithDB(String bankname, String bankmid) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    Bson query = Filters.and(
	            Filters.in("bankname", bankname),
	            Filters.in("bankmid", bankmid),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for Bank: '" + bankname 
	                 + "', MID: '" + bankmid 
	                 + "' in last selected days: " + count, true);

	    return (int) count;
	}

	    
	public int bankNameBankMidAndDateAndMerchantNameCheckWithDB(String merchantName, String bankname, String bankmid) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    Bson query = Filters.and(
	            Filters.in("merchantName", merchantName),
	            Filters.in("bankname", bankname),
	            Filters.in("bankmid", bankmid),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for merchant: " + merchantName 
	                 + ", Bank: " + bankname 
	                 + ", MID: " + bankmid 
	                 + " in last selected days: " + count, true);

	    return (int) count;
	}

	
	public int bankNameBankMidAndAndCheckWithDB(String bankname, String bankmid) {
	//    Reporter.log("📅 startEpoch: " + startEpoch, true);
	 //   Reporter.log("📅 endEpoch: " + endEpoch, true);

	    Bson query = Filters.and(
	            Filters.in("bankname", bankname),
	            Filters.in("bankmid", bankmid),
//	            Filters.gte("created_on", startEpoch),
//	            Filters.lte("created_on", endEpoch),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for Bank: '" + bankname 
	                 + "', MID: '" + bankmid 
	                 + "' in last selected days: " + count, true);

	    return (int) count;
	}
	
	public int selectMerchantAndDateAndCheckWithDB(String merchantName) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    Bson query = Filters.and(
	            Filters.in("merchantName", merchantName),
	            Filters.gte("created_on", startEpoch),
	            Filters.lte("created_on", endEpoch),
	            Filters.eq("is_test", false),
	            Filters.eq("parentId", 0)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for merchant: " + merchantName 
	                + " in last selected days: " + count, true);

	    return (int) count;
	}
	
	
	public int selectTransactionIDAndDateAndCheckWithDB(String transactionID) {
	    Reporter.log("📅 startEpoch: " + startEpoch, true);
	    Reporter.log("📅 endEpoch: " + endEpoch, true);

	    Bson query = Filters.and(
	    		Filters.eq("_id", new ObjectId(transactionID))
//	            Filters.gte("created_on", startEpoch),
//	            Filters.lte("created_on", endEpoch),
//	            Filters.eq("is_test", true),
//	           Filters.eq("parentId", 0)
	    );

	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total transactions for merchant: " + transactionID 
	                + " in last selected days: " + count, true);

	    return (int) count;
	}
	
	public int getCountOfEmail(String emailId) {
	    Reporter.log("📧 Checking documents for email: " + emailId, true);

	    // Create MongoDB filter query
	    Bson query = Filters.eq("emailId", emailId);

	    // Count documents that match this query
	    long count = collection.countDocuments(query);

	    Reporter.log("📦 Total documents for emailId " + emailId + ": " + count, true);

	    return (int) count;
	}
	
	//------------------------------------------ 
	
	public int getDistinctDayInYearCount(String emailId) {
	    Reporter.log("📧 Checking distinct dayInYear count for email: " + emailId, true);

	    // Create MongoDB filter query
	    Bson matchQuery = Filters.and(
	        Filters.eq("emailId", emailId),
	        Filters.eq("parentId", 0),
	        Filters.in("year", Arrays.asList(2024, 2025))
	    );

	    // Build aggregation pipeline
	    List<Bson> pipeline = Arrays.asList(
	        Aggregates.match(matchQuery),
	        Aggregates.group("$dayInYear"), // group by dayInYear (distinct)
	        Aggregates.count("distinctDayInYearCount") // count unique values
	    );

	    // Run aggregation
	    AggregateIterable<Document> result = collection.aggregate(pipeline);

	    // Extract the count from the result
	    int count = 0;
	    Document doc = result.first();
	    if (doc != null) {
	        count = doc.getInteger("distinctDayInYearCount", 0);
	    }

	    Reporter.log("📅 Total distinct dayInYear values for " + emailId + ": " + count, true);

	    return count;
	}


	

}
