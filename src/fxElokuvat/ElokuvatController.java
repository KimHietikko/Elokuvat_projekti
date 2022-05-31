package fxElokuvat;


import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import kanta.Tarkistaja;
import elokuvarekisteri.Elokuvarekisteri;
import elokuvarekisteri.Elokuvat;
import elokuvarekisteri.Elokuva;
import elokuvarekisteri.Nayttelija;
import elokuvarekisteri.SailoException;
import static fxElokuvat.TietueDialogController.*;




/**
 * @author kimhi
 * @version 12.2.2018
 * P‰‰ikkunan controlleri
 */
public class ElokuvatController implements Initializable{
    
    
    private Elokuva apuelokuva = new Elokuva();
    private Nayttelija apunayttelija = new Nayttelija(); // N‰yttelij‰ jolta voidaan kysell‰ tietoja.
    
    @FXML private TextField hakuehto;
    
    @FXML private ScrollPane panelElokuva; 
    
    @FXML private GridPane gridElokuva;

    @FXML private StringGrid<Nayttelija> tableNayttelijat;
    
    @FXML private ComboBoxChooser<String> cbKentat;
    
    @FXML private ListChooser<Elokuva> chooserElokuvat;
    
    
    private String tietokannanNimi = "Elokuvat";

    private Elokuvarekisteri elokuvarekisteri;
    
    private Elokuva elokuvaKohdalla;
    
    private TextField edits[];
    
    private int kentta = 0;
    
    
    /**
     * Avaa suunnitelman selaimessa
     */
    @FXML private void handleApua() {
        avustus();
    }
    
    
    /**
     * Avaa tietokannan nimi ikkunan, johon voi laittaa jonkun toisen tietokannan nimen ja avata sen
     */
    @FXML private void handleAvaa() {
        ModalController.showModal(ElokuvatController.class.getResource("TietokannanNimiView.fxml"), "Avaa", null, "");
    }
    
    
    /**
     * Hakupalkin toiminto
     */
    @FXML private void handleHakuehto() {
        hae(0);
    }
    
    
    /**
     * Lopeta -kohdan toiminto, tallentaa ja sulkee ohjelman
     */
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }
     
    
    /**
     * Avaa uusi elokuva -dialogin
     */
    @FXML private void handleMuokkaaElokuva() {
        muokkaa(kentta);
    }
    
    
    /**
     * Avaa lis‰‰ n‰yttelij‰t -dialogin, jolloin n‰yttelij‰‰ pystyy muokata
     */
    @FXML private void handleMuokkaaNayttelija() {
        muokkaaNayttelijaa();
    }
    
    
    /**
     * Poistaa elokuvan
     */
    @FXML private void handlePoistaElokuva() {
        poistaElokuva();
    }
    
    
    /**
     * Poistaa n‰yttelij‰n
     */
    @FXML private void handlePoistaNayttelija() {
        poistaNayttelija();
    }
    
    
    /**
     * Antaa randomin elokuvaehdotuksen
     */
    @FXML private void handleRandom() {
        random();
    }
    
    
    /**
     * Tallentaa muutokset
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * N‰ytt‰‰ ohjelman tiedot
     */
    @FXML private void handleTietoja() {
        ModalController.showModal(ElokuvatController.class.getResource("AboutView.fxml"), "Tietoja", null, "");
    }
    
    
    /**
     * Avaa tulostusn‰kym‰n
     */
    @FXML private void handleTulostus() {
        ModalController.showModal(ElokuvatController.class.getResource("TulostusView.fxml"), "Tulostus", null, "");
    }
    
    
    /**
     * Lis‰‰ uuden elokuvan
     */
    @FXML private void handleUusiElokuva() {
        uusiElokuva();
    }
    
    
    /**
     * Lis‰‰ uuden n‰yttelij‰n
     */
    @FXML private void handleUusiNayttelija() {
        uusiNayttelija();
    }
    
    
//=========================================================================   
    
    
    /**
     * Alustaa elokuvan tiedot
     */
    protected void alusta() {
        chooserElokuvat.clear();
        chooserElokuvat.addSelectionListener(e -> naytaElokuva());
        cbKentat.clear(); 
        for (int k = apuelokuva.ekaKentta(); k < apuelokuva.getKenttia(); k++) {
            cbKentat.add(apuelokuva.getKysymys(k), null); 
        }
        cbKentat.getSelectionModel().select(0); 
        
        edits = TietueDialogController.luoKentat(gridElokuva, apuelokuva);
        for (TextField edit: edits) {
             if ( edit != null ) { 
                 edit.setEditable(false); 
                 edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); }); 
                 edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta)); 
                 edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);});
             }
        }
        
        
        // alustetaan n‰yttelij‰taulukon otsikot
        int eka = apunayttelija.ekaKentta();
        int lkm = apunayttelija.getKenttia();
        String[] headings = new String[lkm-eka];
        for (int i=0, k=eka; k<lkm; i++, k++) {
            headings[i] = apunayttelija.getKysymys(k);
        }
        tableNayttelijat.initTable(headings);
        tableNayttelijat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableNayttelijat.setEditable(false);
        tableNayttelijat.setPlaceholder(new Label("Ei viel‰ n‰yttelijˆit‰"));
        
        
        tableNayttelijat.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaNayttelijaa(); } );
        tableNayttelijat.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaNayttelijaa();});
           
    }
    
    
    /**
     * Kysyt‰‰n tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = TietokannanNimiController.kysyNimi(null, tietokannanNimi);
        if (uusinimi == null) {
            return false;
        }
        lueTiedosto(uusinimi);
        return true;
    }
    
    
    /**
     * N‰ytet‰‰n ohjelman suunnitelma erillisess‰ selaimessa.
     */
    private void avustus() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2018k/ht/kijohiet");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    
    /**
     * Hakee elokuvien tiedot listaan
     * @param enr elokuvan numero, joka aktivoidaan haun j‰lkeen
     */
    protected void hae(int enr) {
        int eNro = enr; // eNro elokuvan numero, joka aktivoidaan haun j‰lkeen
        if ( eNro <= 0 ) {
            Elokuva kohdalla = elokuvaKohdalla;
            if ( kohdalla != null ) {
                eNro = kohdalla.getTunnusNro();
            }
        }
        
        int k = cbKentat.getSelectedIndex() + apuelokuva.ekaKentta();
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) {
            ehto = "*" + ehto + "*";
        }
        
        chooserElokuvat.clear(); 
        
        int index = 0; 
        Collection<Elokuva> elokuvat; 
        try { 
            elokuvat = elokuvarekisteri.etsi(ehto, k); 
            int i = 0; 
            for (Elokuva elokuva : elokuvat) { 
                if (elokuva .getTunnusNro() == eNro) {
                    index = i; 
                }
                chooserElokuvat.add(elokuva.getNimi(), elokuva ); 
                i++; 
            } 
        } catch (SailoException ex) { 
            Dialogs.showMessageDialog("Elokuvan hakemisessa ongelmia! " + ex.getMessage()); 
        }       
        chooserElokuvat.setSelectedIndex(index); // t‰st‰ tulee muutosviesti joka n‰ytt‰‰ elokuvan
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    
    /**
     * Alustaa elokuvarekisterin lukemalla sen valitun nimisest‰ tiedostosta
     * @param nimi tiedosto josta elokuvarekisterin tiedot luetaan
     * @return null jos onnistuu, muuten virhe
     */
    protected String lueTiedosto(String nimi) {
        tietokannanNimi = nimi;
        setTitle("Tietokanta - " + tietokannanNimi);
        try {
            elokuvarekisteri.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage();
            if (virhe != null) {
                Dialogs.showMessageDialog(virhe);
            }
            return virhe;
        }
    }
    
    
    /**
     * Muokataan elokuvaa
     * @param k kentt‰
     */
    private void muokkaa(int k) { 
         if ( elokuvaKohdalla == null ) {
             return; 
         }
         try { 
             Elokuva elokuva; 
             elokuva = TietueDialogController.kysyTietue(null, elokuvaKohdalla.clone(), k); 
             if ( elokuva == null ) {
                 return;
             }
             elokuvarekisteri.korvaaTaiLisaa(elokuva); 
             hae(elokuva.getTunnusNro()); 
         } catch (CloneNotSupportedException e) { 
             //
         } catch (SailoException e) { 
             Dialogs.showMessageDialog(e.getMessage()); 
         } 
    }
    

    /**
     * Muokataan n‰yttelij‰‰
     */
    private void muokkaaNayttelijaa() {
        int r = tableNayttelijat.getRowNr();
        if ( r < 0 ) {
            return; // klikattu ehk‰ otsikkorivi‰
        }
        Nayttelija nayttelija = tableNayttelijat.getObject();
        if ( nayttelija == null ) return;
        int k = tableNayttelijat.getColumnNr()+nayttelija.ekaKentta();
        try {
            nayttelija = TietueDialogController.kysyTietue(null, nayttelija.clone(), k);
            if ( nayttelija == null ) {
                return;
            }
            elokuvarekisteri.korvaaTaiLisaa(nayttelija); 
            naytaNayttelijat(elokuvaKohdalla); 
            tableNayttelijat.selectRow(r);  // j‰rjestet‰‰n sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */ 
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lis‰‰misess‰: " + e.getMessage());
        }
    }
    
    
    /**
     * N‰ytt‰‰ listasta valitun elokuvan tiedot, tilap‰isesti yhteen isoon edit-kentt‰‰n
     */
    protected void naytaElokuva() {
        elokuvaKohdalla = chooserElokuvat.getSelectedObject();
        if (elokuvaKohdalla == null) {
            return;
        }
        
        TietueDialogController.naytaTietue(edits, elokuvaKohdalla);
        naytaNayttelijat(elokuvaKohdalla);
        
    }
    
    
    /**
     * Lis‰t‰‰n yhden harrastuksen tiedot taulukkoon. 
     * @param nayttelija n‰yttelij‰ joka n‰ytet‰‰n
     */
    private void naytaNayttelija(Nayttelija nayttelija) {
        int kenttia = nayttelija.getKenttia();
        String[] rivi = new String[kenttia-nayttelija.ekaKentta()];
        for (int i=0, k=nayttelija.ekaKentta(); k < kenttia; i++, k++) {
            rivi[i] = nayttelija.anna(k);
        }
        tableNayttelijat.add(nayttelija,rivi);
    }
    
    
    /**
     * N‰ytet‰‰n n‰yttelij‰t taulukkoon.  Tyhjennet‰‰n ensin taulukko ja sitten
     * lis‰t‰‰n siihen kaikki n‰yttelij‰t
     * @param elokuva elokuva, jonka n‰yttelij‰t n‰ytet‰‰n
     */
    private void naytaNayttelijat(Elokuva elokuva) {
        tableNayttelijat.clear();
        if ( elokuva == null ) {
            return;
        }
       
        try {
            List<Nayttelija> nayttelijat = elokuvarekisteri.annaNayttelijat(elokuva);
            if ( nayttelijat.size() == 0 ) {
                return;
            }
            for (Nayttelija nayttelija: nayttelijat) {
                naytaNayttelija(nayttelija);
            }
        } catch (SailoException e) {
            // naytaVirhe(e.getMessage());
        } 
    }
    
    
    /**
     * Poistetaan listalta valittu elokuva
     */
    private void poistaElokuva() {
        if ( elokuvaKohdalla == null ) {
            return;
        }
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko elokuva: " + elokuvaKohdalla.getNimi(), "Kyll‰", "Ei") ) {
            return;
        }
        elokuvarekisteri.poista(elokuvaKohdalla);
        int index = chooserElokuvat.getSelectedIndex();
        hae(0);
        chooserElokuvat.setSelectedIndex(index);
    }
    
    
    /**
     * Poistetaan n‰yttelij‰taulukosta valitulla kohdalla oleva n‰yttelij‰.
     */
    private void poistaNayttelija() {
        Nayttelija nayttelija = tableNayttelijat.getObject();
        if ( nayttelija == null ) {
            return;
        }
        int rivi = tableNayttelijat.getRowNr();
        elokuvarekisteri.poistaNayttelija(nayttelija);
        naytaNayttelijat(elokuvaKohdalla);
        tableNayttelijat.selectRow(rivi);
    }
    
    
    /**
     * N‰ytet‰‰n random elokuvaehdotus
     */
    private void random() {
        Dialogs.showMessageDialog("Elokuvaehdotus: " + Elokuvat.alkiot[Tarkistaja.rand(0, Elokuvat.alkiot.length-2)].getNimi());
    }
    
    
    /**
     * @param elokuvarekisteri rekisteri jota k‰ytet‰‰n t‰ss‰ k‰yttˆliittym‰ss‰
     */
    public void setElokuvarekisteri(Elokuvarekisteri elokuvarekisteri) {
        this.elokuvarekisteri = elokuvarekisteri;
    }
    
    
    /**
     * Asettaa otsikon ohjelmalle
     */
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    
    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstin‰ 
     */
    private String tallenna() {
        try {
            elokuvarekisteri.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Talletuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * Tulostaa elokuvan tiedot muun muassa n‰yttelij‰t
     * @param os tietovirta johon tulostetaan
     * @param elokuva tulostettava elokuva
     */
    public void tulosta(PrintStream os, final Elokuva elokuva) {
        os.println("----------------------------------------------");
        elokuva.tulosta(os);
        os.println("----------------------------------------------");
        try {
            List<Nayttelija> nayttelijat = elokuvarekisteri.annaNayttelijat(elokuva);
            for (Nayttelija nayttelija:nayttelijat) {
                nayttelija.tulosta(os);
            }  
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("N‰yttelijˆiden lis‰‰misess‰ ongelmia! " + ex.getMessage());
        }
    }
    
    
    /**
     * Tulostaa listassa olevat elokuvat tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki elokuvat");
            Collection<Elokuva> elokuvat = elokuvarekisteri.etsi("", -1);
            for (Elokuva elokuva:elokuvat) {
                tulosta(os, elokuva);
                os.println("\n\n");
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Elokuvan hakemisessa ongelmia! " + ex.getMessage());
        }
    }
    
    
    /**
     * Luo uuden elokuvan jota aletaan editoimaan 
     */
    protected void uusiElokuva() {
        try {
            Elokuva uusi = new Elokuva();
            uusi = TietueDialogController.kysyTietue(null, uusi, 0); 
            if ( uusi == null ) {
                return; 
            }
            uusi.rekisteroi(); 
            elokuvarekisteri.lisaa(uusi);
            hae(uusi.getTunnusNro()); 
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
    }
    
    
    /**
     * Luo uuden n‰yttelij‰n jota aletaan editoimaan 
     */
    protected void uusiNayttelija() {
        if ( elokuvaKohdalla == null ) {
            return;
        }
        try {
            Nayttelija uusi = new Nayttelija(elokuvaKohdalla.getTunnusNro());
            uusi = TietueDialogController.kysyTietue(null, uusi, 0);
            if ( uusi == null ) {
                return;
            }
            uusi.rekisteroi();
            elokuvarekisteri.lisaa(uusi);
            naytaNayttelijat(elokuvaKohdalla); 
            tableNayttelijat.selectRow(1000);  // j‰rjestet‰‰n viimeinen rivi valituksi
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Lis‰‰minen ep‰onnistui: " + e.getMessage());
        } 
    }
    
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
}
