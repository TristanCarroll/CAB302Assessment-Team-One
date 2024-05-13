package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import static com.example.byebyeboxeyes.controller.SignUpController.passwordHash;
import com.example.byebyeboxeyes.model.UserDAO;

import com.example.byebyeboxeyes.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.apache.commons.text.RandomStringGenerator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;
import java.util.Properties;

public class ResetPasswordController {
    @FXML
    private StackPane mainStackPane;
    @FXML
    public TextField resetPwdUsername;
    @FXML
    public TextField resetPwdEmail;
    final String host = "byebyeboxeyes@gmail.com";
    final String username = "byebyeboxeyes@gmail.com";
    final String password = "bnxcszmktwaltrzu";
    private final UserDAO userDAO;

    private void setupListener() {
        StateManager.windowHeightProperty().addListener((obs, oldVal, newVal) -> {
            double paddingValue = newVal.doubleValue() * 0.60; // 60% of height
            mainStackPane.setPadding(new Insets(20, 20, paddingValue, 20));
        });
    }

    // session object
    Properties props = new Properties();
    public ResetPasswordController() {
        userDAO = UserDAO.getInstance();
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
    }
    Session session = Session.getDefaultInstance(props,
        new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        }
    );

    public void onSendEmailClick(ActionEvent actionEvent) {
        // generate new temporary password
        String newTempPassword = generateTempPassword();
        // attempt to send email with new temporary password
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(resetPwdEmail.getText()));
            message.setSubject(" NEW TEMPORARY PASSWORD ");
            //Text in email
            String emailMessage = String.format("Hello, %s. Here is your temporary password: \n %s", resetPwdUsername.getText(), newTempPassword);
            System.out.println(emailMessage);
            message.setText(emailMessage);
            //send the message
            Transport.send(message);

            System.out.println("message sent successfully via mail ... !!! ");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText("Email Has Been Sent");
            alert.setContentText("An email containing a temporary password for you to log in with has been sent.");
            alert.showAndWait();
            String userEmail = resetPwdEmail.getText();
            userDAO.updateUserPassword(passwordHash(newTempPassword), userEmail);
            System.out.println("password updated.");
        } catch (MessagingException e) {e.printStackTrace();}
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
