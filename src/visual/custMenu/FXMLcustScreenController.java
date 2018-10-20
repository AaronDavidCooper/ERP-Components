/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual.custMenu;

import entity.Customer;
import mainApp.DbFactory;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 *
 * @author AARON
 */
public class FXMLcustScreenController implements Initializable {
    
    @FXML private TextField accField;
    @FXML private TextField nameField;
    @FXML private TextField addLine1Field;
    @FXML private TextField addLine2Field;
    @FXML private TextField addLine3Field;
    @FXML private TextField countryField;
    @FXML private TextField postcodeField;
    @FXML private TextField telephoneField;
    @FXML private CheckBox vatBox;
    @FXML private Label addwarningLabel;
    @FXML private TableView<Customer> custView;
    @FXML private TableColumn<Customer, String> accCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private Button addupdateButton;

    private SessionFactory factory;
    private Session session;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Passing sessionfactory
        factory = DbFactory.getSessionFactory(); 
        
        // Updating tableview with stock info
        accCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("accRef"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));     
        custView.setItems(getCusts());
        
        custView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                updateFields();
            }
        });

    }    
    
    private ObservableList getCusts(){
        // Grabs customer information from database
        session = factory.getCurrentSession();
        session.beginTransaction();
        List download = session.createQuery("from Customer").getResultList();
        session.close();
        // converts List to ObservableList & returns data
        ObservableList<Customer> customers = FXCollections.observableArrayList(download);        
        return customers;
    }
    
    public void submitCust(){        
        if(addupdateButton.getText().equals("Add")){
            // Checks all fields are filled in *** IMPROVE DATA VALIDATION ***
            if(accField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                addLine1Field.getText().trim().isEmpty() ||
                addLine2Field.getText().trim().isEmpty() ||
                addLine3Field.getText().trim().isEmpty() ||
                countryField.getText().trim().isEmpty() ||
                postcodeField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty() ) {
                String warning = "All fields must be filled in!";
                addwarningLabel.setText(warning);
            } else{
                try{
                // Creates new customer and initialises session
                session = factory.getCurrentSession();
                Customer newCustomer = new Customer(accField.getText(),
                                        nameField.getText(),
                                        addLine1Field.getText(),
                                        addLine2Field.getText(),
                                        addLine3Field.getText(),
                                        countryField.getText(),
                                        postcodeField.getText(),
                                        telephoneField.getText(),
                                        vatBox.isSelected()); 
                // Saves item to database
                session.beginTransaction();            
                session.save(newCustomer);            
                session.getTransaction().commit(); 
                // Confirmation to user & update tableview
                addwarningLabel.setTextFill(Color.web("#00a32d"));
                addwarningLabel.setText("Customer added to database!");
                custView.setItems(getCusts()); 
                clearFields();

                } catch (Exception e) {
                    e.printStackTrace();
                }   finally{
                    session.close();
                }
            }
        } else{
            try{
                session = factory.getCurrentSession();
                session.beginTransaction();
                // Grabs customer from db
                Customer changeCust = session.get(Customer.class, accField.getText());
                // Updates customer fields
                changeCust.setName(nameField.getText());
                changeCust.setAddressLine1(addLine1Field.getText());
                changeCust.setAddressLine2(addLine2Field.getText());
                changeCust.setAddressLine3(addLine3Field.getText());
                changeCust.setCountry(countryField.getText());
                changeCust.setPostcode(postcodeField.getText());
                changeCust.setTelephone(telephoneField.getText());
                // Pushes customer changes to SQL db
                session.getTransaction().commit();
                
                // Refresh table, confirming back to user and enables specific UI options.
                custView.setItems(getCusts());
                addwarningLabel.setTextFill(Color.web("#00a32d"));
                addwarningLabel.setText("Customers detailed updated!");                
                clearFields();
                accField.setDisable(false);
                vatBox.setDisable(false);
                addupdateButton.setText("Add");
                
            } catch (Exception e) {
                    e.printStackTrace();
                }   finally{
                    session.close();
                }
        }
    }
    
    
    public void updateFields(){
        session = factory.getCurrentSession();
        session.beginTransaction();        
        
        // Creates an instance of the customer selected (to grab ID)
        Customer custSelected = custView.getSelectionModel().getSelectedItem();
        
        // Updates fields with selected customer
        accField.setText(custSelected.getAccRef());
        accField.setDisable(true);
        nameField.setText(custSelected.getName());
        addLine1Field.setText(custSelected.getAddressLine1());
        addLine2Field.setText(custSelected.getAddressLine2());
        addLine3Field.setText(custSelected.getAddressLine3());
        countryField.setText(custSelected.getCountry());
        postcodeField.setText(custSelected.getPostcode());
        telephoneField.setText(custSelected.getPostcode());
        vatBox.setSelected(custSelected.isVatApplicable());   
        vatBox.setDisable(true);
        addupdateButton.setText("Update");        
        session.close();      
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
    
    public void clearFields(){
        accField.clear();
        nameField.clear();
        addLine1Field.clear();
        addLine2Field.clear();
        addLine3Field.clear();
        countryField.clear();
        postcodeField.clear();
        telephoneField.clear();
        vatBox.setSelected(false);
    }    
    
    
}
