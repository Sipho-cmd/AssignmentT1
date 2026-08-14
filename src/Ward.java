public class Ward {
    private String[][] beds = new String[4][5]; // stores patient ID or null if empty

    public Ward() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                beds[row][col] = null;
            }
        }
    }

    private String bedLabel(int row, int col) {
        int bedNumber = row * 5 + col + 1;
        return String.format("B%02d", bedNumber);
    }

    public boolean allocateBed(String bedLabel, String patientID) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                if (bedLabel(row, col).equalsIgnoreCase(bedLabel)) {
                    if (beds[row][col] != null) {
                        System.out.println("Bed already occupied.");
                        return false;
                    }
                    beds[row][col] = patientID;
                    return true;
                }
            }
        }
        System.out.println("Bed not found.");
        return false;
    }

    public boolean releaseBed(String bedLabel) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                if (bedLabel(row, col).equalsIgnoreCase(bedLabel)) {
                    if (beds[row][col] == null) {
                        System.out.println("Bed is already empty.");
                        return false;
                    }
                    beds[row][col] = null;
                    return true;
                }
            }
        }
        System.out.println("Bed not found.");
        return false;
    }

    public void displayWardLayout() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                String status = beds[row][col] == null ? "Empty" : beds[row][col];
                System.out.print(bedLabel(row, col) + "[" + status + "] ");
            }
            System.out.println();
        }
    }

    public void displayAvailableBeds() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                if (beds[row][col] == null) {
                    System.out.println(bedLabel(row, col) + " - Available");
                }
            }
        }
    }

    public void displayOccupiedBeds() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                if (beds[row][col] != null) {
                    System.out.println(bedLabel(row, col) + " - Occupied by Patient ID: " + beds[row][col]);
                }
            }
        }
    }

    public int countOccupiedBeds() {
        int count = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                if (beds[row][col] != null) count++;
            }
        }
        return count;
    }

    public int getTotalBeds() {
        return 20;
    }
}