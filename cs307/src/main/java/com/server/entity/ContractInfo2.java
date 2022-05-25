package com.server.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ContractInfo2 {
    @Id
    private String product_model;
    private String salesman;
    private int quantity;
    private int unit_price;
    private String estimate_delivery_date;
    private String lodgement_date;
    public void setContractInfo2(String product_model,String salesman,int quantity, int unit_price, String estimate_delivery_date,String lodgement_date){
        this.product_model = product_model;
        this.salesman = salesman;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.estimate_delivery_date = estimate_delivery_date;
        this.lodgement_date = lodgement_date;

    }
}