
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;

import com.csvreader.CsvReader;

public class dataImportV1 {
    protected Connection con = null;

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

    public void impCenter() throws SQLException {

        String sql = "insert into sustc(supply_center) VALUES (?) on conflict do nothing ;";
        String[] message;
        String center;
        try (PreparedStatement impC = con.prepareStatement(sql)) {
            CsvReader infile = new CsvReader("center.csv", ',', Charset.forName("UTF-8"));
            infile.readHeaders();
            while (infile.readRecord()) {
                message = infile.getValues();
                center = message[1];
                impC.setString(1, center);
                impC.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void impEnterprise() throws SQLException {

        String sql = "insert into client(c_name, country, city, industry, sustc_id) VALUES (?,?,?,?,?) on conflict do nothing ;";
        String[] message;
        String cName, country, city, industry;
        int supplyCenter;

        try (PreparedStatement impE = con.prepareStatement(sql)) {
            CsvReader infile = new CsvReader("enterprise.csv", ',', Charset.forName("UTF-8"));
            infile.readHeaders();
            while (infile.readRecord()) {
                message = infile.getValues();
                cName = message[1];
                country = message[2];
                city = message[3].equals("") ? null : message[3];
                supplyCenter = getSupplyCenter(message[4]);
                industry = message[5];
                impE.setString(1, cName);
                impE.setString(2, country);
                impE.setString(3, city);
                impE.setString(4, industry);
                impE.setInt(5, supplyCenter);
                impE.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void impStaff() throws SQLException {

        String sql = "insert into staff(staff_id, staff_name, gender, age, sustc_id, phone, type) VALUES(?,?,?,?,?,?,?) on conflict do nothing ;";
        String[] message;
        String name, gender, phone, type;
        int age, number, supplyCenter;

        try (PreparedStatement impS = con.prepareStatement(sql)) {
            CsvReader infile = new CsvReader("staff.csv", ',', Charset.forName("UTF-8"));
            infile.readHeaders();
            while (infile.readRecord()) {
                message = infile.getValues();
                name = message[1];
                age = Integer.parseInt(message[2]);
                gender = message[3];
                number = Integer.parseInt(message[4]);
                phone = message[6];
                supplyCenter = getSupplyCenter(message[5]);
                type = message[7];
                impS.setInt(1, number);
                impS.setString(2, name);
                impS.setString(3, gender);
                impS.setInt(4, age);
                impS.setInt(5, supplyCenter);
                impS.setString(6, phone);
                impS.setString(7, type);
                impS.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void impModel() throws SQLException {

        String sql = "insert into product(code, model_name, price, p_name) VALUES (?,?,?,?)on conflict do nothing ;";
        String[] message;
        String code, model_name, p_name;
        int price;

        try (PreparedStatement impM = con.prepareStatement(sql)) {
            CsvReader infile = new CsvReader("model.csv", ',', Charset.forName("UTF-8"));
            infile.readHeaders();
            while (infile.readRecord()) {
                message = infile.getValues();
                code = message[1];
                model_name = message[2];
                p_name = message[3];
                price = Integer.parseInt(message[4]);
                impM.setString(1, code);
                impM.setString(2, model_name);
                impM.setString(4, p_name);
                impM.setInt(3, price);

                impM.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStockIn() throws SQLException {

        String sql = "insert into stock(sustc_id,model_id,supply_staff_id," +
                "purchase_price,stock_quantity,stock_date) values(?,?,?,?,?,?) on conflict do nothing";
        String[] stockInfo;
        String supply_center;
        String product_model;
        String dateChange;
        Date date;
        int purchase_price, quantity, supply_staff, sustc_id, model_id;
        boolean checkStaff;
        boolean checkProduct;
        boolean checkSupCenter;
        String[] staffInfo;
        try (PreparedStatement stockIn = con.prepareStatement(sql);) {
            CsvReader infile = new CsvReader("in_stock_test.csv", ',', Charset.forName("UTF-8"));
            infile.readHeaders();
            while (infile.readRecord()) {
                stockInfo = infile.getValues();
                supply_center = stockInfo[1];
                product_model = stockInfo[2];
                supply_staff = Integer.parseInt(stockInfo[3]);
                dateChange = stockInfo[4].replace('/', '-');
                date = Date.valueOf(dateChange);
                purchase_price = Integer.parseInt(stockInfo[5]);
                quantity = Integer.parseInt(stockInfo[6]);
                checkProduct = checkProduct(product_model);
                checkStaff = checkStaff(supply_staff);
                checkSupCenter = checkSupCen(supply_center);
                staffInfo = getStaffInfo(supply_staff);
                sustc_id = getSupplyCenter(supply_center);
                if (!checkProduct || !checkStaff || !checkSupCenter) {
                    continue;
                } else if (Integer.parseInt(staffInfo[0]) != sustc_id) {
                    continue;
                } else if (!staffInfo[1].equals("Supply Staff")) {
                    continue;
                } else {
                    model_id = getProductId(product_model);
                    stockIn.setInt(1, sustc_id);
                    stockIn.setInt(2, model_id);
                    stockIn.setInt(3, supply_staff);
                    stockIn.setInt(4, purchase_price);
                    stockIn.setInt(5, quantity);
                    stockIn.setDate(6, date);
                }
                //先获得前一状态的tot再update
                int tot = getTotQuantity(quantity, sustc_id, model_id);
                stockIn.execute();
                updateTotQuantity(tot, sustc_id, model_id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }

    public String[] getStaffInfo(int staff_id) {

        String sql = "select * from staff where staff_id=?";
        String[] ans = new String[2];
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, staff_id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ans[0] = result.getString("sustc_id");
                ans[1] = result.getString("type");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public int getSupplyCenter(String supplyCenter) {

        String sql = "select * from sustc where supply_center =?";
        int ans = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, supplyCenter);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                ans = Integer.parseInt(result.getString("sustc_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public int getProductId(String model) {

        String sql = "select * from product where model_name=?";
        int ans = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, model);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ans = Integer.parseInt(result.getString("model_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public boolean checkProduct(String model) {
        //返回false?即为产品不存在

        String sql = "select * from product where model_name =?";
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, model);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ans = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public boolean checkStaff(int staffId) {

        String sql = "select * from staff where staff_id =?";
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, staffId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ans = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ans;
    }

    public boolean checkSupCen(String supCenter) {

        String sql = "select * from sustc where supply_center =?";
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, supCenter);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ans = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void placeOrder() throws SQLException {

        String orderSql = "insert into orders(contract_id,model_id,quantity" +
                ",tot_income,salesman_id,delivery_date,lodgement_date,contract_type)" +
                "values(?,?,?,?,?,?,?,?)on conflict do nothing";
//        String orderSql = "insert into orders(contract_id,model_id,quantity" +
//                ",salesman_id,delivery_date,lodgement_date,contract_type)" +
//                "values(?,?,?,?,?,?,?)on conflict do nothing";
        String contractSql = "insert into contract(contract_id,date,client_id,manager_id)" +
                "values(?,?,?,?)on conflict do nothing";
        String billSql = "insert into bills(sustc_id,contract_id,contract_expense,contract_income," +
                "profit) values(?,?,?,?,?) on conflict do nothing";
        String updateBillSql = "update bills set contract_expense=? , contract_income=?, profit=? where contract_id=? ";


        String line;
        String[] orderInfo, staffInfo;
        int[] clientInfo;
        String cName, productModel, conType, conId;
        int quantity, salesId, modelId, clientId, conManager, sustcId, income, num, conExpense, conIncome, conProfit, purchasePrice;
        Date conDate, lodgementDate, delDate;
        try {
            PreparedStatement order = con.prepareStatement(orderSql);
            PreparedStatement contract = con.prepareStatement(contractSql);
            PreparedStatement billInsert = con.prepareStatement(billSql);
            PreparedStatement billUpdate = con.prepareStatement(updateBillSql);

            try (BufferedReader infile
                         = new BufferedReader(new FileReader("task2_test_data_final_public.tsv"))) {
                while ((line = infile.readLine()) != null) {
                    orderInfo = line.split("\t");
                    if (!orderInfo[0].equals("contract_num")) {
                        conId = orderInfo[0];
                        cName = orderInfo[1];
                        productModel = orderInfo[2];
                        quantity = Integer.parseInt(orderInfo[3]);
                        conManager = Integer.parseInt(orderInfo[4]);
                        conDate = java.sql.Date.valueOf(orderInfo[5]);
                        delDate = java.sql.Date.valueOf(orderInfo[6]);
                        lodgementDate = java.sql.Date.valueOf(orderInfo[7]);
                        salesId = Integer.parseInt(orderInfo[8]);
                        conType = orderInfo[9];
                        staffInfo = getStaffInfo(salesId);
                        modelId = getProductId(productModel);
                        clientInfo = getClientInfoByName(cName);
                        clientId = clientInfo[0];
                        sustcId = clientInfo[1];
                        num = getUnitPrice(modelId);
                        income = quantity * num;
                        if (staffInfo[1].equals("Salesman") && checkStock(sustcId, modelId, quantity)) {

                            contract.setString(1, conId);
                            contract.setDate(2, conDate);
                            contract.setInt(3, clientId);
                            contract.setInt(4, conManager);
                            order.setString(1, conId);
                            order.setInt(2, modelId);
                            order.setInt(3, quantity);
                            order.setInt(4, income);
                            order.setInt(5, salesId);
                            order.setDate(6, delDate);
                            order.setDate(7, lodgementDate);
                            order.setString(8, conType);

                            updateStock(modelId, quantity, sustcId);
                            contract.execute();
                            order.execute();
                            //已经插入过则是true then update
//                            if (checkBill(conId)) {
//                                purchasePrice = getPurchasePrice(sustcId, modelId, );
//                                conExpense = quantity * purchasePrice;
//                                num = getUnitPrice(modelId);
//                                income = quantity * num;
//                                billUpdate.setInt(1, );
//                                billUpdate.setInt(2, );
//                                billUpdate.setInt(3, );
//                                billUpdate.setString(4, conId);
//                                billUpdate.execute();
//                            }else{
//                                billInsert.setInt(1,sustcId);
//                                billInsert.setString(2,conId);
//                                billInsert.setInt(3,);
//                                billInsert.setInt(4,);
//                                billInsert.setInt(5,);
//                                billInsert.execute();
//                            }
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public int getUnitPrice(int modelId) {
        String sql = "select price from product where model_id=?";
        int ans = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, modelId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ans = Integer.parseInt(resultSet.getString("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void updateStock(int modelId, int quantity, int sustcId) {

        String sql = "update stock set tot_quantity=? where model_id=? and sustc_id=?";
        int rest;
        int stockQuantity = getStock(modelId, sustcId);
        rest = stockQuantity - quantity;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, rest);
            preparedStatement.setInt(2, modelId);
            preparedStatement.setInt(3, sustcId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //返回现有的tot库存数量
    public int getStock(int modelId, int sustcId) {

        int ans = 0;
        String sql = "select * from stock where model_id=? and sustc_id=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, modelId);
            stmt.setInt(2, sustcId);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                ans = Integer.parseInt(result.getString("tot_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public boolean checkUpdateStock(int sustcId, int modelId, int quantity) {

        String sql = "select * from stock where model_id=? and sustc_id=?";
        boolean ans = false;
        int before = getStock(modelId, sustcId);
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, modelId);
            preparedStatement.setInt(2, sustcId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int totQuantity = Integer.parseInt(result.getString("stock_quantity"));
                ans = before >= (quantity - totQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public boolean checkStock(int sustcId, int modelId, int quantity) {

        String sql = "select * from stock where model_id=? and sustc_id=?";
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, modelId);
            preparedStatement.setInt(2, sustcId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int totQuantity = Integer.parseInt(result.getString("tot_quantity"));
                ans = totQuantity >= (quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public int[] getClientInfoByName(String cName) {

        String sql = "select * from client where c_name=?";
        int[] ans = new int[2];
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, cName);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ans[0] = Integer.parseInt(result.getString("client_id"));
                ans[1] = Integer.parseInt(result.getString("sustc_id"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void updateOrder() throws SQLException {

        String sql = "update orders set quantity=?,delivery_date=?,lodgement_date=?,tot_income=?" +
                "where contract_id=? and model_id=? and salesman_id=?";
        String line;
        String[] updateInfo;
        String conId, productModel;
        int salesmanId, quantity, modelId, clientId, sustcId, income;
        String[] clientInfo;
        Date delDate, lodDate;
        try (PreparedStatement updateOrder = con.prepareStatement(sql); BufferedReader infile
                = new BufferedReader(new FileReader("update_final_test.tsv"))) {
            while ((line = infile.readLine()) != null) {
                updateInfo = line.split("\t");
                conId = updateInfo[0];
                if (conId.equals("contract")) {
                    continue;
                } else {
                    productModel = updateInfo[1];
                    salesmanId = Integer.parseInt(updateInfo[2]);
                    quantity = Integer.parseInt(updateInfo[3]);
                    delDate = java.sql.Date.valueOf(updateInfo[4]);
                    lodDate = java.sql.Date.valueOf(updateInfo[5]);
                    modelId = getProductId(productModel);
                    clientId = getContractInfo(conId);
                    clientInfo = getClientInfoById(clientId);
                    sustcId = Integer.parseInt(clientInfo[1]);
                    income = quantity * getUnitPrice(modelId);
                    //更新后的数量是0即删除，检查quantity
                    if (quantity == 0) {
                        deleteOneOrder(conId, modelId, salesmanId);
                        returnUpdateStock(modelId, sustcId, quantity, conId, salesmanId);
                    } else if (checkUpdateStock(sustcId, modelId, quantity)) {
                        //然后检查是否会超出库存上限
                        //update stock
                        //judge if number=0 and needed to be removed
                        //dont delete contracts
                        updateOrder.setInt(1, quantity);
                        updateOrder.setDate(2, delDate);
                        updateOrder.setDate(3, lodDate);
                        updateOrder.setInt(4, income);
                        updateOrder.setString(5, conId);

                        updateOrder.setInt(6, modelId);
                        updateOrder.setInt(7, salesmanId);

                        returnUpdateStock(modelId, sustcId, quantity, conId, salesmanId);
                        updateOrder.execute();
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getOrderQuantity(String conId, int modelId, int salesId) {

        int ans = 0;
        String sql = "select * from orders where model_id=? and salesman_id=? and contract_id=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, modelId);
            stmt.setInt(2, salesId);
            stmt.setString(3, conId);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                ans = Integer.parseInt(result.getString("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void deleteOneOrder(String conId, int modelId, int salesId) {

        String sql = "delete from orders where contract_id=? and model_id=? and salesman_id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, conId);
            preparedStatement.setInt(2, modelId);
            preparedStatement.setInt(3, salesId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean checkSaleOrder(String conId, int salesId, int modelId) {

        String sql = "select * from orders where contract_id=? and salesman_id=? and model_id=?";
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, conId);
            preparedStatement.setInt(2, salesId);
            preparedStatement.setInt(3, modelId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ans = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void deleteOrder() throws SQLException {


        String line;
        String[] testInfo;
        String conId;
        int salesId, seq, modelId, quantity;
        int[] stockInfo;

        try (BufferedReader infile
                     = new BufferedReader(new FileReader("delete_final.tsv"))) {
            while ((line = infile.readLine()) != null) {
                testInfo = line.split("\t");
                if (testInfo[0].equals("contract")) {
                    continue;
                }

                conId = testInfo[0];
                salesId = Integer.parseInt(testInfo[1]);
                seq = Integer.parseInt(testInfo[2]);
                stockInfo = getSeqModel(salesId, conId, seq);
                modelId = stockInfo[0];
                quantity = stockInfo[1];
                if (modelId == 0) {
                    continue;
                }
                if (checkSaleOrder(conId, salesId, modelId)) {
                    int sustcId, clientId;
                    String[] clientInfo;
                    clientId = getContractInfo(conId);
                    clientInfo = getClientInfoById(clientId);
                    sustcId = Integer.parseInt(clientInfo[1]);

                    returnDeleteStock(modelId, sustcId, quantity);
                    deleteOneOrder(conId, modelId, salesId);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnDeleteStock(int modelId, int sustcId, int quantity) {

        String sql = "update stock set tot_quantity=? where model_id=? and sustc_id=?";
        int now;
        int stockQuantity = getStock(modelId, sustcId);
        now = stockQuantity + quantity;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, now);
            preparedStatement.setInt(2, modelId);
            preparedStatement.setInt(3, sustcId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnUpdateStock(int modelId, int sustcId, int quantity, String conId, int salesId) {

        String sql = "update stock set tot_quantity=? where model_id=? and sustc_id=?";
        int now;
        //update前的
        int stockQuantity = getStock(modelId, sustcId);
        int orderQuantity = getOrderQuantity(conId, modelId, salesId);
        //quantity是update后的
        now = stockQuantity + (orderQuantity - quantity);
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, now);
            preparedStatement.setInt(2, modelId);
            preparedStatement.setInt(3, sustcId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int[] getSeqModel(int salesId, String conId, int seq) {

        String sql = "select * ,  rank() over (partition by salesman_id order by delivery_date) " +
                "from orders  join product p on orders.model_id = p.model_id where salesman_id=? and contract_id=? " +
                "order by p_name limit 1 offset ?";
        int offset = seq - 1;
        int[] ans = new int[2];
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, salesId);
            preparedStatement.setString(2, conId);
            preparedStatement.setInt(3, offset);
            ResultSet result = preparedStatement.executeQuery();
            //如果查询返回的是空列这里会get到什么？还是会报错？
            if (result.next()) {
                ans[0] = Integer.parseInt(result.getString("model_id"));
                ans[1] = Integer.parseInt(result.getString("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public int getContractInfo(String conId) {

        String sql = "select * from contract where contract_id=?";
        int ans = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, conId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ans = Integer.parseInt(result.getString("client_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public String[] getClientInfoById(int cId) {

        String sql = "select * from client where client_id=?";
        String[] ans = new String[2];
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, cId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ans[0] = result.getString("c_name");
                ans[1] = result.getString("sustc_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public int getTotQuantity(int quantity, int sustcId, int modelId) {

        String sqlGet = "select * from stock where sustc_id=? and model_id=?";
        int tot = quantity;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlGet);
            preparedStatement.setInt(1, sustcId);
            preparedStatement.setInt(2, modelId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                tot += Integer.parseInt(result.getString("tot_quantity"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tot;
    }

    public void updateTotQuantity(int tot, int sustcId, int modelId) {

        String sqlSet = "update stock set tot_quantity=? where sustc_id=? and model_id=?";
        try {
            PreparedStatement stmtSet = con.prepareStatement(sqlSet);
            stmtSet.setInt(1, tot);
            stmtSet.setInt(2, sustcId);
            stmtSet.setInt(3, modelId);
            stmtSet.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //functions to implement the table: bill_2022
    public long[] getMonthlyIncome(int month) {
        String sql = "insert into bill_2022(total_income,month,most_profitable_product_id," +
                "most_profitable_sustc_id,salesman_of_the_month)values(?,?,?,?,?)";
        String getIncome = "select sum(month.tot_income)\n" +
                "from(\n" +
                "    SELECT real_month,tot_income\n" +
                "    from (\n" +
                "         select extract(month from lodgement_date) as real_month,tot_income\n" +
                "        from orders\n" +
                "             )t1\n" +
                "    where real_month=?\n" +
                "        )month";
        String getCenter = "select sum(month.tot_income) as res, sustc_id\n" +
                "from (\n" +
                "         select tot_income, sustc_id\n" +
                "         from (\n" +
                "                  select extract(month from lodgement_date) as real_month, tot_income, c2.sustc_id\n" +
                "                  from orders\n" +
                "             join contract c on orders.contract_id = c.contract_id\n" +
                "             join client c2 on c2.client_id = c.client_id\n" +
                "              ) t1\n" +
                "         where real_month = ?\n" +
                "     ) month\n" +
                "group by sustc_id\n" +
                "order by res desc\n" +
                "limit 1 offset 0;";
        String getProduct = "select sum(month.tot_income) as res, model_id\n" +
                "from (\n" +
                "         select tot_income, model_id\n" +
                "         from (\n" +
                "                  select extract(month from lodgement_date) as real_month, tot_income, model_id\n" +
                "                  from orders\n" +
                "              ) t1\n" +
                "         where real_month = ?\n" +
                "     ) month\n" +
                "group by model_id\n" +
                "order by res desc\n" +
                "limit 1 offset 0;";
        String getStaff = "select sum(month.tot_income) as s, salesman_id\n" +
                "from (\n" +
                "         select tot_income, salesman_id\n" +
                "         from (\n" +
                "                  select extract(month from lodgement_date) as real_month, tot_income, salesman_id\n" +
                "                  from orders\n" +
                "              ) t1\n" +
                "         where real_month = ?\n" +
                "     ) month\n" +
                "group by salesman_id\n" +
                "order by s desc\n" +
                "limit 1 offset 0;";
        long[] income = new long[4];
        int staffId=0,mId=0,sustcId=0;
        try {
            PreparedStatement stmtSet = con.prepareStatement(sql);
            PreparedStatement stmtGet = con.prepareStatement(getIncome);
            PreparedStatement stmtCenter = con.prepareStatement(getCenter);
            PreparedStatement stmtProduct = con.prepareStatement(getProduct);
            PreparedStatement stmtStaff = con.prepareStatement(getStaff);
            stmtGet.setInt(1, month);
            stmtCenter.setInt(1, month);
            stmtProduct.setInt(1, month);
            stmtStaff.setInt(1, month);
            ResultSet resultSetI = stmtGet.executeQuery();
            ResultSet resultSetC = stmtCenter.executeQuery();
            ResultSet resultSetP = stmtProduct.executeQuery();
            ResultSet resultSetS = stmtStaff.executeQuery();
            if (resultSetI.next()&&resultSetI.getString("sum")!=null) {
                income[0] = Integer.parseInt(resultSetI.getString("sum"));
            }
            if (resultSetC.next()&&resultSetC.getString("res")!=null) {
                income[1] = Integer.parseInt(resultSetC.getString("res"));
                sustcId=Integer.parseInt(resultSetC.getString("sustc_id"));

            }
            if (resultSetP.next()&&resultSetP.getString("res")!=null) {
                income[2] = Integer.parseInt(resultSetP.getString("res"));
                mId=Integer.parseInt(resultSetP.getString("model_id"));
            }
            if (resultSetS.next()&&resultSetS.getString("s")!=null) {
                income[3] = Integer.parseInt(resultSetS.getString("s"));
                staffId = Integer.parseInt(resultSetS.getString("salesman_id"));

            }
            if(income[0]!=0 && mId!=0 &&staffId!=0&&sustcId!=0) {
                stmtSet.setLong(1, income[0]);
                stmtSet.setLong(2, month);
                stmtSet.setLong(3, mId);
                stmtSet.setLong(4, sustcId);
                stmtSet.setLong(5, staffId);
                stmtSet.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }

}


