package fxElokuvat;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Tietokannan kysymiselle oma controlleri
 * @author kimhi
 * @version 14.2.2018
 *
 */
public class TietokannanNimiController implements ModalControllerInterface<String> {

    
    @FXML private TextField textVastaus;
    
    private String vastaus = null;
    
    
    /**
     * Ottaa kirjoitetun tietokannan nimen ja sulkee ikkunan
     */
    @FXML private void handleOK() {
        vastaus = textVastaus.getText();
        ModalController.closeStage(textVastaus);
    }

    
    /**
     * Sulkee ikkunan, kun painetaan Cancel
     */
    @FXML private void handleCancel() {
        ModalController.closeStage(textVastaus);
    }


    /**
     * Palauttaa vastauksen
     */
    @Override
    public String getResult() {
        return vastaus;
    }

    
    /**
     * Asetetaan oletusnimi tietokannan kyselyyn
     */
    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
        
    }
    
    
    /**
     * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
     */
    @Override
    public void handleShown() {
        textVastaus.requestFocus();
        
    }


    /**
     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mit‰ nime‰ n‰ytet‰‰n oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                TietokannanNimiController.class.getResource("TietokannanNimiView.fxml"),
                "Tietokannan nimi",
                modalityStage, oletus);
    }
}
