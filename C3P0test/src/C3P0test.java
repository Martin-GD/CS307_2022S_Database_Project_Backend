/*
* Date: 2021/05/09 by wwy
* */
import com.mchange.v2.c3p0.*;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class C3P0test {
    public static void main(String[] args) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        /* ----------------------------Connection------------------------- */
        // loads the jdbc driver, here we use pgsql
        // if the db is mysql, the string will be "com.mysql.cj.jdbc.Driver"
        dataSource.setDriverClass("org.postgresql.Driver");
        dataSource.setUser("wwy");
        dataSource.setPassword("000000");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/cs307");

        // test 3: change the max pool size and initial pool size
        /*
        dataSource.setInitialPoolSize(1);
        dataSource.setMaxPoolSize(1);*/


        Connection connection = dataSource.getConnection();
        System.out.println(connection.getClass().getName());    // Test connection statement

        /*
        // inner proxy mechanism
        Object inner= getConnectionInner(connection);
        System.out.println(inner.getClass().getName());
        */

        // test 1: add one connection, watch the Idle/busy/all num
        /*
        Connection connection1 = dataSource.getConnection();
        System.out.println(connection1.getClass().getName());
        //System.out.println(connection==connection1);

         */


        // test 2: add two more connections, watch the all num
        /*
        Connection connection2 = dataSource.getConnection();
        System.out.println(connection==connection2);
        Connection connection3 = dataSource.getConnection();
        System.out.println(connection==connection2);

         */



        /* ----------------------------Get Statement------------------------- */
        poolStatus(dataSource);


        /* ----------------------------Query------------------------- */
        /*
        String sql_query = "insert into emp(id,name,salary) values (11,'test1',5000)";
        try {
            PreparedStatement ps_query = connection.prepareStatement(sql_query);
            ps_query.executeLargeUpdate();
            poolStatus(dataSource);

        }catch (Exception e){
            e.printStackTrace();
        }

         */



    }

    public static Object getConnectionInner(Object connection){
        Object result = null;
        Field field = null;
        try {
            field = connection.getClass().getDeclaredField("inner");
            field.setAccessible(true); // get the
            result = field.get(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void poolStatus(ComboPooledDataSource dataSource) {
        try {
            System.out.println("Busy Num " + dataSource.getNumBusyConnections());
            System.out.println("Idle Num" + dataSource.getNumIdleConnections());
            System.out.println("All Num" + dataSource.getNumConnections());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
