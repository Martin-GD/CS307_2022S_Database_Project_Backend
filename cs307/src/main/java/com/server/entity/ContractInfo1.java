package com.server.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ContractInfo1 {
    @Id
    private String number;
    private String manager;
    private String enterprise;
    private String supply_center;
    public void setContractInfo1(String number,String manager,String enterprise,String supply_center){
        this.number = number;
        this.manager = manager;
        this.enterprise = enterprise;
        this.supply_center = supply_center;

    }
}