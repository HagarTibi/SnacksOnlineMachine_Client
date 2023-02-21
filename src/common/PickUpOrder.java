package common;

import java.io.Serializable;

/**
 * Entity for the order type of Pickup order.
 * This entity refers to an OL configuration order that is for pickup in local machine
 */

public class PickUpOrder implements Serializable{
	private String order_code;
	private String confirm_date;
	private String machine_id;
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PickUpOrder(String order_code,String confirm_date, String machine_id) {
		this.order_code=order_code;
		this.confirm_date=confirm_date;
		this.machine_id=machine_id;
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

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}
}