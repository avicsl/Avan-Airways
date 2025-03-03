import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
 
public class UserController implements Initializable {
 
    @FXML
    private TextField IDTextfield;
 
    @FXML
    private TextField IDTextfield1;
 
    @FXML
    private Button backbtn;
 
    @FXML
    private Label emailLabel1;
 
    @FXML
    private Label fnameLabel1;
 
    @FXML
    private Label fnameLabel11;
 
    @FXML
    private Label fnameLabel2;
 
    @FXML
    private TextField fnameTextfield1;
 
    @FXML
    private Label lnameLabel1;
 
    @FXML
    private Label nameLabel1;
 
    @FXML
    private Label nameLabel11;
 
    @FXML
    private Label nameLabel12;
 
    @FXML
    private Label numberLabel1;
 
    @FXML
    private Label numberLabel11;
 
    @FXML
    private Label numberLabel12;
 
    @FXML
    private Label homelabel;
 
    @FXML
    private Button logoutbtn, about, booking, explore, flight, hommee;
 
    @FXML
    private BorderPane borderpane;
 
    @FXML
    private BorderPane bp;
 
    @FXML
    private BorderPane borderp;
 
    @FXML
    private AnchorPane user;
 
    @FXML
    private TextField usernameTextfield;
 
    private String username;
 
    @Override
public void initialize(URL url, ResourceBundle rb) {
    if (borderpane == null) {
    } else {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource("userhome.fxml"));
            borderpane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    public void setUsername(String username) {
        this.username = username;
        displayName(username);
    }
 
    public void displayName(String name) {
        homelabel.setText("Welcome, " + name + "!");
    }
 
    @FXML
    void handleLogin(ActionEvent event) {
        String enteredUsername = usernameTextfield.getText();
        if (!enteredUsername.isEmpty()) {
            displayName(enteredUsername);
        } else {
            homelabel.setText("Please enter your username!");
        }
    }
 
    @FXML
    private void logoutButtonHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
 
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
 
        if (result.isPresent() && result.get() == yesButton) {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
 
    @FXML
    private void backButtonHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("userflight.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
 
    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            AnchorPane view = loader.load();
            UserController controller = loader.getController();
            if (controller != null) {
                controller.displayName(username);
            }
            borderpane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void home(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("userhome.fxml"));
        borderpane.setCenter(view);
    }
 
 
    @FXML
    private void booking(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("userbooking.fxml"));
        borderpane.setCenter(view);
    }
   
    @FXML
    private void flight(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("userflight.fxml"));
        borderpane.setCenter(view);
    }
 
    @FXML
    private void explore(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("userexplore.fxml"));
        borderpane.setCenter(view);
    }
 
    @FXML
    private void about(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("userabout.fxml"));
        borderpane.setCenter(view);
    }
    
}
 
