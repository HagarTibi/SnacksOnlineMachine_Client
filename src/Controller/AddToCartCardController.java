package Controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import common.ItemInCatalog;
import common.ItemInMachine;
import common.ItemInOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

// AddToCartCardController
// Controller for each item inside the cart - control the "+", "-" and "x" actions

public class AddToCartCardController{
	public static AddToCartCardController addToCartCardController; 
	@FXML
	private Button cancelItemBtn;
	
	@FXML
    private Text cart_itemName;

	@FXML
	private TextField item_count;

    @FXML
    private ImageView mini_img;

    @FXML
    private Button minusBtn;

    @FXML
    private Button plusBtn;
    
    @FXML
    private Label NoMoreLabel;
    
    private ItemInCatalog item;
    
    /**
	 * Set the data of the item to be presented in the cart.  
	 *
	 * @param item item from catalog that was added to cart
	 */
    public void setData_itemInCart(ItemInCatalog item) {
    	this.item=item;
    	//Image image = new Image(getClass().getResourceAsStream(item.getImg_src()));
    	Image image = recieveImageForProduct(item); //check
    	mini_img.setImage(image);
    	cart_itemName.setText(item.getItem_name());
    	item_count.setText(String.valueOf(item.getAmount_in_cart()));
    	NoMoreLabel.setVisible(false);
    	if (getAmountOfItem(item.getItem_code()) == 1 || item.getAmount_in_cart() == getAmountOfItem(item.getItem_code())) {
    		plusBtn.setDisable(true);
    		NoMoreLabel.setVisible(true);
    	}
    }
	private Image recieveImageForProduct(ItemInCatalog product)
	{
		Image image = new Image(new ByteArrayInputStream(product.getImg_src()));
		return image;
	}
    
    /**
	 * Action event to increase the amount of the item in the cart.
	 * It will update the amount of the item in the cart and the final price
	 * of the order. The amount to acquire is connected to real-time data form
	 * the DB and not allowing to acquire more then it has in the machine.   
	 *
	 * @param event action event of "+" button
	 */
    @FXML
    public void increaseItemAmount (ActionEvent event) {
    	item.updateAmount(1);
    	item_count.setText(String.valueOf(item.getAmount_in_cart()));
    	OrderCatalogScreenController.orderCatalogScreenController.updateFinalPrice(item.getItem_price());
    	if (item.getAmount_in_cart() == getAmountOfItem(item.getItem_code())) {
    		plusBtn.setDisable(true);
    		NoMoreLabel.setVisible(true);
    	}
    }
    
    /**
	 * Action event to decrease the amount of the item in the cart.
	 * It will update the amount of the item in the cart and the final price
	 * of the order. When the user clicks the "-" button from '1' the item
	 * will be removed from the cart.   
	 *
	 * @param event action event of "-" button
	 */
    @FXML
    public void decreaseItemAmount (ActionEvent event) {
    	if (item.getAmount_in_cart() == 1)
    		onClickX(event);
    	else {
	    	item.updateAmount(-1);
	    	item_count.setText(String.valueOf(item.getAmount_in_cart()));
	    	OrderCatalogScreenController.orderCatalogScreenController.updateFinalPrice(-(item.getItem_price()));
	    	if (item.getAmount_in_cart() < getAmountOfItem(item.getItem_code())) {
	    		plusBtn.setDisable(false);
	    		NoMoreLabel.setVisible(false);
	    	}
    	}
    }
    
    /**
	 * Action event to erase the item from the cart.
	 * It will update the amount of the item to '1' (in case it will be added again) and the final price
	 * of the order.
	 *
	 * @param event action event of "X" button
	 */
    @FXML
	public void onClickX(ActionEvent event) {
    	OrderCatalogScreenController.orderCatalogScreenController.updateFinalPrice(-(item.getAmount_in_cart())*(item.getItem_price()));
    	item.setAmount_in_cart(1);
    	Client.cart.remove(item);
    	OrderCatalogScreenController.orderCatalogScreenController.AddItemToCart();
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
    	return Integer.MAX_VALUE;
    }
}
