package denda;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import menuBezeroa.MenuBezeroa;

public class Denda extends Application {

    private Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Denda.fxml"));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        
        this.stage = stage;
        stage.setScene(scene);
        stage.setTitle("Denda");
        stage.show();
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erosketa");
        alert.setHeaderText(null);
        alert.setContentText("Produktua saskira gehitu da!");
        alert.showAndWait();
    }
    
    @FXML
    private void itxiButtonClicked(ActionEvent event) {
        try {
            if (stage != null) {
                stage.close();
            }
            
            MenuBezeroa menuBezeroa = new MenuBezeroa();
            Stage newStage = new Stage();
            menuBezeroa.start(newStage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}