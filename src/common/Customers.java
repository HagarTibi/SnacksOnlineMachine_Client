package common;



/**
 * Entity of customer - able to perform an order in EK and OL configurations
 */

public class Customers extends User {

	    private String credit_card_num;
	    private String credit_card_exp;
		private String  cvv;
		private boolean is_subscriber;


	public Customers(String first_name, String last_name, String user_id, String password, String username, Roles role,
					 String phoneNumber, String emailAddress, String credit_card_num,
					 String credit_card_exp, String cvv, boolean is_subscriber) {
		super(username, password, first_name, last_name, role, user_id, phoneNumber, emailAddress);
		this.credit_card_num = credit_card_num;
		this.credit_card_exp = credit_card_exp;
		this.cvv = cvv;
		this.is_subscriber = is_subscriber;
	}

	public Customers(User user, String credit_card_num,
					 String credit_card_exp, String cvv, boolean is_subscriber) {
		super(user.getUsername(), user.getPassword(), user.getFirst_name(), user.getLast_name(), user.getRole(),
				user.getUser_id(), user.getPhoneNumber(), user.getEmailAddress());
		this.credit_card_num = credit_card_num;
		this.credit_card_exp = credit_card_exp;
		this.cvv = cvv;
		this.is_subscriber = is_subscriber;
	}


	public String getCustomer_id() {
		return getUser_id();
	}

	public String getCredit_card_num() {
		return credit_card_num;
	}

	public void setCredit_card_num(String credit_card_num) {
		this.credit_card_num = credit_card_num;
	}

	public String getCredit_card_exp() {
		return credit_card_exp;
	}

	public void setCredit_card_exp(String credit_card_exp) {
		this.credit_card_exp = credit_card_exp;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public boolean isIs_subscriber() {
		return is_subscriber;
	}

	public void setIs_subscriber(boolean is_subscriber) {
		this.is_subscriber = is_subscriber;
	}
}