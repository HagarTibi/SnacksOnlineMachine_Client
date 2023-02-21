package common;

import java.io.Serializable;

/**
 * describes item from the catalog to be added to the OrderCatalogScreen
 */

public class ItemInCatalog implements Serializable{
	private String item_code;
	private String item_name;
	private String description;
	private float item_price;
	private byte[] img_src;
	private int amount_in_cart;

	public ItemInCatalog() {}

	public ItemInCatalog(String item_code, String item_name, String description, float item_price, byte[] img_src) {
		this.item_code = item_code;
		this.item_name = item_name;
		this.description = description;
		this.item_price = item_price;
		this.img_src = img_src;
	}

	public int getAmount_in_cart() {
		return amount_in_cart;
	}

	public void setAmount_in_cart(int amount_in_cart) {
		this.amount_in_cart = amount_in_cart;
	}

	public void updateAmount(int amount) {
		if (amount_in_cart + amount < 0)
			amount_in_cart=0;
		else
			amount_in_cart += amount;
	}

	public String getItem_code() {
		return item_code;
	}

	public String getItem_name() {
		return item_name;
	}

	public String getDescription() {
		return description;
	}

	public float getItem_price() {
		return item_price;
	}

	public byte[]  getImg_src() {
		return img_src;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setItem_price(float item_price) {
		this.item_price = item_price;
	}

	public void setImg_src(byte[]  img_src) {
		this.img_src = img_src;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ItemInCatalog)) return false;
		ItemInCatalog other = (ItemInCatalog) o;
		return this.item_code.equals(other.getItem_code());
	}

}
