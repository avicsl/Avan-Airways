import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
 
public class AdminBookingController implements Initializable {
 
    private final ObservableList<Booking> bookingList = FXCollections.observableArrayList();
 
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingid;
    @FXML private TableColumn<Booking, String> passengerid;
    @FXML private TableColumn<Booking, String> flightid;
    @FXML private TableColumn<Booking, String> seatid;
    @FXML private TableColumn<Booking, String> bookingdate;
    @FXML private TableColumn<Booking, String> amount;
    @FXML private TextField amounttextfield;
    @FXML private TextField searchField;
    @FXML private Button btnupdate;
    @FXML private Button btndelete;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTable();
        loadBookingData();
        setButtonActions();
        setUpSearchField();
    }
 
    private void setUpSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBookingList(newValue));
    }
   
    private void filterBookingList(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            bookingTable.setItems(bookingList);
            return;
        }
 
        ObservableList<Booking> filteredList = FXCollections.observableArrayList();
 
        for (Booking booking : bookingList) {
            if (booking.getBookingid().toLowerCase().contains(searchText.toLowerCase()) ||
                booking.getPassengerid().toLowerCase().contains(searchText.toLowerCase()) ||
                booking.getFlightid().toLowerCase().contains(searchText.toLowerCase()) ||
                booking.getSeatid().toLowerCase().contains(searchText.toLowerCase()) ||
                booking.getBookingdate().toLowerCase().contains(searchText.toLowerCase()) ||
                booking.getAmount().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(booking);
            }
        }
 
        bookingTable.setItems(filteredList);
    }
 
    private void setUpTable() {
        bookingid.setCellValueFactory(new PropertyValueFactory<>("bookingid"));
        passengerid.setCellValueFactory(new PropertyValueFactory<>("passengerid"));
        flightid.setCellValueFactory(new PropertyValueFactory<>("flightid"));
        seatid.setCellValueFactory(new PropertyValueFactory<>("seatid"));
        bookingdate.setCellValueFactory(new PropertyValueFactory<>("bookingdate"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
 
        bookingTable.setItems(bookingList);
    }
 
    private void loadBookingData() {
        new Thread(() -> {
            ObservableList<Booking> tempBookingList = FXCollections.observableArrayList();
 
            try (ResultSet rs = DatabaseHandler.getBooking()) {
                if (rs == null) {
                    System.out.println("No data retrieved from the database.");
                    return;
                }
                while (rs.next()) {
                    tempBookingList.add(new Booking(
                            rs.getString("Booking_ID"),
                            rs.getString("id"),
                            rs.getString("Flight_ID"),
                            rs.getString("Seat_ID"),
                            rs.getString("Amount"),
                            rs.getString("Booking_Date")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
 
            Platform.runLater(() -> {
                bookingList.setAll(tempBookingList);
                bookingTable.refresh();
            });
 
        }).start();
    }
 
    private void setButtonActions() {
        btnupdate.setOnAction(event -> updateBooking());
        btndelete.setOnAction(event -> deleteBooking());
    }
 
    private void updateBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Error", "Please select a booking to update.");
            return;
        }
   
        String newAmountStr = amounttextfield.getText().trim();
        if (newAmountStr.isEmpty()) {
            showAlert("Error", "Amount field cannot be empty.");
            return;
        }
   
        try {
            BigDecimal newAmount = new BigDecimal(newAmountStr);
   
            boolean success = DatabaseHandler.updateBooking(
                    selectedBooking.getBookingid(),
                    selectedBooking.getFlightid(),
                    selectedBooking.getSeatid(),
                    newAmount
            );
   
            if (success) {
                showAlert("Success", "Booking amount updated successfully.");
                amounttextfield.clear(); // Clear the amount field
               
                // Reload the data and filter again based on current search query
                loadBookingData();
                filterBookingList(searchField.getText().trim());
   
            } else {
                showAlert("Error", "Failed to update booking.");
            }
   
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount format. Please enter a valid number.");
        }
    }
 
    private void deleteBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Error", "Please select a booking to delete.");
            return;
        }
 
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this booking?", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = DatabaseHandler.deleteBooking(selectedBooking.getBookingid());
                if (success) {
                    showAlert("Success", "Booking deleted successfully.");
                    searchField.clear();  // Clear search field
                    amounttextfield.clear(); // Clear amount text field
                    loadBookingData(); // Refresh table after deletion
                } else {
                    showAlert("Error", "Failed to delete booking.");
                }
            }
        });
    }
 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
 
 