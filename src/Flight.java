import javafx.beans.property.SimpleStringProperty;
 
public class Flight {
    private final SimpleStringProperty flightId;
    private final SimpleStringProperty passengerId;
    private final SimpleStringProperty departureLocation;
    private final SimpleStringProperty arrivalLocation;
    private final SimpleStringProperty departureTime;
    private final SimpleStringProperty arrivalTime;
    private final SimpleStringProperty departureDate;
    private final SimpleStringProperty roundtrip;
    private final SimpleStringProperty returnDate;
 
    public Flight(String flightId, String passengerId, String departureLocation, String arrivalLocation,
                  String departureTime, String arrivalTime, String departureDate, String roundtrip, String returnDate) {
        this.flightId = new SimpleStringProperty(flightId);
        this.passengerId = new SimpleStringProperty(passengerId);
        this.departureLocation = new SimpleStringProperty(departureLocation);
        this.arrivalLocation = new SimpleStringProperty(arrivalLocation);
        this.departureTime = new SimpleStringProperty(departureTime);
        this.arrivalTime = new SimpleStringProperty(arrivalTime);
        this.departureDate = new SimpleStringProperty(departureDate);
        this.roundtrip = new SimpleStringProperty(roundtrip);
        this.returnDate = new SimpleStringProperty(returnDate != null ? returnDate : "N/A"); 
    }
 
    public String getFlightId() { return flightId.get(); }
    public String getPassengerId() { return passengerId.get(); }
    public String getDepartureLocation() { return departureLocation.get(); }
    public String getArrivalLocation() { return arrivalLocation.get(); }
    public String getDepartureTime() { return departureTime.get(); }
    public String getArrivalTime() { return arrivalTime.get(); }
    public String getDepartureDate() { return departureDate.get(); }
    public String getRoundtrip() { return roundtrip.get(); }
    public String getReturnDate() { return returnDate.get(); }
 
    public SimpleStringProperty flightIdProperty() { return flightId; }
    public SimpleStringProperty passengerIdProperty() { return passengerId; }
    public SimpleStringProperty departureLocationProperty() { return departureLocation; }
    public SimpleStringProperty arrivalLocationProperty() { return arrivalLocation; }
    public SimpleStringProperty departureTimeProperty() { return departureTime; }
    public SimpleStringProperty arrivalTimeProperty() { return arrivalTime; }
    public SimpleStringProperty departureDateProperty() { return departureDate; }
    public SimpleStringProperty roundtripProperty() { return roundtrip; }
    public SimpleStringProperty returnDateProperty() { return returnDate; }
 
    // ✅ Optional: Setter methods (if updating data dynamically in TableView)
    public void setReturnDate(String value) { returnDate.set(value != null ? value : "N/A"); }
}