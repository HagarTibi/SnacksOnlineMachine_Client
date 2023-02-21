package Controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import common.ItemInCatalog;
import common.MsgHandler;
import common.Sale;
import common.TypeMsg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SaleCardController implements Initializable{


    @FXML
    private ImageView image;

    @FXML
    private Text txtname;

    @FXML
    private Text txtdescription;

    @FXML
    private Button buttonon;

    @FXML
    private Button buttonoff;
    @FXML
    private HBox cardhboxpane;


    public Sale sale;
    public String area;

	/**
	 * Cancel the active of this sale on show the button active 
	 * (Value of this area hashmap=1)
	 * @author G-10 */
    @FXML
    void CancelActiviteSale(ActionEvent event) {//cancel activity sale in area
        sale.getAreaSale().put(area,"1");
        ClientUI.chat.accept((Object)BuildMsg());//send requst to bring sales in area
        //need update this detail sale 1/2
        ChangeOnOffOption(true,false);//the button active disapear and button cancel appear
    }
	/**
	 *  active of this sale on show the button active by marketing worker 
	 *  now the subscriber can buy with this sale (Value of this area hashmap=2)
	 * @author G-10 */
    @FXML
    void MakeSaleActive(ActionEvent event) {//make specific sale active
        sale.getAreaSale().put(area,"2");//change area sale to active
        ClientUI.chat.accept((Object)BuildMsg());//send requst to bring sales in area
        ChangeOnOffOption(false,true);//the button active disapear and button cancel appear
    }
    public MsgHandler<Object> BuildMsg() {//send msg with area and sale 
        ArrayList<Object> lstarea=new ArrayList<>();
        lstarea.add((Object)sale);

        return new MsgHandler<Object>(TypeMsg.Update_Active_Sale_InArea,lstarea);
    }

    /**
	 *  Set data of sale card 
	 * @author G-10 */
    public void SetData(Sale sale,String area) {//set the card sale to the exist sale
        Image im = receiveImageForProduct(sale);
        image.setImage(im);
        txtname.setText(sale.getName());
        txtdescription.setText(sale.getDescription());
        this.sale=sale;
        this.area=area;
    }

    /**
	 *  Active/Not Active sale button 
	 *  @param boolean active,boolean not active
	 * @author G-10 */
    public void ChangeOnOffOption(boolean on,boolean off) {//switchbutton from active to cancel /off-on
        buttonon.setVisible(on);
        buttonoff.setVisible(off);
    }
    /**
	 *  Check if this sale active to know what the initnalize
	 *  state that the marketing worker will see
	 *  @param Sale  ,Area
	 * @author G-10 */
    public void CheckActiveByNumberInArea(Sale s,String area) {//CheckActive Of Sale In Area
        if(s.getAreaSale().get(area).equals("1"))//check if in this area this sale is define by the marketing manager but not active
        {
            ChangeOnOffOption(true,false);
        }
        if(s.getAreaSale().get(area).equals("2"))//if in this area this sale is define by the marketing manager and active
        {
            ChangeOnOffOption(false,true);
        }
    }
    private Image receiveImageForProduct(Sale sale)
    {
        Image image = new Image(new ByteArrayInputStream(sale.getImgURL()));
        return image;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}