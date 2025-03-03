import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.DialogPane;

public class UserExploreController {

    @FXML
    private Button australiabtn;

    @FXML
    private Button boracaybtn;

    @FXML
    private Button japanbtn;

    @FXML
    private Button sokorbtn;

    @FXML
    private Button switzerlandbtn;

    @FXML
    private Button uaebtn;

    @FXML
private void showBoracayInfo(ActionEvent event) {
    showAlert("Boracay, Philippines", """
        ✈ Travel Time: 1 hour (flight to Caticlan or Kalibo, then a boat ride)
        🌟 Peak Season: November to May (dry season, best beach weather)
        📌 Tourist Spots: White Beach, Puka Shell Beach, Willy’s Rock, D’Mall shopping area
        ❗ Trivia: Boracay’s White Beach is consistently ranked among the world's best beaches!
        """);
}

@FXML
private void showJapanInfo(ActionEvent event) {
    showAlert("Tokyo, Japan", """
        ✈ Travel Time: 4 to 5 hours
        🌟 Peak Season: March to May (cherry blossom season) & September to November (autumn foliage)
        📌 Tourist Spots: Shibuya Crossing, Tokyo Tower, Sensō-ji Temple, Akihabara district
        ❗ Trivia: Tokyo has the busiest pedestrian crossing in the world—Shibuya Crossing!
        """);
}

@FXML
private void showUAEInfo(ActionEvent event) {
    showAlert("Dubai, UAE", """
        ✈ Travel Time: 9 to 10 hours
        🌟 Peak Season: November to March (cooler temperatures, ideal for exploring)
        📌 Tourist Spots: Burj Khalifa, Palm Jumeirah, Dubai Mall, Desert Safari
        ❗ Trivia: Dubai is home to the tallest building in the world—the Burj Khalifa (828m)!
        """);
}

@FXML
private void showAustraliaInfo(ActionEvent event) {
    showAlert("Sydney, Australia", """
        ✈ Travel Time: 8 to 9 hours
        🌟 Peak Season: December to February (summer in Australia)
        📌Tourist Spots: Sydney Opera House, Bondi Beach, Sydney Harbour Bridge, Taronga Zoo
        ❗ Trivia: Sydney has over 100 beaches along its coastline!
        """);
}

@FXML
private void showSwitzerlandInfo(ActionEvent event) {
    showAlert("Zurich, Switzerland", """
        ✈ Travel Time: 15 to 17 hours (with layovers)
        🌟 Peak Season: June to September (summer for hiking) & December to February (winter for skiing)
        📌 Tourist Spots: Lake Zurich, Bahnhofstrasse shopping street, Old Town, Swiss Alps
        ❗ Trivia: Switzerland has four official languages—German, French, Italian, and Romansh!
        """);
}

@FXML
private void showSouthKoreaInfo(ActionEvent event) {
    showAlert("Seoul, South Korea", """
        ✈ Travel Time: 4 to 5 hours
        🌟 Peak Season: March to May (cherry blossoms) & September to November (autumn colors)
        📌 Tourist Spots: Gyeongbokgung Palace, Myeongdong Shopping Street, N Seoul Tower, Bukchon Hanok Village
        ❗ Trivia: The world’s first internet café (PC bang) started in South Korea!
        """);
}

    private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.NONE); 
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.getButtonTypes().add(ButtonType.OK); 

    TextArea textArea = new TextArea(content);
    textArea.setWrapText(true);
    textArea.setEditable(false);
    textArea.setPrefSize(400, 160);
    alert.getDialogPane().setContent(textArea);
    alert.showAndWait();
    }
}
