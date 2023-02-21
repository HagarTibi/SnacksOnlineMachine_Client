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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SaleCardManagerController implements Initializable {
	public static SaleCardManagerController saleCardManager;
    @FXML
    private ImageView image;

    @FXML
    private Text txtname;

    @FXML
    private Text txtdescription;
    
    @FXML
    private Text txtPercentage;
    
    @FXML
    private Text txtactiveHours;

    @FXML
    private Button addSale;
    


    @FXML
    private HBox cardhboxpane;
    private Sale sale;
    
    
    /**

    The SetData method is used to set the data of a Sale object to the various elements in the FXML file.
    It sets the image of the "image" ImageView object to the image associated with the Sale object, using the imgURL property.
    It sets the text of the "txtname", "txtdescription", "txtPercentage", and "txtactiveHours" Text objects to the corresponding properties of the Sale object.
    It also assigns the Sale object passed as a parameter to the sale variable, which is an instance variable of the class.
    @param sale the Sale object whose data is being set to the elements in the FXML file
    */
    public void SetData(Sale sale) {
        Image im = receiveImageForProduct(sale);
    	image.setImage(im);
    	txtname.setText(sale.getName());
    	txtdescription.setText(sale.getDescription());
    	txtPercentage.setText(sale.getPercentage());
    	txtactiveHours.setText(sale.getHours());
    	this.sale=sale;
    }
    private Image receiveImageForProduct(Sale sale)
    {
        Image image = new Image(new ByteArrayInputStream(sale.getImgURL()));
        return image;
    }
    
    /**

    The onClickAddSale method is responsible for handling a button click event. When the button is clicked, it checks if the sale variable is already in the "salestoadd" list of the SalesCatalogScreenController class.
    If it is not in the list, it is added to the list and the AddCardSaleToScrollPaneRight() method of the SalesCatalogScreenController is called.
    @param event the button click event that triggers the method
    */
    @FXML
    public void onClickAddSale(ActionEvent event) {
		if(!(SalesCatalogScreenController.salestoadd.contains(sale))) {
			SalesCatalogScreenController.salestoadd.add(sale);
			
			SalesCatalogScreenController.salesCatalogController.AddCardSaleToScrollPaneRight();
		}
    	
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
