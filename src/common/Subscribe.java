package common;

import java.io.Serializable;

/**
 * Entity to demonstrate subscriber user within the system
 */

public class Subscribe extends User  {

	private String customerID;
	private String sub_num;
	private String first_order;
	private String delay_pay;
	private Boolean isLogin;
	
	public Subscribe(User user) {
		super(user);
	}
	public Subscribe(String username, String password, String first_name, String last_name, Roles role, String user_id, String phoneNumber, String emailAddress, String customer_id, String subscriber_num, String first_order) {
		super(username, password, first_name, last_name, role, user_id, phoneNumber, emailAddress);
		this.customerID = customer_id;
		this.sub_num = subscriber_num;
		this.first_order = first_order;
	}

	public Subscribe(User user, String subscriber_num, String made_first_order) {
		super(user.getUsername(), user.getPassword(), user.getFirst_name(), user.getLast_name(), user.getRole(),
				user.getUser_id(), user.getPhoneNumber(), user.getEmailAddress());
		this.customerID = user.getUsername();
		this.sub_num = subscriber_num;
		this.first_order = made_first_order;
	}
	public Subscribe(Customers toSub,String sub_num,String first_order,String delay_pay) {
		this.customerID=toSub.getUser_id();
		this.sub_num=sub_num;
		this.first_order=first_order;
		this.delay_pay=delay_pay;
		 setIsLogin(false);
	}


	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getSub_num() {
		return sub_num;
	}

	public void setSub_num(String sub_num) {
		this.sub_num = sub_num;
	}

	public String getFirst_order() {
		return first_order;
	}

	public void setFirst_order(String first_order) {
		this.first_order = first_order;
	}

	public String getDelay_pay() {
		return delay_pay;
	}

	public void setDelay_pay(String delay_pay) {
		this.delay_pay = delay_pay;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}
}
