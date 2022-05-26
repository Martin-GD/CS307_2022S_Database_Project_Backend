package com.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class AvgStockByCenterE {
    @Id
    private String supply_center;
    private double average;
    public void setAvgStockByCenterE(String supply_center,double average){
        this.supply_center = supply_center;
        this.average = average;
    }
}