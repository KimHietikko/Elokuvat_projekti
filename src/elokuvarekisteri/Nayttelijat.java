package elokuvarekisteri;

import java.util.*;

import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.io.PrintWriter; 

/**
 * @author kimhi
 * @version 11.3.2018
 * Elokuvarekisterin Nayttelijat-luokka, joka osaa luoda uuden nayttelijan
 */
public class Nayttelijat implements Iterable<Nayttelija> {

    
    private boolean muutettu = false;
    
    private String tiedostonPerusNimi = "";
    
    
    /** Taulukko näyttelijöistä */
    private final Collection<Nayttelija> alkiot = new ArrayList<Nayttelija>();
    
    
    /**
     * Näyttelijöiden alustaminen
     */
    public Nayttelijat() {
        // toistaiseksi ei tarvitse tehdä mitään
    }
    
    
    /** 
     * Palauttaa varakopiotiedoston nimen 
     * @return varakopiotiedoston nimi 
     */ 
    public String getBakNimi() { 
        return tiedostonPerusNimi + ".bak"; 
    } 
  
  
    /**
     * Palauttaa näyttelijöiden lukumäärän
     * @return näyttelijöiden lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    } 
  
  
    /** 
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen 
     * @return tallennustiedoston nimi 
     */ 
    public String getTiedostonNimi() { 
        return tiedostonPerusNimi + ".dat"; 
    } 
  
  
    /** 
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen 
     * @return tallennustiedoston nimi 
     */ 
    public String getTiedostonPerusNimi() { 
        return tiedostonPerusNimi; 
    } 
    
    
    /**
     * Iteraattori kaikkien nayttelijoiden läpikäymiseen
     * @return nayttelijaiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Nayttelijat nayttelijat = new Nayttelijat();
     *  Nayttelija nayttelija21 = new Nayttelija(2); nayttelijat.lisaa(nayttelija21);
     *  Nayttelija nayttelija11 = new Nayttelija(1); nayttelijat.lisaa(nayttelija11);
     *  Nayttelija nayttelija22 = new Nayttelija(2); nayttelijat.lisaa(nayttelija22);
     *  Nayttelija nayttelija12 = new Nayttelija(1); nayttelijat.lisaa(nayttelija12);
     *  Nayttelija nayttelija23 = new Nayttelija(2); nayttelijat.lisaa(nayttelija23);
     * 
     *  Iterator<Nayttelija> i2=nayttelijat.iterator();
     *  i2.next() === nayttelija21;
     *  i2.next() === nayttelija11;
     *  i2.next() === nayttelija22;
     *  i2.next() === nayttelija12;
     *  i2.next() === nayttelija23;
     *  i2.next() === nayttelija12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int eNrot[] = {2,1,2,1,2};
     *  
     *  for ( Nayttelija nayttelija : nayttelijat ) { 
     *    nayttelija.getElokuvaNro() === eNrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Nayttelija> iterator() {
        return alkiot.iterator();
    }
    
    
    /**
     * Korvaa näyttelijän tietorakenteessa.  Ottaa näyttelijän omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva näyttelijä.  Jos ei löydy,
     * niin lisätään uutena näyttelijänä.
     * @param nayttelija lisättävän näyttelijän viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Nayttelijat nayttelijat = new Nayttelijat();
     * Nayttelija nayttelija1 = new Nayttelija(), nayttelija2 = new Nayttelija();
     * nayttelija1.rekisteroi(); nayttelija2.rekisteroi();
     * nayttelijat.getLkm() === 0;
     * nayttelijat.korvaaTaiLisaa(nayttelija1); nayttelijat.getLkm() === 1;
     * nayttelijat.korvaaTaiLisaa(nayttelija2); nayttelijat.getLkm() === 2;
     * Nayttelija nayttelija3 = nayttelija1.clone();
     * nayttelija3.aseta(2,"kkk");
     * Iterator<Nayttelija> i2=nayttelijat.iterator();
     * i2.next() === nayttelija1;
     * nayttelijat.korvaaTaiLisaa(nayttelija3); nayttelijat.getLkm() === 2;
     * i2=nayttelijat.iterator();
     * Nayttelija n = i2.next();
     * n === nayttelija3;
     * n == nayttelija3 === true;
     * n == nayttelija1 === false;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Nayttelija nayttelija) throws SailoException {
        int id = nayttelija.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (((ArrayList<Nayttelija>) alkiot).get(i).getTunnusNro() == id) {
                ((ArrayList<Nayttelija>) alkiot).set(i, nayttelija);
                muutettu = true;
                return;
            }
        }
        lisaa(nayttelija);
    }
    
    
    /**
     * Lisää uuden nayttelijan tietorakenteeseen
     * @param nayttelija lisättävä nayttelija
     */
    public void lisaa(Nayttelija nayttelija) {
        alkiot.add(nayttelija);
        muutettu = true;
    }
    
    
    /** 
     * Luetaan aikaisemmin annetun nimisestä tiedostosta 
     * @throws SailoException jos tulee poikkeus 
     */ 
    public void lueTiedostosta() throws SailoException { 
        lueTiedostosta(getTiedostonPerusNimi()); 
    } 
   
     
    /** 
     * Lukee näyttelijät tiedostosta. 
     * @param tied tiedoston nimen alkuosa 
     * @throws SailoException jos lukeminen epäonnistuu
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * #import java.io.File; 
     *  Nayttelijat nayttelijat = new Nayttelijat(); 
     *  Nayttelija nayttelija21 = new Nayttelija(); nayttelija21.nayttelijanTiedot(2); 
     *  Nayttelija nayttelija11 = new Nayttelija(); nayttelija11.nayttelijanTiedot(1); 
     *  Nayttelija nayttelija22 = new Nayttelija(); nayttelija22.nayttelijanTiedot(2);
     *  Nayttelija nayttelija12 = new Nayttelija(); nayttelija12.nayttelijanTiedot(1);  
     *  Nayttelija nayttelija23 = new Nayttelija(); nayttelija23.nayttelijanTiedot(2);  
     *  String tiedNimi = "testielokuvat"; 
     *  File ftied = new File(tiedNimi+".dat"); 
     *  ftied.delete(); 
     *  nayttelijat.lueTiedostosta(tiedNimi); #THROWS SailoException 
     *  nayttelijat.lisaa(nayttelija21); 
     *  nayttelijat.lisaa(nayttelija11); 
     *  nayttelijat.lisaa(nayttelija22); 
     *  nayttelijat.lisaa(nayttelija12); 
     *  nayttelijat.lisaa(nayttelija23); 
     *  nayttelijat.tallenna(); 
     *  nayttelijat = new Nayttelijat(); 
     *  nayttelijat.lueTiedostosta(tiedNimi); 
     *  Iterator<Nayttelija> i = nayttelijat.iterator(); 
     *  i.next().toString() === nayttelija21.toString(); 
     *  i.next().toString() === nayttelija11.toString(); 
     *  i.next().toString() === nayttelija22.toString(); 
     *  i.next().toString() === nayttelija12.toString(); 
     *  i.next().toString() === nayttelija23.toString(); 
     *  i.hasNext() === false; 
     *  nayttelijat.lisaa(nayttelija23); 
     *  nayttelijat.tallenna(); 
     *  ftied.delete() === true; 
     *  File fbak = new File(tiedNimi+".bak"); 
     *  fbak.delete() === true; 
     * </pre> 
     */ 
    public void lueTiedostosta(String tied) throws SailoException { 
        setTiedostonPerusNimi(tied); 
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) { 
   
            String rivi; 
            while ( (rivi = fi.readLine()) != null ) { 
                rivi = rivi.trim(); 
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) {
                    continue; 
                }
                Nayttelija nayttelija = new Nayttelija(); 
                nayttelija.parse(rivi);
                lisaa(nayttelija); 
            } 
            muutettu = false;
   
        } catch ( FileNotFoundException e ) { 
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea"); 
        } catch ( IOException e ) { 
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage()); 
        } 
    }
    
    
    /**
     * Haetaan kaikki elokuvan nayttelijat
     * @param tunnusnro elokuvan tunnusnumero jonka nayttelijoita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin nayttelijoihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Nayttelijat nayttelijat = new Nayttelijat();
     *  Nayttelija nayttelija21 = new Nayttelija(2); nayttelijat.lisaa(nayttelija21);
     *  Nayttelija nayttelija11 = new Nayttelija(1); nayttelijat.lisaa(nayttelija11);
     *  Nayttelija nayttelija22 = new Nayttelija(2); nayttelijat.lisaa(nayttelija22);
     *  Nayttelija nayttelija12 = new Nayttelija(1); nayttelijat.lisaa(nayttelija12);
     *  Nayttelija nayttelija23 = new Nayttelija(2); nayttelijat.lisaa(nayttelija23);
     *  Nayttelija nayttelija51 = new Nayttelija(5); nayttelijat.lisaa(nayttelija51);
     *  
     *  List<Nayttelija> loytyneet;
     *  loytyneet = nayttelijat.naytaNayttelijat(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = nayttelijat.naytaNayttelijat(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == nayttelija11 === true;
     *  loytyneet.get(1) == nayttelija12 === true;
     *  loytyneet = nayttelijat.naytaNayttelijat(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == nayttelija51 === true;
     * </pre> 
     */
    public List<Nayttelija> naytaNayttelijat(int tunnusnro) {
        List<Nayttelija> loydetyt = new ArrayList<Nayttelija>();
        for (Nayttelija nayttelija : alkiot) {
            if (nayttelija.getElokuvaNro() == tunnusnro) loydetyt.add(nayttelija);
        }
        return loydetyt;
    } 
    
    
    /**
     * Poistaa valitun näyttelijän
     * @param nayttelija poistettava näyttelijä
     * @return tosi jos löytyi poistettava tietue
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     *  Nayttelijat nayttelijat = new Nayttelijat();
     *  Nayttelija nayttelija21 = new Nayttelija(); nayttelija21.nayttelijanTiedot(2);
     *  Nayttelija nayttelija11 = new Nayttelija(); nayttelija11.nayttelijanTiedot(1);
     *  Nayttelija nayttelija22 = new Nayttelija(); nayttelija22.nayttelijanTiedot(2);
     *  Nayttelija nayttelija12 = new Nayttelija(); nayttelija12.nayttelijanTiedot(1);
     *  Nayttelija nayttelija23 = new Nayttelija(); nayttelija23.nayttelijanTiedot(2);
     *  nayttelijat.lisaa(nayttelija21);
     *  nayttelijat.lisaa(nayttelija11);
     *  nayttelijat.lisaa(nayttelija22);
     *  nayttelijat.lisaa(nayttelija12);
     *  nayttelijat.poista(nayttelija23) === false ; nayttelijat.getLkm() === 4;
     *  nayttelijat.poista(nayttelija11) === true;   nayttelijat.getLkm() === 3;
     *  List<Nayttelija> n = nayttelijat.naytaNayttelijat(1);
     *  n.size() === 1;
     *  n.get(0) === nayttelija12;
     * </pre>
     */
    public boolean poista(Nayttelija nayttelija) {
        boolean ret = alkiot.remove(nayttelija);
        if (ret) {
            muutettu = true;
        }
        return ret;
    }
    
    
    /**
     * Poistaa kaikki tietyn tietyn elokuvan näyttelijät
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin
     * @example
     * <pre name="test">
     *  Nayttelijat nayttelijat = new Nayttelijat();
     *  Nayttelija nayttelija21 = new Nayttelija(); nayttelija21.nayttelijanTiedot(2);
     *  Nayttelija nayttelija11 = new Nayttelija(); nayttelija11.nayttelijanTiedot(1);
     *  Nayttelija nayttelija22 = new Nayttelija(); nayttelija22.nayttelijanTiedot(2);
     *  Nayttelija nayttelija12 = new Nayttelija(); nayttelija12.nayttelijanTiedot(1);
     *  Nayttelija nayttelija23 = new Nayttelija(); nayttelija23.nayttelijanTiedot(2);
     *  nayttelijat.lisaa(nayttelija21);
     *  nayttelijat.lisaa(nayttelija11);
     *  nayttelijat.lisaa(nayttelija22);
     *  nayttelijat.lisaa(nayttelija12);
     *  nayttelijat.lisaa(nayttelija23);
     *  nayttelijat.poistaElokuvanNayttelijat(2) === 3;  nayttelijat.getLkm() === 2;
     *  nayttelijat.poistaElokuvanNayttelijat(3) === 0;  nayttelijat.getLkm() === 2;
     *  List<Nayttelija> n = nayttelijat.naytaNayttelijat(2);
     *  n.size() === 0;
     *  n = nayttelijat.naytaNayttelijat(1);
     *  n.get(0) === nayttelija11;
     *  n.get(1) === nayttelija12;
     * </pre>
     */
    public int poistaElokuvanNayttelijat(int tunnusNro) {
        int n = 0;
        for (Iterator<Nayttelija> it = alkiot.iterator(); it.hasNext();) {
            Nayttelija nayttelija = it.next();
            if ( nayttelija.getElokuvaNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) {
            muutettu = true;
        }
        return n;
    }
    
    
    /**   
     * Laitetaan muutos, jolloin pakotetaan tallentamaan.     
     */   
    public void setMuutos() {   
        muutettu = true;
    }
    
    
    /** 
     * Asettaa tiedoston perusnimen ilman tarkenninta 
     * @param tied tallennustiedoston perusnimi 
     */ 
    public void setTiedostonPerusNimi(String tied) { 
        tiedostonPerusNimi = tied; 
    }
    
    
    /** 
     * Tallentaa näyttelijät tiedostoon. 
     * @throws SailoException jos talletus epäonnistuu 
     */
    public void tallenna() throws SailoException { 
        if ( !muutettu ) {
            return; 
        }
   
        File fbak = new File(getBakNimi()); 
        File ftied = new File(getTiedostonNimi()); 
        fbak.delete();
        ftied.renameTo(fbak); 
   
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) { 
            for (Nayttelija nayttelija : this) { 
                fo.println(nayttelija.toString()); 
            } 
        } catch ( FileNotFoundException ex ) { 
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea"); 
        } catch ( IOException ex ) { 
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia"); 
        } 
   
        muutettu = false;
     }
}
