package com.addbook.jdbctest;

import com.addbook.jdbc.DBConnection;
import com.addbook.jdbc.Details;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class AddressBookJsonServerTest {
    DBConnection dbc = new DBConnection();

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    @Test
    public void test_WriteDataToJsonServer() {
        List<Details> contacts = dbc.retrieveDataFromDB();
        for(Details e : contacts){
            HashMap<String, String> map = new HashMap<>();
            String fname = e.getFirstName();
            String lname = e.getLastName();
            long phNo = e.getPhoneNumber();
            String email = e.getEmailId();
            String date = e.getDate();

            map.put("first name", fname);
            map.put("last name", lname);
            map.put("phone", String.valueOf(phNo));
            map.put("email", email);
            map.put("joindate", date);
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(map)
                    .when()
                    .post("/contacts/create");
        }
    }

    @Test
    public void test_ReadFromJsonServer() {
        Response response = RestAssured.get("/contacts/list");
        System.out.println(response.asString());
    }
}