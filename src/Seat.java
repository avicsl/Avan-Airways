import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
 
public class Seat {
    private final StringProperty seatid;
    private final StringProperty flightid;
    private final StringProperty seatnumber;
    private final StringProperty seatclass;
 
    public Seat(String seatid, String flightid, String seatnumber, String seatclass) {
        this.seatid = new SimpleStringProperty(seatid);
        this.flightid = new SimpleStringProperty(flightid);
        this.seatnumber = new SimpleStringProperty(seatnumber);
        this.seatclass = new SimpleStringProperty(seatclass);
    }
 
    // Getters
    public String getSeatid() {
        return seatid.get();
    }
 
    public String getFlightid() {
        return flightid.get();
    }
 
    public String getSeatnumber() {
        return seatnumber.get();
    }
 
    public String getSeatclass() {
        return seatclass.get();
    }
 
    public void setSeatclass(String seatclass) {
        this.seatclass.set(seatclass);
    }
 
    public void setSeatnumber(String seatnumber) {
        this.seatnumber.set(seatnumber);
    }
   
 
    // Property Getters (for JavaFX TableView binding)
    public StringProperty seatidProperty() {
        return seatid;
    }
 
    public StringProperty flightidProperty() {
        return flightid;
    }
 
    public StringProperty seatnumberProperty() {
        return seatnumber;
    }
 
    public StringProperty seatclassProperty() {
        return seatclass;
    }
}
 