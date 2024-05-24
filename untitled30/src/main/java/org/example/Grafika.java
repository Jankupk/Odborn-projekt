package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Grafika extends JFrame {
    JPanel panel;
    JLabel nameLabel, chipDateLabel, chipCodeLabel, rankLabel, chipCodeOpenLabel, deleteNameLabel;
    JTextField chipDateField, chipCodeField, nameField, rankField, chipCodeOpenField, deleteNameField;
    JButton addButton, openDoorButton, closeDoorButton, printEmployeesButton, deleteButton, viewLogsButton, startButton, endButton;
    JComboBox<String> possibleEmployees;
    JTextArea employeeListTextArea;

    DBConnect databaseData;
    Door door;

    public Grafika() {
        databaseData = new DBConnect();
        door = new Door();
        databaseData.createNewTable();

        createMainMenu();
    }

    private void createMainMenu() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        startButton = new JButton("Start");
        endButton = new JButton("Konec");

        panel.add(startButton);
        panel.add(endButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(panel);
                createEmployeeGUI();
                validate();
                repaint();
            }
        });

        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(panel);
        setTitle("Hlavní menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createEmployeeGUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(15, 2));
        panel.setBackground(Color.lightGray);
        nameLabel = new JLabel("Jméno: ");
        nameField = new JTextField();
        chipDateLabel = new JLabel("Datum čipu: ");
        chipDateField = new JTextField();
        chipCodeLabel = new JLabel("Kód čipu: ");
        chipCodeField = new JTextField();
        rankLabel = new JLabel("Hodnost: ");
        rankField = new JTextField();
        chipCodeOpenLabel = new JLabel("Otevřít dveře (kód čipu): ");
        chipCodeOpenField = new JTextField();
        deleteNameLabel = new JLabel("Jméno zaměstnance k vymazání: ");
        deleteNameField = new JTextField();

        addButton = new JButton("Přidat zaměstnance");
        openDoorButton = new JButton("Otevřít dveře");
        closeDoorButton = new JButton("Zavřít dveře");
        printEmployeesButton = new JButton("Zobrazit zaměstnance");
        deleteButton = new JButton("Vymazat zaměstnance");
        viewLogsButton = new JButton("Co se stalo");

        possibleEmployees = new JComboBox<>();
        for (Employee employee : databaseData.getAllEmployees()) {
            possibleEmployees.addItem(employee.getName());
        }

        employeeListTextArea = new JTextArea(10, 30);
        employeeListTextArea.setEditable(false);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(chipDateLabel);
        panel.add(chipDateField);
        panel.add(chipCodeLabel);
        panel.add(chipCodeField);
        panel.add(rankLabel);
        panel.add(rankField);
        panel.add(new JLabel(""));
        panel.add(addButton);
        panel.add(chipCodeOpenLabel);
        panel.add(chipCodeOpenField);
        panel.add(new JLabel(""));
        panel.add(openDoorButton);
        panel.add(new JLabel(""));
        panel.add(closeDoorButton);
        panel.add(new JLabel(""));
        panel.add(printEmployeesButton);
        panel.add(deleteNameLabel);
        panel.add(deleteNameField);
        panel.add(new JLabel(""));
        panel.add(deleteButton);
        panel.add(new JLabel(""));
        panel.add(viewLogsButton);
        panel.add(new JLabel("Seznam zaměstnanců:"));
        panel.add(possibleEmployees);

        panel.add(new JLabel("Seznam zaměstnanců a hodností nebo co se stalo :"));
        panel.add(new JScrollPane(employeeListTextArea));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String chipDate = chipDateField.getText();
                String chipCode = chipCodeField.getText();
                String rank = rankField.getText();
                Employee employee = new Employee(name, chipDate, chipCode, rank);
                if (databaseData.employeeExists(name)) {
                    JOptionPane.showMessageDialog(null, "Zaměstnanec se jménem " + name + " již existuje.");
                } else if (databaseData.chipCodeExists(chipCode)) {
                    JOptionPane.showMessageDialog(null, "Čipový kód " + chipCode + " již existuje.");
                } else {
                    databaseData.insertEmployeeWithChip(employee);
                    possibleEmployees.addItem(employee.getName());
                    JOptionPane.showMessageDialog(null, "Zaměstnanec přidán!");
                }
            }
        });

        openDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chipCode = chipCodeOpenField.getText();
                if (door.openDoor(chipCode)) {
                    JOptionPane.showMessageDialog(null, "Dveře otevřeny!");
                } else {
                    JOptionPane.showMessageDialog(null, "Neplatný kód čipu. Dveře se neotevřely.");
                }
            }
        });

        closeDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                door.closeDoor();
                JOptionPane.showMessageDialog(null, "Dveře zavřeny!");
            }
        });

        printEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayEmployeeNamesAndRanks();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = deleteNameField.getText();
                int confirm = JOptionPane.showConfirmDialog(null, "Opravdu chcete vymazat zaměstnance " + name + "?", "Potvrzení vymazání", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (databaseData.deleteEmployeeByName(name)) {
                        JOptionPane.showMessageDialog(null, "Zaměstnanec " + name + " byl vymazán.");
                        possibleEmployees.removeItem(name);
                    } else {
                        JOptionPane.showMessageDialog(null, "Zaměstnanec " + name + " nebyl nalezen.");
                    }
                }
            }
        });

        viewLogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDoorLogs();
            }
        });

        add(panel);
        setTitle("Employee Management");
        setSize(400, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displayEmployeeNamesAndRanks() {
        employeeListTextArea.setText("");
        ArrayList<Employee> employees = databaseData.getAllEmployees();
        StringBuilder sb = new StringBuilder();
        for (Employee employee : employees) {
            sb.append("Name: ").append(employee.getName()).append(", Rank: ").append(employee.getRank()).append("\n");
        }
        employeeListTextArea.setText(sb.toString());
    }

    private void displayDoorLogs() {
        employeeListTextArea.setText("");
        ArrayList<String> logs = databaseData.getDoorLogs();
        StringBuilder sb = new StringBuilder();
        for (String log : logs) {
            sb.append(log).append("\n");
        }
        employeeListTextArea.setText(sb.toString());
    }
}
