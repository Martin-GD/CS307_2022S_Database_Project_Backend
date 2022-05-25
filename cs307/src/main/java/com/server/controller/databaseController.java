package com.server.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class databaseController implements ServletContextAware {
    public Connection con = null;
    private String host = "localhost";
    private String dbname = "CS307Proj2";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";
    @Override
    public void setServletContext(ServletContext servletContext) {
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

}
