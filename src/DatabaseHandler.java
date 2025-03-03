import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DatabaseHandler {
 
    private static DatabaseHandler handler = null;
 
    private DatabaseHandler() {
    }
 
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }
 
    public static Connection getDBConnection() {
        Connection connection = null;
        String dburl = "jdbc:mysql://127.0.0.1:3306/cebpacdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Manila";
        String userName = "root";
        String password = "Vhina05solo02_";
        try {
            connection = DriverManager.getConnection(dburl, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return connection;
    }

//PASSENGER TABLE----------------------------------------------------------------------------------------------------------------
 
    private static String loggedInUserId; 

    public static String getLoggedInUserId() {
        return loggedInUserId;
    }

    public ResultSet execQuery(String query) {
        ResultSet result = null;
        try {
            Connection conn = getDBConnection();
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery: " + ex.getLocalizedMessage());
        }
        return result;
    }
 
    public static boolean validateLogin(String username, String password) {
        String query = "SELECT id FROM admin WHERE UserName = ? AND Password = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstatement = conn.prepareStatement(query)) {
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            ResultSet result = pstatement.executeQuery();
            if (result.next()) {
                loggedInUserId = result.getString("id"); // Store the logged-in user ID
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 
    public static boolean changePassword(String username, String newPassword) {
        String query = "UPDATE admin SET Password = ? WHERE Username = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstatement = conn.prepareStatement(query)) {
            pstatement.setString(1, newPassword);
            pstatement.setString(2, username);
            return pstatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 
    public static ResultSet getUsers() {
        String query = "SELECT * FROM admin";
        try {
            return getInstance().execQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public static boolean addUser(User user) {
        if (isPhoneNumberExists(user.getPhoneNumber())) {
            showAlert("Error", "Duplicate Entry", "Phone number already exists!", AlertType.ERROR);
            return false; // Prevent insertion
        }
    
        if (isEmailExists(user.getEmail())) {
            showAlert("Error", "Duplicate Entry", "Email already exists!", AlertType.ERROR);
            return false; // Prevent insertion
        }

        if (isUsernameExists(user.getUsername())) {
            showAlert("Error", "Duplicate Entry", "Username already exists!", AlertType.ERROR);
            return false; // Prevent insertion
        }
        
    
        String query = "INSERT INTO admin (first_name, last_name, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getDBConnection();
             PreparedStatement pstatement = conn.prepareStatement(query)) {
                pstatement.setString(1, user.getFirstName());
                pstatement.setString(2, user.getLastName());
                pstatement.setString(3, user.getUsername());
                pstatement.setString(4, user.getPassword());
                pstatement.setString(5, user.getEmail());
                pstatement.setString(6, user.getPhoneNumber());
                return pstatement.executeUpdate() > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 
    public static boolean deleteUser(String id) {
        String query = "DELETE FROM admin WHERE id = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstatement = conn.prepareStatement(query)) {
            pstatement.setString(1, id);
            return pstatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateUser (String oldID, User updatedID) {
        if (isPhoneNumberExists(updatedID.getPhoneNumber())) {
            showAlert("Error", "Duplicate Entry", "Phone number already exists!", AlertType.ERROR);
            return false; // Prevent insertion
        }
    
        if (isEmailExists(updatedID.getEmail())) {
            showAlert("Error", "Duplicate Entry", "Email already exists!", AlertType.ERROR);
            return false; // Prevent insertion
        }

        if (isUsernameExists(updatedID.getUsername())) {
            showAlert("Error", "Duplicate Entry", "Username already exists!", AlertType.ERROR);
            return false; // Prevent insertion
        }
        String query = "UPDATE admin SET first_name = ?, last_name = ?, username = ?, password = ?, email = ?, phone_number = ? WHERE id = ?";
        try (PreparedStatement pstmt = getDBConnection().prepareStatement(query)) {
            pstmt.setString(1, updatedID.getFirstName());
            pstmt.setString(2, updatedID.getLastName());
            pstmt.setString(3, updatedID.getUsername());
            pstmt.setString(4, updatedID.getPassword());
            pstmt.setString(5, updatedID.getEmail());
            pstmt.setString(6, updatedID.getPhoneNumber());
            pstmt.setString(7, oldID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }    
    
    public static boolean isPhoneNumberExists(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM admin WHERE phone_number = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM admin WHERE username = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM admin WHERE username = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     
  
  
    public static boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM admin WHERE email = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static ResultSet getUserById(String userId) {
        String query = "SELECT * FROM admin WHERE id = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

//FLIGHT TABLE------------------------------------------------------------------------------------------------------------------

    public static ResultSet getFlightBookings() {
        String query = "SELECT * FROM Flight";
        try {
            Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            return pstmt.executeQuery(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertFlight(String userId, String departure, String arrival,
    String departureTime, String arrivalTime, LocalDate departureDate,
    String roundtrip, LocalDate returnDate) {

    // Exclude flight_id since it's auto-generated
    String sql = "INSERT INTO Flight (id, departure_location, arrival_location, " +
                 "departure_time, arrival_time, departure_date, roundtrip, return_date) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, userId);
        stmt.setString(2, departure);
        stmt.setString(3, arrival);
        stmt.setString(4, departureTime);
        stmt.setString(5, arrivalTime);
        stmt.setDate(6, java.sql.Date.valueOf(departureDate));
        stmt.setString(7, roundtrip);

        if (returnDate != null) {
            stmt.setDate(8, java.sql.Date.valueOf(returnDate));
        } else {
            stmt.setNull(8, java.sql.Types.DATE);
        }

        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public boolean updateFlight(String flightNumber, String newDeparture, String newDestination, String newDate, String newTime, double newPrice) {
        String query = "UPDATE flight SET departure = ?, destination = ?, date = ?, time = ?, price = ? WHERE flight_number = ?";
        try (Connection conn =getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newDeparture);
            stmt.setString(2, newDestination);
            stmt.setString(3, newDate);
            stmt.setString(4, newTime);
            stmt.setDouble(5, newPrice);
            stmt.setString(6, flightNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFlight(String flightNumber) {
        String query = "DELETE FROM flight WHERE flight_number = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, flightNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void listFlights() {
        String query = "SELECT * FROM flight";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("Flight Number: " + rs.getString("flight_number") + ", Departure: " + rs.getString("departure") + ", Destination: " + rs.getString("destination") + ", Date: " + rs.getString("date") + ", Time: " + rs.getString("time") + ", Price: " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//SEAT TABLE--------------------------------------------------------------------------------------------------------------------
    
    public static ResultSet getSeat() {
        String query = "SELECT * FROM seat";
        try {
            Connection conn =getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean insertSeat(String flightId, String seatClass) {
        String sql = "INSERT INTO seat (flight_id, seat_class) VALUES (?, ?)";
       
        try (Connection conn = getDBConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
           
            pst.setString(1, flightId);
            pst.setString(2, seatClass);
       
            pst.executeUpdate();
            return true; // Seat successfully inserted
    
        } catch (SQLException e) {
            if (e.getMessage().contains("No more seats available for this class")) {
                return false; // Triggered error, seat class is full
            }
            e.printStackTrace();
            return false;
        }
    }
    

    public static String getLastFlightId() {
        String query = "SELECT flight_id FROM flight ORDER BY id DESC LIMIT 1"; // Adjust 'id' to your primary key
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            if (rs.next()) {
                return rs.getString("flight_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no flights exist
    }
    
    public static boolean updateSeat(String seatId, String newSeatClass, String newSeatNumber) {
        String query = "UPDATE Seat SET Seat_Class = ?, Seat_Number = ? WHERE Seat_ID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newSeatClass);
            stmt.setString(2, newSeatNumber);
            stmt.setString(3, seatId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   
 
    public static boolean deleteSeat(String seatId) {
        String query = "DELETE FROM seat WHERE seat_id = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, seatId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

//BOOKING TABLE-----------------------------------------------------------------------------------------------------------------
    
    public static ResultSet getBooking() {
        String query = "SELECT * FROM Booking";
        try {
            Connection conn =getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void insertBooking(String id, String flightId, String seatId, BigDecimal Amount) {
        String sql = "INSERT INTO booking (id, flight_id, seat_id, amount) VALUES (?,?, ?, ?)";
    
        try (Connection conn = getDBConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            pst.setString(2, flightId);
            pst.setString(3, seatId);
            pst.setBigDecimal(4, Amount);
    
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSeatIdsForLastFlight() {
        String flightId = getLastFlightId();
        if (flightId == null) {
            return Collections.emptyList(); // Return empty if no flights exist
        }
    
        List<String> seatIds = new ArrayList<>();
        String query = "SELECT seat_id FROM seat WHERE flight_id = ?"; 
    
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, flightId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seatIds.add(rs.getString("seat_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return seatIds;
    }

    public static void insertBookingsForLastFlight(BigDecimal amount) {
    String flightId = getLastFlightId();
    if (flightId == null) {
        System.out.println("No flight found.");
        return;
    }

    List<String> seatIds = getSeatIdsForLastFlight();
    if (seatIds.isEmpty()) {
        System.out.println("No seats found for flight: " + flightId);
        return;
    }

    String sql = "INSERT INTO booking (id, flight_id, seat_id, amount) VALUES (?, ?, ?, ?)";

    try (Connection conn = getDBConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        for (String seatId : seatIds) {
            pst.setString(1, bookingId);
            pst.setString(2, flightId);
            pst.setString(3, seatId);
            pst.setBigDecimal(4, amount);
            pst.executeUpdate();
        }

        System.out.println("Successfully inserted " + seatIds.size() + " bookings.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public static boolean updateBooking(String bookingId, String flightId, String seatId, BigDecimal amount) {
        String query = "UPDATE booking SET Amount = ? WHERE Booking_ID = ? AND Flight_ID = ? AND Seat_ID = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBigDecimal(1, amount);
            pstmt.setString(2, bookingId);
            pstmt.setString(3, flightId);
            pstmt.setString(4, seatId);
    
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public static boolean deleteBooking(String bookingId) {
        String query = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 

//PAYMENT TABLE-----------------------------------------------------------------------------------------------------------------
    
    public static ResultSet getPayment() {
        String query = "SELECT * FROM Payment";
        try {
            Connection conn =getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertPayment(String bookingid, String paymentmethod) {
        String sql = "INSERT INTO payment (booking_id, payment_method) VALUES (?, ?)";
    
        try (Connection conn = getDBConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, bookingid);
            pst.setString(2, paymentmethod);
    
            pst.executeUpdate();
            System.out.println("Seat inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getLastBookingID() {
        String query = "SELECT booking_id FROM booking ORDER BY id DESC LIMIT 1"; // Adjust 'id' to your primary key
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            if (rs.next()) {
                return rs.getString("booking_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no flights exist
    }

    public static boolean updatePayment(String paymentID, String newMethod) {
        String query = "UPDATE payment SET Payment_Method = ? WHERE Payment_ID = ?";
       
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
   
            stmt.setString(1, newMethod);
            stmt.setString(2, paymentID);
   
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Magbabalik ng true kung may na-update
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   
    public static boolean deletePayment(String paymentId) {
        String query = "DELETE FROM payment WHERE payment_id = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, paymentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
//USER FLIGHT ---------------------------------------------------------------------------------------------------------------

    public ObservableList<Flight> getFlightsForUser(String userId) {
        ObservableList<Flight> flights = FXCollections.observableArrayList();
        String query = "SELECT * FROM Flight WHERE id = ?";
        List<Flight> flightList = new ArrayList<>();

        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            String returnDateStr = (rs.getDate("return_date") != null) ? rs.getDate("return_date").toString() : null;

            Flight flight = new Flight(
                rs.getString("flight_id"),
                rs.getString("id"),
                rs.getString("departure_location"),
                rs.getString("arrival_location"),
                rs.getString("departure_time"),
                rs.getString("arrival_time"),
                rs.getString("departure_date"),
                rs.getString("roundtrip"),
                returnDateStr
            );

            flightList.add(flight);
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }
}