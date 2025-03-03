import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserHomeController implements Initializable {

    @FXML
    private TextField IDTextfield;
 
    @FXML
    private TextField fnameTextfield;
 
    @FXML
    private TextField lnameTextfield;
 
    @FXML
    private TextField emailTextfield;
 
    @FXML
    private TextField numberTextfield;
 
    @FXML
    private TextField usernameTextfield;
 
    @FXML
    private PasswordField passPasswordfield;
 
    @FXML
    private Label timelabel;

    @FXML
    private Button updateButton;
 
    @FXML
    private Label datelabel;
 
    private static String loggedInUsername;
    private String originalEmail;
    private String originalPhoneNumber;
    private String originalUsername;

 
    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    private String getPhoneNumber() {
        return numberTextfield.getText();
    }
    
    private String getEmail() {
        return emailTextfield.getText();
    }

    private String getUsername() {
        return usernameTextfield.getText();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IDTextfield.setEditable(false);
        fnameTextfield.setEditable(false);
        lnameTextfield.setEditable(false);
        emailTextfield.setEditable(false);
        numberTextfield.setEditable(false);
        usernameTextfield.setEditable(false);
        passPasswordfield.setEditable(false);
        
        startDateTimeUpdater();
 
        if (loggedInUsername != null && !loggedInUsername.isEmpty()) {
            loadUserData(loggedInUsername);
        } else {
            System.out.println("No logged-in user found.");
        }
    }
 
    private void startDateTimeUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                String formattedTime = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                String formattedDate = now.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
 
                javafx.application.Platform.runLater(() -> {
                    timelabel.setText(formattedTime);
                    datelabel.setText(formattedDate);
                });
            }
        }, 0, 1000);
    }
 
    private void loadUserData(String username) {
        String query = "SELECT * FROM admin WHERE username = ?";
 
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
 
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IDTextfield.setText(rs.getString("id"));
                    fnameTextfield.setText(rs.getString("first_name"));
                    lnameTextfield.setText(rs.getString("last_name"));
                    emailTextfield.setText(rs.getString("email"));
                    numberTextfield.setText(rs.getString("phone_number"));
                    usernameTextfield.setText(rs.getString("username"));
                    passPasswordfield.setText(rs.getString("password"));
                    
                    // Store original values for validation during update
                    originalEmail = rs.getString("email");
                    originalPhoneNumber = rs.getString("phone_number");
                } else {
                    System.out.println("No data found for user: " + username);
                }
            }
 
        } catch (SQLException e) {
            System.out.println("Error loading user data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        boolean isEditable = !fnameTextfield.isEditable();
        
        fnameTextfield.setEditable(isEditable);
        lnameTextfield.setEditable(isEditable);
        emailTextfield.setEditable(isEditable);
        numberTextfield.setEditable(isEditable);
        usernameTextfield.setEditable(isEditable);
        passPasswordfield.setEditable(isEditable);

        if (!isEditable) { 
            updateUserData();
        }
        updateButton.setText(isEditable ? "Save" : "Update");
    }
    
    private void updateUserData() {
        String currentPhoneNumber = getPhoneNumber();
        String currentEmail = getEmail();
        String currentUsername =getUsername();


        // Validate phone number format (must start with '09' and be 11 digits long)
        if (!currentPhoneNumber.matches("^09\\d{9}$")) {
            showAlert("Error", "Invalid Phone Number", "Phone number must start with '09' and be exactly 11 digits.", AlertType.ERROR);
            return;
        }
    
        // Validate email format (must end with '@gmail.com')
        if (!currentEmail.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            showAlert("Error", "Invalid Email", "Email must end with '@gmail.com'.", AlertType.ERROR);
            return;
        }
    
        // Check for duplicate phone number if it has changed
        if (!currentPhoneNumber.equals(originalPhoneNumber) && isPhoneNumberExists(currentPhoneNumber)) {
            showAlert("Error", "Duplicate Entry", "Phone number already exists!", AlertType.ERROR);
            return;
        }
    
        // Check for duplicate email if it has changed
        if (!currentEmail.equals(originalEmail) && isEmailExists(currentEmail)) {
            showAlert("Error", "Duplicate Entry", "Email already exists!", AlertType.ERROR);
            return;
        }

        if (!currentUsername.equals(originalUsername) && isUsernameExists(currentUsername)) {
            showAlert("Error", "Duplicate Entry", "Username already exists!", AlertType.ERROR);
            return;
        }
    
        String query = "UPDATE admin SET first_name = ?, last_name = ?, email = ?, phone_number = ?, username = ?, password = ? WHERE id = ?";
    
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, fnameTextfield.getText());
            stmt.setString(2, lnameTextfield.getText());
            stmt.setString(3, currentEmail);
            stmt.setString(4, currentPhoneNumber);
            stmt.setString(5, usernameTextfield.getText());
            stmt.setString(6, passPasswordfield.getText());
            stmt.setString(7, IDTextfield.getText());
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                originalEmail = currentEmail;
                originalPhoneNumber = currentPhoneNumber;
                originalUsername = currentUsername;
    
                showAlert("Fly Forward, Fly Avan!", "Details Updated", "Your details have been successfully updated!", AlertType.INFORMATION);
            } else {
                showAlert("Fly Forward, Fly Avan!", "Update Failed", "Something went wrong. Please try again.", AlertType.ERROR);
            }
    
        } catch (SQLException e) {
            System.out.println("Error updating user data: " + e.getMessage());
            showAlert("Database Error", "Update Failed", "An error occurred while updating your details.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public static boolean isPhoneNumberExists(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM admin WHERE phone_number = ?";
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
  
    public static boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM admin WHERE email = ?";
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM admin WHERE username = ?";
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}