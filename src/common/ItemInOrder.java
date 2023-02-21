package common;

import java.io.Serializable;

/**
 * describes item to be added to a final order, before saving it in the DB
 */

public class ItemInOrder implements Serializable{
	private String item_name;
	private int item_amount;
	private float item_price;
	
	public ItemInOrder(String item_name, int item_amount, float item_price) {
		super();
		this.item_name = item_name;
		this.item_amount = item_amount;
		this.item_price = item_price;
	}

	public ItemInOrder(int item_amount) {
		this.item_amount = item_amount;
	}

	public String getItem_name() {
		return item_name;
	}

	public int getItem_amount() {
		return item_amount;
	}

	public float getItem_price() {
		return item_price;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public void setItem_amount(int item_amount) {
		this.item_amount = item_amount;
	}

	public void setItem_price(float item_price) {
		this.item_price = item_price;
	}
	
	/*@Override
	  public boolean equals(Object o) {
	    if (!(o instanceof ItemInOrder)) return false;
	    ItemInOrder other = (ItemInOrder) o;
	    return item_code.equals(other.getItem_code());
	}*/
	
}
	
	