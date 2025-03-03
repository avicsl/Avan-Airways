import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
 
public class LoginController {
 
    @FXML
    private Label usernameLabel;
 
    @FXML
    private Label passwordLabel;
 
    @FXML
    private TextField usernameTextfield;
 
    @FXML
    private PasswordField passwordPasswordfield;
 
    @FXML
    private Button loginButton;
 
    @FXML
    private Button adminbtn;
 
    private Stage stage;
    private Scene scene;
    private Parent root;
 
    @FXML
    public void loginbuttonHandler(ActionEvent event) throws IOException {
        String uname = usernameTextfield.getText().trim();
        String pword = passwordPasswordfield.getText().trim();
 
        if (uname.isEmpty() || pword.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Required");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please enter both username and password.");
            alert.showAndWait();
            return;
        }
 
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
 
        if (dbHandler.validateLogin(uname, pword)) {
            // Set the logged-in username
            UserHomeController.setLoggedInUsername(uname);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user.fxml"));
            root = loader.load();
            UserController UserController = loader.getController();
            UserController.displayName(uname);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Please check your username and password.");
            alert.showAndWait();
        }
    }
 
     @FXML
     public void signupButtonHandler(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
         root = loader.load();
 
         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
     }
 
    @FXML
    public void adminButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("validation.fxml"));
        root = loader.load();
 
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
 