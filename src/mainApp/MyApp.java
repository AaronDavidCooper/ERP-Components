package mainApp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;


/**
 *
 * @author AARON
 */
public class MyApp extends Application {
    
    private SessionFactory factory;
    
    @Override
    public void start(Stage stage) throws Exception {
        // Initialise SessionFactory through method in DbFactory
        this.factory = DbFactory.getSessionFactory();        
        
        // Gets first scene
        Parent root = FXMLLoader.load(getClass().getResource("/visual/mainMenu/FXMLmainScreen.fxml"));        
        //Parent root = FXMLLoader.load(getClass().getResource("/visual/salesOrderMenu/FXMLselectsalesorderscreen.fxml"));       
        Scene scene = new Scene(root);  
        
        // Sets the scene onto stage
        stage.setScene(scene);
        stage.show();
                
        // Closes SessionFactory on exiting program 
        stage.setOnCloseRequest(e -> closeProgram() );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void closeProgram(){
        this.factory.close();
    }
    
}
