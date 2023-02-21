package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A class representing an inventory report for a given machine in a given month and year.
 * @auther: G-10
 */

public class InventoryReport implements Serializable {

    private String month, year, machine_id, location;
    private static final long serialVersionUID = 1L;
    private HashMap<String, Integer> itemsAndAmount = new HashMap<String, Integer>();
    private HashMap<String, Integer> itemsBelowThreshold = new HashMap<String, Integer>();
    private HashMap<String, Integer> unavailable_items = new HashMap<String, Integer>();
    private Integer threshold_level;
    private ArrayList<ProductStatus> productStatus = new ArrayList<>();

    /**
     * Constructs a new InventoryReport with the given parameters.
     *
     * @param month the month of the inventory report
     * @param year the year of the inventory report
     * @param machine_id the ID of the machine for which the report is generated
     * @param location the location of the machine
     */
    public InventoryReport(String month, String year, String machine_id, String location) {
        this.month = month;
        this.year = year;
        this.machine_id = machine_id;
        this.location = location;
    }

    /**
     * Returns the month of the inventory report.
     *
     * @return the month of the inventory report
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month of the inventory report.
     *
     * @param month the month of the inventory report
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Returns the year of the inventory report.
     *
     * @return the year of the inventory report
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of the inventory report.
     *
     * @param year the year of the inventory report
     */
    public void setYear(String year) {
        this.year = year;
    }
    /**
     * Returns the ID of the machine for which the inventory report was generated.
     *
     * @return the ID of the machine for which the inventory report was generated
     */
    public String getMachine_id() {
        return machine_id;
    }

    /**
     * Sets the ID of the machine for which the inventory report was generated.
     *
     * @param machine_id the ID of the machine for which the inventory report was generated
     */
    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    /**
     * Returns a mapping of items to their quantities in the inventory report.
     *
     * @return a mapping of items to their quantities in the inventory report
     */
    public HashMap<String, Integer> getItemsAndAmount() {
        return itemsAndAmount;
    }

    /**
     * Sets the mapping of items to their quantities in the inventory report.
     *
     * @param itemsAndAmount the mapping of items to their quantities in the inventory report
     */
    public void setItemsAndAmount(HashMap<String, Integer> itemsAndAmount) {
        this.itemsAndAmount = itemsAndAmount;
    }

    /**
     * Returns a mapping of items to their quantities that are unavailable in the inventory report.
     *
     * @return a mapping of items to their quantities that are unavailable in the inventory report
     */
    public  HashMap<String, Integer> getUnavailable_items() {
        return unavailable_items;
    }

    /**
     * Sets the unavailable items for this Product.
     * @param unavailable_items the new unavailable items
     */
    public void setUnavailable_items( HashMap<String, Integer> unavailable_items) {
        this.unavailable_items = unavailable_items;
    }

    /**
     * Returns the items below threshold for this Product.
     * @return the items below threshold
     */
    public HashMap<String, Integer> getItemsBelowThreshold() {
        return itemsBelowThreshold;
    }

    /**
     * Sets the items below threshold for this Product.
     * @param itemsBelowThreshold the new items below threshold
     */
    public void setItemsBelowThreshold(HashMap<String, Integer> itemsBelowThreshold) {
        this.itemsBelowThreshold = itemsBelowThreshold;
    }

    /**
     * Generates the items hashmap for this Product.
     * @param items the items to add to the hashmap
     */
    public void generateItemsHashmap(String items){
        String[] items_amount = items.split(",", 0);
        for( int i = 0; i < items_amount.length - 1; i+=2)
        {
            itemsAndAmount.put(items_amount[i], Integer.parseInt(items_amount[i+1]));
            productStatus.add(new ProductStatus(items_amount[i]));
        }
    }

    /**
     * Generates the items below threshold for this Product.
     * @param items the items to add to the hashmap
     */
    public void generateItemsBelowThreshold(String items){
        String[] items_threshold = items.split(",", 0);
        for( int i = 0; i < items_threshold.length - 1; i+=2)
        {
            itemsBelowThreshold.put(items_threshold[i], Integer.parseInt(items_threshold[i+1]));
            for (ProductStatus name:productStatus ) {
                if (name.getName().equals(items_threshold[i])) {
                    name.setThresholdCount(Integer.parseInt(items_threshold[i + 1]));
                    break;
                }
            }
        }
    }

    /**
     * Generates the unavailable items for this Product.
     * @param items the items to add to the hashmap
     */
    public void generateUnavailableItems(String items){
        String[] items_unavailable = items.split(",", 0);
        for( int i = 0; i < items_unavailable.length; i+=2) {
            unavailable_items.put(items_unavailable[i], Integer.parseInt(items_unavailable[i + 1]));
            for (ProductStatus name : productStatus) {
                if (name.getName().equals(items_unavailable[i])) {
                    name.setUnavailableCount(Integer.parseInt(items_unavailable[i + 1]));
                    break;
                }
            }
        }
    }


    /**
     * Returns the threshold level for this Product.
     * @return the threshold level
     */
    public Integer getThreshold_level() {
        return threshold_level;
    }

    /**
     * Sets the threshold level for this Product.
     * @param threshold_level the new threshold level
     */
    public void setThreshold_level(Integer threshold_level) {
        this.threshold_level = threshold_level;
    }

    /**
     * Returns the location of this Product.
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of this Product.
     * @param location the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the status of this Product.
     * @return the product status
     */
    public ArrayList<ProductStatus> getProductStatus() {
        return productStatus;
    }

    /**
     * Sets the status of this Product.
     * @param productStatus the new product status
     */
    public void setProductStatus(ArrayList<ProductStatus> productStatus) {
        this.productStatus = productStatus;
    }


}
