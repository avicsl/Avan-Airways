import java.io.IOException;
import java.math.BigDecimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;



public class UserFlight3Controller implements Initializable {

    @FXML
    private AnchorPane anchorpane; 

    @FXML
    private ComboBox<String> payment;

    @FXML
    private ComboBox<String> seatclass;

    @FXML
    private TextField amountField, flightid; 

    private int baseAmount = 0; 

    private int totalPassengers = 0;

    public void setAmountField(int amount) {
        this.baseAmount = amount;  
        updateAmount(); 
    }

    public void setPassengerCount(int adults, int children, int infants) {
        this.totalPassengers = adults + children + infants;
    }

    
    @Override
public void initialize(URL location, ResourceBundle resources) {
    payment.getItems().addAll("PM1 - American Express", "PM2 - GCash", "PM3 - Maya", 
                              "PM4 - PayPal", "PM5 - Visa MasterCard", "PM6 - JCB");
    seatclass.getItems().addAll("Economy Class", "Business Class", "First Class");

    seatclass.setOnAction(event -> updateAmount());
}

private void updateAmount() {
    if (seatclass.getValue() != null) {
        int finalAmount = baseAmount * totalPassengers; // Adjust based on passenger count

        switch (seatclass.getValue()) {
            case "Business Class":
                finalAmount *= 3;
                break;
            case "First Class":
                finalAmount *= 5;
                break;
            default: // Economy Class
                break;
        }

        amountField.setText(String.valueOf(finalAmount));
    }
}


    private boolean seatSelected = false;  
    private boolean bookingCompleted = false;  
    
    @FXML
private void seat(ActionEvent event) {
    String seatClass = seatclass.getValue();

    if (seatClass == null || totalPassengers == 0) {
        showAlert(AlertType.WARNING, "Fly Forward, Fly Avan!", "Please select a seat class.");
        return;
    }

    try {
        String flightId = DatabaseHandler.getLastFlightId();
       
        if (flightId == null || flightId.isEmpty()) {
            System.out.println("No flight records found.");
            return;
        }

        for (int i = 0; i < totalPassengers; i++) {
            boolean seatInserted = DatabaseHandler.insertSeat(flightId, seatClass);
           
            if (!seatInserted) {
                // Show an alert with choices if seat class is full
                Optional<ButtonType> result = showConfirmation("Fly Forward, Fly Avan!", 
                    "No more seats available in " + seatClass + ". Would you like to select a different class?");
               
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    seatclass.setValue(null);  // Reset seat class selection
                    return;
                } else {
                    showAlert(AlertType.INFORMATION, "Fly Forward, Fly Avan!", "Seat selection cancelled.");
                    return;
                }
            }
        }

        seatSelected = true;  // Mark seat selection as completed
        showAlert(AlertType.INFORMATION, "Fly Forward, Fly Avan!", "Seat selections saved successfully!");

    } catch (Exception e) {
        showAlert(AlertType.ERROR, "Error", "An error occurred while saving seat selections.");
        e.printStackTrace();
    }
}

    @FXML
    private void book(ActionEvent event) {
        String amountText = amountField.getText().trim(); 
    
        try {
            if (amountText.isEmpty()) {
                showAlert(AlertType.WARNING, "Fly Forward, Fly Avan!", "Please choose your seat class first.");
                return;
            }
    
            BigDecimal amount = new BigDecimal(amountText); 
    
            String flightId = DatabaseHandler.getLastFlightId();
            if (flightId == null) {
                System.out.println("No flight records found.");
                return;
            }
    
            String id = DatabaseHandler.getLoggedInUserId(); 
            if (id == null || id.isEmpty()) {
                System.out.println("User is not logged in.");
                return;
            }
    
            List<String> seatIds = DatabaseHandler.getSeatIdsForLastFlight();
            if (seatIds.isEmpty()) {
                System.out.println("No seat records found for the last flight.");
                return;
            }
    
            for (String seatId : seatIds) {
                DatabaseHandler.insertBooking(id, flightId, seatId, amount);
            }
    
            bookingCompleted = true;  
            showAlert(AlertType.INFORMATION, "Fly Forward, Fly Avan!", "Make sure that you really understand the note.");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "An error occurred while saving seat selections.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void finalpayment(ActionEvent event) {
        if (!seatSelected || !bookingCompleted) {
            showAlert(AlertType.WARNING, "Incomplete Process", "Please select seats and make that you marked understood.");
            return;
        }
        String Payment = payment.getValue();
    
        if (Payment == null) {
            showAlert(AlertType.WARNING, "Invalid Input", "Please select a payment method.");
            return;
        }
        try {
            String booking = DatabaseHandler.getLastBookingID();
            
            if (booking == null || booking.isEmpty()) {
                showAlert(AlertType.ERROR, "No Records", "No booking records found.");
                return;
            }
            DatabaseHandler.insertPayment(booking, Payment);
            showAlert(AlertType.INFORMATION, "Fly Forward, Fly Avan!", "You have successfully booked at Avan Airlines!");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight.fxml"));
            AnchorPane view = loader.load();
            BorderPane parentBorderPane = (BorderPane) ((Node) event.getSource()).getScene().lookup("#borderpane");
            if (parentBorderPane != null) {
                parentBorderPane.setCenter(view);
            } else {
                System.out.println("Error: Could not find the BorderPane with ID 'borderpane'");
            }
    
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Fly Forward, Fly Avan!", "An error occurred while loading the page.");
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Fly Forward, Fly Avan!", "An error occurred while processing your payment.");
            e.printStackTrace();
        }
    }
    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
    
}    

