package fxElokuvat;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kanta.Tietue;

/**
 * Kysyt��n tietueen tiedot luomalla sille uusi dialogi
 *
 * @author vesal
 * @version 11.1.2016
 * @param <TYPE> Mink� tyyppisi� olioita k�sitell��n
 *
 */
public class TietueDialogController<TYPE extends Tietue> implements ModalControllerInterface<TYPE>,Initializable  {

    @FXML private ScrollPane panelTietue;
    @FXML private GridPane gridTietue;
    @FXML private Label labelVirhe;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
       // alusta(); 
    }
   
    @FXML private void handleOK() {
        if ( tietueKohdalla != null && tietueKohdalla.anna(tietueKohdalla.ekaKentta()).trim().equals("") ) {
            naytaVirhe("Ei saa olla tyhj�");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

   
    @FXML private void handleCancel() {
        tietueKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

// ========================================================   
    private TYPE tietueKohdalla;
    private TextField[] edits;
    private int kentta = 0;
   

    /**
     * Luodaan GridPaneen tietueen tiedot
     * @param gridTietue mihin tiedot luodaan
     * @param aputietue malli josta tiedot otetaan
     * @return luodut tekstikent�t
     */
    public static<TYPE extends Tietue> TextField[] luoKentat(GridPane gridTietue, TYPE aputietue) {
        gridTietue.getChildren().clear();
        TextField[] edits = new TextField[aputietue.getKenttia()];
       
        for (int i=0, k = aputietue.ekaKentta(); k < aputietue.getKenttia(); k++, i++) {
            Label label = new Label(aputietue.getKysymys(k));
            gridTietue.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridTietue.add(edit, 1, i);
        }
        return edits;
    }
   

    /**
     * Palautetaan komponentin id:st� saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mik� arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
   
   
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikentt�, johon voidaan tulostaa tietueen tiedot.
     */
    protected void alusta() {
        edits = luoKentat(gridTietue, tietueKohdalla);
       
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosTietueeseen((TextField)(e.getSource())));
        // panelTietue.setFitToHeight(true);
    }
   
   
    @Override
    public void setDefault(TYPE oletus) {
        tietueKohdalla = oletus;
        alusta();
        naytaTietue(edits, tietueKohdalla);
    }

   
    @Override
    public TYPE getResult() {
        return tietueKohdalla;
    }
   
   
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
   
   
    /**
     * Mit� tehd��n kun dialogi on n�ytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(tietueKohdalla.ekaKentta(), Math.min(kentta, tietueKohdalla.getKenttia()-1));
        edits[kentta].requestFocus();
    }
   
   
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

   
    /**
     * K�sitell��n teitueeseen tullut muutos
     * @param edit muuttunut kentt�
     */
    protected void kasitteleMuutosTietueeseen(TextField edit) {
        if (tietueKohdalla == null) return;
        int k = getFieldId(edit,tietueKohdalla.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = tietueKohdalla.aseta(k,s); 
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
   
   
    /**
     * N�ytet��n tietueen tiedot TextField komponentteihin
     * @param edits taulukko TextFieldeist� johon n�ytet��n
     * @param tietue n�ytett�v� tietue
     */
    public static void naytaTietue(TextField[] edits, Tietue tietue) {
        if (tietue == null) return;
        for (int k = tietue.ekaKentta(); k < tietue.getKenttia(); k++) {
            edits[k].setText(tietue.anna(k));
        }
    }
   
   
    /**
     * Luodaan tietueen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mit� dataan n�ytet��n oletuksena
     * @param kentta mik� kentt� saa fokuksen kun n�ytet��n
     * @return null jos painetaan Cancel, muuten t�ytetty tietue
     */
    public static<TYPE extends Tietue> TYPE kysyTietue(Stage modalityStage, TYPE oletus, int kentta) {
        return ModalController.<TYPE, TietueDialogController<TYPE>>showModal(
                TietueDialogController.class.getResource("TietueDialogView.fxml"),
                "Kerho",
                modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta) 
                );
    }

}