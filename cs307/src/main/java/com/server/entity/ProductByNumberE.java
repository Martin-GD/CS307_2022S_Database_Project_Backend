package com.server.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ProductByNumberE {
    @Id
    private String model_name;
    private String product_name;
    private String supply_center;
    private int purchase_price;
    private int quantity;

    public void setProductByNumberE(String supply_center,String product_name,String model_name,int purchase_price, int quantity){
        this.product_name = product_name;
        this.model_name = model_name;
        this.purchase_price = purchase_price;
        this.quantity = quantity;

    }
}