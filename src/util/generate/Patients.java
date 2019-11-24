package util.generate;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Patients {
    private ArrayList<String> surnames;
    private ArrayList<Name> names;
    private ArrayList<String> addresses;
    private ArrayList<String> emails;
    private ArrayList<String> phone_numbers;
    private Random rand;
    private Connection connection;
    private int num = 0;


    public Patients(Random rand, Connection connection) {
        this.rand = rand;
        this.connection = connection;
        getNamesGenders();
        surnames = getFromFile("surnames.txt");
        emails = getFromFile("emails.txt");
        addresses = getFromFile("addresses.txt");
        phone_numbers = getFromFile("phone_numbers.txt");
    }

    public void addPatient() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO patients (pid, first_name, last_name, date_of_birth, gender, email, address)" +
                    "VALUES (?, ?, ?, ?, ?::gender_type, ?, ?)");

            int pid = num++;
            Name name = names.get(rand.nextInt(names.size()));
            String surname = surnames.get(rand.nextInt(surnames.size()));
            String address = addresses.get(rand.nextInt(addresses.size()));
            String email = emails.get(rand.nextInt(emails.size()));

            long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2010, 12, 31).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            java.sql.Date date = java.sql.Date.valueOf(randomDate);


            preparedStatement.setInt(1, pid);
            preparedStatement.setString(2, name.name);
            preparedStatement.setString(3, surname);
            preparedStatement.setDate(4, date);
            preparedStatement.setString(5, GenderType.valueOf(name.gender).toString());
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, address);
            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("INSERT INTO patient_contacts (pid, phone_number)" +
                    "VALUES (?, ?)");
            preparedStatement.setInt(1, pid);
            Long phone_number = Long.parseLong(phone_numbers.get(rand.nextInt(phone_numbers.size())));
            preparedStatement.setObject(2, phone_number);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ArrayList<String> getFromFile(String filename) {

        InputStream input = getClass().getResourceAsStream("/res/" + filename);

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        ArrayList<String> result = new ArrayList<>();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty())
                    continue;
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void getNamesGenders() {
        InputStream input = getClass().getResourceAsStream("/res/name_gender.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        names = new ArrayList<>();
        String name;
        try {
            while ((name = reader.readLine()) != null) {
                String[] array = name.split(",");
                names.add(new Name(array[0], array[1], array[2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse(ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            String s = array.get(i);
            s = s.replaceAll("\\D", "");
//            s = s.toLowerCase();
//            if (!onlyLowerCase)
//                s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            array.set(i, s);
        }
    }

    public static void writeParsedData(ArrayList<String> array, String filename) {
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter("res/" + filename));
            for (int i = 0; i < array.size(); i++)
                writer.write(array.get(i) + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


