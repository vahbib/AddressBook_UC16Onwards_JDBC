package com.addbook.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;



public class AddressBook {

    Scanner sc = new Scanner(System.in);
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private List<Details> addressBook = new ArrayList<Details>();
    Map<String, ArrayList<Details>> cityMap = new HashMap<>();
    Map<String, ArrayList<Details>> stateMap = new HashMap<>();

    public List<Details> getAddressBook() {
        return this.addressBook;
    }

    public void setAddressBook(List<Details> adrs) {
        this.addressBook = adrs;
    }

    public void addDetails(Details detailsObj) {
        addressBook.add(detailsObj); // ADDING DETAILS

        ArrayList<Details> cityDetails = cityMap.get(detailsObj.getCity());
        if (cityDetails == null) {
            ArrayList<Details> firstInsertion = new ArrayList<>();
            firstInsertion.add(detailsObj);
            cityMap.put(detailsObj.getCity(), firstInsertion);
        } else {
            cityDetails.add(detailsObj);
            cityMap.put(detailsObj.getCity(), cityDetails);
        }

        ArrayList<Details> stateDetails = stateMap.get(detailsObj.getState());
        if (cityDetails == null) {
            ArrayList<Details> firstInsertion = new ArrayList<>();
            firstInsertion.add(detailsObj);
            stateMap.put(detailsObj.getState(), firstInsertion);
        } else {
            stateDetails.add(detailsObj);
            stateMap.put(detailsObj.getState(), stateDetails);
        }
    }

    public List<Details> viewAllDetails() {
        return addressBook;
    }

    // DISPLAYING DETAILS
    public Details viewDetailsGivenName(String firstName, String lastName) {
        for (Details c : addressBook)
            if (c.getFirstName() == firstName && c.getLastName() == lastName)
                return c;

        return null;
    }

    // TO EDIT EXISTING DETAILS
    public void edit(Details det) {
        System.out.println("1. Enter the First Name.");
        System.out.println("2. Enter the Last Name.");
        System.out.println("3. Enter the Phone Number.");
        System.out.println("4. Enter the Email ID.");
        System.out.println("5. Enter the Address.");
        System.out.println("6. Enter the City.");
        System.out.println("7. Enter the State.");
        System.out.println("8. Enter the Zip Code.");

        System.out.println("Enter Number of Correspondence Statement.");

        int n = 0;
        int choice = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the Detail");

        switch (choice) {
            case 1:
                updateDetailsFirstName(det, sc.nextLine());
                n = 1;
                break;
            case 2:
                updateDetailsLastName(det, sc.nextLine());
                n = 1;
                break;
            case 3:
                updateDetailsPhoneNumber(det, Long.parseLong(sc.nextLine()));
                n = 1;
                break;
            case 4:
                updateDetailsEmail(det, sc.nextLine());
                n = 1;
                break;
            case 5:
                updateDetailsAddress(det, sc.nextLine());
                n = 1;
                break;
            case 6:
                updateDetailsCity(det, sc.nextLine());
                n = 1;
                break;
            case 7:
                updateDetailsState(det, sc.nextLine());
                n = 1;
                break;
            case 8:
                updateDetailsZip(det, Long.parseLong(sc.nextLine()));
                n = 1;
                break;
            default:
                System.out.println("INVALID INPUT");
                break;
        }
        if (n == 1) {
            System.out.println("Detail Edited");
        } else {
            System.out.println("Name Not Found");
        }

    }

    public void updateDetailsPhoneNumber(Details dObj, long phoneNumber) {
        dObj.setPhoneNumber(phoneNumber);
    }

    public void updateDetailsFirstName(Details dobj, String upFirstName) {
        dobj.setFirstName(upFirstName);
    }

    public void updateDetailsLastName(Details dObj, String lastName) {
        dObj.setLastName(lastName);
    }

    public void updateDetailsEmail(Details dObj, String email) {
        dObj.setEmailId(email);
    }

    public void updateDetailsCity(Details dObj, String city) {
        dObj.setCity(city);
    }

    public void updateDetailsAddress(Details dObj, String address) {
        dObj.setAddress(address);
    }

    public void updateDetailsState(Details dObj, String state) {
        dObj.setState(state);
    }

    public void updateDetailsZip(Details dObj, long zip) {
        dObj.setZipCode(zip);
    }

    /*
     * UserCase 11 Sorting Alphabetically by Name
     */
    public void sortNameAlpha() {
        List<Details> list = addressBook.stream().sorted(Comparator.comparing(Details::getName))
                .collect(Collectors.toList());
        addressBook = new ArrayList<>(list);
        addressBook.stream().forEach(System.out::println);
    }

    // REMOVING ANY DETAIL
    public boolean removeDetails(String first, String last) {
        int n = 0;
        for (Details c : addressBook)
            if (c.getFirstName().equalsIgnoreCase(first) && c.getLastName().equalsIgnoreCase(last)) {
                addressBook.remove(c);
                n = 1;
                return true;

            }
        if (n == 0) {
            System.out.println("Name not Found");
        } else {
            System.out.println("Name Removed");
        }
        return false;

    }

    // UserCase 8
    public ArrayList<Details> viewPersonByCity(String city) {
        return cityMap.get(city);
    }

    public ArrayList<Details> viewPersonByState(String state) {
        return stateMap.get(state);
    }

    // UserCAse 9
    public void viewByCity(String city) {
        cityMap.values().stream().forEach(contacts -> System.out.println(contacts));
    }

    public void viewByState(String state) {
        stateMap.values().stream().forEach(contacts -> System.out.println(contacts));
    }

    // UserCAse 10
    public int countCity(String city) {
        return cityMap.get(city).size();
    }

    public int countState(String state) {
        return stateMap.get(state).size();
    }

    // UserCase 7
    public boolean noDuplicate(String firstName, String lastName) {

        return addressBook.stream().anyMatch(
                contact -> contact.getFirstName().equals(firstName) && contact.getFirstName().equals(lastName));

    }

    // UserCAse 12
    public void sortCity() {
        List<Details> list = addressBook.stream().sorted(Comparator.comparing(Details::getCity))
                .collect(Collectors.toList());
        addressBook = new ArrayList<>(list);
    }

    public void sortState() {
        List<Details> list = addressBook.stream().sorted(Comparator.comparing(Details::getState))
                .collect(Collectors.toList());
        addressBook = new ArrayList<>(list);
    }

    public void sortPin() {
        List<Details> list = addressBook.stream().sorted(Comparator.comparing(Details::getZipCode))
                .collect(Collectors.toList());
        addressBook = new ArrayList<>(list);
    }

    /*
     * UserCase 13 IO Operation
     */
    public void readFromFile() throws FileNotFoundException {
        File f = new File("F:\\Java_Workspace\\AddressBook.txt");
        Scanner myFile = new Scanner(f);
        while (myFile.hasNextLine()) {
            try {
                Details det = new Details();
                String data = myFile.nextLine();
                String[] str = data.split(" ");
                det.setFirstName(str[0]);
                det.setLastName(str[1]);
                det.setAddress(str[2]);
                det.setCity(str[3]);
                det.setState(str[4]);
                det.setZipCode(Long.parseLong(str[5]));
                det.setPhoneNumber(Long.parseLong(str[6]));
                det.setEmailId(str[7]);
                addDetails(det);
            } catch (Exception e) {
                System.out.println("Invalid Details");
            }
        }

    }

    public void writeInFile() {
        try {
            FileWriter fileWriter = new FileWriter("F:\\Java_Workspace\\AddressBook.txt", true);
            for (Details c : addressBook) {
                fileWriter.write(c.getFirstName() + " " + c.getLastName() + " " + c.getAddress() + " " + c.getCity()
                        + " " + c.getState() + " " + c.getState() + " " + c.getPhoneNumber() + " " + c.getEmailId()
                        + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("File not exists.");
        }
    }

    /*
     * UserCase14: Ability to Read/Write the Address Book with Persons Contact as
     * CSV File
     */
    public void writeCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        String CSV_write_file = "F:\\Java_Workspace\\Day12AddressBookSystem\\AddressBookCSV.csv";
        Writer writer = Files.newBufferedWriter(Paths.get(CSV_write_file));

        StatefulBeanToCsv<Details> beanToCsv = new StatefulBeanToCsvBuilder(writer).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
        beanToCsv.write(addressBook);
        writer.close();

    }

    public void readCSV() throws IOException {
        String file_read = "F:\\Java_Workspace\\Day12AddressBookSystem\\AddressCSV.csv";
        Reader reader = Files.newBufferedReader(Paths.get(file_read));

        CSVReader readCSV = new CSVReader(reader);

        String[] adding;
        adding = readCSV.readNext();
        while ((adding = readCSV.readNext()) != null) {
            Details c = new Details();
            c.setFirstName(adding[0]);
            c.setLastName(adding[1]);
            c.setAddress(adding[2]);
            c.setCity(adding[3]);
            c.setEmailId(adding[4]);
            c.setPhoneNumber(Long.parseLong(adding[5]));
            c.setState(adding[6]);
            c.setZipCode(Long.parseLong(adding[7]));
            addressBook.add(c);
        }
        readCSV.close();
    }
    /*
     * UserCase15:
     * Ability to Read or Write the Address Book with Persons Contact as JSON File
     */
    public void jsonWrite() throws IOException {
        String file_write = "F:\\Java_Workspace\\Day12AddressBookSystem\\AddressGson.json";
        Gson write = new Gson();
        String json = write.toJson(addressBook);
        FileWriter writer = new FileWriter(file_write);
        writer.write(json);
        writer.close();
    }

    public void jsonRead() throws FileNotFoundException {
        String file = "F:\\Java_Workspace\\Day12AddressBookSystem\\AddressGson.json";
        Gson gson = new Gson();
        BufferedReader read= new BufferedReader(new FileReader(file));
        Details[] view= gson.fromJson(read, Details[].class);
        List<Details> contactList = Arrays.asList(view);
        for (Details c : contactList){
            addressBook.add(c);
        }
    }

}

class AddressBookDictionary extends AddressBook {
    Map<String, AddressBook> address_book = new HashMap<>();

    public void addAddressBook(String name, AddressBook addressbook) {
        address_book.put(name, addressbook);
    }

    public boolean presentAddressBook(String name) {
        return address_book.containsKey(name);
    }

    public AddressBook returnAddressBook(String name) {
        return address_book.get(name);
    }

    // UserCAse 9
    public void viewByCity(String city) {
        address_book.values().stream().forEach(addBook -> addBook.viewByCity(city));
    }

    public void viewByState(String state) {
        address_book.values().stream().forEach(addBook -> addBook.viewByState(state));
    }

    // UserCAse 8
    public ArrayList<Details> returnByCity(String city) {
        ArrayList<Details> cityDetails = new ArrayList<>();
        address_book.values().stream().forEach(c -> cityDetails.addAll(c.viewPersonByCity(city)));
        return cityDetails;
    }

    public ArrayList<Details> returnByState(String state) {
        ArrayList<Details> stateDetails = new ArrayList<>();
        address_book.values().stream().forEach(c -> stateDetails.addAll(c.viewPersonByState(state)));
        return stateDetails;
    }

    /* Counting Number of Cities and States */
    public int countCity(String city) {
        return address_book.values().stream().mapToInt(addBook -> addBook.countCity(city)).sum();
    }

    public int countState(String state) {
        return address_book.values().stream().mapToInt(addBook -> addBook.countState(state)).sum();
    }
}