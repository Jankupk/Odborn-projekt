package org.example;

import java.sql.*;
import java.util.ArrayList;

public class DBConnect {
    private static String url = "jdbc:sqlite:employeeSystem.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewTable() {
        String sqlEmployees = "CREATE TABLE IF NOT EXISTS Employees ("
                + "idPerson INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "idChip INTEGER, "
                + "chipCode TEXT UNIQUE, "
                + "rank TEXT)";
        executeSQL(sqlEmployees);

        String sqlChips = "CREATE TABLE IF NOT EXISTS Chip ("
                + "idChip INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "old TEXT)";
        executeSQL(sqlChips);

        String sqlDoorLogs = "CREATE TABLE IF NOT EXISTS DoorLogs ("
                + "logId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "employeeName TEXT, "
                + "action TEXT, "
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        executeSQL(sqlDoorLogs);
    }

    public static boolean employeeExists(String name) {
        String sql = "SELECT COUNT(*) FROM Employees WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean chipCodeExists(String chipCode) {
        String sql = "SELECT COUNT(*) FROM Employees WHERE chipCode = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chipCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static int insertChip(String old) {
        String sql = "INSERT INTO Chip(old) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, old);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Chip inserted with ID: " + rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int insertEmployee(String name, String chipCode, String rank) {
        if (employeeExists(name)) {
            System.out.println("Employee with name " + name + " already exists.");
            return -1;
        }

        if (chipCodeExists(chipCode)) {
            System.out.println("Chip code " + chipCode + " already exists.");
            return -1;
        }

        String sql = "INSERT INTO Employees(name, chipCode, rank) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, chipCode);
            pstmt.setString(3, rank);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Employee inserted with ID: " + rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static void addChipToEmployee(int idEmployee, int idChip) {
        String sql = "UPDATE Employees SET idChip=? WHERE idPerson=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idChip);
            pstmt.setInt(2, idEmployee);
            pstmt.executeUpdate();
            System.out.println("Chip " + idChip + " assigned to employee " + idEmployee);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void executeSQL(String sql) {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readNotes() {
        String sql = "SELECT old FROM Chip";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String old = rs.getString("old");
                System.out.println("Stáří čipu je " + old);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertEmployeeWithChip(Employee employee) {
        int employeeId = insertEmployee(employee.getName(), employee.getChipCode(), employee.getRank());
        if (employeeId != -1) {
            int chipId = insertChip(employee.getChipDate());
            if (chipId != -1) {
                addChipToEmployee(employeeId, chipId);
                System.out.println("Zaměstnanec je přidán");
            } else {
                System.out.println("Chyba při vkládání čipu");
            }
        } else {
            System.out.println("Chyba při vkládání zaměstnance");
        }
    }

    public Employee getEmployeeByChipCode(String chipCode) {
        String sql = "SELECT * FROM Employees WHERE chipCode = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chipCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Zaměstnanec nalezen: " + rs.getString("name"));
                return new Employee(rs.getString("name"), rs.getString("idChip"), rs.getString("chipCode"), rs.getString("rank"));
            } else {
                System.out.println("Zaměstnanec s kódem čipu " + chipCode + " nenalezen.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Employee> getAllEmployees() {
        String sql = "SELECT * FROM Employees";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ArrayList<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                employees.add(new Employee(rs.getString("name"), rs.getString("idChip"), rs.getString("chipCode"), rs.getString("rank")));
            }
            return employees;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void printAllChips() {
        String sql = "SELECT * FROM Chip";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Chip Table:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idChip") + ", Old: " + rs.getString("old"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printEmployeeNamesAndRanks() {
        String sql = "SELECT name, rank FROM Employees";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Employees Table:");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") + ", Rank: " + rs.getString("rank"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean deleteEmployeeByName(String name) {
        String sql = "DELETE FROM Employees WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void logDoorAction(String employeeName, String action) {
        String sql = "INSERT INTO DoorLogs(employeeName, action) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeName);
            pstmt.setString(2, action);
            pstmt.executeUpdate();
            System.out.println("Logged action: " + action + " by " + employeeName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> getDoorLogs() {
        String sql = "SELECT DoorLogs.timestamp, DoorLogs.employeeName, DoorLogs.action, Employees.rank " +
                "FROM DoorLogs " +
                "JOIN Employees ON DoorLogs.employeeName = Employees.name";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ArrayList<String> logs = new ArrayList<>();
            while (rs.next()) {
                String log = "Timestamp: " + rs.getString("timestamp") +
                        ", Name: " + rs.getString("employeeName") +
                        ", Action: " + rs.getString("action") +
                        ", Rank: " + rs.getString("rank");
                logs.add(log);
            }
            return logs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

// tohle je na mě už vážne moc já to nezzládám prosím pomoc 5 hodin to tu nějak dělám a pořád nějaké errory udělám jednu část a potom na mě vyleze 5 nových erororů hmm to je fakt sranda ani tan chatgpt to necápe co má asi tak dělat proč tohle vůbec píšu já jsem asi vážně zoufalej proč proč proč
// jenom tohle vážne něco proč tohle je tak moc už na mě
// proč já jsem natom tak špatně já se asi na to
// konečně to už něco dělá asi tak po 50 hidinách jo ale pořád není vyhráno