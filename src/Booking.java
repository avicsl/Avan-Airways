import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
 
public class Booking {
    private final StringProperty bookingid;
    private final StringProperty passengerid;
    private final StringProperty flightid;
    private final StringProperty seatid;
    private final StringProperty amount;
    private final StringProperty bookingdate;
 
    public Booking(String bookingid, String passengerid, String flightid, String seatid, String amount, String bookingdate) {
        this.bookingid = new SimpleStringProperty(bookingid);
        this.passengerid = new SimpleStringProperty(passengerid);
        this.flightid = new SimpleStringProperty(flightid);
        this.seatid = new SimpleStringProperty(seatid);
        this.amount = new SimpleStringProperty(amount);
        this.bookingdate = new SimpleStringProperty(bookingdate);
    }
 
    // Getters
    public String getBookingid() {
        return bookingid.get();
    }
 
    public String getPassengerid() {
        return passengerid.get();
    }
 
    public String getFlightid() {
        return flightid.get();
    }
 
    public String getSeatid() {
        return seatid.get();
    }
 
    public String getAmount() {
        return amount.get();
    }
 
    public String getBookingdate() {
        return bookingdate.get();
    }
 
    // Setter for amount (Fixes the error)
    public void setAmount(String newAmount) {
        this.amount.set(newAmount);
    }
 
    // Property Getters (for JavaFX TableView binding)
    public StringProperty bookingidProperty() {
        return bookingid;
    }
 
    public StringProperty passengeridProperty() {
        return passengerid;
    }
 
    public StringProperty flightidProperty() {
        return flightid;
    }
 
    public StringProperty seatidProperty() {
        return seatid;
    }
 
    public StringProperty amountProperty() {
        return amount;
    }
 
    public StringProperty bookingdateProperty() {
        return bookingdate;
    }
}