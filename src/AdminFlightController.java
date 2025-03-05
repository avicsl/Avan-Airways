import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
 
public class AdminFlightController implements Initializable {
 
    private ObservableList<Flight> flightList = FXCollections.observableArrayList();
    private FilteredList<Flight> filteredFlights;
 
    @FXML private TableView<Flight> flightTable;
    @FXML private TableColumn<Flight, String> flightId;
    @FXML private TableColumn<Flight, String> passengerId;
    @FXML private TableColumn<Flight, String> departureLocation;
    @FXML private TableColumn<Flight, String> arrivalLocation;
    @FXML private TableColumn<Flight, String> departureTime;
    @FXML private TableColumn<Flight, String> arrivalTime;
    @FXML private TableColumn<Flight, String> departureDate;
    @FXML private TableColumn<Flight, String> roundtrip;
    @FXML private TableColumn<Flight, String> returnDate;
    @FXML private TextField searchField;
    @FXML private Button btnupdate;
    @FXML private Button btndelete;
    @FXML private ComboBox<String> arrivallocationcombobox;
    @FXML private TextField departuredatetextfield;
    @FXML private ComboBox<String> departuretimecombobox;
    @FXML private TextField returndatetextfield;
    @FXML private TextField roundtriptextfield;
 
    private Map<String, Integer> flightDurations = new HashMap<>();
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTable();
        loadFlightData();
        setUpSearch();
        setupFlightDurations();
 
        departuretimecombobox.getItems().addAll("07:00 AM", "01:00 PM", "08:00 PM");
        arrivallocationcombobox.getItems().addAll(flightDurations.keySet());
 
        departuretimecombobox.setOnAction(e -> updateArrivalTime());
        arrivallocationcombobox.setOnAction(e -> updateArrivalTime());
 
        roundtriptextfield.textProperty().addListener((observable, oldValue, newValue) -> handleRoundTrip(newValue));
 
        btnupdate.setOnAction(e -> updateFlight());
    }
 
    private void updateFlight() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showErrorAlert("❌ No flight selected!");
            return;
        }
 
        String newArrivalLocation = arrivallocationcombobox.getValue();
        String newDepartureDate = departuredatetextfield.getText();
        String newDepartureTime = departuretimecombobox.getValue();
        String newArrivalTime = computeArrivalTime(newDepartureTime, newArrivalLocation);
        String newRoundTrip = roundtriptextfield.getText();
        String newReturnDate = returndatetextfield.getText();
 
        // Validate departure date
        if (!validateDate(newDepartureDate, "Departure Date")) {
            return;
        }
 
        // Validate return date if roundtrip is "yes"
        if (newRoundTrip.equalsIgnoreCase("yes") && !validateDate(newReturnDate, "Return Date")) {
            return;
        }
 
        // Validate return date is not earlier than departure date
        if (newRoundTrip.equalsIgnoreCase("yes") && !validateReturnDate(newDepartureDate, newReturnDate)) {
            return;
        }
 
        try (Connection connection = DatabaseHandler.getDBConnection()) {
            String query = "UPDATE flight SET arrival_location = ?, departure_date = ?, departure_time = ?, " +
                           "arrival_time = ?, roundtrip = ?, return_date = ? WHERE flight_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newArrivalLocation);
            ps.setString(2, newDepartureDate);
            ps.setString(3, newDepartureTime);
            ps.setString(4, newArrivalTime);
            ps.setString(5, newRoundTrip);
            ps.setString(6, newRoundTrip.equalsIgnoreCase("yes") ? newReturnDate : null);
            ps.setString(7, selectedFlight.getFlightId());
 
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                loadFlightData(); // Refresh the table
                clearFields(); // Clear input fields
                showUpdateAlert(); // Show alert message
            } else {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    private void clearFields() {
        arrivallocationcombobox.setValue(null);
        departuredatetextfield.clear();
        departuretimecombobox.setValue(null);
        returndatetextfield.clear();
        roundtriptextfield.clear();
    }
 
    private void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Flight Update");
        alert.setHeaderText(null);
        alert.setContentText("✅ The selected flight has been updated successfully!");
        alert.showAndWait();
    }
 
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
 
    private void setUpTable() {
        flightId.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        passengerId.setCellValueFactory(new PropertyValueFactory<>("passengerId"));
        departureLocation.setCellValueFactory(new PropertyValueFactory<>("departureLocation"));
        arrivalLocation.setCellValueFactory(new PropertyValueFactory<>("arrivalLocation"));
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        departureDate.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        roundtrip.setCellValueFactory(new PropertyValueFactory<>("roundtrip"));
        returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
 
        filteredFlights = new FilteredList<>(flightList, p -> true);
        SortedList<Flight> sortedFlights = new SortedList<>(filteredFlights);
        sortedFlights.comparatorProperty().bind(flightTable.comparatorProperty());
 
        flightTable.setItems(sortedFlights);
    }
 
    private void loadFlightData() {
        flightList.clear();
        ResultSet rs = DatabaseHandler.getFlightBookings();
 
        if (rs == null) {
            return;
        }
 
        try {
            while (rs.next()) {
                flightList.add(new Flight(
                        rs.getString("flight_id"),
                        rs.getString("id"),
                        rs.getString("departure_location"),
                        rs.getString("arrival_location"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getString("departure_date"),
                        rs.getString("roundtrip"),
                        rs.getString("return_date") == null ? "N/A" : rs.getString("return_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    private void setUpSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFlights.setPredicate(flight -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
 
                String lowerCaseFilter = newValue.toLowerCase();
                return flight.getFlightId().toLowerCase().contains(lowerCaseFilter) ||
                       flight.getPassengerId().toLowerCase().contains(lowerCaseFilter) ||
                       flight.getDepartureLocation().toLowerCase().contains(lowerCaseFilter) ||
                       flight.getArrivalLocation().toLowerCase().contains(lowerCaseFilter) ||
                       flight.getDepartureDate().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
 
    private void setupFlightDurations() {
        flightDurations.put("Cebu - CEB", 90);
        flightDurations.put("Puerto Princesa - PPS", 80);
        flightDurations.put("Boracay - BCY", 60);
        flightDurations.put("Bohol - BHL", 90);
        flightDurations.put("Davao - DVO", 120);
        flightDurations.put("Batanes - BAT", 105);
        flightDurations.put("Singapore - SIN", 225);
        flightDurations.put("Tokyo - NRT", 270);
        flightDurations.put("Seoul - ICN", 240);
        flightDurations.put("Dubai - DXB", 570);
        flightDurations.put("Sydney - SYD", 510);
        flightDurations.put("Zurich - ZRH", 840);
    }
 
    private void updateArrivalTime() {
        String departureTime = departuretimecombobox.getValue();
        String arrivalLocation = arrivallocationcombobox.getValue();
 
        if (departureTime == null || arrivalLocation == null) {
            return;
        }
 
        String arrivalTime = computeArrivalTime(departureTime, arrivalLocation);
        System.out.println("✅ Computed arrival time: " + arrivalTime);
    }
 
    private String computeArrivalTime(String departureTime, String arrivalLocation) {
        Integer duration = flightDurations.get(arrivalLocation);
        if (duration == null) {
            return "N/A";
        }
 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime departureLocalTime = LocalTime.parse(departureTime, formatter);
        LocalTime arrivalLocalTime = departureLocalTime.plusMinutes(duration);
        return arrivalLocalTime.format(formatter);
    }
 
    private void handleRoundTrip(String value) {
        if (returndatetextfield == null) {
            System.out.println("❌ returndatetextfield is null!");
            return;
        }
 
        if (value.equalsIgnoreCase("yes")) {
            returndatetextfield.setDisable(false);
        } else {
            returndatetextfield.setDisable(true);
            returndatetextfield.clear(); // Clear return date field if disabled
        }
    }
 
    private boolean validateDate(String date, String fieldName) {
        if (date == null || date.trim().isEmpty()) {
            showErrorAlert("❌ " + fieldName + " is empty!");
            return false;
        }
 
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            showErrorAlert("❌ Invalid date format in " + fieldName + ". Please use yyyy-MM-dd.");
            return false;
        }
    }
 
    private boolean validateReturnDate(String departureDate, String returnDate) {
        if (returnDate == null || returnDate.trim().isEmpty()) {
            return true; // Kung walang return date, valid pa rin
        }
 
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate depDate = LocalDate.parse(departureDate, formatter);
            LocalDate retDate = LocalDate.parse(returnDate, formatter);
 
            if (retDate.isBefore(depDate)) {
                showErrorAlert("❌ Return date cannot be earlier than departure date.");
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            showErrorAlert("❌ Invalid date format in return date. Please use yyyy-MM-dd.");
            return false;
        }
    }
 
    @FXML
    private void updateFlight(ActionEvent event) {
        System.out.println("Update button clicked!");  // Debugging output
        // Your update logic here
    }
 
    @FXML
    private void deleteFlight() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
 
        // Kung walang flight na naka-select, magpakita ng error alert
        if (selectedFlight == null) {
            showErrorAlert("❌ No flight selected! Please select a flight to delete.");
            return;
        }
 
        // Confirmation alert bago mag-delete
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this flight?");
        confirmAlert.setContentText("Flight ID: " + selectedFlight.getFlightId() + "\nThis action cannot be undone.");
 
        // Kapag pinindot ang OK sa confirmation alert, ituloy ang deletion
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection connection = DatabaseHandler.getDBConnection()) {
                    String query = "DELETE FROM flight WHERE flight_id = ?";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, selectedFlight.getFlightId());
 
                    int rowsDeleted = ps.executeUpdate();
                    if (rowsDeleted > 0) {
                        // Magpakita ng success alert
                        showSuccessAlert("✅ Flight deleted successfully!\nFlight ID: " + selectedFlight.getFlightId());
                        flightList.remove(selectedFlight); // Alisin sa table
                    } else {
                        showErrorAlert("❌ Flight deletion failed. Please try again.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorAlert("❌ An error occurred while deleting the flight. Please check the database connection.");
                }
            }
        });
    }
 
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
 
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
