/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual.itemMenu;

import mainApp.DbFactory;
import entity.Item;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 *
 * @author AARON
 */
public class FXMLitemScreenController implements Initializable {
    
    @FXML private TextField itemcodeField;
    @FXML private TextField descriptionField;
    @FXML private TextField openingstockField;
    @FXML private TextField costpriceField;
    @FXML private TextField salespriceField;
    @FXML private Label addwarningLabel;
    @FXML private TableView<Item> itemView;
    @FXML private TableColumn<Item, String> codeCol;
    @FXML private TableColumn<Item, String> descCol;
    @FXML private TableColumn<Item, Integer> stockCol;
    @FXML private TableColumn<Item, Double> costCol;
    @FXML private TableColumn<Item, Double> salesCol;
    private SessionFactory factory;
    private Session session;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Passing sessionfactory
        factory = DbFactory.getSessionFactory(); 
        
        // Updating tableview with stock info
        codeCol.setCellValueFactory(new PropertyValueFactory<Item, String>("itemCode"));
        descCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("stock"));
        costCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("costPrice"));
        salesCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("salesPrice"));        
        itemView.setItems(getItems());
        
        // Updating tableview so we can edit the values
        itemView.setEditable(true);
        codeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        stockCol.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));
        costCol.setCellFactory(TextFieldTableCell.<Item, Double>forTableColumn(new DoubleStringConverter()));
        salesCol.setCellFactory(TextFieldTableCell.<Item, Double>forTableColumn(new DoubleStringConverter()));
    }    
    
    private ObservableList getItems(){
        // Grabs item information from database
        session = factory.getCurrentSession();
        session.beginTransaction();
        List download = session.createQuery("from Item").getResultList();
        session.close();
        // converts List to ObservableList & returns data
        ObservableList<Item> items = FXCollections.observableArrayList(download);        
        return items;
    }
    
    public void addItem(){
        // Checks all fields are filled in *** IMPROVE DATA VALIDATION ***
        if(itemcodeField.getText().trim().isEmpty() ||
           descriptionField.getText().trim().isEmpty() ||
           openingstockField.getText().trim().isEmpty() ||
           costpriceField.getText().trim().isEmpty() ||
           salespriceField.getText().trim().isEmpty()) {
            String warning = "All fields must be filled in!";
            addwarningLabel.setText(warning);
        } else{
            try{
            // Creates new item and initialises session
            session = factory.getCurrentSession();
            Item newitem = new Item(itemcodeField.getText(),
                        descriptionField.getText(), 
                        Integer.parseInt(openingstockField.getText()), 
                        Double.parseDouble(costpriceField.getText()), 
                        Double.parseDouble(salespriceField.getText()) );
            // Saves item to database
            session.beginTransaction();            
            session.save(newitem);            
            session.getTransaction().commit(); 
            // Confirmation to user & update tableview
            addwarningLabel.setTextFill(Color.web("#00a32d"));
            addwarningLabel.setText("Item added to database!");
            itemView.setItems(getItems());
            
            } catch (Exception e) {
                e.printStackTrace();
            }   finally{
                session.close();
            }
        }
    }
    
    public void changeItemCodeEvent(CellEditEvent changedCell){
        session = factory.getCurrentSession();
        session.beginTransaction();  
        
        // Creates an instance of the item selected (to grab ID)
        Item itemSelected = itemView.getSelectionModel().getSelectedItem();
        
        // Retreives matching item from DB
        Item itemChange = session.get(Item.class, itemSelected.getId());
        itemChange.setItemCode(changedCell.getNewValue().toString());
        
        // Pushes change to DB and refreshes tableview
        session.getTransaction().commit(); 
        itemView.setItems(getItems());
    }

    public void changeDescEvent(CellEditEvent changedCell){
        session = factory.getCurrentSession();
        session.beginTransaction();  
        
        // Creates an instance of the item selected (to grab ID)
        Item itemSelected = itemView.getSelectionModel().getSelectedItem();
        
        // Retreives matching item from DB
        Item itemChange = session.get(Item.class, itemSelected.getId());
        itemChange.setDescription(changedCell.getNewValue().toString());
        
        // Pushes change to DB and refreshes tableview
        session.getTransaction().commit(); 
        itemView.setItems(getItems());
    }    
    
    public void changeItemStockEvent(CellEditEvent changedCell){
        session = factory.getCurrentSession();
        session.beginTransaction();  
        
        // Creates an instance of the item selected (to grab ID)
        Item itemSelected = itemView.getSelectionModel().getSelectedItem();
        
        // Retreives matching item from DB
        Item itemChange = session.get(Item.class, itemSelected.getId());
        itemChange.setStock(Integer.parseInt(changedCell.getNewValue().toString()));
        
        // Pushes change to DB and refreshes tableview
        session.getTransaction().commit(); 
        itemView.setItems(getItems());
    }    
    
    public void changeCostPriceEvent(CellEditEvent changedCell){
        session = factory.getCurrentSession();
        session.beginTransaction();  
        
        // Creates an instance of the item selected (to grab ID)
        Item itemSelected = itemView.getSelectionModel().getSelectedItem();
        
        // Retreives matching item from DB
        Item itemChange = session.get(Item.class, itemSelected.getId());
        itemChange.setCostPrice(Double.parseDouble(changedCell.getNewValue().toString()));
        
        // Pushes change to DB and refreshes tableview
        session.getTransaction().commit(); 
        itemView.setItems(getItems());
    }    
    
    public void changeSalesPriceEvent(CellEditEvent changedCell){
        session = factory.getCurrentSession();
        session.beginTransaction();  
        
        // Creates an instance of the item selected (to grab ID)
        Item itemSelected = itemView.getSelectionModel().getSelectedItem();
        
        // Retreives matching item from DB
        Item itemChange = session.get(Item.class, itemSelected.getId());
        itemChange.setSalesPrice(Double.parseDouble(changedCell.getNewValue().toString()));
        
        // Pushes change to DB and refreshes tableview
        session.getTransaction().commit(); 
        itemView.setItems(getItems());
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
