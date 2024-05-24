package org.example;

public class Employee {
    private String name;
    private String chipDate;
    private String chipCode;
    private String rank;

    public Employee(String name, String chipDate, String chipCode, String rank) {
        this.name = name;
        this.chipDate = chipDate;
        this.chipCode = chipCode;
        this.rank = rank;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
