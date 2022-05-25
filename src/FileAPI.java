import com.csvreader.CsvReader;

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

public class FileAPI {
    protected Connection con = null;

    static Map<String,center> centerM = new LinkedHashMap<>();
    static Map<String,enterprise> enterpriseM = new LinkedHashMap<>();
    static Map<String,model> modelM = new LinkedHashMap<>();
    static Map<String,staff> staffM = new LinkedHashMap<>();

    private String host = "localhost";
    private String dbname = "CS307Proj2";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";

    protected void getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    protected void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
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
