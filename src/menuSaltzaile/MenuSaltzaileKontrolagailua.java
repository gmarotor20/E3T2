package menuSaltzaile;

import bezeroBilaketa.BezeroBilaketa;
import eskaera.Eskaera;
import inbentarioa.Inbentarioa;
import kexak.Kexak;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import saltzaileAgenda.SaltzaileAgendaKontrolagailua;
import java.util.Optional;

/**
 * Saltzailearen menu nagusiaren kontrolagailua.
 * Sistemaren atal desberdinen arteko nabigazioa kudeatzen du
 * eta Agendaren atalera sarbide mugatua kontrolatzen du.
 *
 * @author E2T3
 * @version 1.0
 */
public class MenuSaltzaileKontrolagailua {

    /** Menuaren leihoaren uneko stage-a. */
    private Stage stage;

    /** Saioan dagoen saltzailearen erabiltzaile izena. */
    private static String erabiltzailea;

    /** Saioan dagoen saltzailearen rola (Administratzailea edo Saltzailea). */
    private static String aldaketak;

    /** Saioa ixteko MenuItem-a. */
    @FXML private MenuItem saioaItximenu;

    /**
     * Uneko leihoaren stage-a ezartzen du.
     *
     * @param stage menuaren leihoaren stage-a.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Saioan dagoen saltzailearen erabiltzaile izena ezartzen du.
     *
     * @param erabiltzailea saltzailearen erabiltzaile izena.
     */
    public void setErabiltzailea(String erabiltzailea) {
        MenuSaltzaileKontrolagailua.erabiltzailea = erabiltzailea;
    }

    /**
     * Saioan dagoen saltzailearen rola ezartzen du.
     *
     * @param aldaketak saltzailearen rola.
     */
    public void setAldaketak(String aldaketak) {
        MenuSaltzaileKontrolagailua.aldaketak = aldaketak;
    }

    /**
     * Bezeroen bilaketa leihoa irekitzen du.
     */
    @FXML
    private void bilatu() {
        try {
            BezeroBilaketa bilaketa = new BezeroBilaketa();
            Stage newStage = new Stage();
            bilaketa.start(newStage);
            if (stage != null) stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saltzaileen agenda leihoa irekitzen du.
     * Administratzailea roleko erabiltzaileentzat soilik eskuragarri dago.
     * Erabiltzaileak baimenik ez badu, sarbide ukatua mezua erakusten du.
     */
    @FXML
    private void agenda() {
        if (!"Administratzailea".equals(aldaketak)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sarbide Ukatua");
            alert.setHeaderText("Administratzaile Baimena Beharrezkoa");
            alert.setContentText(
                    "Saltzaileak kudeatzeko baimena ez duzu. Atal hau administratzaileentzat soilik dago.\n\n"
                            + "Erabiltzailea: " + erabiltzailea + "\n" + "Sarbide maila: SALTZAILE\n"
                            + "Beharrezko maila: ADMINISTRATZAILEA\n\n");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/saltzaileAgenda/SaltzaileAgenda.fxml"));
            Scene scene = new Scene(loader.load());
            Stage agendaStage = new Stage();
            SaltzaileAgendaKontrolagailua kontrolagailua = loader.getController();
            kontrolagailua.setStage(agendaStage);
            agendaStage.setTitle("Agenda");
            agendaStage.setScene(scene);
            agendaStage.show();
            if (stage != null) stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Produktuen inbentario leihoa irekitzen du.
     */
    @FXML
    private void inbentario() {
        try {
            Inbentarioa inbentario = new Inbentarioa();
            Stage newStage = new Stage();
            inbentario.start(newStage);
            if (stage != null) stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bezeroek egindako erosketen leihoa irekitzen du.
     */
    @FXML
    private void eskaera() {
        try {
            Eskaera eskaerak = new Eskaera();
            Stage newStage = new Stage();
            eskaerak.start(newStage);
            if (stage != null) stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bezeroen kexen leihoa irekitzen du.
     */
    @FXML
    private void kexak() {
        try {
            Kexak kexak = new Kexak();
            Stage newStage = new Stage();
            kexak.start(newStage);
            if (stage != null) stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uneko saltzailearen saioa ixten du.
     * Itxi aurretik berrespena erakusten du eta loginera bideratzen du.
     */
    @FXML
    private void saioaItxi() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Saioa itxi");
        confirm.setHeaderText("Ziur zaude saioa itxi nahi duzula?");
        confirm.setContentText("Saioa itxiz gero, berriro saioa hasi beharko duzu.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                erabiltzailea = null;
                aldaketak = null;
                if (stage != null) stage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
                Scene scene = new Scene(loader.load());
                Stage loginStage = new Stage();
                loginStage.setTitle("Login");
                loginStage.setScene(scene);
                loginStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}