package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Grafika extends JFrame {
    JPanel panel;
    JLabel nameLabel, chipDateLabel, chipCodeLabel, chipCodeOpenLabel;
    JTextField chipDateField, chipCodeField, nameField, chipCodeOpenField;
    JButton addButton, openDoorButton, closeDoorButton;
    JComboBox<String> possibleEmployees;

    DBConnect databaseData;
    Door door;

    public Grafika() {
        databaseData = new DBConnect();
        door = new Door();
        databaseData.createNewTable();

        panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));
        nameLabel = new JLabel("Jméno: ");
        nameField = new JTextField();
        chipDateLabel = new JLabel("Datum čipu: ");
        chipDateField = new JTextField();
        chipCodeLabel = new JLabel("Kód čipu: ");
        chipCodeField = new JTextField();
        chipCodeOpenLabel = new JLabel("Otevřít dveře (kód čipu): ");
        chipCodeOpenField = new JTextField();

        addButton = new JButton("Přidat zaměstnance");
        openDoorButton = new JButton("Otevřít dveře");
        closeDoorButton = new JButton("Zavřít dveře");

        possibleEmployees = new JComboBox<>();
        for (Employee employee : databaseData.getAllEmployees()) {
            possibleEmployees.addItem(employee.getName());
        }

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(chipDateLabel);
        panel.add(chipDateField);
        panel.add(chipCodeLabel);
        panel.add(chipCodeField);
        panel.add(new JLabel(""));
        panel.add(addButton);
        panel.add(chipCodeOpenLabel);
        panel.add(chipCodeOpenField);
        panel.add(new JLabel(""));
        panel.add(openDoorButton);
        panel.add(new JLabel(""));
        panel.add(closeDoorButton);
        panel.add(new JLabel("Seznam zaměstnanců:"));
        panel.add(possibleEmployees);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String chipDate = chipDateField.getText();
                String chipCode = chipCodeField.getText();
                Employee employee = new Employee(name, chipDate, chipCode);
                databaseData.insertEmployeeWithChip(employee);
                possibleEmployees.addItem(employee.getName());
                JOptionPane.showMessageDialog(null, "Zaměstnanec přidán!");
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

        add(panel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
    }
}