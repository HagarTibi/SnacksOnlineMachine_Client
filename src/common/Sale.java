package common;

import java.io.Serializable;
import java.lang.reflect.AnnotatedArrayType;
import java.util.HashMap;

/**
 * Sale Class to represent a Sales department sales for orders
 */

public class Sale implements Serializable {
	private String saleid;
	private String name;
	private String description;
	private byte[] imgURL;
	private String percentage;
	private String hours;
	public HashMap<String,String> areaSale;
	/**
	 * Values in areaSales HashMap :
	 * 0-the Markerting manager not define the sale to the area
	 * 1-the Markerting manager define the sale to the area but the marketing worker not approve the sale
	 * 2-the Markerting manager define the sale to the area and the marketing worker  approve the sale
	 * @author G-10*/

	public Sale(String saleid,String name,String percentage,String description,String hours,HashMap<String,String> areaSale,byte[] imgURL) {
		this.saleid=saleid;
		this.name=name;
		this.percentage=percentage;
		this.description=description;
		this.imgURL=imgURL;
		this.hours=hours;
		this.areaSale=areaSale;

	}
	public Sale(String saleid,String name,String percentage,String description,String hours,byte[] imgURL) {
		this.saleid=saleid;
		this.name=name;
		this.percentage=percentage;

		this.description=description;
		this.imgURL=imgURL;
		this.hours=hours;
		/*this.areaSale=areaSale;*/

		areaSale=new HashMap<>();
		areaSale.put("North","0");
		areaSale.put("South","0");
		areaSale.put("UAE","0");

	}
	public Sale(String saleid,String name,String percentage,String description,String hours,HashMap<String,String> areaSale) {
		this.saleid=saleid;
		this.name=name;
		this.percentage=percentage;
		this.description=description;
		this.hours=hours;
		this.areaSale=areaSale;
	}



	public String getSaleid() {
		return saleid;
	}
	public void setSaleid(String saleid) {
		this.saleid = saleid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getImgURL() {
		return imgURL;
	}
	public void setImgURL(byte[] imgURL) {
		this.imgURL=imgURL;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public HashMap<String, String> getAreaSale() {
		return areaSale;
	}
	public void setAreaSale(HashMap<String, String> areaSale) {
		this.areaSale = areaSale;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getMapStringArea() {
		return "North,"+getAreaSale().get("North")+",South,"+getAreaSale().get("South")+",UAE,"+getAreaSale().get("UAE");
	}

}