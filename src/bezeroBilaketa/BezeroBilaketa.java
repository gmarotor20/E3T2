package bezeroBilaketa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Bezeroen bilaketa aplikazioaren abiarazle nagusia.
 * BezeroBilaketa leihoa abiarazten du eta kontrolagailuari stage-a pasatzen dio.
 *
 * @author E3T2
 * @version 1.0
 */
public class BezeroBilaketa extends Application {

    /**
     * JavaFX aplikazioa abiarazten du BezeroBilaketa leihoa irekiz.
     *
     * @param stage aplikazioaren leiho nagusia.
     * @throws Exception FXMLa kargatzean errorea gertatzen bada.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BezeroBilaketa.fxml"));
        Scene scene = new Scene(loader.load());

        BezeroKontrolagailua controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Bezeroen Bilaketa");
        stage.show();
    }

    /**
     * Aplikazioaren sarrera puntua.
     *
     * @param args komando lerroko argumentuak.
     */
    public static void main(String[] args) {
        launch();
    }
}