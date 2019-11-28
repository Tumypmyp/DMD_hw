package util.generate;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Staff {
    public static int MAX_ACCESS_LEVEL = 10000;
    private ArrayList<String> surnames;
    private ArrayList<Name> names;
    private ArrayList<String> addresses;
    private ArrayList<String> emails;
    private ArrayList<String> phone_numbers;
    private ArrayList<String> lorem;
    private Random rand;
    private Connection connection;
    private int staffId = 0;
    private int posId = 0;
    private int numPos;
    private int numStaff;
    private libReading lib;

    public Staff(int numPos, int numStaff, Random rand, Connection connection) {
        this.numStaff = numStaff;
        this.numPos = numPos;
        this.rand = rand;
        this.connection = connection;
        this.lib = new libReading(rand);
        names = lib.getNamesGenders();
        surnames = lib.getFromFile("surnames.txt");
        emails = lib.getFromFile("emails.txt");
        addresses = lib.getFromFile("addresses.txt");
//        profile_images = lib.getFromFile("lorem.txt");

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
            String positionName = lorem.get(pid + rand.nextInt(lorem.size() / numPos - 3) * numPos);
            positionName = positionName.substring(0, Math.min(30 - 10, positionName.length())) + pid;
            int level = rand.nextInt(MAX_ACCESS_LEVEL);
            int salary = rand.nextInt(200) * 1000;

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
            String profile_image = "https://picsum.photos/id/"+ rand.nextInt(1000) + "/500/500";
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

            Long phone_number = Long.parseLong(phone_numbers.get(sid + rand.nextInt(phone_numbers.size() / numStaff - 3) * numStaff));
            preparedStatement.setInt(1, sid);
            preparedStatement.setObject(2, phone_number);

            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement(
                    "INSERT INTO portfolio (sid, description, skills, achievements, electronic_copy, loading_date, comments, access_level)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");


            String description = lib.getRandString(lorem);
            String skills = lib.getRandString(lorem);
            String achievements = lib.getRandString(lorem);
            String copy = lib.getRandString(lorem, 100);
            java.sql.Date loadingDate = lib.getRandDate(dateOfBirth.toLocalDate().getYear(), 2005);
            String comments = lib.getRandString(lorem);

            preparedStatement.setInt(1, sid);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, skills);
            preparedStatement.setString(4, achievements);
            preparedStatement.setString(5, copy);
            preparedStatement.setDate(6, loadingDate);
            preparedStatement.setString(7, comments);
            preparedStatement.setInt(8, rand.nextInt(MAX_ACCESS_LEVEL) + 1);

            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement(
                    "INSERT INTO previous_positions (sid, place, position, work_began, work_finished, employer_feedback)" +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            int prevPosNum = rand.nextInt(5);
            for (int i = 0; i < prevPosNum; ++i) {
                String place = lib.getRandString(lorem, 100);
                String position = lib.getRandString(lorem, 30);
                int midDate = rand.nextInt(2005 - dateOfBirth.toLocalDate().getYear()) + dateOfBirth.toLocalDate().getYear();
                java.sql.Date dateFrom = lib.getRandDate(dateOfBirth.toLocalDate().getYear(), midDate);
                java.sql.Date dateTo = lib.getRandDate(midDate, 2005);
                String feedback = lib.getRandString(lorem);

                preparedStatement.setInt(1, sid);
                preparedStatement.setString(2, place);
                preparedStatement.setString(3, position);
                preparedStatement.setDate(4, dateFrom);
                preparedStatement.setDate(5, dateTo);
                preparedStatement.setString(6, feedback);
                preparedStatement.executeUpdate();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


