module com.example.byebyeboxeyes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jpackage;
    requires org.apache.commons.text;

    opens com.example.byebyeboxeyes.model;
    opens com.example.byebyeboxeyes to javafx.fxml;
    exports com.example.byebyeboxeyes;
    exports com.example.byebyeboxeyes.timer;
    opens com.example.byebyeboxeyes.timer to javafx.fxml;
    exports com.example.byebyeboxeyes.model;
    exports com.example.byebyeboxeyes.controller;
    opens com.example.byebyeboxeyes.controller to javafx.fxml;

}