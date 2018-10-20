/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual.mainMenu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AARON
 */
public class FXMLmainScreenController implements Initializable {
    
    public void openItemManager(ActionEvent event) throws IOException{
        // Gets the scene info
        Parent itemRoot = FXMLLoader.load(getClass().getResource("/visual/itemMenu/FXMLitemScreen.fxml"));
        Scene itemScene = new Scene(itemRoot);        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        // Sets new scene onto stage
        window.setScene(itemScene);
        window.show();
    }

    public void openCustManager(ActionEvent event) throws IOException{
        // Gets the scene info
        Parent itemRoot = FXMLLoader.load(getClass().getResource("/visual/custMenu/FXMLcustScreen.fxml"));
        Scene itemScene = new Scene(itemRoot);        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        // Sets new scene onto stage
        window.setScene(itemScene);
        window.show();
    }

    public void salesOrderProcessing(ActionEvent event) throws IOException{
        // Gets the scene info
        Parent itemRoot = FXMLLoader.load(getClass().getResource("/visual/salesOrderMenu/FXMLselectSalesOrderScreen.fxml"));
        Scene itemScene = new Scene(itemRoot);        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();  
        // Sets new scene onto stage
        window.setScene(itemScene);
        window.show(); 
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
