

package com.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class FavoriteProductModelE {
    @Id
    private String model_name;
    private int quantity;
    public void setFavoriteProductModelE(String model_name,int quantity){
        this.model_name = model_name;
        this.quantity = quantity;
    }
}