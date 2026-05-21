package saltzaileAgenda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SaltzaileAgenda extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SaltzaileAgenda.fxml"));
        Scene scene = new Scene(loader.load());
        
        SaltzaileAgendaKontrolagailua kontrolagailua = loader.getController();
        kontrolagailua.setStage(stage);
        
        stage.setScene(scene);
        stage.setTitle("Agenda");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}