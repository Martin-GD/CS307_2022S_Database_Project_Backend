package com.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class MonthlyAllE {
    @Id
    private String month;
    private String Revenue_this_month = "0";
    private String Hot_commodity = null;
    private String Hot_supply_center= null;
    private String salesKing= null;
    public void setMonthlyAllE(String month,String Revenue_this_month,String Hot_commodity,String Hot_supply_center,String salesKing){
        this.month = month;
        this.Revenue_this_month = Revenue_this_month;
        this.Hot_commodity = Hot_commodity;
        this.Hot_supply_center = Hot_supply_center;
        this.salesKing = salesKing;
    }
}