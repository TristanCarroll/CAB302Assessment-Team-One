package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.apache.commons.text.RandomStringGenerator;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

public class SettingsController {
    public Button logoutButton;

    public void onLogoutButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }

    public void onResetButtonClick(ActionEvent actionEvent) {
        // generate new temporary password
        String newTempPassword = generateTempPassword();
        System.out.println(newTempPassword);
        // get current user
        // User currentUser = userDAO.getUser();
    }

    public String generateTempPassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
        return generator.generate(12);
    }
}
