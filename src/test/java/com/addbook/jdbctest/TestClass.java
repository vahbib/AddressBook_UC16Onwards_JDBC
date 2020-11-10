package com.addbook.jdbctest;

import com.addbook.jdbc.DBConnection;
import com.addbook.jdbc.Details;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestClass {

    DBConnection dbo;
    @Before
    public void init(){
        dbo = new DBConnection();
    }
    @Test
    public void check_retrieveData(){
        List<Details> list = dbo.retrieveDataFromDB();
//        System.out.println(list);
        Assert.assertEquals(6, list.size());
    }

    @Test
    public void Check_DataShouldGetUpdated() {
        dbo.updateDetailsInDB("Bibhav",  "Teacher");


    }

    @Test
    public void ShouldReturnDataByDate() {

        List<Details> list1 = dbo.retrieveContactWithinDateRange("2017-01-01", "2019-12-31");
        Assert.assertEquals(2, list1.size());
    }

    @Test
    public void Check_DataShouldGetInsertedInTables() throws SQLException {

        dbo.addContactAtomicTransaction(1, "Anushka", "Singh", 545214521, "anushka@nitt.edu", "Indira Nagar", "Lucknow",
                "Uttar Pradesh", 254145, "Friend", "Bibhav");
    }

    @Test
    public void AddMultipleContactUsingThread() throws SQLException {
        Details[] details = {
                new Details(2, "Morris", "Chrish", 5343513, "cmor@gmail.com", "HSR","Banglore", "karnataka", 876101, "Doctor", "Kohli"),
                new Details(3, "Shiva", "Mahadev", 108108108, "shiva@kailash.com", "Mountain Cliff", "Kailash", "Uttarakhand", 3211521, "Husband", "Parvati")
        };

        dbo.addContactsWithThread(Arrays.asList(details));
        int count = dbo.countNumEntries();
        Assert.assertEquals(3, count);
    }
}