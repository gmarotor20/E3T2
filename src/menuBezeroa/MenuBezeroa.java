package menuBezeroa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuBezeroa extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuBezeroa.fxml"));
        Scene scene = new Scene(loader.load());
        
        MenuBezeroaKontrolagailua controller = loader.getController();
        controller.setStage(stage);

        stage.setScene(scene);
        stage.setTitle("Menu Bezeroa");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}