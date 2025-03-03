import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UserFlightController {

    @FXML
    private AnchorPane anchorpane; 

    @FXML
    private void CEB(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Cebu - CEB");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }


    @FXML
    private void PPS(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Puerto Princesa - PPS");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void BCY(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Boracay - BCY");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void BHL(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Bohol - BHL");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void DVO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Davao - DVO");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void BAT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Batanes - BAT");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }


    @FXML
    private void SIN(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
        AnchorPane view = loader.load();
        UserFlight2Controller controller = loader.getController();
        controller.setFlightLocations("Manila - MNL", "Singapore - SIN");
        BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
        parentBorderPane.setCenter(view);
    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @FXML
    private void NRT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Tokyo - NRT");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void ICN(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Seoul - ICN");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void DXB(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Dubai - DXB");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void SYD(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Sydney - SYD");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    @FXML
    private void ZRH(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            UserFlight2Controller controller = loader.getController();
            controller.setFlightLocations("Manila - MNL", "Zurich - ZRH");
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            }
        }
    }
