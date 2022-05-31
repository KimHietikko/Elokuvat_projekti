package fxElokuvat;

import elokuvarekisteri.Elokuvarekisteri;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


/** Elokuvat, pääikkuna
 * @author kimhi
 * @version 30.1.2018
 *
 */
public class ElokuvatMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("ElokuvatView.fxml"));
            final Pane root = ldr.load();
            final ElokuvatController elokuvatCtrl = (ElokuvatController) ldr.getController();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("elokuvat.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Elokuvat");
            
            primaryStage.setOnCloseRequest((event) -> {
                if ( !elokuvatCtrl.voikoSulkea() ) {
                    event.consume();
                }
            });
            
            Elokuvarekisteri elokuvarekisteri = new Elokuvarekisteri();
            elokuvatCtrl.setElokuvarekisteri(elokuvarekisteri);
            
            primaryStage.show();
            Application.Parameters params = getParameters();
            if ( params.getRaw().size() > 0 ) {
                elokuvatCtrl.lueTiedosto(params.getRaw().get(0));
            }
            else {
                if ( !elokuvatCtrl.avaa() ) {
                    Platform.exit();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}