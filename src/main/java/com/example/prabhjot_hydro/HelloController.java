package com.example.prabhjot_hydro;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class HelloController {

    @FXML
    private TextField tfAccountNumber;
    @FXML
    private TextField tfHydroUnits;
    @FXML
    private RadioButton rbSummer;
    @FXML
    private RadioButton rbWinter;
    @FXML
    private RadioButton rbFall;
    @FXML
    private Label lblEstimate;
    @FXML
    private TableView<HydroRecord> tableHydroData;
    @FXML
    private TableColumn<HydroRecord, String> colAccountNumber;
    @FXML
    private TableColumn<HydroRecord, Integer> colHydroUnits;
    @FXML
    private TableColumn<HydroRecord, String> colSeason;
    @FXML
    private TableColumn<HydroRecord, Double> colEstimate;

    @FXML
    private ToggleGroup seasonToggleGroup;


    private Connection conn;
    private final String jdbcUrl = "jdbc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD";
    private final String username = "COMP228_F23_jp_23";
    private final String password = "Prabhjot7739#";

    private ObservableList<HydroRecord> hydroData = FXCollections.observableArrayList();

    public void initialize() {
        connectToDb();
        setupTableColumns();
        loadHydroData();
    }

    private void connectToDb() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            try {

                conn = DriverManager.getConnection(jdbcUrl, username, password);
            } catch (SQLException primaryEx) {
                System.out.println("Primary database connection failed. Trying alternate database...");

                String alternateJdbcUrl = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";
                conn = DriverManager.getConnection(alternateJdbcUrl, username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupTableColumns() {
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colHydroUnits.setCellValueFactory(new PropertyValueFactory<>("hydroUnits"));
        colSeason.setCellValueFactory(new PropertyValueFactory<>("season"));
        colEstimate.setCellValueFactory(new PropertyValueFactory<>("estimate"));
        tableHydroData.setItems(hydroData);
    }

    private void loadHydroData() {
        String sql = "SELECT account_number, hydro_units, season, estimate FROM Prabhjot_Hydro";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            hydroData.clear();
            while (rs.next()) {
                String accountNumber = rs.getString("account_number");
                int hydroUnits = rs.getInt("hydro_units");
                String season = rs.getString("season");
                double estimate = rs.getDouble("estimate");
                HydroRecord record = new HydroRecord(accountNumber, hydroUnits, season, estimate);
                hydroData.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGetEstimateClick() {
        try {
            String accountNumber = tfAccountNumber.getText();
            int hydroUnits = Integer.parseInt(tfHydroUnits.getText());
            String season = getSelectedSeason();
            double estimate = calculateEstimate(hydroUnits, season);
            lblEstimate.setText("Estimated Bill: $" + estimate);
            saveHydroData(accountNumber, hydroUnits, season, estimate);
            loadHydroData();
        } catch (NumberFormatException e) {
            lblEstimate.setText("Invalid input");
        } catch (SQLException e) {
            lblEstimate.setText("Database error");
        }
    }

    private String getSelectedSeason() {
        if (rbSummer.isSelected()) return "Summer";
        if (rbWinter.isSelected()) return "Winter";
        if (rbFall.isSelected()) return "Fall";
        return null;
    }

    private double calculateEstimate(int units, String season) {

        switch (season) {
            case "Summer": return units * 0.50;
            case "Winter": return units * 0.70;
            case "Fall":   return units * 0.30;
            default:       return 0;
        }
    }

    private void saveHydroData(String accountNumber, int hydroUnits, String season, double estimate) throws SQLException {
        String sql = "INSERT INTO Prabhjot_Hydro (account_number, hydro_units, season, estimate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setInt(2, hydroUnits);
            pstmt.setString(3, season);
            pstmt.setDouble(4, estimate);
            pstmt.executeUpdate();
        }
    }


}

