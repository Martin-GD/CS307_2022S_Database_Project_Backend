package com.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class OrdersE {
    @Id
    private int order_id;
    private String contract_id;
    private String model_id = null;
    private String quantity= null;
    private String salesman= null;
    private String delivery_date= null;
    private String lodgement_date= null;
    private String contract_type= null;
    public void setOrdersE(int order_id,String contract_id,String model_id,
                           String quantity,String salesman,String delivery_date,
                           String lodgement_date, String contract_type){
        this.order_id = order_id;
        this.contract_id = contract_id;
        this.model_id = model_id;
        this.quantity = quantity;
        this.salesman = salesman;
        this.delivery_date = delivery_date;
        this.lodgement_date = lodgement_date;
        this.contract_type = contract_type;
    }
}