package Controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import client.Client;
import common.ItemInCatalog;
import common.ItemInMachine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

// ItemInCatalogCardController
// Controller for each item in the catalog - control the setting of the item inside the catalog
// and the "Add To Cart" action

public class ItemInCatalogCardController{
	public static ItemInCatalogCardController itemInCatalogCardController; 

    @FXML
    private Button addToCartBtn;

    @FXML
    private Text description;

    @FXML
    private HBox itemCardBox;

    @FXML
    private Text itemCode;

    @FXML
    private ImageView itemImage;

    @FXML
    private Text itemName;

    @FXML
    private Text itemPrice;
    
    @FXML
    private Label unavailableLable;
    
    private ItemInCatalog item;
    
    /**
	 * Set the data of the item to be presented in the catalog.
	 * this method is called from the OrderCatalogScreenController  
	 *
	 * @param item item from Client.arrCatalog that was added to cart
	 */
    public void setData_itemInCatalog(ItemInCatalog item) {

    	Image image = receiveImageForProduct(item);
    	itemImage.setImage(image);
    	description.setText(item.getDescription());
    	itemCode.setText(item.getItem_code());
    	itemName.setText(item.getItem_name());
    	float price = item.getItem_price();
    	String price_str = String.format("%.2f", price);
    	itemPrice.setText(price_str+" NIS");
    	this.item = item;
    	unavailableLable.setVisible(false);
    	if (!(OLMainController.orderType == "REMOTE_DELIVERY")) {
	    	if (getAmountOfItem(item.getItem_code()) == 0) {
	    		unavailableLable.setVisible(true);
	    		addToCartBtn.setDisable(true);
	    	}
	    	else {
	    		unavailableLable.setVisible(false);
	    		addToCartBtn.setDisable(false);
	    	}
    	}
    }
	public Image receiveImageForProduct(ItemInCatalog product)
	{
		Image image = new Image(new ByteArrayInputStream(product.getImg_src()));
		return image;
	}
    
    /**
	 * The user pressed the "Add To Cart" button and the new item appears
	 * on the cart side, the order object is updated and also the order price 
	 *
	 * @param event ActionEvent when "Add To Cart" button is pressed
	 */
    @FXML // for the "Add To Cart" Button
    public void addToCartOnRight (ActionEvent event) {
    	if(!(Client.cart.contains(item))) {
    		Client.cart.add(item); // adding item to showCart
    		OrderCatalogScreenController.orderCatalogScreenController.setErrorMsg();
    		OrderCatalogScreenController.orderCatalogScreenController.updateFinalPrice(item.getItem_price()); // update the gui
    		OrderCatalogScreenController.orderCatalogScreenController.AddItemToCart();
    	}
    }
    
    /**
	 * Method that uses the real-time amounts of items (saved in the client)
	 * and checks the amount in cart according to the actual amount in the machine.
	 * In case of no machine (Delivery Order) the itemsAmount array from the client is empty so
	 * we will enable any amount from the machine.
	 * @param item_code code of the item as his primary key
	 * @return the real time amount of the item (with this item code) in the machine 
	 */
    public int getAmountOfItem (String item_code) {
    	for (ItemInMachine i : Client.itemsAmounts) {
    		if (i.getItem_code().equals(item_code))
    			return i.getItem_amount_in_machine();
    	}
    	return 0;
    }
    
    /**
	 * @return ItemInCatalog getter for item 
	 */
	public ItemInCatalog getItem() {
		return item;
	}

}
