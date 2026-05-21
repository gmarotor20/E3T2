package erregistratu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Erregistratu extends Application  {
	 @Override
	    public void start(Stage stage) throws Exception {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("Erregistratu.fxml"));
	        Scene scene = new Scene(loader.load());

	        stage.setScene(scene);
	        stage.setTitle("Erregistratu");
	        stage.show();
	    }

	    public static void main(String[] args) {
	        launch();
	    }

}
