package mainApp;

import entity.Customer;
import entity.Item;
import entity.SOdetail;
import entity.SalesOrder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DbFactory {

    public static SessionFactory factory;
    
//private to disallow creating instances by other classes.
    private DbFactory() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (factory == null) {
            DbFactory.factory = new Configuration()
                                                .configure("hibernate.cfg.xml")
                                                .addAnnotatedClass(Item.class)
                                                .addAnnotatedClass(Customer.class)
                                                .addAnnotatedClass(SalesOrder.class)
                                                .addAnnotatedClass(SOdetail.class)
                                                .buildSessionFactory();
        }
        return factory;
    }    
}