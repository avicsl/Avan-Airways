import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
 
public class AdminSeatController implements Initializable {
 
    private final ObservableList<Seat> seatList = FXCollections.observableArrayList();
    private FilteredList<Seat> filteredSeats;
 
    @FXML
    private TableView<Seat> seatTable;
 
    @FXML
    private TableColumn<Seat, String> seatid;
 
    @FXML
    private TableColumn<Seat, String> flightid;
 
    @FXML
    private TableColumn<Seat, String> seatnumber;
 
    @FXML
    private TableColumn<Seat, String> seatclass;
 
    @FXML
    private ComboBox<String> seatClassComboBox;
 
    @FXML
    private TextField searchField;
 
    @FXML
    private Button btnupdate, btndelete;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTable();
        loadSeatData();
        setupSearch();
        setupComboBox();
        setButtonActions();
    }
 
    private void setUpTable() {
        seatid.setCellValueFactory(new PropertyValueFactory<>("seatid"));
        flightid.setCellValueFactory(new PropertyValueFactory<>("flightid"));
        seatnumber.setCellValueFactory(new PropertyValueFactory<>("seatnumber"));
        seatclass.setCellValueFactory(new PropertyValueFactory<>("seatclass"));
 
        filteredSeats = new FilteredList<>(seatList, p -> true);
        seatTable.setItems(filteredSeats);
    }
 
    private void loadSeatData() {
        seatList.clear();
 
        new Thread(() -> {
            ObservableList<Seat> tempSeatList = FXCollections.observableArrayList();
 
            try (ResultSet rs = DatabaseHandler.getSeat()) {
                if (rs == null) {
                    System.out.println("No data retrieved from the database.");
                    return;
                }
                while (rs.next()) {
                    tempSeatList.add(new Seat(
                            rs.getString("Seat_ID"),
                            rs.getString("Flight_ID"),
                            rs.getString("Seat_Number"),
                            rs.getString("Seat_Class")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
 
            Platform.runLater(() -> {
                seatList.setAll(tempSeatList);
                filteredSeats.setPredicate(p -> true);
            });
 
        }).start();
    }
 
    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredSeats.setPredicate(seat -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                return seat.getSeatid().toLowerCase().contains(searchKeyword) ||
                        seat.getFlightid().toLowerCase().contains(searchKeyword) ||
                        seat.getSeatnumber().toLowerCase().contains(searchKeyword) ||
                        seat.getSeatclass().toLowerCase().contains(searchKeyword);
            });
        });
    }
 
    private void setupComboBox() {
        seatClassComboBox.setItems(FXCollections.observableArrayList(
                "Economy Class", "Business Class", "First Class"
        ));
    }
 
    private void setButtonActions() {
        btnupdate.setOnAction(event -> updateSeat());
        btndelete.setOnAction(event -> deleteSeat());
    }
 
    private void updateSeat() {
        Seat selectedSeat = seatTable.getSelectionModel().getSelectedItem();
        if (selectedSeat == null) {
            showAlert("Error", "Please select a seat to update.");
            return;
        }
 
        String newSeatClass = seatClassComboBox.getValue();
        if (newSeatClass == null || newSeatClass.trim().isEmpty()) {
            showAlert("Error", "Please select a seat class.");
            return;
        }
 
        String newSeatNumber = modifySeatNumber(selectedSeat.getSeatnumber(), newSeatClass);
        boolean success = DatabaseHandler.updateSeat(selectedSeat.getSeatid(), newSeatClass, newSeatNumber);
 
        if (success) {
            selectedSeat.setSeatclass(newSeatClass);
            selectedSeat.setSeatnumber(newSeatNumber);
            seatTable.refresh();
            showAlert("Success", "Seat updated successfully.");
            loadSeatData(); 
        } else {
            showAlert("Error", "Failed to update seat.");
        }
    }
 
    private String modifySeatNumber(String oldSeatNumber, String newSeatClass) {
        String randNumber = String.format("%02d", (int) (Math.random() * 100));
        String randLetters = new String(new char[] {
            (char) ('A' + (int) (Math.random() * 26)),
            (char) ('A' + (int) (Math.random() * 26))
        });

        String seatPrefix;
        switch (newSeatClass) {
            case "Economy Class":
                seatPrefix = "E";
                break;
            case "Business Class":
                seatPrefix = "B";
                break;
            case "First Class":
                seatPrefix = "F";
                break;
            default:
                seatPrefix = "";
                break;
        }
        return seatPrefix + randNumber + randLetters;
    }
 
    private void deleteSeat() {
        Seat selectedSeat = seatTable.getSelectionModel().getSelectedItem();
        if (selectedSeat == null) {
            showAlert("Error", "Please select a seat to delete.");
            return;
        }
 
        boolean success = DatabaseHandler.deleteSeat(selectedSeat.getSeatid());
 
        if (success) {
            seatList.remove(selectedSeat);
            showAlert("Success", "Seat deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete seat.");
        }
    }
 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
 