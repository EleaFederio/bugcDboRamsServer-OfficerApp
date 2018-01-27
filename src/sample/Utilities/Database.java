package sample.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public Connection connection = null;

    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //connection = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net/sql12216057", "sql12216057", "Qu5yvfUmw8");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/bugc", "root", "");
            System.out.println("+   Check Connection (OK)   +");
        }catch (ClassNotFoundException cnf){
            System.out.println("Driver not found");
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            System.out.println("Database.connect() Error: "+ex);
        }
    }
}
