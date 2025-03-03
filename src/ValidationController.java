import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ValidationController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordField; 
    @FXML
    private Button adminloginbtn;

    @FXML
    private void adminloginbtnHandler(ActionEvent event) throws IOException {
        // Get user input
        String enteredUsername = usernameTextfield.getText();
        String enteredPassword = passwordField.getText();

        // Validate credentials
        if ("admin".equals(enteredUsername) && "1234".equals(enteredPassword)) {
            // Load home.fxml if credentials are correct
            Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            // Show alert if credentials are incorrect
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("The username or password is incorrect. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void backButtonHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
