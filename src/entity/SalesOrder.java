/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author AARON
 */

@Entity
@Table(name="ORDR")
public class SalesOrder {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="docnum")
    private int docNum;    
    
    @Column(name="docdate") 
    private Date docDate;
    
    @Column(name="custacc")
    private String custAcc;
    
    @Column(name="doctitle")
    private String docTitle;
    
    @Column(name="docstatus")
    private boolean docOpen;
    
    @Column(name="doctotal")
    private double docTotal;
    
    @Column(name="vattotal")
    private double vatTotal;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="soDocNum", cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    private List<SOdetail> rowDetails;
    
    public SalesOrder(){
        this.docOpen = true;
    }

    public SalesOrder(Date docDate, String custAcc, String docTitle) {
        this.docDate = docDate;
        this.custAcc = custAcc;
        this.docTitle = docTitle;
    }

    public int getDocNum() {
        return docNum;
    }

    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getCustAcc() {
        return custAcc;
    }

    public void setCustAcc(String custAcc) {
        this.custAcc = custAcc;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public boolean isDocOpen() {
        return docOpen;
    }

    public void setDocOpen(boolean docOpen) {
        this.docOpen = docOpen;
    }

    public double getDocTotal() {
        return docTotal;
    }

    public void setDocTotal(double docTotal) {
        this.docTotal = docTotal;
    }

    public double getVatTotal() {
        return vatTotal;
    }

    public void setVatTotal(double vatTotal) {
        this.vatTotal = vatTotal;
    }

    public List<SOdetail> getRowDetails() {
        return rowDetails;
    }

    public void setRowDetails(List<SOdetail> rowDetails) {
        this.rowDetails = rowDetails;
    }

    @Override
    public String toString() {
        return "SalesOrder{" + "docNum=" + docNum + ", docDate=" + docDate + ", custAcc=" + custAcc + ", docTitle=" + docTitle + ", docOpen=" + docOpen + ", docTotal=" + docTotal + ", vatTotal=" + vatTotal + '}';
    }
    
    
    
}
