package common;

import java.io.Serializable;

/**
 * Entity for manager and inventory worker to manage supply,
 * items that are below threshold levels
 */

public class ItemsAlert implements Serializable {
	private String alert_id;
	private String machine_id;
	private String date;
	private AlertStatus status;
	private String items_in_alert;
	private String manager_username;
	private String worker_username;
	
	public ItemsAlert(String alert_id,String machine_id,String date,
			AlertStatus status, String items_in_alert,String manager_username, String worker_username) {
		this.setAlert_id(alert_id);
		this.setMachine_id(machine_id);
		this.setDate(date);
		this.setStatus(status);
		this.setItems_in_alert(items_in_alert);
		this.setManager_username(manager_username);
		this.setWorker_username(worker_username);
	}

	public String getAlert_id() {
		return alert_id;
	}

	public void setAlert_id(String alert_id) {
		this.alert_id = alert_id;
	}

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}

	public String getItems_in_alert() {
		return items_in_alert;
	}

	public void setItems_in_alert(String items_in_alert) {
		this.items_in_alert = items_in_alert;
	}

	public String getManager_username() {
		return manager_username;
	}

	public void setManager_username(String manager_username) {
		this.manager_username = manager_username;
	}

	public String getWorker_username() {
		return worker_username;
	}

	public void setWorker_username(String worker_username) {
		this.worker_username = worker_username;
	}
	
}