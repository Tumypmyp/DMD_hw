package util.generate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Patients {
    private ArrayList<String> surnames;
    private ArrayList<Name> names;
    private ArrayList<String> addresses;
    private ArrayList<String> emails;
    private ArrayList<String> phone_numbers;
    private ArrayList<String> profile_images;
    private ArrayList<String> statss;
    private ArrayList<String> commentss;
    private ArrayList<String> descriptions;
    private ArrayList<String> lorem;
    private Random rand;
    private Connection connection;
    private int num = 0;
    private int numStuff;
    private libReading lib;

    public Patients(int numStuff, Random rand, Connection connection) {
        this.numStuff = numStuff;
        this.rand = rand;
        this.connection = connection;
        this.lib = new libReading(rand);
        names = lib.getNamesGenders();
        surnames = lib.getFromFile("surnames.txt");
        emails = lib.getFromFile("emails.txt");
        addresses = lib.getFromFile("addresses.txt");
        profile_images = lib.getFromFile("lorem.txt");
        statss = lib.getFromFile("lorem.txt");
        commentss = lib.getFromFile("lorem.txt");

        phone_numbers = lib.getFromFile("phone_numbers.txt");

        descriptions = lib.getFromFile("lorem.txt");
        lorem = lib.getFromFile("lorem.txt");
    }

    public void addPatient() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO patients (pid, first_name, last_name, date_of_birth, gender, email, address, profile_image, stats, comments, status)" +
                            "VALUES (?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?, ?, ?)");

            int pid = num++;
            Name name = names.get(rand.nextInt(names.size()));
            String surname = lib.getRandString(surnames);
            String address = lib.getRandString(addresses);
            String email = lib.getRandString(emails);
            String profile_image = lib.getRandString(profile_images, 100);
            String stats = lib.getRandString(statss);
            String comments = lib.getRandString(commentss);
            String status = lib.getRandString(statss, 10).split(" ")[0];
            java.sql.Date dateOfBirth = lib.getRandDate(1970, 2005);

            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, name.name);
            preparedStatement.setString(3, surname);
            preparedStatement.setDate(4, dateOfBirth);
            preparedStatement.setString(5, GenderType.valueOf(name.gender).toString());
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, address);
            preparedStatement.setString(8, profile_image);
            preparedStatement.setString(9, stats);
            preparedStatement.setString(10, comments);
            preparedStatement.setString(11, status);

            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("INSERT INTO patient_contacts (pid, phone_number)" +
                    "VALUES (?, ?)");

            int phoneNum = rand.nextInt(4);
            for (int i = 0; i < phoneNum; ++i) {
                Long phone_number = Long.parseLong(phone_numbers.get(rand.nextInt(phone_numbers.size())));
                preparedStatement.setInt(1, pid);
                preparedStatement.setObject(2, phone_number);

                preparedStatement.executeUpdate();
            }

            preparedStatement = connection.prepareStatement("INSERT INTO medical_history (pid, description, time_loaded, last_modified, access_level, electronic_copy, comments)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, lib.getRandString(descriptions));


            int midTime1 = dateOfBirth.toLocalDate().getYear() + rand.nextInt(2019 - dateOfBirth.toLocalDate().getYear());
            preparedStatement.setDate(3, lib.getRandDate(dateOfBirth.toLocalDate().getYear(), midTime1));
            preparedStatement.setDate(4, lib.getRandDate(midTime1, 2019));
            preparedStatement.setInt(5, rand.nextInt(10) + 1);
            preparedStatement.setString(6, lib.getRandString(lorem, 100));
            preparedStatement.setString(7, lib.getRandString(lorem));

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO patients_illnesses (pid, illness, illness_start, illness_finish, therapist_id, comments)" +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < rand.nextInt(10); ++i) {
                preparedStatement.setInt(1, pid);
                preparedStatement.setString(2, lib.getRandString(lorem, 100).split(" ")[0]);
                int midTime = 1970 + rand.nextInt(2019 - 1970);
                preparedStatement.setDate(3, lib.getRandDate(1970, midTime));
                preparedStatement.setDate(4, lib.getRandDate(midTime, 2019));
                preparedStatement.setInt(5, rand.nextInt(numStuff));
                preparedStatement.setString(6, lib.getRandString(lorem));

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


