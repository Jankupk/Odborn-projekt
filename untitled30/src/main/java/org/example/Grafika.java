package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Grafika extends JFrame {
    JPanel panel;
    JLabel nameLabel, chipDateLabel, chipCodeLabel;
    JTextField chipDateField, chipCodeField, nameField;
    JButton addButton;
    JComboBox<String> possibleEmployees;

    DBConnect databaseData;

    public Grafika(){
        databaseData = new DBConnect();

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        nameLabel = new JLabel("Jméno: ");
        nameField = new JTextField();

        chipDateLabel = new JLabel("Datum čipu: ");
        chipDateField = new JTextField();

        chipCodeLabel = new JLabel("Kód čipu: ");
        chipCodeField = new JTextField();

        addButton = new JButton("Přidat zaměstnance ");
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(chipDateLabel);
        panel.add(chipDateField);
        panel.add(chipCodeLabel);
        panel.add(chipCodeField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        possibleEmployees = new JComboBox<>();

        for (Employee employee: databaseData.getAllEmployees()){
            possibleEmployees.addItem(employee.getName());
        }

        panel.add(possibleEmployees);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Toto je v podstatě funkce "Přidat zaměstnance", nikoliv zobrazit, co chceš na první stranu
                String name = nameField.getText();
                String chipDate = chipDateField.getText();
                String chipCode = chipCodeField.getText();
                DBConnect.insertEmployeeWithChip(new Employee(name, chipDate, chipCode));
                */
            }
        });
        add(panel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
    }
}
