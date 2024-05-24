package org.example;

public class Employee {
    private String name;
    private String chipDate;
    private String chipCode;

    public Employee(String name, String chipDate, String chipCode) {
        this.name = name;
        this.chipDate = chipDate;
        this.chipCode = chipCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChipDate() {
        return chipDate;
    }

    public void setChipDate(String chipDate) {
        this.chipDate = chipDate;
    }

    public String getChipCode() {
        return chipCode;
    }

    public void setChipCode(String chipCode) {
        this.chipCode = chipCode;
    }
}










