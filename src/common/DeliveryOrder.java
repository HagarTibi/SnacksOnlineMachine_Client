package common;

import java.io.Serializable;

/**
 * Entity for specific type of order - Delivery. include address and estimated delivery time
 */

public class DeliveryOrder implements Serializable {

    private String id;
    private String address;
    private String purchaseDate;
    private String estimatedDate;
    private String receivedDate;
    private String status;

    public DeliveryOrder(String id, String address, String purchaseDate, String estimatedDate, String receivedDate, String status) {
        this.id = id;
        this.address = address;
        this.purchaseDate = purchaseDate;
        this.estimatedDate = estimatedDate;
        this.receivedDate = receivedDate;
        this.status = status;
    }

    public DeliveryOrder() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

