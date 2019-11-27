package util.generate;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Staff {
    private ArrayList<String> surnames;
    private ArrayList<Name> names;
    private ArrayList<String> addresses;
    private ArrayList<String> emails;
    private ArrayList<String> phone_numbers;
    private ArrayList<String> profile_images;
    //    private ArrayList<String> statss;
//    private ArrayList<String> commentss;
//    private ArrayList<String> descriptions;
    private ArrayList<String> lorem;
    private Random rand;
    private Connection connection;
    private int staffId = 0;
    private int posId = 0;
    private int numPos;
    private libReading lib;

    public Staff(int numPos, Random rand, Connection connection) {
        this.numPos = numPos;
        this.rand = rand;
        this.connection = connection;
        this.lib = new libReading(rand);
        names = lib.getNamesGenders();
        surnames = lib.getFromFile("surnames.txt");
        emails = lib.getFromFile("emails.txt");
        addresses = lib.getFromFile("addresses.txt");
        profile_images = lib.getFromFile("lorem.txt");

        phone_numbers = lib.getFromFile("phone_numbers.txt");

        lorem = lib.getFromFile("lorem.txt");
    }

    public void addPosition() {
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO staff_position (position_id, position_name, position_access_level, position_salary, wh_start, wh_end)" +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            int pid = posId++;
            String positionName = lib.getRandString(lorem, 30);
            int level = rand.nextInt(10) + 1;
            int salary =  rand.nextInt(100) * 1000;

            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, positionName);
            preparedStatement.setInt(3, level);
            preparedStatement.setInt(4, salary);
            preparedStatement.setTime(5, java.sql.Time.valueOf("08:00:00"));
            preparedStatement.setTime(6, java.sql.Time.valueOf("19:00:00"));

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStaff() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO staff (sid, first_name, last_name, date_of_birth, gender, position_id, email, address, profile_image)" +
                            "VALUES (?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?)");

            int sid = staffId++;
            Name name = names.get(rand.nextInt(names.size()));
            String surname = lib.getRandString(surnames);
            String address = lib.getRandString(addresses);
            String email = lib.getRandString(emails);
            String profile_image = lib.getRandString(profile_images, 100);
            java.sql.Date dateOfBirth = lib.getRandDate(1970, 2005);
            int posId = rand.nextInt(numPos);

            preparedStatement.setInt(1, sid);
            preparedStatement.setString(2, name.name);
            preparedStatement.setString(3, surname);
            preparedStatement.setDate(4, dateOfBirth);
            preparedStatement.setString(5, GenderType.valueOf(name.gender).toString());
            preparedStatement.setInt(6, posId);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, address);
            preparedStatement.setString(9, profile_image);

            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("INSERT INTO staff_contacts (sid, phone_number)" +
                    "VALUES (?, ?)");

            int phoneNum = rand.nextInt(4);
            for (int i = 0; i < phoneNum; ++i) {
                Long phone_number = Long.parseLong(phone_numbers.get(rand.nextInt(phone_numbers.size())));
                preparedStatement.setInt(1, sid);
                preparedStatement.setObject(2, phone_number);

                preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


