import util.*;
import util.generate.*;

import java.sql.Connection;
import java.util.Random;


public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please enter at least first 3 parameters :\n\t" +
                    "database, login, password, initRandom, minNumber, maxNumber");
            return;
        }
        Connection connection;
        try {
            connection = ConnectionConfiguration.getConnection(args[0], args[1], args[2]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (connection == null) {
            System.out.println("hmm check the password");
            return;
        }

        int initRandom = (int) System.currentTimeMillis();
        int minNumber = 10;
        int maxNumber = 100;
        if (3 < args.length)
            initRandom = Integer.parseInt(args[3]);
        if (4 < args.length)
            minNumber = Integer.parseInt(args[4]);
        if (5 < args.length)
            maxNumber = Integer.parseInt(args[4]);
        Random rand = new Random(initRandom);

        TableFunctions.clear(connection);
        TableFunctions.initialisation(connection);

        Patients patient = new Patients(rand, connection);
        int numPatient = rand.nextInt(maxNumber - minNumber + 1) + minNumber;
        try {
            for (int i = 0; i < numPatient; ++i)
                patient.addPatient();
            System.out.println(numPatient + " patients added");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnectionConfiguration.closeConnection(connection);
    }
}
