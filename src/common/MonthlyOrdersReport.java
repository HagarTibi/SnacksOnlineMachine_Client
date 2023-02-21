package common;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Entity for monthly orders report that is sent to the DB
 */

public class MonthlyOrdersReport implements  Serializable {

	private String totalOrdersCount , numberOfCanceledOrders;
	private HashMap<String, String> itemsAndAmountHashMap = new HashMap<String, String>();

	private String machine_location;

	private String month,location;
	private String year;
	private String mostPurchasedItemName;
	private String Area;

	public MonthlyOrdersReport(String month, String year, String location) {
		this.month = month;
		this.year = year;
		this.location = location;
	}

	public MonthlyOrdersReport(String machine_location, HashMap<String, String> itemsAndAmountHashMap, String totalOrdersCount, String month, String year, String mostPurchasedItemName, String area) {
		this.itemsAndAmountHashMap = itemsAndAmountHashMap;
		this.machine_location=machine_location;
		this.totalOrdersCount = totalOrdersCount;
		this.month=month;
		this.year=year;
		this.mostPurchasedItemName = mostPurchasedItemName;
		this.Area = area;
	}
	public String getTotalOrdersCount() {
		return totalOrdersCount;
	}




}