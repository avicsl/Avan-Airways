import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
 
public class SignupController {
 
    @FXML
    private Button adminloginbtn, backbtn, createAccountBtn;
 
    @FXML
    private TextField emailTextfield, fnameTextfield, lnameTextfield, usernameTextfield1, numberTextfield2;
 
    @FXML
    private PasswordField passPasswordfield;
 
    @FXML
    private void backButtonHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
 
    @FXML
    private void createAccount(ActionEvent event) {
        if (!validateInputs()) return;
 
        // Check for duplicate username or email
        if (DatabaseHandler.isUsernameExists(usernameTextfield1.getText())) {
            showAlert(Alert.AlertType.ERROR, "Username already exists. Please choose a different username.");
            return;
        }
 
        if (DatabaseHandler.isEmailExists(emailTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, "Email already exists. Please use a different email.");
            return;
        }
 
        // Create new user if no duplicates
        User newUser = new User("", fnameTextfield.getText(), lnameTextfield.getText(), usernameTextfield1.getText(), passPasswordfield.getText(), emailTextfield.getText(), numberTextfield2.getText());
 
        if (DatabaseHandler.addUser(newUser)) {
            showAlert(Alert.AlertType.INFORMATION, "Account Created Successfully!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to create account.");
        }
    }
 
    private boolean validateInputs() {
        if (usernameTextfield1.getText().isEmpty() || passPasswordfield.getText().isEmpty() || fnameTextfield.getText().isEmpty() || lnameTextfield.getText().isEmpty() || emailTextfield.getText().isEmpty() || numberTextfield2.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields must be filled!");
            return false;
        }
        if (!emailTextfield.getText().endsWith("@gmail.com")) {
            showAlert(Alert.AlertType.ERROR, "Email must end with @gmail.com!");
            return false;
        }
        if (!numberTextfield2.getText().matches("^09\\d{9}$")) {
            showAlert(Alert.AlertType.ERROR, "Phone number must start with 09 and have exactly 11 digits!");
            return false;
        }
        return true;
    }
 
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
 
    private void clearFields() {
        usernameTextfield1.clear();
        passPasswordfield.clear();
        fnameTextfield.clear();
        lnameTextfield.clear();
        emailTextfield.clear();
        numberTextfield2.clear();
    }
}