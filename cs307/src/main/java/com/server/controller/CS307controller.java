package com.server.controller;


import com.server.entity.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class CS307controller {
    database db = new database();
    private Connection con = db.getCon();



    private ResultSet resultSet;
    private static PreparedStatement AllStaffCount = null;
    private static PreparedStatement ContractCount = null;
    private static PreparedStatement OrderCount = null;
    private static PreparedStatement NeverSoldProductCount = null;
    private static PreparedStatement FavoriteProductModel = null;
    private static PreparedStatement AvgStockByCenter = null;
    private static PreparedStatement ProductByNumber = null;
    private static PreparedStatement ContractInfo = null;
    private static PreparedStatement OrderInfo = null;

    private String host = "localhost";
    private String dbname = "CS307Proj2";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";

    public void openDB() {
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


    public void closeDB() {
        if (con != null) {
            try {

                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @RequestMapping("/getAllStaffCount")
    public StaffCount[] getAllStaffCount() {
//        openDB();
        ArrayList<StaffCount> ans = new ArrayList<>();

        try {
            AllStaffCount = con.prepareStatement("select type,count(*) as number from staff " +
                    "group by type");
            resultSet = AllStaffCount.executeQuery();

            while (resultSet.next()) {
                StaffCount t = new StaffCount();
                t.setStaffCount(resultSet.getString("type"),resultSet.getInt("number"));
                ans.add(t);
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.format("%-20s  %-5s", resultSet.getString("type"),
//                        resultSet.getString("number")));
//                ans.add(sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        closeDB();
        StaffCount[] arr = new StaffCount[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getContractCount")
    public String getContractCount() {
//        openDB();
        StringBuilder sb = new StringBuilder();
        try {
            ContractCount = con.prepareStatement("select count(*) as number from contract ");

            resultSet = ContractCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        closeDB();
        return sb.toString();
    }

    @RequestMapping("/getOrderCount")
    public String getOrderCount() {
//        openDB();
        StringBuilder sb = new StringBuilder();
        try {
            OrderCount = con.prepareStatement("select count(*) as number from orders");
            resultSet = OrderCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        closeDB();
        return sb.toString();
    }

    @RequestMapping("/getNeverSoldProductCount")
    public String getNeverSoldProductCount() {
//        openDB();
        StringBuilder sb = new StringBuilder();
        try {
            NeverSoldProductCount = con.prepareStatement("select count(model_id) as number " +
                    "from (select model_id, sum(stoNum)as allStoNum, sum(totNum) as allTotNum " +
                    "from (select model_id, sustc_id, sum(stock_quantity) stoNum, avg(tot_quantity) as totNum " +
                    "      from stock " +
                    "      group by model_id, sustc_id) m1 " +
                    "group by model_id)m2 " +
                    "where allStoNum = allTotNum;");
            resultSet = NeverSoldProductCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        closeDB();
        return sb.toString();
    }

    @RequestMapping("/getFavoriteProductModel")
    public FavoriteProductModelE[] getFavoriteProductModel() {
//        openDB();

        ArrayList<FavoriteProductModelE> ans = new ArrayList<>();
        try {
            FavoriteProductModel = con.prepareStatement("select model_name, num as number "
                    + "from ((select model_id, sum(quantity) as num from orders group by model_id) m2 left join product on m2.model_id = product.model_id)s1, " +
                    "(select max(num) maxq from (select model_id, sum(quantity) as num from orders group by model_id) m1) s2 where s1.num = s2.maxq");
            resultSet = FavoriteProductModel.executeQuery();


            while (resultSet.next()) {
                FavoriteProductModelE t = new FavoriteProductModelE();
                t.setFavoriteProductModelE(resultSet.getString("model_name"),resultSet.getInt("number"));
                ans.add(t);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        closeDB();
        FavoriteProductModelE[] arr = new FavoriteProductModelE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getAvgStockByCenter")
    public AvgStockByCenterE[] getAvgStockByCenter() {
//        openDB();
        ArrayList<AvgStockByCenterE> ans = new ArrayList<>();

        try {
            AvgStockByCenter = con.prepareStatement("select m2.supply_center, text(round(avg(sum / 1.0), 1)) as average " +
                    "from (select * " +
                    "      from (select sustc_id, model_id, avg(tot_quantity) as sum " +
                    "            from stock " +
                    "            group by sustc_id, model_id) m1 " +
                    "               left join sustc on m1.sustc_id = sustc.sustc_id) m2 " +
                    "group by m2.supply_center " +
                    "order by supply_center");
            resultSet = AvgStockByCenter.executeQuery();


            while (resultSet.next()) {
                AvgStockByCenterE t = new AvgStockByCenterE();
                t.setAvgStockByCenterE(resultSet.getString("supply_center"),resultSet.getDouble("average"));
                ans.add(t);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        closeDB();
        AvgStockByCenterE[] arr = new AvgStockByCenterE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getProductByNumber")
    public ProductByNumberE[] getProductByNumber(String product_number) {
        openDB();
        ArrayList<ProductByNumberE> ans = new ArrayList<>();

        try {
            ProductByNumber = con.prepareStatement("select distinct supply_center,code as product_name, " +
                    "model_name,purchase_price ,tot_quantity " +
                    "from (select * from stock " +
                    "    left join (select model_id m1_id, code, model_name from product) m1 on m1_id = stock.model_id " +
                    "    left join sustc s on stock.sustc_id = s.sustc_id) m2 " +
                    "where code = ? ");
            ProductByNumber.setString(1,product_number);
            resultSet = ProductByNumber.executeQuery();

            while (resultSet.next()) {
                ProductByNumberE t = new ProductByNumberE();
                t.setProductByNumberE(resultSet.getString("supply_center"),
                        resultSet.getString("product_name"),
                        resultSet.getString("model_name"),
                        resultSet.getInt("purchase_price"),
                        resultSet.getInt("tot_quantity"));
                ans.add(t);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ProductByNumberE[0];
        }
        closeDB();
        ProductByNumberE[] arr = new ProductByNumberE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getContractInfo")
    public Object[] getContractInfo(String contract_number) {
        openDB();
        Object[] arr = new Object[2];
        ContractInfo1 arr0 = new ContractInfo1();
        ArrayList<ContractInfo2> ans = new ArrayList<>();

        try {
            ContractInfo = con.prepareStatement("select contract_id,staff_name,c_name,supply_center from contract " +
                    "left join (select c_name, client_id as c_id , sustc_id from client) c on c_id = contract.client_id " +
                    "left join (select staff_id s_id, staff_name from staff) s on contract.manager_id = s_id " +
                    "left join (select sustc_id as s2_id ,supply_center from sustc) s2 on s2_id = sustc_id "+
                    "where contract_id = ?");
            ContractInfo.setString(1,contract_number);
            resultSet = ContractInfo.executeQuery();
            resultSet.next();
            arr0.setContractInfo1(resultSet.getString("contract_id"),resultSet.getString("staff_name")
                    ,resultSet.getString("c_name"),resultSet.getString("supply_center"));
            ContractInfo1[] need1 = new ContractInfo1[1];
            need1[0] = arr0;
            arr[0]=need1;

            OrderInfo = con.prepareStatement("select model_name,staff_name,quantity,price,delivery_date,lodgement_date from orders " +
                    "left join (select model_id p_id, model_name, price from product)p on orders.model_id = p_id " +
                    "left join (select staff_id s_id, staff_name from staff)s on salesman_id = s_id " +
                    "where contract_id = ?");
            OrderInfo.setString(1,contract_number);
            resultSet = OrderInfo.executeQuery();


            while (resultSet.next()) {
                ContractInfo2 t = new ContractInfo2();
                t.setContractInfo2(resultSet.getString("model_name"),
                        resultSet.getString("staff_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price"),
                        resultSet.getString("delivery_date"),
                        resultSet.getString("lodgement_date"));
                ans.add(t);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDB();
        ContractInfo2[] arr1 = new ContractInfo2[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr1[i] = ans.get(i);

        }
        arr[1] = arr1;
        return arr;
    }


    @RequestMapping("/truncateAll")
    public String truncateAll(){
        openDB();
        String sql="truncate table stock,orders, sustc,staff, product, client,contract restart identity cascade";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return "fail!";
        }
        closeDB();
        return "success!";
    }


}

class database{
    private Connection con = null;
    private String host = "localhost";
    private String dbname = "CS307Proj2";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";

    public database(){
        openDB();
    }

    public void openDB() {
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
        return;
    }


    public void closeDB() {
        if (con != null) {
            try {

                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getCon(){
        return this.con;
    }
}
