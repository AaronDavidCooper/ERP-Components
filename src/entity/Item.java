
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="OITM")
public class Item {
    
    @Id    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @Column(name="item_code")
    private String itemCode;
    
    @Column(name="item_desc")
    private String description;
    
    @Column(name="stock")
    private int stock;
    
    @Column(name="cost_price")
    private double costPrice;
    
    @Column(name="sales_price")
    private double salesPrice;
    
    public Item(){
        
    }

    public Item(String itemCode, String description, int stock, double costPrice, double salesPrice) {
        this.itemCode = itemCode;
        this.description = description;
        this.stock = stock;
        this.costPrice = costPrice;
        this.salesPrice = salesPrice;
    }
    
    

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Override
    public String toString() {
        return "Item{" + "itemCode=" + itemCode + ", description=" + description + ", stock=" + stock + ", costPrice=" + costPrice + ", salesPrice=" + salesPrice + '}';
    }

    public int getId() {
        return id;
    }

    
}
