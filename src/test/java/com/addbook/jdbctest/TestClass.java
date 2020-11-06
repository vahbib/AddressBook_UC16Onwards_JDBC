
package com.addbook.jdbctest;

import com.addbook.jdbc.DBConnection;
import com.addbook.jdbc.Details;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
}