module com.example.prabhjot_hydro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.prabhjot_hydro to javafx.fxml;
    exports com.example.prabhjot_hydro;
}