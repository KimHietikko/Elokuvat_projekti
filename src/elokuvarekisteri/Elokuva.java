package elokuvarekisteri;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tarkistaja;
import kanta.Tietue;

import static kanta.Tarkistaja.*;
import java.io.*;

/**
 * @author kimhi
 * @version 5.3.2018
 * Elokuvarekisterin elokuva, jolla on kaikki tarvittavat tiedot
 */
public class Elokuva implements Cloneable, Tietue{
    
    
    private int        tunnusNro;
    private String     nimi           = "";
    private String     valmistusvuosi = "0";
    private String     genre          = "";
    private String     arvosana       = "0.0";
    private String     ohjaaja        = "";
    
    private static int seuraavaNro    = 1;
    
    
    private static Tarkistaja tarkistaja = new Tarkistaja();
    
    
    /**
     * Antaa k:n kent‰n sis‰llˆn merkkijonona
     * @param k monenenko kent‰n sis‰ltˆ palautetaan
     * @return kent‰n sis‰ltˆ merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + valmistusvuosi;
        case 3: return "" + genre;
        case 4: return "" + arvosana;
        case 5: return "" + ohjaaja;
        default: return "Virhe";
        }
    }
    
    
    /**
     * Asettaa k:n kent‰n arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kent‰n arvo asetetaan
     * @param jono jonoa joka asetetaan kent‰n arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Elokuva elokuva = new Elokuva();
     *   elokuva.aseta(1,"Kummiset‰ 1") === null;
     *   elokuva.aseta(2,"19999") =R= "Valmistusvuosi v‰‰r‰‰ muotoa";
     *   elokuva.aseta(2,"1940") === null;
     *   elokuva.aseta(3,"Draama") === null;
     *   elokuva.aseta(4,"3.33") === "Arvosana v‰‰r‰‰ muotoa";
     *   elokuva.aseta(5,"Francis Ford Coppola") === null;
     * </pre>
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, 'ß', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
            String virhe = tarkistaja.tarkistaValmistusvuosi(tjono);
            if ( virhe != null ) return virhe;
            valmistusvuosi = tjono;
            return null;
        case 3:
            genre = tjono;
            return null;
        case 4:
            String virhe2 = tarkistaja.tarkistaArvosana(tjono);
            if ( virhe2 != null ) return virhe2;
            arvosana = tjono;
            return null;
        case 5:
            ohjaaja = tjono;
            return null;
        default:
            return "Virhe";
        }
    }
    

    /**
     * Tehd‰‰n identtinen klooni elokuvasta
     * @return Object kloonattu elokuva
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Elokuva elokuva = new Elokuva();
     *   elokuva.parse("   1  |  Kummiset‰ I   | 1972  | Rikos, Draama | 9.2");
     *   Elokuva kopio = elokuva.clone();
     *   kopio.toString() === elokuva.toString();
     *   elokuva.parse("   2  |  Kummiset‰ I   | 1972  | Rikos, Draama | 9.2");;
     *   kopio.toString().equals(elokuva.toString()) === false;
     * </pre>
     */
    @Override
    public Elokuva clone() throws CloneNotSupportedException {
        Elokuva uusi;
        uusi = (Elokuva) super.clone();
        return uusi;
    }
    
    
    /**
     * Eka kentt‰ joka on mielek‰s kysytt‰v‰ksi
     * @return ekan kent‰n indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Apumetodi, jolla saadaan t‰ytetty‰ testiarvot elokuvalle.
     */
    public void elokuvanTiedot() {
        nimi = "Kummiset‰ " + rand(0, 9999);
        valmistusvuosi = "" + rand(1900, 2018);
        genre = "Draama";
        arvosana = "8.0";
        ohjaaja = "Francis Ford Coppola";
    }
    
    
    @Override 
    public boolean equals(Object elokuva) { 
        if ( elokuva == null ) {
            return false; 
        }
        return this.toString().equals(elokuva.toString()); 
    }
    
    
    /**
     * Palauttaa elokuvan kenttien lukum‰‰r‰n
     * @return kenttien lukum‰‰r‰
     */
    @Override
    public int getKenttia() {
        return 6;
    }
    
    
    /**
     * Palauttaa k:tta elokuvan kentt‰‰ vastaavan kysymyksen
     * @param k kuinka monennen kent‰n kysymys palautetaan (0-alkuinen)
     * @return k:netta kentt‰‰ vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnus nro";
        case 1: return "Nimi";
        case 2: return "Valmistusvuosi";
        case 3: return "Genre";
        case 4: return "Arvosana";
        case 5: return "Ohjaaja";
        default: return "Virhe";
        }
    }
    
    
    /**
     * @return elokuvan nimi
     * @example
     * <pre name="test">
     *   Elokuva kummiseta = new Elokuva();
     *   kummiseta.elokuvanTiedot();
     *   kummiseta.getNimi() =R= "Kummiset‰ .*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * Palauttaa elokuvan tunnusnumeron.
     * @return elokuvan tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    @Override 
    public int hashCode() { 
        return tunnusNro; 
    }
    
    
    /** 
     * Selvit‰‰ elokuvan tiedot | erotellusta merkkijonosta 
     * Pit‰‰ huolen ett‰ seuraavaNro on suurempi kuin tuleva tunnusNro. 
     * @param rivi josta elokuvan tiedot otetaan 
     *  
     * @example 
     * <pre name="test"> 
     *   Elokuva elokuva = new Elokuva(); 
     *   elokuva.parse("   1  |  Kummiset‰ I   | 1972  | Rikos, Draama | 9.2"); 
     *   elokuva.getTunnusNro() === 1; 
     *   elokuva.toString().startsWith("1|Kummiset‰ I|1972|Rikos, Draama|9.2|") === true; // on enemm‰kin kuin 3 kentt‰‰, siksi loppu | 
     * 
     *   elokuva.rekisteroi(); 
     *   int n = elokuva.getTunnusNro(); 
     *   elokuva.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero 
     *   elokuva.rekisteroi();           // ja tarkistetaan ett‰ seuraavalla kertaa tulee yht‰ isompi 
     *   elokuva.getTunnusNro() === n+20+1; 
     *      
     * </pre> 
     */ 
    public void parse(String rivi) { 
        StringBuffer sb = new StringBuffer(rivi); 
        for (int k = 0; k < getKenttia(); k++) {
            aseta(k, Mjonot.erota(sb, '|'));
        }
    } 
     
  
    /**
     * Antaa elokuvalle rekisterinumeron.
     * @return elokuvan uusi tunnusNro
     * @example
     * <pre name="test">
     *   Elokuva kummiseta1 = new Elokuva();
     *   kummiseta1.getTunnusNro() === 0;
     *   kummiseta1.rekisteroi();
     *   Elokuva kummiseta2 = new Elokuva();
     *   kummiseta2.rekisteroi();
     *   int n1 = kummiseta1.getTunnusNro();
     *   int n2 = kummiseta2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    } 
  
  
    /** 
     * Asettaa tunnusnumeron ja samalla varmistaa ett‰ 
     * seuraava numero on aina suurempi kuin t‰h‰n menness‰ suurin. 
     * @param nr asetettava tunnusnumero 
     */ 
    private void setTunnusNro(int nr) { 
        tunnusNro = nr; 
        if (tunnusNro >= seuraavaNro) {
            seuraavaNro = tunnusNro + 1; 
        }
    } 
    
    
    /** 
     * Palauttaa elokuvan tiedot merkkijonona jonka voi tallentaa tiedostoon. 
     * @return elokuva tolppaeroteltuna merkkijonona  
     * @example 
     * <pre name="test"> 
     *   Elokuva elokuva = new Elokuva(); 
     *   elokuva.parse("   1  |  Kummiset‰ I   | 1972  | Rikos, Draama | 9.2"); 
     *   elokuva.toString().startsWith("1|Kummiset‰ I|1972|Rikos, Draama|9.2|") === true; // on enemm‰kin kuin 3 kentt‰‰, siksi loppu | 
     * </pre>   
     */ 
    @Override 
    public String toString() { 
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }
     
    
    /**
     * Tulostetaan elokuvan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    } 
  
  
    /**
     * Tulostetaan elokuvan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro, 3) + "  " + nimi + "  ");
        out.println("Valmistusvuosi:  " + valmistusvuosi);
        out.println("Genre:  " + genre);
        out.println("Arvosana:  " + arvosana + "/10");
        out.println("Ohjaaja:  " + ohjaaja + "\n");
    } 
}
