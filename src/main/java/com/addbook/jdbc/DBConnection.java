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

    public List<Details> retrieveContactWithinDateRange(String startDate, String endDate) {
        List<Details> contacts = new ArrayList<>();
        try(Connection con = ab_dbo.getConnection()){
            String query = String.format("Select * from addressbook where Joindate between '"+ startDate + "' and '"+ endDate +"'");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                String fname = rs.getString(1);
                String lname = rs.getString(2);
                long phone = rs.getLong(3);
                String email = rs.getString(4);
                String date = rs.getString(5);

                Details details = new Details(fname,lname,phone,email,date);
                contacts.add(details);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return contacts;
    }

    public List<Details> retrieveContactByCity(String city) {
        List<Details> conts = new ArrayList<>();
        try(Connection con = ab_dbo.getConnection()){
            String query = String.format("Select * from addressbook where city = '" + city +"'");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                String fname = rs.getString(1);
                String lname = rs.getString(2);
                long phone = rs.getLong(3);
                String email = rs.getString(4);
                String add = rs.getString(5);
                String state = rs.getString(7);
                long zip = rs.getLong(8);

                Details details = new Details(fname,lname,phone,email,add, state, zip);
                conts.add(details);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return conts;
    }
    public void addContactAtomicTransaction(int id, String fname, String lname, long phone, String mail,
                                            String addr, String city, String state, long zip, String type, String personName) throws SQLException {

        boolean result1 = false, result2 = false, result3 = false, result4 = false;
        Connection con = ab_dbo.getConnection();
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            String query1 = String.format("Insert into name_book values ('" + id + "','" + fname + "','" + lname + "');");
            int result = stmt.executeUpdate(query1);
            if(result == 1)
                result1 = true;

            String query2 = String.format("Insert into addressdetails values ('" + id + "','" + addr + "','" + city + "','" + state + "','" + zip + "');");
            result = stmt.executeUpdate(query2);
            if(result == 1)
                result2 = true;

            String query3 = String.format("Insert into type values ('" + id + "','" + type + "','" + personName + "');");
            result = stmt.executeUpdate(query3);
            if(result == 1)
                result3 = true;

            String query4 = String.format("Insert into contact_details values ('" + id + "','" + phone + "','" + mail + "');");
            result = stmt.executeUpdate(query4);
            if(result == 1)
                result4 = true;

            if(result1 && result2 && result3 && result4)
                con.commit();
        }catch (SQLException e){
            e.printStackTrace();
            con.rollback();
        }finally {
            con.close();
        }
    }
    public void addContactsWithThread(List<Details> details) {

        details.forEach(contact ->{
            Runnable task = () -> {
                try {
                    this.addContactAtomicTransaction(contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(),
                            contact.getEmailId(), contact.getAddress(), contact.getCity(),
                            contact.getState(), contact.getZipCode(), contact.getType(), contact.getPersonName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            };
            Thread thread =  new Thread(task);
            thread.start();
        });
        try{
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public int countNumEntries() throws SQLException {
        int count = 0;
        try(Connection con = this.getConnection()){
            String query = "select count(id) from name_book";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                count = rs.getInt(1);
            }
        }
        return count;
    }
}
