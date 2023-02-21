package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Entity of remote order - refers to OL order of self pickup or delivery.
 * It extends Order object to help in the saving of the order in the 
 * end of the purchase.
 */

public class RemoteOrder extends Order implements Serializable {
	private String order_type; // REMOTE_PiCKUP or REMOTE_DELIVERY
	private String status;
	private String estimated_delivery_date = null;
	private String order_received_date = null;
	private String delivery_address;

	public RemoteOrder(){}
	public RemoteOrder(String order_code,String customer_id, String order_confirmed_date, String status, String estimated_delivery_date, String order_received_date, String delivery_address) {
		super(order_code,customer_id, order_confirmed_date);
		this.status = status;
		this.estimated_delivery_date = estimated_delivery_date;
		this.order_received_date = order_received_date;
		this.delivery_address = delivery_address;
	}

	public String getDelivery_address() {
		return delivery_address;
	}
	public void setDelivery_address(String delivery_address) {
		this.delivery_address = delivery_address;
	}
	public String getOrder_type() {
		return order_type;
	}
	public String getStatus() {
		return status;
	}
	public String getEstimated_delivery_date() {
		return estimated_delivery_date;
	}
	public String getOrder_received_date() {
		return order_received_date;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setEstimated_delivery_date(String estimated_delivery_date) {
		this.estimated_delivery_date = estimated_delivery_date;
	}
	public void setOrder_received_date(String order_received_date) {
		this.order_received_date = order_received_date;
	}


}
