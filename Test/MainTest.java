import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

class MainTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private final String NEWLINE = System.lineSeparator();

    @BeforeEach
    void setUp() throws Exception {
        // Reset the patients list before each test using reflection
        resetPatientsList();

        // Set up output capture
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    // Helper method to reset the static patients list using reflection
    private void resetPatientsList() throws Exception {
        Field patientsField = Main.class.getDeclaredField("patients");
        patientsField.setAccessible(true);
        patientsField.set(null, new ArrayList<Patient>());
    }

    // Helper method to simulate user input
    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    //TEST MAIN METHOD
    @Test
    void testMainMethodExit() {
        provideInput("0" + NEWLINE);


        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }

    //TEST REGISTER PATIENT

    @Test
    void testRegisterPatientOutpatient() {
        // Simulate user input for registering an outpatient
        String input = "P001" + NEWLINE +
                "John" + NEWLINE +
                "Doe" + NEWLINE +
                "45" + NEWLINE +
                "Male" + NEWLINE +
                "Diabetes" + NEWLINE +
                "OUTPATIENT" + NEWLINE;

        provideInput(input);

        Main.registerPatient();

        // Verify output contains success message
        String output = outputStream.toString();
        assertTrue(output.contains("Patient registered successfully."));

        // Verify patient was added (we need to check using findPatient)
        Patient found = Main.findPatient("P001");
        assertNotNull(found);
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
        assertEquals(45, found.getAge());
        assertEquals("Male", found.getGender());
        assertEquals("Diabetes", found.getMedicalCondition());
        assertEquals(PatientCategory.OUTPATIENT, found.getPatientCategory());
    }

    @Test
    void testRegisterPatientInpatient() {
        // Simulate user input for registering an inpatient
        String input = "P002" + NEWLINE +
                "Jane" + NEWLINE +
                "Smith" + NEWLINE +
                "30" + NEWLINE +
                "Female" + NEWLINE +
                "Fracture" + NEWLINE +
                "INPATIENT" + NEWLINE +
                "101" + NEWLINE +
                "B05" + NEWLINE;

        provideInput(input);

        Main.registerPatient();

        // Verify output contains success message
        String output = outputStream.toString();
        assertTrue(output.contains("Patient registered successfully."));

        // Verify patient was added and is an Inpatient
        Patient found = Main.findPatient("P002");
        assertNotNull(found);
        assertTrue(found instanceof Inpatient);
        Inpatient inpatient = (Inpatient) found;
        assertEquals("Jane", inpatient.getFirstName());
        assertEquals("Smith", inpatient.getLastName());
        assertEquals(30, inpatient.getAge());
        assertEquals("Female", inpatient.getGender());
        assertEquals("Fracture", inpatient.getMedicalCondition());
        assertEquals(PatientCategory.INPATIENT, inpatient.getPatientCategory());
        assertEquals(101, inpatient.getWardNumber());
        assertEquals("B05", inpatient.getBedNumber());
    }

    @Test
    void testRegisterPatientEmergency() {
        String input = "P003" + NEWLINE +
                "Bob" + NEWLINE +
                "Johnson" + NEWLINE +
                "60" + NEWLINE +
                "Male" + NEWLINE +
                "Heart Attack" + NEWLINE +
                "EMERGENCY" + NEWLINE;

        provideInput(input);

        Main.registerPatient();

        String output = outputStream.toString();
        assertTrue(output.contains("Patient registered successfully."));

        Patient found = Main.findPatient("P003");
        assertNotNull(found);
        assertEquals("Bob", found.getFirstName());
        assertEquals("Johnson", found.getLastName());
        assertEquals(60, found.getAge());
        assertEquals(PatientCategory.EMERGENCY, found.getPatientCategory());
    }

    //TEST SEARCH PATIENT
    @Test
    void testSearchPatientFound() {
        registerTestPatient("P001", "John", "Doe");

        provideInput("P001" + NEWLINE);
        Main.searchPatient();

        String output = outputStream.toString();
        assertTrue(output.contains("Patient ID: P001"));
        assertTrue(output.contains("Name: John Doe"));
    }

    @Test
    void testSearchPatientNotFound() {
        provideInput("P999" + NEWLINE);
        Main.searchPatient();

        String output = outputStream.toString();
        assertTrue(output.contains("No patient found with that ID."));
    }

    //TEST UPDATE PATIENT
    @Test
    void testUpdatePatientFound() {
        registerTestPatient("P001", "John", "Doe");

        String input = "P001" + NEWLINE +
                "Jonathan" + NEWLINE +
                "Doe-Smith" + NEWLINE +
                "46" + NEWLINE +
                "Male" + NEWLINE +
                "Type 2 Diabetes" + NEWLINE;

        provideInput(input);
        Main.updatePatient();

        String output = outputStream.toString();
        assertTrue(output.contains("Patient updated successfully."));

        // Verify the update
        Patient found = Main.findPatient("P001");
        assertEquals("Jonathan", found.getFirstName());
        assertEquals("Doe-Smith", found.getLastName());
        assertEquals(46, found.getAge());
        assertEquals("Type 2 Diabetes", found.getMedicalCondition());
    }

    @Test
    void testUpdatePatientNotFound() {
        provideInput("P999" + NEWLINE);
        Main.updatePatient();

        String output = outputStream.toString();
        assertTrue(output.contains("No patient found with that ID."));
    }

    //TEST DELETE PATIENT
    @Test
    void testDeletePatientFound() {
        registerTestPatient("P001", "John", "Doe");

        provideInput("P001" + NEWLINE);
        Main.deletePatient();

        String output = outputStream.toString();
        assertTrue(output.contains("Patient deleted successfully."));

        Patient found = Main.findPatient("P001");
        assertNull(found);
    }

    @Test
    void testDeletePatientNotFound() {
        provideInput("P999" + NEWLINE);
        Main.deletePatient();

        String output = outputStream.toString();
        assertTrue(output.contains("No patient found with that ID."));
    }

    //TEST DISPLAY ALL PATIENTS
    @Test
    void testDisplayAllPatientsWithPatients() {
        registerTestPatient("P001", "John", "Doe");
        registerTestPatient("P002", "Jane", "Smith");
        registerTestPatient("P003", "Bob", "Johnson");

        outputStream.reset();

        Main.displayAllPatients();

        String output = outputStream.toString();
        assertTrue(output.contains("Patient ID: P001"));
        assertTrue(output.contains("Patient ID: P002"));
        assertTrue(output.contains("Patient ID: P003"));
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("Jane Smith"));
        assertTrue(output.contains("Bob Johnson"));
    }

    @Test
    void testDisplayAllPatientsEmpty() {
        Main.displayAllPatients();

        String output = outputStream.toString();
        assertTrue(output.contains("No patients registered yet."));
    }

    //TEST ALLOCATE BED

    @Test
    void testAllocateBed() {
        registerTestPatient("P001", "John", "Doe");

        String input = "P001" + NEWLINE +
                "B01" + NEWLINE;
        provideInput(input);
        Main.allocateBed();

        assertDoesNotThrow(() -> Main.allocateBed());
    }

    //EST RELEASE BED

    @Test
    void testReleaseBed() {
        Main.ward.allocateBed("B01", "P001");

        provideInput("B01" + NEWLINE);
        Main.releaseBed();

        assertDoesNotThrow(() -> Main.releaseBed());
    }

    //TEST PATIENT REPORT
    @Test
    void testPatientReport() {
        // Register multiple patients with different last names
        registerTestPatient("P001", "John", "Zebra");
        registerTestPatient("P002", "Jane", "Apple");
        registerTestPatient("P003", "Bob", "Mango");

        outputStream.reset();

        Main.patientReport();

        String output = outputStream.toString();
        assertTrue(output.contains("--- Patient Report (sorted by surname) ---"));
        assertTrue(output.contains("Total registered patients: 3"));

        int appleIndex = output.indexOf("Apple");
        int mangoIndex = output.indexOf("Mango");
        int zebraIndex = output.indexOf("Zebra");
        assertTrue(appleIndex < mangoIndex && mangoIndex < zebraIndex);
    }

    @Test
    void testPatientReportEmpty() {
        Main.patientReport();

        String output = outputStream.toString();
        assertTrue(output.contains("Total registered patients: 0"));
    }

    //TEST BED OCCUPANCY REPORT
    @Test
    void testBedOccupancyReport() {
        // Allocate some beds
        Main.ward.allocateBed("B01", "P001");
        Main.ward.allocateBed("B02", "P002");

        Main.bedOccupancyReport();

        String output = outputStream.toString();
        assertTrue(output.contains("--- Bed Occupancy Report ---"));
        assertTrue(output.contains("Occupied beds: 2"));
        assertTrue(output.contains("Available beds: 18"));
        assertTrue(output.contains("Occupancy percentage: 10.0%"));
    }

    @Test
    void testBedOccupancyReportEmpty() {
        Main.bedOccupancyReport();

        String output = outputStream.toString();
        assertTrue(output.contains("Occupied beds: 0"));
        assertTrue(output.contains("Available beds: 20"));
        assertTrue(output.contains("Occupancy percentage: 0.0%"));
    }

    //TEST FIND PATIENT
    @Test
    void testFindPatientFound() {
        registerTestPatient("P001", "John", "Doe");

        Patient found = Main.findPatient("P001");
        assertNotNull(found);
        assertEquals("P001", found.getPatientID());
        assertEquals("John", found.getFirstName());
    }

    @Test
    void testFindPatientNotFound() {
        Patient found = Main.findPatient("P999");
        assertNull(found);
    }

    @Test
    void testFindPatientCaseInsensitive() {
        registerTestPatient("P001", "John", "Doe");

        Patient found = Main.findPatient("p001");
        assertNotNull(found);
        assertEquals("P001", found.getPatientID());
    }

    private void registerTestPatient(String id, String firstName, String lastName) {
        Patient patient = new Patient(id, firstName, lastName, 30, "Male",
                "Test Condition", PatientCategory.OUTPATIENT);
        Main.patients.add(patient);
    }
}