<<<<<<< Updated upstream
package project2;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        check dm = new check();
        dataImportV1 imp = new dataImportV1();
//        imp.getConnection();
//        imp.impCenter();
//        imp.impEnterprise();
//        imp.impModel();
//        imp.impStaff();
//        imp.setStockIn();
//        imp.placeOrder();
//        imp.updateOrder();
//        imp.deleteOrder();
//        imp.closeConnection();
        dm.openDB();
        System.out.println(dm.getAllStaffCount());
        System.out.println(dm.getContractCount());
        System.out.println(dm.getOrderCount());
        System.out.println(dm.getNeverSoldProductCount());
        System.out.println(dm.getFavoriteProductModel());
        System.out.println(dm.getAvgStockByCenter());
        dm.closeDB();
    }
}
=======
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        check dm = new check();
        dataImport imp = new dataImport();
        imp.getConnection();
//        imp.impCenter();
//        imp.impEnterprise();
//        imp.impModel();
//        imp.impStaff();
        imp.setStockIn();
        imp.placeOrder();
        imp.updateOrder();
        imp.deleteOrder();
        imp.closeConnection();
        dm.openDB();
        System.out.println(dm.getAllStaffCount());
        System.out.println(dm.getContractCount());
        System.out.println(dm.getOrderCount());
        System.out.println(dm.getNeverSoldProductCount());
        System.out.println(dm.getFavoriteProductModel());
        System.out.println(dm.getAvgStockByCenter());
        dm.closeDB();
    }
}
>>>>>>> Stashed changes
