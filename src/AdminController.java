import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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

public class AdminController implements Initializable {

    @FXML
    private Label homelabel;
    
    @FXML
    private Button logoutbtn, passenger, booking, flight, payment, seat;

    @FXML
    private BorderPane borderpane;

    @FXML
    private AnchorPane admin;

    @FXML
    private TextField usernameTextfield; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource("adminpassenger.fxml"));
            borderpane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Parent root = FXMLLoader.load(getClass().getResource("validation.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
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
    private void passenger(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("adminpassenger.fxml"));
        borderpane.setCenter(view);
    }
 
    @FXML
    private void booking(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("adminbooking.fxml"));
        borderpane.setCenter(view);
    }
   
    @FXML
    private void flight(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("adminflight.fxml"));
        borderpane.setCenter(view);
    }
 
    @FXML
    private void payment(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("adminpayment.fxml"));
        borderpane.setCenter(view);
    }
 
    @FXML
    private void seat(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("adminseat.fxml"));
        borderpane.setCenter(view);
    }
}
 
