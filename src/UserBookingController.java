import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
 
public class UserBookingController implements Initializable {
 
    @FXML
    private AnchorPane anchorpane;
 
    @FXML
    private TableView<Flight> userflighttable;
 
    @FXML
    private TableColumn<Flight, String> flightId;
 
    @FXML
    private TableColumn<Flight, String> departureLocation;
 
    @FXML
    private TableColumn<Flight, String> arrivalLocation;
 
    @FXML
    private TableColumn<Flight, String> departureTime;
 
    @FXML
    private TableColumn<Flight, String> arrivalTime;
 
    @FXML
    private TableColumn<Flight, LocalDate> departureDate;
 
    @FXML
    private TableColumn<Flight, String> roundtrip;
 
    @FXML
    private TableColumn<Flight, LocalDate> returnDate;
 
    @FXML
    private Button markasdonebtn;
 
    @FXML
    private TextField searchField;
 
    private DatabaseHandler dbHandler;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = DatabaseHandler.getInstance();
        setupTable();
        loadFlights();
        setupSearch();
    }
 
    private void setupTable() {
        flightId.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        departureLocation.setCellValueFactory(new PropertyValueFactory<>("departureLocation"));
        arrivalLocation.setCellValueFactory(new PropertyValueFactory<>("arrivalLocation"));
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        departureDate.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        roundtrip.setCellValueFactory(new PropertyValueFactory<>("roundtrip"));
        returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }
 
    public void loadFlights() {
        String loggedInUserId = getLoggedInUserId(); // Fetch the currently logged-in user ID
        ObservableList<Flight> flights = dbHandler.getFlightsForUser(loggedInUserId);
        userflighttable.setItems(flights);
    }
 
    private void setupSearch() {
        // Ensure the table has data before applying filtering
        if (userflighttable.getItems() == null) {
            return; // Exit if there's no data yet
        }
   
        // Wrap the existing flight list in a FilteredList
        FilteredList<Flight> filteredData = new FilteredList<>(userflighttable.getItems(), b -> true);
   
        // Add a listener to searchField for real-time filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(flight -> {
                // If search field is empty, display all flights
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
   
                String lowerCaseFilter = newValue.toLowerCase();
   
                // Filter flights based on multiple criteria
                return flight.getFlightId().toLowerCase().contains(lowerCaseFilter)
                    || flight.getDepartureLocation().toLowerCase().contains(lowerCaseFilter)
                    || flight.getArrivalLocation().toLowerCase().contains(lowerCaseFilter)
                    || flight.getDepartureDate().toString().contains(lowerCaseFilter)
                    || flight.getRoundtrip().toLowerCase().contains(lowerCaseFilter);
            });
        });
   
        // Wrap filtered data in a SortedList
        SortedList<Flight> sortedData = new SortedList<>(filteredData);
   
        // Bind the sorted data to the table view
        sortedData.comparatorProperty().bind(userflighttable.comparatorProperty());
   
        // Set the sorted and filtered data to the table
        userflighttable.setItems(sortedData);
    }
 
    private String getLoggedInUserId() {
        String id = DatabaseHandler.getLoggedInUserId(); // Fetch user ID from DatabaseHandler
 
        if (id == null || id.isEmpty()) {
            System.out.println("User is not logged in.");
            return ""; // Handle the case where no user is logged in
        }
 
        return id;
    }
 
    @FXML
        private void addBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight.fxml"));
            AnchorPane view = loader.load();
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @FXML
    private void markAsDone(ActionEvent event) {
    Flight selectedFlight = userflighttable.getSelectionModel().getSelectedItem();
 
    if (selectedFlight == null) {
        showAlert("Fly Forward, Fly Avan!\"", "Please select a flight to mark as done.");
        return;
    }
 
    try (Connection connection = dbHandler.getDBConnection()) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
        LocalDate departureDate = LocalDate.parse(selectedFlight.getDepartureDate(), formatter);
        LocalDate returnDate = null;
 
        if (selectedFlight.getReturnDate() != null && !selectedFlight.getReturnDate().equalsIgnoreCase("N/A")) {
            returnDate = LocalDate.parse(selectedFlight.getReturnDate(), formatter);
        }
 
        String roundTrip = selectedFlight.getRoundtrip();
 
        if (departureDate != null && today.isAfter(departureDate)) {
            if ("Yes".equalsIgnoreCase(roundTrip) && returnDate != null && today.isBefore(returnDate)) {
                showAlert("Fly Forward, Fly Avan!", "This flight cannot be deleted until after the departure or return date.");
                return;
            }
 
            String deleteQuery = "DELETE FROM flight WHERE flight_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                stmt.setString(1, selectedFlight.getFlightId());
                stmt.executeUpdate();
 
                // Fix: Ensure modifiable list
                ObservableList<Flight> flights = FXCollections.observableArrayList(userflighttable.getItems());
                flights.remove(selectedFlight);
                userflighttable.setItems(flights);
 
                showAlert("Fly Forward, Fly Avan!", "Avan Airways hope that you had so much fun!");
            }
        } else {
            showAlert("Fly Forward, Fly Avan!", "You can only delete flights after the departure or return date.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Fly Forward, Fly Avan!", "An error occurred.");
    }
}
// Alert helper function
private void showAlert(String title, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
}