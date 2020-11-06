package com.addbook.jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookMain {
    public static AddressBook getAddressBook(String name) {
        AddressBook ab = new AddressBook();
        ab.setName(name);
        return ab;
    }

    public static void main(String[] args) {
        String addBookName;
        System.out.println("Welcome to Address Book Program in AddressBookMain class on START Master Branch");
        Scanner sc = new Scanner(System.in);
//		AddressBook ab = new AddressBook();
        AddressBook ab;
        AddressBookDictionary abdict = new AddressBookDictionary();
        boolean while1 = true, while2 = true;

        while (while1) {
            System.out.println(
                    "1. Add New Address Book.\n2. Modify Existing Address Book.\n3. Search Contacts in City.\n4. Search Contacts in State.\n5. View Contacts in City.\n6. View Contacts in State.\n7. Number of Contacts in City.\n8. Number of Contacts in State.\n9. Exit");
            System.out.println("Enter your choice: ");
            int ch = Integer.parseInt(sc.nextLine());
            if (ch == 1) {
                System.out.println("Enter the name of Address Book");
                addBookName = sc.nextLine();
                abdict.addAddressBook(addBookName, getAddressBook(addBookName));
            } else if (ch == 2) {
                System.out.println("Enter the name of Address Book");
                String name = sc.nextLine();
                if (abdict.presentAddressBook(name) == true) {
                    ab = abdict.returnAddressBook(name);

                    while (while2) {

                        System.out.println(
                                "1. Add Address.\n2. Display The Details.\n3. Edit a Detail.\n4. Remove a Detail."
                                        + "\n5. Sort Alphabetically By Name.\n6. Sort By City.\n7. Sort By State.\n8. Sort By PinCode.\n9. Write in CSV File."
                                        + "\n10. Read From CSV File.\n11. Write in Gson File.\n12. Read From Gson File.\n13. To Read From File.\n14. To Write in File.\n15. Go to Main Menu.");
                        System.out.println("Enter your choice: ");
                        int n = Integer.parseInt(sc.nextLine());

                        if (n == 1) {

                            System.out.println("Enter the First Name:");
                            String first = sc.nextLine();

                            System.out.println("Enter the Last Name:");
                            String last = sc.nextLine();

                            if (ab.noDuplicate(first, last))
                                System.out.println("Name Already Exists.");
                            else {

                                System.out.println("Enter the Phone No.:");
                                long num = Long.parseLong(sc.nextLine());

                                System.out.println("Enter the Email:");
                                String email = sc.nextLine();

                                System.out.println("Enter the Address:");
                                String add = sc.nextLine();

                                System.out.println("Enter the City:");
                                String city = sc.nextLine();

                                System.out.println("Enter the State:");
                                String state = sc.nextLine();

                                System.out.println("Enter the Zip Code:");
                                long zp = Long.parseLong(sc.nextLine());

                                Details adrs = new Details(first, last, num, email, add, city, state, zp);
                                ab.addDetails(adrs);
                            }
                        } else if (n == 2) {
                            System.out.println("The Details in the List are:");
                            ArrayList<Details> list = (ArrayList<Details>) ab.viewAllDetails();
                            for (Details c : list)
                                System.out.println("\nFirst Name: " + c.getFirstName() + "\nLast Name: "
                                        + c.getLastName() + "\nPhone no.: " + c.getPhoneNumber() + "\nEmail: "
                                        + c.getEmailId() + "\nAddress: " + c.getAddress() + "\nCity: " + c.getCity()
                                        + "\nState: " + c.getState() + "\nState: " + c.getState() + "\nZip Code: "
                                        + c.getZipCode());
                        } else if (n == 3) {
                            System.out.println("Enter the Name to Edit the Details");
                            System.out.println("Enter First Name: ");
                            String fN = sc.nextLine();
                            System.out.println("Enter Last Name: ");
                            String lN = sc.nextLine();
                            ArrayList<Details> list = (ArrayList<Details>) ab.viewAllDetails();
                            for (Details c : list)
                                if (c.getFirstName().equalsIgnoreCase(fN) && c.getLastName().equalsIgnoreCase(lN)) {
                                    ab.edit(c);
                                }
                        } else if (n == 4) {
                            System.out.println("Enter the First Name to remove :");
                            String first = sc.nextLine();
                            System.out.println("Enter the Last Name to remove :");
                            String last = sc.nextLine();
                            System.out.println("Do you want to remove the Detail (Y/N):");
                            String ans = sc.nextLine();
                            if (ans.equals("Y")) {
                                if (ab.removeDetails(first, last))
                                    System.out.println("The Address Detail is successfully deleted.");
                            }
                        } else if (n == 5) {
                            ab.sortNameAlpha();
                        } else if (n == 6) {
                            ab.sortCity();
                        } else if (n == 7) {
                            ab.sortState();
                        } else if (n == 8) {
                            ab.sortPin();
                        } else if (n == 9) {
                            try
                            {
                                ab.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (CsvDataTypeMismatchException e) {
                                e.printStackTrace();
                            } catch (CsvRequiredFieldEmptyException e) {
                                e.printStackTrace();
                            }
                            break;

                        } else if (n == 10) {
                            try {
                                ab.readCSV();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;

                        } else if (n == 11) {
                            try {
                                ab.jsonWrite();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;

                        } else if (n == 12) {
                            try {
                                ab.jsonRead();
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;

                        } else if (n == 13) {
                            try {
                                ab.readFromFile();
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;

                        } else if (n == 14) {
                            ab.writeInFile();
                            break;

                        } else if (n == 15) {
                            while2 = false;

                        } else {
                            System.out.println("PLease Enter a Valid Number.\nThank You!");

                        }
                    }
                } else {
                    System.out.println("Oops!! NO SUCH ADDRESS BOOK EXISTS.");
                }
            } else if (ch == 3) {
                System.out.println("Enter City NAme:");
                String City = sc.nextLine();
                ArrayList<Details> cityDetails = abdict.returnByCity(City);

            } else if (ch == 4) {
                System.out.println("Enter State NAme:");
                String state = sc.nextLine();
                ArrayList<Details> stateDetails = abdict.returnByState(state);

            } else if (ch == 5) {
                System.out.println("Enter City NAme:");
                String City = sc.nextLine();
                abdict.viewByCity(City);
            } else if (ch == 6) {
                System.out.println("Enter State NAme:");
                String State = sc.nextLine();
                abdict.viewByState(State);
            } else if (ch == 7) {
                System.out.println("Enter City NAme:");
                String City = sc.nextLine();
                abdict.countCity(City);
            } else if (ch == 8) {
                System.out.println("Enter State NAme:");
                String State = sc.nextLine();
                abdict.countState(State);
            } else if (ch == 9) {
                System.exit(0);
            } else {
                System.out.println("Invalid Input");
            }
        }
    }

}