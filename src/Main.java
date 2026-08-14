import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    static ArrayList<Patient> patients = new ArrayList<>();
    static Ward ward = new Ward();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n ------ MediCare Hospital - Patient Admission System ------");
            System.out.println("1. Register Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Display All Patients");
            System.out.println("6. Allocate Bed");
            System.out.println("7. Release Bed");
            System.out.println("8. Display Ward Layout");
            System.out.println("9. Display Available Beds");
            System.out.println("10. Display Occupied Beds");
            System.out.println("11. Patient Report (sorted)");
            System.out.println("12. Bed Occupancy Report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> searchPatient();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
                case 5 -> displayAllPatients();
                case 6 -> allocateBed();
                case 7 -> releaseBed();
                case 8 -> ward.displayWardLayout();
                case 9 -> ward.displayAvailableBeds();
                case 10 -> ward.displayOccupiedBeds();
                case 11 -> patientReport();
                case 12 -> bedOccupancyReport();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Goodbye.");
    }

    static void registerPatient() {
        System.out.print("Enter Patient ID: ");
        String patientID = input.nextLine();

        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        System.out.print("Enter Age: ");
        int age = input.nextInt();
        input.nextLine();

        System.out.print("Enter Gender: ");
        String gender = input.nextLine();

        System.out.print("Enter Medical Condition: ");
        String medicalCondition = input.nextLine();

        System.out.print("Enter Category (INPATIENT, OUTPATIENT, EMERGENCY): ");
        String categoryInput = input.nextLine();
        PatientCategory category = PatientCategory.valueOf(categoryInput.toUpperCase());

            Patient newPatient;
            if (category == PatientCategory.INPATIENT) {
                System.out.print("Enter Ward Number: ");
                int wardNumber = input.nextInt();
                input.nextLine();
                System.out.print("Enter Bed Number (e.g. B01): ");
                String bedNumber = input.nextLine();
                newPatient = new Inpatient(patientID, firstName, lastName, age, gender, medicalCondition, wardNumber, bedNumber);

            } else {
                newPatient = new Patient(patientID, firstName, lastName, age, gender, medicalCondition, category);
            }

        patients.add(newPatient);
        System.out.println("Patient registered successfully.");
    }

    static void searchPatient() {
        System.out.print("Enter Patient ID to search: ");
        String patientID = input.nextLine();

        Patient found = findPatient(patientID);
        if (found == null) {
            System.out.println("No patient found with that ID.");

        } else {
            found.displayDetails();
        }
    }

    static void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        String patientID = input.nextLine();

            Patient found = findPatient(patientID);
            if (found == null) {
                System.out.println("No patient found with that ID.");
                return;
            }

        System.out.print("Enter new First Name: ");
        found.setFirstName(input.nextLine());

        System.out.print("Enter new Last Name: ");
        found.setLastName(input.nextLine());

        System.out.print("Enter new Age: ");
        found.setAge(input.nextInt());
        input.nextLine();

        System.out.print("Enter new Gender: ");
        found.setGender(input.nextLine());

        System.out.print("Enter new Medical Condition: ");
        found.setMedicalCondition(input.nextLine());

        System.out.println("Patient updated successfully.");
    }

    static void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        String patientID = input.nextLine();

        Patient found = findPatient(patientID);
        if (found == null) {
            System.out.println("No patient found with that ID.");
            return;
        }

        patients.remove(found);
        System.out.println("Patient deleted successfully.");
    }

    static void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        for (Patient p : patients) {
            p.displayDetails();
            System.out.println("----------------------");
        }
    }

    static void allocateBed() {
        System.out.print("Enter Patient ID: ");
        String patientID = input.nextLine();
        System.out.print("Enter Bed Number (e.g. B01): ");
        String bedNumber = input.nextLine();
        ward.allocateBed(bedNumber, patientID);
    }

    static void releaseBed() {
        System.out.print("Enter Bed Number to release (e.g. B01): ");
        String bedNumber = input.nextLine();
        ward.releaseBed(bedNumber);
    }

    static void patientReport() {
        ArrayList<Patient> sorted = new ArrayList<>(patients);
        sorted.sort(Comparator.comparing(Patient::getLastName));

        System.out.println("--- Patient Report (sorted by surname) ---");
        System.out.println("Total registered patients: " + patients.size());
        for (Patient p : sorted) {
            p.displayDetails();
            System.out.println("----------------------");
        }
    }

    static void bedOccupancyReport() {
        int occupied = ward.countOccupiedBeds();
        int total = ward.getTotalBeds();
        double percentage = ((double) occupied / total) * 100;

        System.out.println("--- Bed Occupancy Report ---");
        System.out.println("Occupied beds: " + occupied);
        System.out.println("Available beds: " + (total - occupied));
        System.out.println("Occupancy percentage: " + percentage + "%");
    }

    static Patient findPatient(String patientID) {
        for (Patient p : patients) {
            if (p.getPatientID().equalsIgnoreCase(patientID)) {
                return p;
            }
        }
        return null;
    }
}