/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author AARON
 */
@Entity
@Table(name="RDR1")
public class SOdetail {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="rowid")
    private int rowId;
    
    @Column(name="row_item_code")
    private String rowItemCode;
    
    @Column(name="row_desc")
    private String rowDesc;
    
    @Column(name="quantity")
    private int quantity;
    
    @Column(name="delqty")
    private int deliveredQty;
    
    @Column(name="unit_price")
    private double rowUnitPrice;
    
    @Column(name="row_total")
    private double rowTotal;
    
    @Column(name="row_status")
    private boolean rowOpen;
    
    @ManyToOne(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="so_docnum")
    private SalesOrder soDocNum;
    
    public SOdetail(){
        
    }

    public SOdetail(String rowItemCode, String rowDesc, int quantity, double rowUnitPrice, double rowTotal) {
        this.rowItemCode = rowItemCode;
        this.rowDesc = rowDesc;
        this.quantity = quantity;
        this.deliveredQty = 0;
        this.rowUnitPrice = rowUnitPrice;
        this.rowTotal = rowTotal;
        this.rowOpen = true;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getRowItemCode() {
        return rowItemCode;
    }

    public void setRowItemCode(String rowItemCode) {
        this.rowItemCode = rowItemCode;
    }

    public String getRowDesc() {
        return rowDesc;
    }

    public void setRowDesc(String rowDesc) {
        this.rowDesc = rowDesc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDeliveredQty() {
        return deliveredQty;
    }

    public void setDeliveredQty(int deliveredQty) {
        this.deliveredQty = deliveredQty;
    }

    public double getRowUnitPrice() {
        return rowUnitPrice;
    }

    public void setRowUnitPrice(double rowUnitPrice) {
        this.rowUnitPrice = rowUnitPrice;
    }

    public double getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal() {
        this.rowTotal = quantity * rowUnitPrice;
    }

    public SalesOrder getSoDocNum() {
        return soDocNum;
    }

    public void setSoDocNum(SalesOrder soDocNum) {
        this.soDocNum = soDocNum;
    }

    @Override
    public String toString() {
        return "SOdetail{" + "rowId=" + rowId + ", rowItemCode=" + rowItemCode + ", rowDesc=" + rowDesc + ", quantity=" + quantity + ", deliveredQty=" + deliveredQty + ", rowUnitPrice=" + rowUnitPrice + ", rowTotal=" + rowTotal + ", rowOpen=" + rowOpen + ", soDocNum=" + soDocNum + '}';
    }

    public boolean isRowOpen() {
        return rowOpen;
    }

    public void setRowOpen(boolean rowOpen) {
        this.rowOpen = rowOpen;
    }
    
    
    
}
