package org.sawmill.theater.ticketing.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.sawmill.theater.ticketing.service.TheatreService;
import org.sawmill.theater.ticketing.service.TheatreServiceImpl;

public class MainController implements Initializable {
    @FXML
    private Button btnAddShow;
    @FXML
    private Button btnUpdateShow;
    @FXML
    private Button btnRemoveShow;
    @FXML
    private Button btnUpdateSeats;
    @FXML
    private Circle statusIndicator;
    
    private TheatreService theatreService = new TheatreServiceImpl();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    /**
     * When this method is called, it will change the Scene to a New Show form
     */
    public void showNewShowtime(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Showtime.fxml"));
        Parent mainParent = loader.load();
        ShowtimeController controller = loader.getController();
        controller.setEditMode(false);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene currentScene = ((Node)event.getSource()).getScene();
        Scene newShowtimeScene = new Scene(mainParent, currentScene.getWidth(), currentScene.getHeight());
        
        window.setScene(newShowtimeScene);
        window.show();
    }
    
    /**
     * When this method is called, it will change the Scene to a Edit Show form
     */
    public void updateShowtime(ActionEvent event) throws IOException {
        
        // Check if there are any shows added first. If not, then alert the user
        if (theatreService.getShowtimes().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Shows Found");
            alert.setHeaderText("There are no shows to update. Please create one first.");
            alert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Showtime.fxml"));
            Parent mainParent = loader.load();
            ShowtimeController controller = loader.getController();
            controller.setEditMode(true);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene currentScene = ((Node)event.getSource()).getScene();
            Scene updateShowtimeScene = new Scene(mainParent, currentScene.getWidth(), currentScene.getHeight());

            window.setScene(updateShowtimeScene);
            window.show();
        }
    }
    
    /**
     * When this method is called, it will change the Scene to a Delete Show form
     */
    public void deleteShowtime(ActionEvent event) throws IOException {
        
        // Check if there are any shows added first. If not, then alert the user
        if (theatreService.getShowtimes().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Shows Found");
            alert.setHeaderText("There are no shows to delete");
            alert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Showtime.fxml"));
            Parent mainParent = loader.load();
            ShowtimeController controller = loader.getController();
            controller.setDeleteMode(true);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene currentScene = ((Node)event.getSource()).getScene();
            Scene deleteShowtimeScene = new Scene(mainParent, currentScene.getWidth(), currentScene.getHeight());

            window.setScene(deleteShowtimeScene);
            window.show(); 
        }
    }
    
    
    /**
     * When this method is called, it will change the Scene to the seat chart for updating seats
     */
    public void updateSeats(ActionEvent event) throws IOException {
        
        // Check if there are any shows added first. If not, then alert the user
        if (theatreService.getShowtimes().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Shows Found");
            alert.setHeaderText("No shows found for updating seats. Please create one first.");
            alert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chart.fxml"));
            Parent mainParent = loader.load();
            ChartController controller = loader.getController();

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene currentScene = ((Node)event.getSource()).getScene();
            Scene updateSeatsScene = new Scene(mainParent, currentScene.getWidth(), currentScene.getHeight());
            controller.setScene(updateSeatsScene);
            controller.setUpSeatLabels();
            controller.getShowtimes();

            window.setScene(updateSeatsScene);
            window.show();
        }
    }
    
    public void printTickets(ActionEvent event) throws IOException {
        
        // Check if there are any shows added first. If not, then alert the user
        if (theatreService.getShowtimes().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Shows Found");
            alert.setHeaderText("No shows found for printing tickets. Please create one first.");
            alert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chart.fxml"));
            Parent mainParent = loader.load();
            ChartController controller = loader.getController();
            
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene currentScene = ((Node)event.getSource()).getScene();
            Scene printTicketsScene = new Scene(mainParent, currentScene.getWidth(), currentScene.getHeight());
            controller.setScene(printTicketsScene);
            controller.setUpSeatLabels();
            controller.getShowtimes();
            
            window.setScene(printTicketsScene);
            window.show();
        }
    }
    
    public void createDatabase(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Name Database File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File dbFile = fileChooser.showSaveDialog(window);
        
        if (dbFile != null) {
            theatreService.createDatabase(dbFile.getAbsolutePath());
            statusIndicator.getStyleClass().clear();
            statusIndicator.getStyleClass().add("ready");
            disableButtons(false);
        }
        
    }
    
    public void connectToDatabase(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database File");
        File dbFile = fileChooser.showOpenDialog(window);
        if (dbFile != null) {
            if (theatreService.checkSelectedDatabase(dbFile.getAbsolutePath())) {
                statusIndicator.getStyleClass().clear();
                statusIndicator.getStyleClass().add("ready");
                disableButtons(false);
            }
        }
    }
    
    public void checkDatabaseStatus() {
        if (theatreService.isDatabaseConnected()) {
            statusIndicator.getStyleClass().clear();
            statusIndicator.getStyleClass().add("ready");
            disableButtons(false);
        } else {
            statusIndicator.getStyleClass().clear();
            statusIndicator.getStyleClass().add("not-ready");
            disableButtons(true);
        }
    }
    
    private void disableButtons(boolean isDisabled) {
        btnAddShow.setDisable(isDisabled);
        btnUpdateShow.setDisable(isDisabled);
        btnRemoveShow.setDisable(isDisabled);
        btnUpdateSeats.setDisable(isDisabled);
    }
    
    public void closeApplication() {
        Platform.exit();
    }

}
