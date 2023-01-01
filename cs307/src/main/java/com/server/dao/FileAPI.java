package com.server.dao;

import com.csvreader.CsvReader;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
@Component
public class FileAPI {
    protected Connection con = null;

    static Map<String,center> centerM = new LinkedHashMap<>();
    static Map<String,enterprise> enterpriseM = new LinkedHashMap<>();
    static Map<String,model> modelM = new LinkedHashMap<>();
    static Map<String,staff> staffM = new LinkedHashMap<>();


    //TODO: ENTERPRISE
    public void getEnterprise(){
        try{
            CsvReader infile = new CsvReader("enterprise.csv",',', Charset.forName("UTF-8"));
            String[] values;
            infile.readHeaders();
            while (infile.readRecord()){
                values=infile.getValues();
                enterpriseM.put(values[1],new enterprise(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeBackEnterprise(){
        try {
            BufferedWriter w = new BufferedWriter (new OutputStreamWriter(new FileOutputStream("enterprise.csv"),"UTF-8"));
            w.write("id,name,country,city,supply_center,industry");
            w.newLine();
            for (enterprise a:enterpriseM.values()){
                w.write(a.getEnterprise());
                w.newLine();
            }
            w.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean addEnterprise(String name,String country,String city,String supply_center,String industry){
        if (enterpriseM.containsKey(name)){
            return false;
        }else {
            enterpriseM.put(name,new enterprise(name, country, city, supply_center, industry));
            return true;
        }
    }
    public boolean deleteEnterprise(String name){
        if (enterpriseM.containsKey(name)){
            enterpriseM.remove(name);
            return true;
        }else return false;
    }
    public boolean updateEnterprise(String name,String country,String city,String supply_center,String industry){
        if (enterpriseM.containsKey(name)){
            enterpriseM.get(name).setEnterprise(name, country, city, supply_center, industry);
            return true;
        }
        return false;
    }

    public String selectEnterprise(boolean id,boolean name,boolean country,
                                   boolean city, boolean supply_center,
                                   boolean industry, int idC,String nameC,
                                   String couC,String citC,String supC,String indC){
        StringBuilder sb = new StringBuilder();
        sb.append(id?String.format("%-4s ","id"):"");
        sb.append(name?String.format("%-50s ","name"):"");
        sb.append(country?String.format("%-30s ","country"):"");
        sb.append(city?String.format("%-30s ","city"):"");
        sb.append(supply_center?String.format("%-50s ","supply_center"):"");
        sb.append(industry?String.format("%-40s ","industry"):"");
        sb.append("\n");
        for (enterprise e:enterpriseM.values()) {
            boolean need = true;
            if (id)
                if (idC!=0)
                    need = e.c_id==idC;

            if (name)
                if (!nameC.equals(""))
                    need = e.name.equals(nameC);

            if (country)
                if (!couC.equals(""))
                    need = e.country.equals(couC);
            if (city)
                if (!citC.equals(""))
                    need = e.city.equals(citC);
            if (supply_center)
                if (!supC.equals(""))
                    need = e.supply_center.equals(supC);
            if (industry)
                if (!indC.equals(""))
                    need = e.industry.equals(indC);
            if (need){
                sb.append(id?String.format("%-4s ",e.c_id):"");
                sb.append(name?String.format("%-50s ",e.name):"");
                sb.append(country?String.format("%-30s ",e.country):"");
                sb.append(city?String.format("%-30s ",e.city):"");
                sb.append(supply_center?String.format("%-50s ",e.supply_center):"");
                sb.append(industry?String.format("%-40s ",e.industry):"");
                sb.append("\n");
            }

        }
        return sb.toString();
    }

    //TODO: CENTER
    public void getCenter(){
        try{
            CsvReader infile = new CsvReader("center.csv",',', Charset.forName("UTF-8"));
            String[] values;
            infile.readHeaders();
            while (infile.readRecord()){
                values=infile.getValues();
                centerM.put(values[1],new center(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBackCenter(){
        try {
            BufferedWriter w = new BufferedWriter (new OutputStreamWriter(new FileOutputStream("center.csv"),"UTF-8"));
            w.write("id,name");
            w.newLine();
            for (center a:centerM.values()){
                w.write(a.getCenter());
                w.newLine();
            }
            w.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addCenter(String name){
        if (centerM.containsKey(name)){
            return false;
        }else {
            centerM.put(name,new center(name));
            return true;
        }
    }

    public boolean deleteCenter(String name){
        if (centerM.containsKey(name)){
            centerM.remove(name);
            return true;
        }else return false;
    }
    public boolean updateCenter(String name){
        if (centerM.containsKey(name)){
            centerM.get(name).setCenter(name);
            return true;
        }
        return false;
    }

    public String selectCenter(boolean id,boolean name, int idC,String nameC){
        StringBuilder sb = new StringBuilder();
        sb.append(id?String.format("%-4s ","id"):"");
        sb.append(name?String.format("%-50s ","name"):"");
        sb.append("\n");
        for (center e:centerM.values()) {
            boolean need = true;
            if (id)
                if (idC!=0)
                    need = e.sustc_id==idC;

            if (name)
                if (!nameC.equals(""))
                    need = e.center.equals(nameC);


            if (need){
                sb.append(id?String.format("%-4s ",e.sustc_id):"");
                sb.append(name?String.format("%-50s ",e.center):"");
                sb.append("\n");
            }

        }
        return sb.toString();
    }

    //TODO: STAFF
    public void getStaff(){
        try{
            CsvReader infile = new CsvReader("staff.csv",',', Charset.forName("UTF-8"));
            String[] values;
            infile.readHeaders();
            while (infile.readRecord()){
                values=infile.getValues();
                staffM.put(values[4],new staff(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBackStaff(){
        try {
            BufferedWriter w = new BufferedWriter (new OutputStreamWriter(new FileOutputStream("staff.csv"),"UTF-8"));
            w.write("id,name,age,gender,number,supply_center,mobile_number,type");
            w.newLine();
            for (staff a:staffM.values()){
                w.write(a.getStaff());
                w.newLine();
            }
            w.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addStaff(String name,String age, String gender,String number, String supply_center,String phone,String type){
        if (staffM.containsKey(number)){
            return false;
        }else {
            staffM.put(number,new staff(name,age,gender,number,supply_center,phone,type));
            return true;
        }
    }

    public boolean deleteStaff(String number){
        if (staffM.containsKey(number)){
            staffM.remove(number);
            return true;
        }else return false;
    }
    public boolean updateStaff(String name,String age, String gender,String number, String supply_center,String phone,String type){
        if (staffM.containsKey(number)){
            staffM.get(number).setStaff(name, age, gender, number, supply_center, phone, type);
            return true;
        }
        return false;
    }

    public String selectStaff(boolean id,boolean name,boolean age,
                              boolean gender, boolean number,
                              boolean supply_center, boolean phone,boolean type, int idC,String nameC,
                              String ageC,String genC,String numC,String supC, String phoC, String typeC){
        StringBuilder sb = new StringBuilder();
        sb.append(id?String.format("%-4s ","id"):"");
        sb.append(name?String.format("%-50s ","name"):"");
        sb.append(age?String.format("%-4s ","age"):"");
        sb.append(gender?String.format("%-7s ","gender"):"");
        sb.append(number?String.format("%-20s ","number"):"");
        sb.append(supply_center?String.format("%-50s ","supply_center"):"");
        sb.append(phone?String.format("%-15s ","phone"):"");
        sb.append(type?String.format("%-30s ","type"):"");
        sb.append("\n");
        for (staff e:staffM.values()) {
            boolean need = true;
            if (id)
                if (idC!=0)
                    need = e.id==idC;

            if (name)
                if (!nameC.equals(""))
                    need = e.name.equals(nameC);

            if (age)
                if (!ageC.equals(""))
                    need = e.age.equals(ageC);
            if (gender)
                if (!genC.equals(""))
                    need = e.gender.equals(genC);

            if (number)
                if (!numC.equals(""))
                    need = e.number.equals(numC);

            if (supply_center)
                if (!supC.equals(""))
                    need = e.supply_center.equals(supC);
            if (phone)
                if (!phoC.equals(""))
                    need = e.phone.equals(phoC);
            if (type)
                if (!typeC.equals(""))
                    need = e.type.equals(typeC);
            if (need){
                sb.append(id?String.format("%-4s ",e.id):"");
                sb.append(name?String.format("%-20s ",e.name):"");
                sb.append(age?String.format("%-4s ",e.age):"");
                sb.append(gender?String.format("%-4s ",e.gender):"");
                sb.append(number?String.format("%-20s ",e.number):"");
                sb.append(supply_center?String.format("%-50s ",e.supply_center):"");
                sb.append(phone?String.format("%-15s ",e.phone):"");
                sb.append(type?String.format("%-20s ",e.type):"");
                sb.append("\n");
            }

        }
        return sb.toString();
    }


    //TODO: MODEL
    public void getModel(){
        try{
            CsvReader infile = new CsvReader("model.csv",',', Charset.forName("UTF-8"));
            String[] values;
            infile.readHeaders();
            while (infile.readRecord()){
                values=infile.getValues();
                modelM.put(values[2],new model(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBackModel(){
        try {
            BufferedWriter w = new BufferedWriter (new OutputStreamWriter(new FileOutputStream("model.csv"),"UTF-8"));
            w.write("id,number,model,name,unit_price");
            w.newLine();
            for (model a:modelM.values()){
                w.write(a.getModel());
                w.newLine();
            }
            w.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addModel(String number, String model,String name,int price){
        if (modelM.containsKey(model)){
            return false;
        }else {
            modelM.put(model,new model(number,model,name,price));
            return true;
        }
    }

    public boolean deleteModel(String model){
        if (modelM.containsKey(model)){
            modelM.remove(model);
            return true;
        }else return false;
    }
    public boolean updateModel(String number,String model, String p_name, int unit_price){
        if (modelM.containsKey(model)){
            modelM.get(model).setModel(number, model, p_name, unit_price);
            return true;
        }
        return false;
    }


    public String selectModel(boolean id,boolean number,boolean model,
                              boolean p_name, boolean unit_price,
                              int idC,String numC,
                              String modC,String pnaC,int unitC){
        StringBuilder sb = new StringBuilder();
        sb.append(id?String.format("%-4s ","id"):"");
        sb.append(number?String.format("%-30s ","number"):"");
        sb.append(model?String.format("%-30s ","model"):"");
        sb.append(p_name?String.format("%-50s ","p_name"):"");
        sb.append(unit_price?String.format("%-10s ","unit_price"):"");

        sb.append("\n");
        for (model e:modelM.values()) {
            boolean need = true;
            if (id)
                if (idC!=0)
                    need = (e.model_id==idC);

            if (number)
                if (!numC.equals(""))
                    need = e.number.equals(numC);

            if (model)
                if (!modC.equals(""))
                    need = e.model.equals(modC);
            if (p_name)
                if (!pnaC.equals(""))
                    need = e.p_name.equals(pnaC);
            if (unit_price)
                if (unitC!=0)
                    need = (e.unit_price==unitC);


            if (need){
                sb.append(id?String.format("%-4s ",e.model_id):"");
                sb.append(number?String.format("%-30s ",e.number):"");
                sb.append(model?String.format("%-30s ",e.model):"");
                sb.append(p_name?String.format("%-50s ",e.p_name):"");
                sb.append(number?String.format("%-10s ",e.number):"");
                sb.append(unit_price?String.format("%-50s ",e.unit_price):"");

                sb.append("\n");
            }

        }
        return sb.toString();
    }

}
class enterprise{
    int c_id;
    String name;
    String country;
    String city;
    String industry;
    String supply_center;
    static int top_id=0;
    public enterprise(String[] values){
        this.c_id = Integer.parseInt(values[0]);
        this.name = values[1];
        this.country = values[2];
        this.city = values[3];
        this.industry = values[5];
        this.supply_center = values[4];
        if (this.c_id>top_id)
            top_id = c_id;
    }
    public enterprise(String name,String country,String city,String supply_center,String industry){
        this.c_id=++top_id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.industry = industry;
        this.supply_center = supply_center;
//        this.values=new String[]{""+this.c_id,name,country,city,supply_center,industry};
    }
    public void setEnterprise(String name,String country,String city,String supply_center,String industry){
        this.name = name;
        this.country = country;
        this.city = city;
        this.industry = industry;
        this.supply_center = supply_center;
    }
    public String getEnterprise(){
        return ""+c_id+","+name+","+country+","+city+","+supply_center+","+industry;
    }

}
class center{
    int sustc_id;
    String center;

    static int top_id = 0;
    public center(String[] values){
        this.sustc_id = Integer.parseInt(values[0]);
        this.center = values[1];
        if (sustc_id>top_id) top_id=sustc_id;
    }

    public center(String center){
        this.sustc_id = ++top_id;
        this.center = center;
    }

    public void setCenter(String center){

        this.center = center;
    }
    public String getCenter(){
        return ""+sustc_id+","+center;
    }
}
class model{
    int model_id;
    String number;
    String model;
    String p_name;
    int unit_price;
    String[] values;
    static int top_id=0;
    public model(String[] values){
        model_id = Integer.parseInt(values[0]);
        this.number = values[1];
        this.model = values[2];
        this.p_name = values[3];
        this.unit_price = Integer.parseInt(values[4]);
        this.values=values;

        if (model_id>top_id) top_id=model_id;
    }

    public model(String number,String model, String p_name, int unit_price){
        this.model_id = ++top_id;
        this.number = number;
        this.model = model;
        this.p_name = p_name;
        this.unit_price = unit_price;
    }

    public void setModel(String number,String model, String p_name, int unit_price){

        this.number = number;
        this.model = model;
        this.p_name = p_name;
        this.unit_price = unit_price;
    }
    public String getModel(){
        return ""+model_id+","+number+","+model+","+p_name+","+unit_price;
    }
}
class staff{
    int id;
    String name;
    String age;
    String gender;
    String number;
    String supply_center;
    String phone;
    String type;

    static int top_id = 0;
    //String id,String name,String age, String gender,String number, String supply_center,String phone,String type
    public staff(String[] values){
        this.id = Integer.parseInt(values[0]);
        this.name = values[1];
        this.age = values[2];
        this.gender = values[3];
        this.number = values[4];
        this.supply_center = values[5];
        this.phone = values[6];
        this.type = values[7];


        if (id>top_id) top_id=id;
    }
    public staff(String name,String age, String gender,String number, String supply_center,String phone,String type){
        this.id = ++top_id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.number = number;
        this.supply_center = supply_center;
        this.phone = phone;
        this.type = type;
    }

    public void setStaff(String name,String age, String gender,String number, String supply_center,String phone,String type){

        this.name = name;
        this.age = age;
        this.gender = gender;
        this.number = number;
        this.supply_center = supply_center;
        this.phone = phone;
        this.type = type;
    }
    public String getStaff(){
        return ""+id+","+name+","+age+","+gender+","+number+","+supply_center+","+phone+","+type;
    }
}
