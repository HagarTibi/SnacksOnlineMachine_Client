package common;

import java.io.Serializable;

/**
 * Entity to save an item data in the aspect of inventory management.
 * This entity is created for the assisting in the making of an inventory report
 * that presents every item's count of the times it's been under threshold level and 
 * in unavailable status (amount of 0). 
 */

public class ProductStatus implements Serializable {

    private String name;
    private Integer thresholdCount;
    private Integer unavailableCount;

    public ProductStatus(String name) {

        this.name = name;
        thresholdCount = 0;
        unavailableCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getThresholdCount() {
        return thresholdCount;
    }

    public void setThresholdCount(Integer thresholdCount) {
        this.thresholdCount = thresholdCount;
    }

    public Integer getUnavailableCount() {
        return unavailableCount;
    }

    public void setUnavailableCount(Integer unavailableCount) {
        this.unavailableCount = unavailableCount;
    }
}
