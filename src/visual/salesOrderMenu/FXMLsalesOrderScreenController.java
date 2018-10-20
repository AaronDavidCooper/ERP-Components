/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual.salesOrderMenu;

import entity.Customer;
import entity.Item;
import entity.SOdetail;
import entity.SalesOrder;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import mainApp.DbFactory;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * FXML Controller class
 *
 * @author AARON
 * 
 */
public class FXMLsalesOrderScreenController implements Initializable {
    
    
    @FXML private TextField salesOrderNoField;
    @FXML private TextField custRefField;
    @FXML private TextField custNameField;
    @FXML private DatePicker datePicked;
    @FXML private TextField titleField;
    
    @FXML private TableView<SOdetail> tableView;
    @FXML private TableColumn<SOdetail, String> codeCol;
    @FXML private TableColumn<SOdetail, String> descCol;
    @FXML private TableColumn<SOdetail, Integer> qtyCol;
    @FXML private TableColumn<SOdetail, Double> costCol;
    @FXML private TableColumn<SOdetail, Double> totalCol;
    
    @FXML private TextField addItemCodeField;
    @FXML private TextField addDescriptionField;
    @FXML private TextField addQtyField;
    @FXML private TextField addUnitPriceField;
    @FXML private TextField addLineTotalField;
    
    @FXML private TextField showDocTotal;
    @FXML private TextField showVatTotal;
    @FXML private TextField showGrandTotal;
    @FXML private Button addUpdateButton;
    
    private SalesOrder salesOrder;
    private List<Customer> customers;
    private List<Item> items;
    private List<String> custAccRefs;
    private ObservableList<String> itemCodes = FXCollections.observableArrayList();
    private SessionFactory factory;
    private Session session;
    private ObservableList<SOdetail> rowDetails = FXCollections.observableArrayList();;
    private Customer matchedCustomer;
    
    // Initialises controller with sales order parameter
    public void initData(SalesOrder openSO){
        // Updating objects used by the controller with the existing doc
        salesOrder = openSO;        
        for(SOdetail s : salesOrder.getRowDetails()){
            rowDetails.add(s);
        }        
        // Updating fields / gui
        salesOrderNoField.setText(String.valueOf(salesOrder.getDocNum()));
        custRefField.setText(salesOrder.getCustAcc());
        custRefField.setDisable(true); 
        datePicked.setValue(openSO.getDocDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        datePicked.setDisable(true);
        titleField.setText(salesOrder.getDocTitle());
        tableView.setEditable(true);
        if(!salesOrder.isDocOpen()){
            addUpdateButton.setDisable(true);
        }
        tableView.setItems(rowDetails);
        growTable();
        setFieldsTrue();
        addUpdateButton.setText("Update Document");
        matchCustomer(); 
        refreshTable();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        if(salesOrder == null){
            salesOrder = new SalesOrder();
        } 
        factory = DbFactory.getSessionFactory(); 
        
        // Downloads customers & items, required for data validation
        session = factory.getCurrentSession();
        session.beginTransaction();
        customers = session.createQuery("from Customer").getResultList();
        items = session.createQuery("from Item").getResultList();
        session.close();
        custAccRefs = new ArrayList<String>();
        
        // Sets customer ref drop down list & validation 
        for(Customer s : customers){
            custAccRefs.add(s.getAccRef());
        }
        TextFields.bindAutoCompletion(custRefField, custAccRefs);
        custRefField.focusedProperty().addListener((ov, oldV, newV) -> {
           if (!newV) { 
              checkCustRef();
           }
        });
       
       
        // Sets itemcode drop down list & validation 
        for(Item s : items){
            itemCodes.add(s.getItemCode());
        }
        TextFields.bindAutoCompletion(addItemCodeField, itemCodes);
        addItemCodeField.focusedProperty().addListener((ov, oldV, newV) -> {
           if (!newV) { 
              itemSelected();
           }
        });        

        // Updating tableview with stock info
        codeCol.setCellValueFactory(new PropertyValueFactory<SOdetail, String>("rowItemCode"));
        descCol.setCellValueFactory(new PropertyValueFactory<SOdetail, String>("rowDesc"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<SOdetail, Integer>("quantity"));
        costCol.setCellValueFactory(new PropertyValueFactory<SOdetail, Double>("rowUnitPrice"));
        totalCol.setCellValueFactory(new PropertyValueFactory<SOdetail, Double>("rowTotal"));  
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label(""));
        tableView.setItems(getRows()); 
        
        // Updating tableview so we can edit the values
        codeCol.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), itemCodes));        
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        qtyCol.setCellFactory(TextFieldTableCell.<SOdetail, Integer>forTableColumn(new IntegerStringConverter()));
        costCol.setCellFactory(TextFieldTableCell.<SOdetail, Double>forTableColumn(new DoubleStringConverter()));
        
        // Adding listeners to various fields
        titleField.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { 
               setTitle();
            }
        });  
        
        addQtyField.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { 
               updatedAddTotal();
            }
        }); 

        addUnitPriceField.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { 
               updatedAddTotal();
            }
        });         
    }    
       
    private void clearAddFields(){
        addDescriptionField.clear();
        addItemCodeField.clear();
        addLineTotalField.clear();
        addQtyField.clear();
        addUnitPriceField.clear();
    }
    
    public void setDate(){
        salesOrder.setDocDate(Date.valueOf(datePicked.getValue()));
        tableView.setEditable(true); 
        setFieldsTrue();
    }
    
    public void setFieldsTrue(){
        // Gives user access to row details, providing cust ref is also filled in.
        if(!custRefField.getText().isEmpty()){
            addItemCodeField.setDisable(false);
            addDescriptionField.setDisable(false);
            addQtyField.setDisable(false);
            addUnitPriceField.setDisable(false);
            addLineTotalField.setDisable(false);
        }
    }
    
    private void checkCustRef(){
        // Finds matching customer, updates the field, sales order & matchedCustomer.
        if(custAccRefs.contains(custRefField.getText())){
            custRefField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            for(int i = 0; i < customers.size(); i++){
                if(customers.get(i).getAccRef().equals(custRefField.getText())){
                    custNameField.setText(customers.get(i).getName());
                    salesOrder.setCustAcc(custRefField.getText());
                    matchedCustomer = customers.get(i);
                    refreshDocFigures();  
                }                
            }
        // Sets the field red so the user knows to change it    
        } else if(!custRefField.getText().isEmpty()){
            custRefField.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            custNameField.setText("");
        }        
    }
    
    private void itemSelected(){
        // If valid item code is selected, row is filled in for user
        if(itemCodes.contains(addItemCodeField.getText())){
            addItemCodeField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            int i = findItemIndex(addItemCodeField.getText());
            addDescriptionField.setText(items.get(i).getDescription());
            addQtyField.setText("1");
            addUnitPriceField.setText(String.valueOf(items.get(i).getSalesPrice()));
            addLineTotalField.setText(String.valueOf(Integer.parseInt(addQtyField.getText()) * Double.parseDouble(addUnitPriceField.getText())));
        // If invalid, sets the field red so the user knows to change it   
        } else if(!addItemCodeField.getText().isEmpty()){
            addItemCodeField.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            addDescriptionField.setText("");
        }         
    }
    
    public void updatedAddTotal(){
        addLineTotalField.setText(String.valueOf(Integer.parseInt(addQtyField.getText()) * Double.parseDouble(addUnitPriceField.getText())));
    }
    
    private ObservableList getRows(){    
        if(salesOrder.getDocNum() == 0){
            return rowDetails;
        } else {
            // populate rowDetails, then get it.
            return null;
        }
    }
    
        
    public void addRow(){
        // Checks all fields are filled in *** NEED TO IMPROVE DATA VALIDATION ***
        if(addItemCodeField.getText().trim().isEmpty() ||
           addDescriptionField.getText().trim().isEmpty() ||
           addQtyField.getText().trim().isEmpty() ||
           addUnitPriceField.getText().trim().isEmpty() ||
           addLineTotalField.getText().trim().isEmpty()) {
            String warning = "All fields must be filled in!";
            System.out.println(warning);
            //addwarningLabel.setText(warning);
        } else{
             try{
            // Creates new SOdetail line
            SOdetail addRow = new SOdetail(addItemCodeField.getText(),
                        addDescriptionField.getText(), 
                        Integer.parseInt(addQtyField.getText()), 
                        Double.parseDouble(addUnitPriceField.getText()), 
                        Double.parseDouble(addLineTotalField.getText()) );
            // Adds SOdetail to List
            rowDetails.add(addRow);
            // Confirmation to user & update tableview
            clearAddFields();
            refreshTable();
            growTable();
            
            } catch (Exception e) {
                e.printStackTrace();
            }   finally{
                session.close();
            }
        }
    }
    
    private void growTable(){
        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1.01)));
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
    }
    
    
    public void changeItemCodeCellEvent(CellEditEvent changedCell){        
        SOdetail lineSelected = tableView.getSelectionModel().getSelectedItem();
        lineSelected.setRowItemCode(changedCell.getNewValue().toString());
        int i = findItemIndex(changedCell.getNewValue().toString());
        lineSelected.setRowDesc(items.get(i).getDescription());
        int resetQty = 1;
        lineSelected.setQuantity(resetQty);
        lineSelected.setRowUnitPrice(items.get(i).getSalesPrice());
        lineSelected.setRowTotal();
        refreshTable();
    }
    
    public void changeQuantityCellEvent(CellEditEvent changedCell){
        SOdetail lineSelected = tableView.getSelectionModel().getSelectedItem();
        lineSelected.setQuantity(Integer.parseInt(changedCell.getNewValue().toString()));
        lineSelected.setRowTotal();
        refreshTable();
    }    
    
    public void changeUnitPriceCellEvent(CellEditEvent changedCell){
        SOdetail lineSelected = tableView.getSelectionModel().getSelectedItem();
        lineSelected.setRowUnitPrice(Double.parseDouble(changedCell.getNewValue().toString()));
        lineSelected.setRowTotal();
        refreshTable();
    }       
    
    public void setTitle(){
        salesOrder.setDocTitle(titleField.getText());
    }
    
    public void checkCurrentDocument(){
        // Used for error checking, not called upon in the programme. 
        System.out.println("Customer");
        System.out.println(matchedCustomer);
        System.out.println("Sales Order");
        System.out.println(salesOrder.toString());
        System.out.println("Row details");
        for(int x = 0; x < rowDetails.size(); x++){
            System.out.println(rowDetails.get(x).toString());
        }
    }
    
    private int findItemIndex(String code){        
        int index = Integer.MIN_VALUE;        
        for(int i = 0; i < items.size(); i++){
                if(items.get(i).getItemCode().equals(code)){
                    index = i;
                }
        }        
        return index;
    }
    
    public void refreshTable(){
        // Fixes JavaFX bug where tableView.setitems doesn't refresh the gui
        tableView.getColumns().get(0).setVisible(false);
        tableView.getColumns().get(0).setVisible(true);
        tableView.getSelectionModel().clearSelection();
        refreshDocFigures();  
    }
    
    private void refreshDocFigures(){
        // Refreshes document net total
        double newDocTotal = 0;
        for(SOdetail s : rowDetails){
           newDocTotal += s.getRowTotal();
        }
        salesOrder.setDocTotal(newDocTotal);
        showDocTotal.setText(String.valueOf(salesOrder.getDocTotal()));
        
        
        // Refreshes document vat total
        double newVatTotal = 0;
        if(matchedCustomer.isVatApplicable()){
            newVatTotal = salesOrder.getDocTotal() * 0.2;
        } else {
            newVatTotal = 0;
        }
        salesOrder.setVatTotal(newVatTotal);
        showVatTotal.setText(String.valueOf(salesOrder.getVatTotal()));
        
        // Refreshes Gross Total
        showGrandTotal.setText(String.valueOf(salesOrder.getDocTotal() + salesOrder.getVatTotal()));        
    }
    
    private void matchCustomer(){
        // Sets customer name and adds it to the controller, useful for VAT check & future stuff.
        for(int i = 0; i < customers.size(); i++){
            if(customers.get(i).getAccRef().equals(custRefField.getText())){
                matchedCustomer = customers.get(i);
                custNameField.setText(matchedCustomer.getName());
            }                
        }
    }
    
    
    public void addUpdateDocument(){
        if(salesOrder.getDocNum() == 0){
            // Checks header fields are filled in
            if(salesOrder.getDocDate().toString().trim().isEmpty() ||
                salesOrder.getCustAcc().trim().isEmpty() ||
                salesOrder.getDocTitle().trim().isEmpty()  ){
            } else{
                try{    
                    // Adds sales order to database
                    session = factory.getCurrentSession();
                    session.beginTransaction();            
                    session.save(salesOrder);            
                    session.getTransaction().commit();
                    // Adds foreign key to sales order row details then adds them to the database
                    for(SOdetail s : rowDetails){
                        s.setSoDocNum(salesOrder);
                        session = factory.getCurrentSession();
                        session.beginTransaction(); 
                        session.save(s);
                        session.getTransaction().commit();
                    }            
                    
                }catch (Exception e) {
                    e.printStackTrace();
                }   finally{
                    session.close();
                }
            }
            
        } else {
            // Updates Existing document to database
            try{                    
                session = factory.getCurrentSession();
                session.beginTransaction();   
                SalesOrder updOrder = session.get(SalesOrder.class, salesOrder.getDocNum());
                updOrder.setDocTitle(salesOrder.getDocTitle());
                updOrder.setDocTotal(salesOrder.getDocTotal());
                updOrder.setVatTotal(salesOrder.getVatTotal());
                session.getTransaction().commit();

                for(SOdetail s : rowDetails){
                    // If rowID is 0, it's a new row so add it to database
                    if(s.getRowId() == 0){
                        s.setSoDocNum(salesOrder);
                        session = factory.getCurrentSession();
                        session.beginTransaction(); 
                        session.save(s);
                        session.getTransaction().commit();
                    } else{ // It's an existing row, update that rows details
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        SOdetail updRow = session.get(SOdetail.class, s.getRowId());
                        updRow.setRowItemCode(s.getRowItemCode());
                        updRow.setRowDesc(s.getRowDesc());
                        updRow.setQuantity(s.getQuantity());
                        updRow.setRowUnitPrice(s.getRowUnitPrice());
                        updRow.setRowTotal(); 
                        session.getTransaction().commit();
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }   finally{
                session.close();
            }
        }    
    }
    
    public void addUpdateReturnToSelect(ActionEvent event) throws IOException{
        addUpdateDocument();
        ReturnToSelect(event);
    }    
    
    public void ReturnToSelect(ActionEvent event) throws IOException{
        // Gets the scene info
        Parent itemRoot = FXMLLoader.load(getClass().getResource("/visual/salesOrderMenu/FXMLselectSalesOrderScreen.fxml"));
        Scene itemScene = new Scene(itemRoot);        
        // Gets the stage info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();  
        // Sets new scene onto stage
        window.setScene(itemScene);
        window.show(); 
    }        
    
}
