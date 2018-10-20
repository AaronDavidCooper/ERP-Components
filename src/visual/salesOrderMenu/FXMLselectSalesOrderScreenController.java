/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual.salesOrderMenu;


import entity.SalesOrder;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mainApp.DbFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * FXML Controller class
 *
 * @author AARON
 */
public class FXMLselectSalesOrderScreenController implements Initializable {

    /**
     * initialises the controller class.
     */
    
    
    @FXML private TableView<SalesOrder> tableView;
    @FXML private TableColumn<SalesOrder, Date> docDateCol;
    @FXML private TableColumn<SalesOrder, String> docNumCol;    
    @FXML private TableColumn<SalesOrder, String> cusRefCol;
    @FXML private TableColumn<SalesOrder, Double> docTotalCol;
    @FXML private TableColumn<SalesOrder, String> docTitleCol;
    
    private SessionFactory factory;
    private Session session; 
    private ObservableList<SalesOrder> listSalesOrders = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        factory = DbFactory.getSessionFactory();    
        
        // Downloading sales orders from mySQL
        session = factory.getCurrentSession();
        session.beginTransaction();
        List download = session.createQuery("from SalesOrder").getResultList();
        listSalesOrders = FXCollections.observableArrayList(download);
        session.close();
        
        // Populating table
        docDateCol.setCellValueFactory(new PropertyValueFactory<SalesOrder, Date>("docDate"));
        docNumCol.setCellValueFactory(new PropertyValueFactory<SalesOrder, String>("docNum"));
        cusRefCol.setCellValueFactory(new PropertyValueFactory<SalesOrder, String>("custAcc"));
        docTotalCol.setCellValueFactory(new PropertyValueFactory<SalesOrder, Double>("docTotal"));
        docTitleCol.setCellValueFactory(new PropertyValueFactory<SalesOrder, String>("docTitle"));        
        tableView.setItems(listSalesOrders);
    }    
    
    
    public void openSelectedOrder(ActionEvent event) throws IOException{
        // Gets the new scene info, seperated FXML loader so we can add in the paramter (selected row)
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/visual/salesOrderMenu/FXMLsalesOrderScreen.fxml"));
        Parent salesOrderParent = loader.load();                
        Scene soScene = new Scene(salesOrderParent);   
        // Connects new Controller with FXML loader and calls initData
        FXMLsalesOrderScreenController controller = loader.getController();
        controller.initData(tableView.getSelectionModel().getSelectedItem());        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();  
        // Sets new scene onto stage
        window.setScene(soScene);
        window.show(); 
    }
    
    public void addNewOrder(ActionEvent event) throws IOException{
        // Gets the new scene info
        Parent itemRoot = FXMLLoader.load(getClass().getResource("/visual/salesOrderMenu/FXMLsalesOrderScreen.fxml"));
        Scene itemScene = new Scene(itemRoot);        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();  
        // Sets new scene onto stage
        window.setScene(itemScene);
        window.show(); 
    }
        
    public void returnToMain(ActionEvent event) throws IOException{
        // Gets the scene info
        Parent itemRoot = FXMLLoader.load(getClass().getResource("/visual/mainMenu/FXMLmainScreen.fxml"));
        Scene itemScene = new Scene(itemRoot);        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();  
        // Sets new scene onto stage
        window.setScene(itemScene);
        window.show(); 
    }        
        
}
