package util.generate;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class libReading {
    Random rand;

    libReading(Random rand){
        this.rand = rand;
    }

    ArrayList<String> getFromFile(String filename) {

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

     String getRandString(ArrayList<String> array) {
        return array.get(rand.nextInt(array.size()));
    }

     String getRandString(ArrayList<String> array, int maxSize) {
        String s = getRandString(array);
        return s.substring(0, Math.min(maxSize, s.length()));
    }

    java.sql.Date getRandDate(int yearFrom, int yearTo) {
        long minDay = LocalDate.of(yearFrom, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(yearTo, 1, 1).toEpochDay();
        long randomDay = Math.abs(rand.nextLong()) % (maxDay - minDay + 1) + minDay;
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return java.sql.Date.valueOf(randomDate);
    }


//    private java.sql.Timestamp getRandTimestamp(int yearFrom, int yearTo) {
//        java.sql.Date date = getRandDate(yearFrom, yearTo);
//        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date.getTime());
//        timestamp.setTime();
//        return  timestamp;
//    }

     ArrayList<Name> getNamesGenders() {
        InputStream input = getClass().getResourceAsStream("/res/name_gender.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        ArrayList<Name> names = new ArrayList<>();
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
        return names;
    }

     void parse(ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            String s = array.get(i);
            s = s.replaceAll("\\D", "");
//            s = s.toLowerCase();
//            if (!onlyLowerCase)
//                s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            array.set(i, s);
        }
    }

     static void writeParsedData(ArrayList<String> array, String filename) {
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
