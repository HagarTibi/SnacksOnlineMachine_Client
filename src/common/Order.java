package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Entity to save the order information during and after the making of the order.
 * This order object refers only to EK configuration
 */

public class Order implements Serializable{
	private String order_code;
	private String customer_id;
	private String machine_id;
	private ArrayList<ItemInCatalog> items_in_order;
	private float final_price;
	private String order_confirmed_date;
	private String machine_area;

	public Order(String order_code, String customer_id, String machine_id, ArrayList<ItemInCatalog> items_in_order,
				 float final_price, String order_confirmed_date) {
		super();
		this.order_code = order_code;
		this.customer_id = customer_id;
		this.machine_id = machine_id;
		this.items_in_order = items_in_order;
		this.final_price = final_price;
		this.order_confirmed_date = order_confirmed_date;
	}

	public Order(String order_code,String customerId, String orderConfirmedDate) {
		this.customer_id = customerId;
		this.order_confirmed_date = orderConfirmedDate;
		this.order_code=order_code;
	}

	public Order() {
		items_in_order = new ArrayList<>();
	}

	public void setItems_in_order(ArrayList<ItemInCatalog> items_in_order) {
		this.items_in_order = items_in_order;
	}

	public float getFinal_price() {
		return final_price;
	}

	public void updateFinalPrice() {
		final_price = 0;
		for (ItemInCatalog item : items_in_order) {
			final_price += item.getAmount_in_cart()*item.getItem_price();
		}
		System.out.println("this is Final Price: "+final_price);
	}

	public void addNewItemFromCatalog(ItemInCatalog itemFromCatalog) {
		items_in_order.add(itemFromCatalog);
		updateFinalPrice();
	}

	public String getOrder_confirmed_date() {
		return order_confirmed_date;
	}

	public void setOrder_confirmed_date(String order_confirmed_date) {
		this.order_confirmed_date = order_confirmed_date;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getOrder_code() {
		return order_code;
	}

	public String getMachine_id() {
		return machine_id;
	}

	public ArrayList<ItemInCatalog> getItems_in_order() {
		return items_in_order;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public void setFinal_price(float final_price) {
		this.final_price = final_price;
	}

	public String getMachine_area() {
		return machine_area;
	}

	public void setMachine_area(String area) {
		this.machine_area = area;
	}
}
