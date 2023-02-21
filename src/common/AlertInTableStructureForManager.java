package common;

import java.io.Serializable;

/**
 * Entity class for manager alerts table - inserted into observable list 
 */

public class AlertInTableStructureForManager implements Serializable {
	
	private String alertID;
	private String machineID;
	private String date;
	private String status;
	
	public AlertInTableStructureForManager(String alertID, String machineID, String date, String status) {
		this.alertID = alertID;
		this.machineID = machineID;
		this.date = date;
		this.status = status;
	}

	public String getAlertID() {
		return alertID;
	}

	public void setAlertID(String alertID) {
		this.alertID = alertID;
	}

	public String getMachineID() {
		return machineID;
	}

	public void setMachineID(String machineID) {
		this.machineID = machineID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}