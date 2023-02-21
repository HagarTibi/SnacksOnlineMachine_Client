package common;

/**
 * Entity for credit card of a user - customer or subscriber
 */

public class Creditcard {
	private String cardnumber;
	private String monthvalidity;
	private String yearvalidity;
	private String cvv;
	
	public Creditcard(String cardnumber, String monthvalidity, String yearvalidity, String cvv) {
		super();
		this.cardnumber = cardnumber;
		this.monthvalidity = monthvalidity;
		this.yearvalidity = yearvalidity;
		this.cvv = cvv;
	}
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public String getMonthvalidity() {
		return monthvalidity;
	}
	public void setMonthvalidity(String monthvalidity) {
		this.monthvalidity = monthvalidity;
	}
	public String getYearvalidity() {
		return yearvalidity;
	}
	public void setYearvalidity(String yearvalidity) {
		this.yearvalidity = yearvalidity;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	@Override
	public String toString() {
		
		return cardnumber+"\n"+monthvalidity+"/"+yearvalidity+"\n"+cvv;
	}
	public static void main(String[] args){
			
	System.out.println(new Creditcard("123", "12","1997", "#3"));	
	}
}
