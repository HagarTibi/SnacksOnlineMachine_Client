package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Entity for monthly orders report that is imported from the DB
 */

public class MonthlyOrdersReportReturns implements Serializable {

    private ArrayList<String> machine_location;
    private String mostPurchasedItemName;
    private String Area;
    private String date;



    private String totalOrdersCount , numberOfCanceledOrders;
    private  ArrayList<Double> percentageOfMachineOrdersArray;





    public MonthlyOrdersReportReturns(ArrayList<String> machine_location, String date, ArrayList<Double> percentageOfMachineOrdersArray, String Area, String mostPurchasedItemName, String totalOrdersCount) {
        this.percentageOfMachineOrdersArray = percentageOfMachineOrdersArray;
        this.totalOrdersCount = totalOrdersCount;
        this.mostPurchasedItemName = mostPurchasedItemName;
        this.Area = Area;
        this.date = date;
        this.machine_location=machine_location;

    }

    public MonthlyOrdersReportReturns() {

    }

    public ArrayList<String> getMachine_location() {
        return machine_location;
    }


    public String getMostPurchasedItemName() {
        return mostPurchasedItemName;
    }

    public void setMostPurchasedItemName(String mostPurchasedItemName) {
        this.mostPurchasedItemName = mostPurchasedItemName;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalOrdersCount() {
        return totalOrdersCount;
    }

    public void setTotalOrdersCount(String totalOrdersCount) {
        this.totalOrdersCount = totalOrdersCount;
    }





    public ArrayList<Double> getPercentageOfMachineOrdersArray() {
        return percentageOfMachineOrdersArray;
    }

    public void setPercentageOfMachineOrdersArray(ArrayList<Double> percentageOfMachineOrdersArray) {
        this.percentageOfMachineOrdersArray = percentageOfMachineOrdersArray;
    }


}