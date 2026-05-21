package menuSaltzaile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuSaltzaile extends Application {
	
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuSaltzaile.fxml"));
        Scene scene = new Scene(loader.load());
        
        MenuSaltzaileKontrolagailua controller = loader.getController();
        controller.setStage(stage);

        stage.setScene(scene);
        stage.setTitle("Menu Saltzaile");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

