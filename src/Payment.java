import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
 
public class Payment {
    private final StringProperty paymentid;
    private final StringProperty bookingid;
    private final StringProperty paymentmethod;
    private final StringProperty paymentstatus;
    private final StringProperty paymentdate;
 
    public Payment(String paymentid, String bookingid, String paymentmethod, String paymentstatus, String paymentdate) {
        this.paymentid = new SimpleStringProperty(paymentid);
        this.bookingid = new SimpleStringProperty(bookingid);
        this.paymentmethod = new SimpleStringProperty(paymentmethod);
        this.paymentstatus = new SimpleStringProperty(paymentstatus);
        this.paymentdate = new SimpleStringProperty(paymentdate);
    }
 
    // Getters
    public String getPaymentid() {
        return paymentid.get();
    }
 
    public String getBookingid() {
        return bookingid.get();
    }
 
    public String getPaymentmethod() {
        return paymentmethod.get();
    }
 
    public String getPaymentstatus() {
        return paymentstatus.get();
    }
 
    public String getPaymentdate() {
        return paymentdate.get();
    }
   
    public void setPaymentmethod(String newMethod) {
        this.paymentmethod.set(newMethod);
    }
   
    public StringProperty paymentidProperty() {
        return paymentid;
    }
 
    public StringProperty bookingidProperty() {
        return bookingid;
    }
 
    public StringProperty paymentmethodProperty() {
        return paymentmethod;
    }
 
    public StringProperty paymentstatusProperty() {
        return paymentstatus;
    }
 
    public StringProperty paymentdateProperty() {
        return paymentdate;
    }
}
 