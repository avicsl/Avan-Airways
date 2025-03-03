import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class UserFlight2Controller {

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private TextField deploc, arrivalloc, arrivaltime, IDTextField, amount;

    @FXML
    private ComboBox<String> deptime, adultcombobox, childrencombobox, infantcombobox;

    @FXML
    private DatePicker depdate, returndate;

    @FXML
    private CheckBox yesbox, nobox;

    private final Map<String, Integer> flightDurations = new HashMap<>();

    private final Map<String, Integer> flightPrices = new HashMap<>();

    @FXML
    private void initialize() {

        String userId = DatabaseHandler.getLoggedInUserId();
        if (userId != null) {
            IDTextField.setText(userId);
        // Restrict departure date selection
        depdate.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusMonths(5).withDayOfMonth(today.plusMonths(5).lengthOfMonth());
                if (date.isBefore(today) || date.isAfter(maxDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }
            }
        
        });

        flightPrices.put("Cebu - CEB", 2499);
        flightPrices.put("Puerto Princesa - PPS", 2699);
        flightPrices.put("Boracay - BCY", 2899);
        flightPrices.put("Bohol - BHL", 2999);
        flightPrices.put("Davao - DVO", 3199);
        flightPrices.put("Batanes - BAT", 4999);
        flightPrices.put("Singapore - SIN", 10499);
        flightPrices.put("Tokyo - NRT", 16499);
        flightPrices.put("Seoul - ICN", 16499);
        flightPrices.put("Dubai - DXB", 29999);
        flightPrices.put("Sydney - SYD", 34999);
        flightPrices.put("Zurich - ZRH", 44999);

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

        deptime.getItems().addAll("7:00 AM", "1:00 PM", "8:00 PM");

        for (int i = 1; i <= 5; i++) {
            adultcombobox.getItems().add(String.valueOf(i));
        }
        for (int i = 0; i <= 3; i++) {
            childrencombobox.getItems().add(String.valueOf(i));
        }
        for (int i = 0; i <= 2; i++) {
            infantcombobox.getItems().add(String.valueOf(i));
        }

        deptime.setOnAction(event -> updateArrivalTime());

        yesbox.setOnAction(event -> handleReturnDate());
        nobox.setOnAction(event -> handleReturnDate());
        returndate.setDisable(true);

        depdate.setOnAction(event -> {
            if (yesbox.isSelected()) {
                updateReturnDateRestrictions();
            }
        });
    }
}

    private int calculateTotalPrice() {
        String destination = arrivalloc.getText();
        if (!flightPrices.containsKey(destination)) {
            return 0; // Default to zero if the destination is not found
        }

        int basePrice = flightPrices.get(destination);
        int adultCount = Integer.parseInt(adultcombobox.getValue());
        int childCount = Integer.parseInt(childrencombobox.getValue());
        int infantCount = Integer.parseInt(infantcombobox.getValue());

        // Pricing logic: Adults full price, children 50% off, infants free
        int totalPrice = (adultCount * basePrice) + (childCount * basePrice) + (infantCount * (basePrice / 2));

        // If round-trip, multiply by 2
        if (yesbox.isSelected()) {
            totalPrice *= 2;
        }

        return totalPrice;
    }


    public void setFlightLocations(String departure, String arrival) {
        deploc.setText(departure);
        arrivalloc.setText(arrival);
        deploc.setEditable(false);
        arrivalloc.setEditable(false);
        arrivaltime.setEditable(false);
        depdate.setEditable(false);
        returndate.setEditable(false);
        IDTextField.setEditable(false);

        Platform.runLater(this::updateArrivalTime);
    }

    private void updateArrivalTime() {
        String selectedTime = deptime.getValue();
        String destination = arrivalloc.getText();

        if (selectedTime != null && flightDurations.containsKey(destination)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            LocalTime departureTime = LocalTime.parse(selectedTime, formatter);

            LocalTime arrivalTime = departureTime.plusMinutes(flightDurations.get(destination));

            arrivaltime.setText(arrivalTime.format(formatter));
        }
    }

    @FXML
    private void back(ActionEvent event) {
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
    private void payment(ActionEvent event) {
    if (!isFormValid()) {
        showAlert("Incomplete Form", "Please complete all required fields before proceeding.");
        return;
    }

    String userId = IDTextField.getText().trim();
    String departure = deploc.getText().trim();
    String arrival = arrivalloc.getText().trim();
    String arrivalTime = arrivaltime.getText().trim();
    String departureTime = (deptime.getValue() != null) ? deptime.getValue() : ""; 

    LocalDate departureDate = depdate.getValue();
    String roundtrip = yesbox.isSelected() ? "Yes" : "No";
    LocalDate returnDate = returndate.getValue(); 

    int adults = 0, children = 0, infants = 0;
    try {
        adults = Integer.parseInt(adultcombobox.getValue());
        children = Integer.parseInt(childrencombobox.getValue());
        infants = Integer.parseInt(infantcombobox.getValue());
    } catch (NumberFormatException | NullPointerException e) {
        showAlert("Invalid Passenger Selection", "Please select valid numbers for all passenger categories.");
        return;
    }

    int totalPrice = calculateTotalPrice();
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Details");
    confirmationAlert.setHeaderText("Are all details correct?");
    confirmationAlert.setContentText("Click 'OK' to proceed or 'Cancel' to review your details.");

    Optional<ButtonType> result = confirmationAlert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
            DatabaseHandler.insertFlight(userId, departure, arrival, departureTime, arrivalTime, departureDate, roundtrip, returnDate);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight3.fxml"));
            AnchorPane view = loader.load();
            UserFlight3Controller controller = loader.getController();
            controller.setAmountField(totalPrice);
            controller.setPassengerCount(adults, children, infants);
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            if (parentBorderPane != null) {
                parentBorderPane.setCenter(view);
            } else {
                showAlert("Navigation Error", "Could not find the main application layout.");
            }
        } catch (IOException e) {
            showAlert("Loading Error", "Failed to load the next screen. Please try again.");
            e.printStackTrace();
        }
    } else {
       
    }
}

    private void handleReturnDate() {
        if (nobox.isSelected()) {
            yesbox.setSelected(false);
            returndate.setDisable(true);
            returndate.setValue(null);
        } else if (yesbox.isSelected()) {
            nobox.setSelected(false);
            returndate.setDisable(false);
            updateReturnDateRestrictions();
        }
    }

    private void updateReturnDateRestrictions() {
        if (depdate.getValue() != null) {
            LocalDate dep = depdate.getValue();
            LocalDate maxReturnDate = dep.plusMonths(6);

            returndate.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (date.isBefore(dep) || date.isAfter(maxReturnDate)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #cccccc;");
                    }
                }
            });
        }
    }

    private boolean isFormValid() {
        if (deploc.getText().trim().isEmpty() || 
            arrivalloc.getText().trim().isEmpty() || 
            arrivaltime.getText().trim().isEmpty() || 
            IDTextField.getText().trim().isEmpty() ||
            deptime.getValue() == null ||
            adultcombobox.getValue() == null ||
            childrencombobox.getValue() == null ||
            infantcombobox.getValue() == null ||
            depdate.getValue() == null ||
            (!yesbox.isSelected() && !nobox.isSelected()) || 
            (yesbox.isSelected() && returndate.getValue() == null)) {
            
            return false; 
        }
        return true; 
    }
    

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}