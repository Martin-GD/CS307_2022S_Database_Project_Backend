
import java.sql.*;

public class check {
    private Connection con = null;
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
    private String dbname = "project2";
    private String user = "test";
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


    public String getAllStaffCount() {
        StringBuilder sb = new StringBuilder();
        try {
            AllStaffCount = con.prepareStatement("select type,count(*) as number from staff " +
                    "group by type");
            resultSet = AllStaffCount.executeQuery();
            while (resultSet.next()) {
                sb.append(String.format("%-20s  %-5s", resultSet.getString("type"),
                        resultSet.getString("number"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getContractCount() {
        StringBuilder sb = new StringBuilder();
        try {
            ContractCount = con.prepareStatement("select count(*) as number from contract");

            resultSet = ContractCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getOrderCount() {
        StringBuilder sb = new StringBuilder();
        try {
            OrderCount = con.prepareStatement("select count(*) as number from orders");
            resultSet = OrderCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getNeverSoldProductCount() {
        StringBuilder sb = new StringBuilder();
        try {
            NeverSoldProductCount = con.prepareStatement("select distinct count(m1.model_id)\n" +
                    "from (select model_id, sum(stock_quantity) as stoNum from stock group by model_id) m1\n" +
                    "         join stock on m1.model_id = stock.model_id\n" +
                    "where stoNum = tot_quantity; ");
            resultSet = NeverSoldProductCount.executeQuery();

            while (resultSet.next()) {
                sb.append(String.format("%-5s", resultSet.getString("number"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getFavoriteProductModel() {
        StringBuilder sb = new StringBuilder();
        try {
            FavoriteProductModel = con.prepareStatement("select model_name, num as number "
                    + "from ((select model_id, sum(quantity) as num from orders group by model_id) m2 left join product on m2.model_id = product.model_id)s1, " +
                    "(select max(num) maxq from (select model_id, sum(quantity) as num from orders group by model_id) m1) s2 where s1.num = s2.maxq");
            resultSet = FavoriteProductModel.executeQuery();
            sb.append(String.format("%-50s  %-5s", "model_name", " quantity")).append("\n");
            while (resultSet.next()) {
                sb.append(String.format("%-50s  %-5s", resultSet.getString("model_name"),
                        resultSet.getString("number"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getAvgStockByCenter() {
        StringBuilder sb = new StringBuilder();
        try {
            AvgStockByCenter = con.prepareStatement("select supply_center, average from (select sustc_id, model_id, round(avg(stock_quantity), 1) as average "
                    + "from stock group by sustc_id, model_id) m1 left join sustc on m1.sustc_id = sustc.sustc_id "
                    + "where model_id = 1 order by supply_center");
            resultSet = AvgStockByCenter.executeQuery();
//            sb.append(String.format("%-50s  %-5s", "supply_center", " average")).append("\n");
            while (resultSet.next()) {
                sb.append(String.format("%-50s  %-5s", resultSet.getString("supply_center"),
                        resultSet.getString("average"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getProductByNumber(String product_number) {
        StringBuilder sb = new StringBuilder();
        try {
            ProductByNumber = con.prepareStatement("select distinct supply_center,code as product_name, " +
                    "model_name,purchase_price as purchase_prise,tot_quantity " +
                    "from (select * from stock " +
                    "    left join (select model_id m1_id, code, model_name from product) m1 on m1_id = stock.model_id " +
                    "    left join sustc s on stock.sustc_id = s.sustc_id) m2 " +
                    "where code = ? ");
            ProductByNumber.setString(1,product_number);
            resultSet = ProductByNumber.executeQuery();
            sb.append(String.format("%-50s  %-20s  %-50s  %-15s  %-15s", "supply_center",
                    "product_name","model_name","purchase_prise","quantity")).append("\n");
            while (resultSet.next()) {
                sb.append(String.format("%-50s  %-20s  %-50s  %-15s  %-15s", resultSet.getString("supply_center"),
                        resultSet.getString("product_name"),
                        resultSet.getString("model_name"),
                        resultSet.getString("purchase_price"),
                        resultSet.getString("tot_quantity"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getContractInfo(String contract_number) {
        StringBuilder sb = new StringBuilder();
        try {
            ContractInfo = con.prepareStatement("select contract_id,staff_name,c_name,supply_center from contract " +
                    "left join (select c_name, client_id as c_id , sustc_id from client) c on c_id = contract.client_id " +
                    "left join (select staff_id s_id, staff_name from staff) s on contract.manager_id = s_id " +
                    "left join (select sustc_id as s2_id ,supply_center from sustc) s2 on s2_id = sustc_id "+
                    "where contract_id = ?");
            ContractInfo.setString(1,contract_number);
            resultSet = ContractInfo.executeQuery();
            sb.append("number: ").append(resultSet.getString("contract_id")).append("\n");
            sb.append("manager: ").append(resultSet.getString("staff_name")).append("\n");
            sb.append("enterprise: ").append(resultSet.getString("c_name")).append("\n");
            sb.append("supply_center: ").append(resultSet.getString("supply_center")).append("\n");

            OrderInfo = con.prepareStatement("select model_name,staff_name,quantity,price,delivery_date,lodgement_date from orders " +
                    "left join (select model_id p_id, model_name, price from product)p on orders.model_id = p_id " +
                    "left join (select staff_id s_id, staff_name from staff)s on salesman_id = s_id " +
                    "where contract_id = ?");
            OrderInfo.setString(1,contract_number);
            resultSet = OrderInfo.executeQuery();

            int a = 0;
            while (resultSet.next()) {
                if (a==0){
                    sb.append(String.format("%-50s  %-30s  %-10s  %-15s  %-25s  %-20s",
                            "product_model", "salesman","quantity","unit_price","estimate_delivery_date","lodgement_date")).append("\n");
                    a++;
                }
                sb.append(String.format("%-50s  %-30s  %-10s  %-15s  %-25s  %-20s",
                        resultSet.getString("model_name"),
                        resultSet.getString("staff_name"),
                        resultSet.getString("quantity"),
                        resultSet.getString("price"),
                        resultSet.getString("delivery_date"),
                        resultSet.getString("lodgement_date"))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
