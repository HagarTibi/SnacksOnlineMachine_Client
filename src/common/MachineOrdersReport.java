package common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Entity for orders report of machines, including items and their amounts
 */

public class MachineOrdersReport implements Serializable {

    private String location;


    private String date;
    private String mostPurchasedItemName;
    private String Area;
    private String totalOrdersCount;
    private HashMap<String, String> itemsAndAmountHashMap = new HashMap<String, String>();


    public MachineOrdersReport(String location, HashMap<String, String> itemsAndAmountHashMap, String totalOrdersCount, String date, String mostPurchasedItemName) {
        this.itemsAndAmountHashMap = itemsAndAmountHashMap;
        this.totalOrdersCount = totalOrdersCount;

        this.mostPurchasedItemName = mostPurchasedItemName;
        this.date=date;
        this.location=location;
    }

    public MachineOrdersReport() {

    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getMostPurchasedItemName() {
        return mostPurchasedItemName;
    }

    public String getArea() {
        return Area;
    }

    public String getTotalOrdersCount() {
        return totalOrdersCount;
    }


    public HashMap<String, String> getItemsAndAmountHashMap() {
        return itemsAndAmountHashMap;
    }


}