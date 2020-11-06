
package com.addbook.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DBConnection {

    static DBConnection ab_dbo = new DBConnection();

    public Connection getConnection(){
        String jdbcURL = "jdbc:mysql://localhost:3306/addressbookdb?useSSL=false";
        String userName = "root";
        String password = "Asep@25355";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Loaded..");
        }catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath", e);
        }

        listDrivers();
        try {
            System.out.println("Connecting to the Database... " + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection was successful !!");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    public static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while(driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println( "  " + driverClass.getClass().getName());
        }
    }

    public List<Details> retrieveDataFromDB(){
        List<Details> contactList = new ArrayList<>();
        try(Connection con = ab_dbo.getConnection()){
            String query = "Select * from addressbook";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                String fname = rs.getString(1);
                String lname = rs.getString(2);
                long phone = rs.getLong(3);
                String mail = rs.getString(4);
                String address = rs.getString(5);
                String city = rs.getString(6);
                String state = rs.getString(7);
                long zipcode = rs.getLong(8);
                String type = rs.getString(9);
                String personName = rs.getString(10);

                Details details = new Details(fname,lname,phone,mail, address,city,state,zipcode,type, personName);
                contactList.add(details);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return contactList;
    }
    public void updateDetailsInDB(String firstName, String value) {
        try(Connection con = ab_dbo.getConnection()){
            String query = String.format("update addressbook set type = ? where FirstName = ?");
            PreparedStatement p_stmt = con.prepareStatement(query);

            p_stmt.setString(1,value);
            p_stmt.setString(2,firstName);

            int result = p_stmt.executeUpdate();
            System.out.println(result);
        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}