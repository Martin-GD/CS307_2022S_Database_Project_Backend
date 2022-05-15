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
    String[] values;
    static int top_id = 0;
    public center(String[] values){
        this.sustc_id = Integer.parseInt(values[0]);
        this.center = values[1];
        this.values=values;

        if (sustc_id>top_id) top_id=sustc_id;
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
    String[] values;
    static int top_id = 0;
    public staff(String s_id,String s_name,String gender,String age,String supply_center,String phone,String line,String[] values){
        this.id = Integer.parseInt(values[0]);
        this.name = values[1];
        this.age = values[2];
        this.gender = values[3];
        this.number = values[4];
        this.supply_center = values[5];
        this.phone = values[6];
        this.type = values[7];
        this.values=values;

        if (id>top_id) top_id=id;
    }
    public String getStaff(){
        return ""+id+","+name+","+age+","+gender+","+number+","+supply_center+","+phone+","+type;
    }
}
