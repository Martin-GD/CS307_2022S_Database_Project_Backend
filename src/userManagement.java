import java.sql.*;


public class userManagement {
    protected Connection con = null;

    private String host = "localhost";
    private String dbname = "project2";
    private String user = "test";
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

    public void changeConnectionToManager(String password) {
        //pwd=password_m
        user = "manager";
        pwd = password;
        getConnection();
    }

    public void changeConnectionBack(String password) {
        //pwd=123456
        user = "test";
        pwd = password;
        getConnection();
    }

    public void createRole(String role_name) {
        //do block for creating roles do not exist
        String sql = """
                DO
                $do$
                BEGIN
                   IF EXISTS (
                      SELECT FROM pg_catalog.pg_roles
                      WHERE  rolname = ?)
                   ELSE
                      CREATE ROLE ? ;
                   END IF;
                END
                $do$;""";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, role_name);
            stmt.setString(2, role_name);
            stmt.setString(3, role_name);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //一定要在test user下切换成staff不然会查不到
    public void grantUsages(String userName) {
        String sqlGrant = "grant normal_staffs to ?";
        String sql = "set role = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            PreparedStatement stmtGrant = con.prepareStatement(sqlGrant);
            stmt.setString(1, userName);
            stmtGrant.setString(1, userName);
            stmt.execute();
            stmtGrant.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {

    }

}
