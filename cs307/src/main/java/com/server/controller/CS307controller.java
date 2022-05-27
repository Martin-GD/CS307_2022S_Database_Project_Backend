package com.server.controller;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.server.entity.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class CS307controller {
    database db = new database();




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
    private static PreparedStatement MonthBill = null;

    public CS307controller() throws SQLException {
    }

    @RequestMapping("/changeToManager")
    public String changeToManager(String password){
        try{
            db.changeConnectionToManager(password);
        }catch (Exception e){
            return "fail!";
        }
        return "success!";
    }
    @RequestMapping("/changeBack")
    public String changeBack(String password){
        try{
            db.changeConnectionBack(password);
        }catch (Exception e){
            return "fail!";
        }
        return "success!";
    }

    @RequestMapping("/createRole")
    public String createRole(String role_name){
        try{
            db.createRole(role_name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "fail!";
        }
        return "success!";
    }


    @RequestMapping("/OrderSelect")
    public OrdersE[] OrderSelect(String contract_id, String model_id, String salesman_id) throws SQLException {
        Connection con = db.getCon();
        ArrayList<OrdersE> ans = new ArrayList<>();
        boolean temp = false;
        try{
            String sql = "select order_id, contract_id,model_id,quantity,salesman_id,delivery_date,lodgement_date,contract_type from orders where ";
            if (!contract_id.equals("")){
                sql +=("contract_id = "+"? ");
                temp = true;
            }
            if (temp&& !model_id.equals("")){
                sql += " and ";
                temp = false;
            }
            if (!model_id.equals("")){
                sql +=("model_id = "+"? ");
                temp = true;
            }
            if (temp&&!salesman_id.equals("")){
                sql += " and ";
                temp = false;
            }
            if (!salesman_id.equals("")){
                sql +=("salesman_id = "+"? ");
                temp = true;
            }
//            System.out.println(sql);
            PreparedStatement t = con.prepareStatement(sql);
            if (!contract_id.equals("")){
                t.setString(1,contract_id);
            }
            if (!model_id.equals("")){
                if (!contract_id.equals(""))
                    t.setString(2,model_id);
                else
                    t.setString(1,model_id);
            }
            if (!salesman_id.equals("")){
                if (!contract_id.equals("")){
                    if (!model_id.equals(""))
                        t.setString(3,salesman_id);
                    else t.setString(2,salesman_id);
                }else {
                    if (!model_id.equals(""))
                        t.setString(2,salesman_id);
                    else t.setString(1,salesman_id);
                }
            }

            resultSet = t.executeQuery();
            while (resultSet.next()){
                OrdersE a = new OrdersE();
                a.setOrdersE(resultSet.getInt("order_id"),
                        resultSet.getString("contract_id"),
                        resultSet.getString("model_id"),
                        resultSet.getString("quantity"),
                        resultSet.getString("salesman_id"),
                        resultSet.getString("delivery_date"),
                        resultSet.getString("lodgement_date"),
                        resultSet.getString("contract_type")
                        );
                ans.add(a);
            }


        }catch (Exception e){

        } finally {
            con.close();
        }
        OrdersE[] arr = new OrdersE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getAllStaffCount")
    public StaffCount[] getAllStaffCount() throws SQLException {

        Connection con = db.getCon();
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
        } finally {
            con.close();
        }

        StaffCount[] arr = new StaffCount[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getContractCount")
    public String getContractCount() throws SQLException {

        Connection con = db.getCon();
        StringBuilder sb = new StringBuilder();
        try {
            ContractCount = con.prepareStatement("select count(*) as number from contract ");

            resultSet = ContractCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
//        closeDB();
        return sb.toString();
    }

    @RequestMapping("/getOrderCount")
    public String getOrderCount() throws SQLException {
        Connection con = db.getCon();
        StringBuilder sb = new StringBuilder();
        try {
            OrderCount = con.prepareStatement("select count(*) as number from orders");
            resultSet = OrderCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
//        closeDB();
        return sb.toString();
    }

    @RequestMapping("/getNeverSoldProductCount")
    public String getNeverSoldProductCount() throws SQLException {
        Connection con = db.getCon();
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
        } finally {
            con.close();
        }
//        closeDB();
        return sb.toString();
    }

    @RequestMapping("/getFavoriteProductModel")
    public FavoriteProductModelE[] getFavoriteProductModel() throws SQLException {
        Connection con = db.getCon();

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
        } finally {
            con.close();
        }
//        closeDB();
        FavoriteProductModelE[] arr = new FavoriteProductModelE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getAvgStockByCenter")
    public AvgStockByCenterE[] getAvgStockByCenter() throws SQLException {
        Connection con = db.getCon();
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
        } finally {
            con.close();
        }
//        closeDB();
        AvgStockByCenterE[] arr = new AvgStockByCenterE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }

    @RequestMapping("/getProductByNumber")
    public ProductByNumberE[] getProductByNumber(String product_number) throws SQLException {
        Connection con = db.getCon();
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
        } finally {
            con.close();
        }
//        closeDB();
        ProductByNumberE[] arr = new ProductByNumberE[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr[i] = ans.get(i);
        }
        return arr;
    }


    @RequestMapping("/getContractInfo")
    public Object[] getContractInfo(String contract_number) throws SQLException {
        Connection con = db.getCon();
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
        } finally {
            con.close();
        }
//        closeDB();
        ContractInfo2[] arr1 = new ContractInfo2[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            arr1[i] = ans.get(i);

        }
        arr[1] = arr1;
        return arr;
    }

    @RequestMapping("/getMonthlyAll")
    public MonthlyAllE[][] getMonthlyAll() throws SQLException {
        Connection con = db.getCon();
        MonthlyAllE[][] arr = new MonthlyAllE[13][1];
        for (int i = 1; i <= 12; i++) {
            int month = i;
            try {
                MonthBill = con.prepareStatement("select * from bill_2022 where month=?");
                MonthBill.setInt(1, month);
                resultSet = MonthBill.executeQuery();
                MonthlyAllE t = new MonthlyAllE();

                if (resultSet.next()) {
                    t.setMonthlyAllE(resultSet.getString("month"),
                            resultSet.getString("total_income"),
                            resultSet.getString("most_profitable_product_id"),
                            resultSet.getString("most_profitable_sustc_id"),
                            resultSet.getString("salesman_of_the_month"));

                }else{

                }

                arr[i][0]=t;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        con.close();

        return arr;
    }


    @RequestMapping("/truncateAll")
    public String truncateAll() throws SQLException {
        Connection con = db.getCon();
        String sql="truncate table stock,orders, sustc,staff, product, client,contract,bill_2022 restart identity cascade";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return "fail!";
        } finally {
            con.close();
        }
//        closeDB();
        return "success!";
    }


}

class database{

    private ComboPooledDataSource dbPool = new ComboPooledDataSource();
    private String host = "localhost";
    private String dbname = "CS307Proj2";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";

    public database(){
        openDB();
    }

    public void openDB() {
//        try {
//            Class.forName("org.postgresql.Driver");
//
//        } catch (Exception e) {
//            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
//            System.exit(1);
//        }
//        try {
//            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
//            con = DriverManager.getConnection(url, user, pwd);
//
//        } catch (SQLException e) {
//            System.err.println("Database connection failed");
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//        return;
        try {
            dbPool.setDriverClass("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
        dbPool.setUser(user);
        dbPool.setPassword(pwd);
        dbPool.setJdbcUrl(url);

        dbPool.setInitialPoolSize(8);
        dbPool.setMaxPoolSize(12);
    }

    public void changeConnectionToManager(String password) {
        //pwd=password_m
        this.user = "manager";
        this.pwd = password;
        dbPool.close();
        dbPool = new ComboPooledDataSource();
        openDB();
    }

    public void changeConnectionBack(String password) {
        //pwd=123456
        user = "test";
        pwd = password;
        dbPool.close();
        dbPool = new ComboPooledDataSource();
        openDB();
    }

    public void createRole(String role_name) throws SQLException {
        //do block for creating roles do not exist
        Connection con = dbPool.getConnection();

        String sql="DO " +
                "                $do$ " +
                "                BEGIN " +
                "                   IF EXISTS ( " +
                "                      SELECT FROM pg_catalog.pg_roles " +
                "                      WHERE  rolname = ";
        sql+="'"+role_name;
        sql+="')then ELSE create role \""+role_name+"\"; end if; end $do$;";
        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.execute();
            grantUsages(role_name);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //一定要在test user下切换成staff不然会查不到
    public void grantUsages(String userName) throws SQLException {
        Connection con = dbPool.getConnection();

        String sqlGrant = "grant normal_staffs to ";
        String sql = "set role = ";
        try {
            sql+="\""+userName+"\"";
            PreparedStatement stmt = con.prepareStatement(sql);
            sqlGrant+="\""+userName+"\"";
            PreparedStatement stmtGrant = con.prepareStatement(sqlGrant);
            stmtGrant.execute();
            stmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    public Connection getCon() throws SQLException {
        return this.dbPool.getConnection();
    }
}
