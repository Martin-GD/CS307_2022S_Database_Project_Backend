package com.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class StaffCount {
    @Id
    private String type;
    private int number;
    public void setStaffCount(String type,int number){
        this.type = type;
        this.number = number;
    }
}
