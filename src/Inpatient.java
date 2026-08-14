public class Inpatient extends Patient {
    private int wardNumber;
    private String bedNumber;

    public Inpatient(String patientID, String firstName, String lastName, int age,
                     String gender, String medicalCondition,
                     int wardNumber, String bedNumber) {
        super(patientID, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }

    public int getWardNumber() { return wardNumber; }
    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Ward Number: " + wardNumber);
        System.out.println("Bed Number: " + bedNumber);
    }
}