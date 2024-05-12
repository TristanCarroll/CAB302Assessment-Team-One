package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.text.RandomStringGenerator;
import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

public class ResetPasswordController {
    @FXML
    public TextField resetPwdUsername;
    @FXML
    public TextField resetPwdEmail;

    public void onSendEmailClick(ActionEvent actionEvent) {
        // generate new temporary password
        String newTempPassword = generateTempPassword();
        System.out.println(newTempPassword);
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }

    public String generateTempPassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
        return generator.generate(12);
    }
}
