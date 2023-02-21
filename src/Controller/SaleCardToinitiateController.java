package Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import common.Sale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SaleCardToinitiateController {
	  public static SaleCardToinitiateController rightcard;
	  @FXML
	  private Button cancelSale;

	  @FXML
	  private HBox cardhboxpane;

	  @FXML
	  private ImageView image;

	  @FXML
	  private CheckBox northChoose;

	  @FXML
	  private CheckBox southChoose;

	  @FXML
	  private Text txtPercentage;

	  @FXML
	  private Text txtname;

	  @FXML
	  private CheckBox uaeChoose;
	  
	  private Sale sale;
	  private String keyNorth="North";
	  private String keySouth="South";
	  private String keyUAE="UAE";
	  private String value;
	  
	  /**

	  The SetData method is used to set the data of a Sale object to the various elements in the FXML file.
	  It sets the image of the "image" ImageView object to the image associated with the Sale object, using the imgURL property.
	  It sets the text of the "txtname" and "txtPercentage" Text objects to the corresponding properties of the Sale object.
	  It also assigns the Sale object passed as a parameter to the sale variable, which is an instance variable of the class.
	  It also sets the selected property of the "northChoose", "southChoose", and "uaeChoose" CheckBox objects based on the values in the areaSale map of the Sale object.
	  @param sale the Sale object whose data is being set to the elements in the FXML file
	  */
	  public void SetData(Sale sale) {

		  	Image im = receiveImageForProduct(sale);
	    	image.setImage(im);
	    	txtname.setText(sale.getName());
	    	txtPercentage.setText(sale.getPercentage());
	    	this.sale=sale;
	    	value=sale.areaSale.get(keyNorth);
	    	if(!(value.equals("0"))) {
	    		northChoose.setSelected(true);
	    	}
	    	value=sale.areaSale.get(keySouth);
	    	if(!(value.equals("0"))) {
	    		southChoose.setSelected(true);
	    	}
	    	value=sale.areaSale.get(keyUAE);
	    	if(!(value.equals("0"))) {
	    		uaeChoose.setSelected(true);
	    	}
	  }
	  
	  /**

	  The northClick method is responsible for handling the onClick event of the "northChoose" CheckBox.
	  When the checkbox is selected, it puts the value of "1" in the "North" key of the areaSale map of the Sale object and
	  when the checkbox is deselected, it puts the value of "0" in the "North" key of the areaSale map of the Sale object,
	  if the original value of the "North" key is not "0" or "2".
	  @param event the onClick event of the "northChoose" CheckBox
	  */
	  @FXML
	  public void northClick(ActionEvent event) {
		  String valueNorth=sale.areaSale.get(keyNorth);
		  if(northChoose.isSelected()) {
			  if(!(valueNorth.equals("2"))) {
				  sale.areaSale.put("North", "1");
			  }
		  }else
		  {
			  if(!(valueNorth.equals("0"))){
				  sale.areaSale.put("North", "0");
			  }
		  }
	  }
	 
	  /**

	  The southClick method is responsible for handling the onClick event of the "southChoose" CheckBox.
	  When the checkbox is selected, it puts the value of "1" in the "South" key of the areaSale map of the Sale object and
	  when the checkbox is deselected, it puts the value of "0" in the "South" key of the areaSale map of the Sale object,
	  if the original value of the "South" key is not "0" or "2".
	  @param event the onClick event of the "southChoose" CheckBox
	  */
	  
	  @FXML
	  public void southClick(ActionEvent event) {
		  String valueSouth=sale.areaSale.get(keySouth);
		  if(southChoose.isSelected()) {
			  if(!(valueSouth.equals("2"))) {
				  sale.areaSale.put("South", "1");
			  }
		  }else
		  {
			  if(!(valueSouth.equals("0"))){
				  sale.areaSale.put("South", "0");
			  }
		  }
	  }
	  /**

	  The uaeClick method is responsible for handling the onClick event of the "uaeChoose" CheckBox.
	  When the checkbox is selected, it puts the value of "1" in the "UAE" key of the areaSale map of the Sale object and
	  when the checkbox is deselected, it puts the value of "0" in the "UAE" key of the areaSale map of the Sale object,
	  if the original value of the "UAE" key is not "0" or "2".
	  @param event the onClick event of the "uaeChoose" CheckBox
	  */
	  @FXML
	  public void uaeClick(ActionEvent event) {
		  String valueUAE=sale.areaSale.get(keyUAE);
		  if(uaeChoose.isSelected()) {
			  if(!(valueUAE.equals("2"))) {
				  sale.areaSale.put("UAE", "1");
			  }
		  }else
		  {
			  if(!(valueUAE.equals("0"))){
				  sale.areaSale.put("UAE", "0");
			  }
		  }
	  }
	  
	  
	  
	  /**

	  The onClickX method is responsible for handling the onClick event of an "X" button.
	  When the button is clicked, it removes the sale object from the "salestoadd" list of the SalesCatalogScreenController class and
	  calls the AddCardSaleToScrollPaneRight() method of the SalesCatalogScreenController to update the list on the UI.
	  @param event the onClick event of the "X" button
	  */
	  @FXML
	  public void onClickX(ActionEvent event) {
		  SalesCatalogScreenController.salestoadd.remove(sale);
		  SalesCatalogScreenController.salesCatalogController.AddCardSaleToScrollPaneRight();
	  }

	private Image receiveImageForProduct(Sale sale)
	{
		Image image = new Image(new ByteArrayInputStream(sale.getImgURL()));
		return image;
	}
	  
	  
	  
	  
	    

}
