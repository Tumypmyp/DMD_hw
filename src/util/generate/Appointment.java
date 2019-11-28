package util.generate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Random;

public class Appointment {

    private static int MAX_ACCESS_LEVEL = 10000;
    private ArrayList<String> lorem;
    private Connection connection;
    private Random rand;
    private int numApp = 0;
    private libReading lib;
    private int numStaff;
    private int numPatient;

    public Appointment(int numStaff, int numPatient, Random rand, Connection connection) {
        this.numStaff = numStaff;
        this.numPatient = numPatient;
        this.rand = rand;
        this.connection = connection;
        this.lib = new libReading(rand);
        lorem = lib.getFromFile("lorem.txt");
    }

    //*
    public void addAppointment() {
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO appointments(appointment_id, appointment_type, appointment_time, place) " +
                            "VALUES (?, ?::appointment_type_id, ?, ?)");

            int appId = numApp++;
            String type = "home_visit buiseness_meeting hospital_visit".split(" ")[rand.nextInt(3)];
            preparedStatement.setInt(1, appId);
            preparedStatement.setString(2, AppointmentType.valueOf(type).toString());
            preparedStatement.setDate(3, lib.getRandDate(2009 + (int)Math.sqrt(rand.nextInt(120)), 2020));
            preparedStatement.setString(4, lib.getRandString(lorem, 100));

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO staff_in_appointment(appointment_id, staff_member)" +
                    "VALUES (?, ?)");

            preparedStatement.setInt(1, appId);
            preparedStatement.setInt(2, rand.nextInt(numStaff));

            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("INSERT INTO patients_in_appointment(appointment_id, patient_member)" +
                    "VALUES (?, ?)");

            preparedStatement.setInt(1, appId);
            preparedStatement.setInt(2, rand.nextInt(numPatient));

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
