package org.example;

public class Door {
    private DBConnect dbConnect;
    private boolean doorOpen;

    public Door() {
        this.dbConnect = new DBConnect();
        this.doorOpen = false;
    }

    public boolean openDoor(String chipCode) {
        System.out.println("Pokouším se otevřít dveře s kódem čipu: " + chipCode);
        Employee employee = dbConnect.getEmployeeByChipCode(chipCode);
        if (employee != null) {
            System.out.println("Dveře se otevřely pro zaměstnance: " + employee.getName());
            dbConnect.logDoorAction(employee.getName(), "Otevřeno");
            doorOpen = true;
            return true;
        } else {
            System.out.println("Neplatný kód čipu. Dveře se neotevřely.");
            doorOpen = false;
            return false;
        }
    }

    public void closeDoor() {
        if (doorOpen) {
            System.out.println("Dveře se zavřely.");
            dbConnect.logDoorAction("Unknown", "Zavřeno");
            doorOpen = false;
        } else {
            System.out.println("Dveře jsou již zavřené.");
        }
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }
}
