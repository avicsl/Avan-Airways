import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
 
public class AdminPaymentController implements Initializable {
 
    private final ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    private FilteredList<Payment> filteredPayments;
 
    @FXML
    private TableView<Payment> paymentTable;
   
    @FXML
    private TableColumn<Payment, String> bookingid, paymentdate, paymentid, paymentmethod, paymentstatus;
   
    @FXML
    private Button btnupdate, btndelete;
   
    @FXML
    private ComboBox<String> paymentmethodcombobox;
   
    @FXML
    private TextField searchField;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTable();
        loadPaymentData();
        setButtonActions();
        setupSearch();
       
        paymentmethodcombobox.getItems().addAll(
            "PM1 - American Express", "PM2 - GCash", "PM3 - Maya",
            "PM4 - PayPal", "PM5 - Visa MasterCard", "PM6 - JCB"
        );
    }
 
    private void setUpTable() {
        paymentid.setCellValueFactory(new PropertyValueFactory<>("paymentid"));
        bookingid.setCellValueFactory(new PropertyValueFactory<>("bookingid"));
        paymentmethod.setCellValueFactory(new PropertyValueFactory<>("paymentmethod"));
        paymentstatus.setCellValueFactory(new PropertyValueFactory<>("paymentstatus"));
        paymentdate.setCellValueFactory(new PropertyValueFactory<>("paymentdate"));
 
        filteredPayments = new FilteredList<>(paymentList, p -> true);
        paymentTable.setItems(filteredPayments);
    }
 
    private void loadPaymentData() {
        paymentList.clear();
        new Thread(() -> {
            ObservableList<Payment> tempPaymentList = FXCollections.observableArrayList();
            try (ResultSet rs = DatabaseHandler.getPayment()) {
                if (rs == null) {
                    System.out.println("No data retrieved from the database.");
                    return;
                }
                while (rs.next()) {
                    tempPaymentList.add(new Payment(
                            rs.getString("Payment_ID"),
                            rs.getString("Booking_ID"),
                            rs.getString("Payment_Method"),
                            rs.getString("Payment_Status"),
                            rs.getString("Payment_Date")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> paymentList.setAll(tempPaymentList));
        }).start();
    }
 
    private void setButtonActions() {
        btnupdate.setOnAction(event -> updatePaymentMethod());
        btndelete.setOnAction(event -> deletePayment());
    }
 
    private void updatePaymentMethod() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showAlert("Error", "Please select a payment to update.");
            return;
        }
   
        String newMethod = paymentmethodcombobox.getValue();
        if (newMethod == null || newMethod.isEmpty()) {
            showAlert("Error", "Please select a payment method.");
            return;
        }
   
        if (!newMethod.equals(selectedPayment.getPaymentmethod())) {
            try {
                boolean success = DatabaseHandler.updatePayment(selectedPayment.getPaymentid(), newMethod);
                if (success) {
                    selectedPayment.paymentmethodProperty().set(newMethod);
                    paymentTable.refresh();
                    showAlert("Success", "Payment method updated successfully.");
                } else {
                    showAlert("Error", "Failed to update payment method.");
                }
            } catch (Exception e) {
                showAlert("Error", "Database update method missing.");
                e.printStackTrace();
            }
        } else {
            showAlert("Info", "No changes were made.");
        }
    }
   
    private void deletePayment() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showAlert("Error", "Please select a payment to delete.");
            return;
        }
 
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this payment?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = DatabaseHandler.deletePayment(selectedPayment.getPaymentid());
                if (success) {
                    paymentList.remove(selectedPayment);
                    showAlert("Success", "Payment deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete payment.");
                }
            }
        });
    }
 
    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPayments.setPredicate(payment -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return payment.getPaymentid().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getBookingid().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getPaymentmethod().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getPaymentstatus().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getPaymentdate().toLowerCase().contains(lowerCaseFilter);
            });
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
 