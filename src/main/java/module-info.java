module com.example.byebyeboxeyes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.byebyeboxeyes to javafx.fxml;
    exports com.example.byebyeboxeyes;
    exports com.example.byebyeboxeyes.controller;
    opens com.example.byebyeboxeyes.controller to javafx.fxml;
}