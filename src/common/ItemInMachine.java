package common;

import java.io.Serializable;

/**
 * describes item in a local machine and it's current amount
 * as imported from the DB
 */


public class ItemInMachine implements Serializable{
	private String item_code;
	private String item_name;
	private int item_amount_in_machine;
	
	public ItemInMachine(String item_code, String item_name, int item_amount_in_machine) {
		super();
		this.item_code = item_code;
		this.item_name = item_name;
		this.item_amount_in_machine = item_amount_in_machine;
	}

	public String getItem_code() {
		return item_code;
	}

	public String getItem_name() {
		return item_name;
	}

	public int getItem_amount_in_machine() {
		return item_amount_in_machine;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public void setItem_amount_in_machine(int item_amount_in_machine) {
		this.item_amount_in_machine = item_amount_in_machine;
	}
	
}
