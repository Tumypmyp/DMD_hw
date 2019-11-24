package util.generate;

public class Name {
    String name;
    String gender;
    double probability;

    Name(String name, String gender, String probability) {
        this.name = name;
        this.gender = (gender.equals("M") ? "male" : "female");
        this.probability = Double.parseDouble(probability) * 100;
    }

}
