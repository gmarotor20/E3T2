package kexak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Kexak extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Kexak.fxml"));
		Scene scene = new Scene(loader.load());

		KexakKontrolagailua controller = loader.getController();
		controller.setStage(stage);

		stage.setScene(scene);
		stage.setTitle("Kexak");
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}