
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        check dm = new check();
        dataImportV1 imp = new dataImportV1();
        imp.getConnection();
//        imp.impCenter();
//        imp.impEnterprise();
//        imp.impModel();
//        imp.impStaff();
//
//        imp.setStockIn();
//        imp.placeOrder();
//        imp.updateOrder();
//        imp.deleteOrder();
//        imp.closeConnection();
        dm.openDB();
//        System.out.println(dm.getAllStaffCount());
//        System.out.println(dm.getContractCount());
//        System.out.println(dm.getOrderCount());
//        System.out.println(dm.getNeverSoldProductCount());
//        System.out.println(dm.getFavoriteProductModel());
//        System.out.println(dm.getAvgStockByCenter());
        System.out.println(dm.getProductByNumber("A50L172"));
        System.out.println(dm.getContractInfo("CSE0000106"));
//for implementing bill_2022:
//        for (int i = 1; i <13 ; i++) {
//            imp.getMonthlyIncome(i);
//            System.out.println(dm.getMonthlyAll(i));
//        }
        dm.closeDB();
    }
}
