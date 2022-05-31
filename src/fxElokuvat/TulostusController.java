package fxElokuvat;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/** Tulostuksen oma controlleri
 * @author kimhi
 * @version 14.2.2018
 *
 */
public class TulostusController implements ModalControllerInterface<Object> {

    /**
     * Tulostusalue, mitä halutaan tulostaa
     */
    @FXML TextArea tulostusAlue;
    
    
    /**
     * Sulkee tulostus-ikkunan
     */
    @FXML private void handleOK() {
        ModalController.closeStage(tulostusAlue);
    }
    
    
    /**
     * Tulostaa näkyvät tiedot
     */
    @FXML private void handleTulostus() {
        Dialogs.showMessageDialog("Vielä ei osata tulostaa");
    }


    /**
     * Ei käytössä
     */
    @Override
    public Object getResult() {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Ei käytössä
     */
    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }


    /**
     * Ei käytössä
     */
    @Override
    public void setDefault(Object arg0) {
        // TODO Auto-generated method stub
        
    }

}
