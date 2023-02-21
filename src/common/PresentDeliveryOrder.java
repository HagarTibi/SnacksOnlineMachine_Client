package common;

import java.io.Serializable;

/**
 * Entity delivery orders to be presented to the customer/subscriber in his
 * Tracker window ("Show Active Orders" button).
 */

public class PresentDeliveryOrder implements Serializable{
	private String order_code;
	private String confirm_date;
	private String estimated;
	private String address;
	private String status;
	
	public PresentDeliveryOrder(String order_code, String confirm_date, String estimated, String address) {
		this.order_code=order_code;
		this.confirm_date=confirm_date;
		this.estimated=estimated;
		this.address=address;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getConfirm_date() {
		return confirm_date;
	}
	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}
	public String getEstimated() {
		return estimated;
	}
	public void setEstimated(String estimated) {
		this.estimated = estimated;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public  String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}