import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
 
public class AdminPassengerController implements Initializable {
 
    ObservableList<User> userlist = FXCollections.observableArrayList();
 
    private FilteredList<User> filteredUsers;
 
    @FXML
    private TableView<User> mytable;
 
    @FXML
    private TableColumn<User, String> idcol;
 
    @FXML
    private TableColumn<User, String> usernamecol;
 
    @FXML
    private TableColumn<User, String> passwordcol;
 
    @FXML
    private TableColumn<User, String> fnamecol;
 
    @FXML
    private TableColumn<User, String> lnamecol;
 
    @FXML
    private TableColumn<User, String> emailcol;
 
    @FXML
    private TableColumn<User, String> phonenumbercol;
 
    @FXML
    private TextField searchField;
 
    @FXML
    private Button btncreate;
 
    @FXML
    private Button btndelete;
 
    @FXML
    private Button btnupdate;
 
    @FXML
    private Button logoutbtn;
 
    @FXML
    private TextField usernameTextfield;
 
    @FXML
    private PasswordField passPasswordfield;
 
    @FXML
    private TextField fnameTextfield;
 
    @FXML
    private TextField lnameTextfield;
 
    @FXML
    private TextField emailTextfield;
 
    @FXML
    private TextField numberTextfield;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCol();
        displayUsers();
        setupSearch();
    }
 
    private void initializeCol() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernamecol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordcol.setCellValueFactory(new PropertyValueFactory<>("password"));
        fnamecol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lnamecol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phonenumbercol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
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
 
    private void displayUsers() {
        userlist.clear();
       
        new Thread(() -> {
            ObservableList<User> tempUserList = FXCollections.observableArrayList();
            try (ResultSet result = DatabaseHandler.getUsers()) {
                if (result == null) return;
                while (result.next()) {
                    tempUserList.add(new User(
                        result.getString("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("phone_number")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                userlist.setAll(tempUserList);
                filteredUsers.setPredicate(p -> true);
            });
        }).start();
    }
 
    private void refreshTable() {
        userlist.clear();
        displayUsers();  
    }
 
    private void setupSearch() {
        filteredUsers = new FilteredList<>(userlist, p -> true);
        mytable.setItems(filteredUsers);
       
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getId().toLowerCase().contains(lowerCaseFilter) ||
                       user.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                       user.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                       user.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                       user.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                       user.getPhoneNumber().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
 
    @FXML
    private void createUser(ActionEvent event) {
    String username = usernameTextfield.getText().trim();
    String password = passPasswordfield.getText().trim();
    String firstName = fnameTextfield.getText().trim();
    String lastName = lnameTextfield.getText().trim();
    String email = emailTextfield.getText().trim();
    String phoneNumber = numberTextfield.getText().trim();
 
    if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
        showAlert(AlertType.ERROR, "All fields must be filled!");
        return;
    }
 
    if (!email.endsWith("@gmail.com")) {
        showAlert(AlertType.ERROR, "Email must end with @gmail.com!");
        return;
    }
 
    if (!phoneNumber.matches("^09\\d{9}$")) {
        showAlert(AlertType.ERROR, "Phone number must start with 09 and have exactly 11 digits!");
        return;
    }
 
    User user = new User("", firstName, lastName, username, password, email, phoneNumber);
 
    if (DatabaseHandler.addUser(user)) {
        showAlert(AlertType.INFORMATION, "Account Created Successfully!");
        refreshTable();
        clearFields();
    } else {
        showAlert(AlertType.ERROR, "Cannot create new user.");
    }
}
 
@FXML
private void updateUser(ActionEvent event) {
    User selectedUser = mytable.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
        showAlert(AlertType.ERROR, "Please select a user to update.");
        return;
    }
 
    String newUsername = usernameTextfield.getText().trim();
    String newPassword = passPasswordfield.getText().trim();
    String newFirstName = fnameTextfield.getText().trim();
    String newLastName = lnameTextfield.getText().trim();
    String newEmail = emailTextfield.getText().trim();
    String newPhoneNumber = numberTextfield.getText().trim();
 
    if (newUsername.isEmpty() || newPassword.isEmpty() || newFirstName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty() || newPhoneNumber.isEmpty()) {
        showAlert(AlertType.ERROR, "All fields must be filled!");
        return;
    }
 
    if (!newEmail.endsWith("@gmail.com")) {
        showAlert(AlertType.ERROR, "Email must end with @gmail.com!");
        return;
    }
 
    if (!newPhoneNumber.matches("^09\\d{9}$")) {
        showAlert(AlertType.ERROR, "Phone number must start with 09 and have exactly 11 digits!");
        return;
    }
 
    Alert confirmation = new Alert(AlertType.CONFIRMATION);
    confirmation.setTitle("Confirm Update");
    confirmation.setContentText("Are you sure you want to update this user?");
    Optional<ButtonType> result = confirmation.showAndWait();
 
    if (result.isPresent() && result.get() == ButtonType.OK) {
        User updatedUser = new User(selectedUser.getId(), newFirstName, newLastName, newUsername, newPassword, newEmail, newPhoneNumber);
 
        if (DatabaseHandler.updateUser(selectedUser.getId(), updatedUser)) {
            showAlert(AlertType.INFORMATION, "User updated successfully.");
            refreshTable();
            clearFields(); // Nililinis ang mga text field pagkatapos ng update
        } else {
            showAlert(AlertType.ERROR, "Failed to update user.");
        }
    }
}
 
@FXML
private void deleteUser(ActionEvent event) {
    User selectedUser = mytable.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
        showAlert(AlertType.ERROR, "Please select a user to delete.");
        return;
    }
 
    Alert confirmation = new Alert(AlertType.CONFIRMATION);
    confirmation.setTitle("Confirm Delete");
    confirmation.setContentText("Are you sure you want to delete user: " + selectedUser.getUsername() + "?");
    Optional<ButtonType> result = confirmation.showAndWait();
 
    if (result.isPresent() && result.get() == ButtonType.OK) {
        if (DatabaseHandler.deleteUser(selectedUser.getId())) {
            showAlert(AlertType.INFORMATION, "User deleted successfully.");
            refreshTable();
            clearFields(); // Nililinis ang mga text field pagkatapos ng delete
        } else {
            showAlert(AlertType.ERROR, "Failed to delete user.");
        }
    }
}
 
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
 
    private void clearFields() {
        usernameTextfield.clear();
        passPasswordfield.clear();
        fnameTextfield.clear();
        lnameTextfield.clear();
        emailTextfield.clear();
        numberTextfield.clear();
        }
    }
 