package elokuvarekisteri;

import java.io.File; 
import java.util.Collection; 
import java.util.List; 

/**
 * @author kimhi
 * @version 8.3.2018
 * Elokuvarekisteri-luokka, joka pitää käskyttää Elokuvat- ja Nayttelijat-luokkaa
 */
public class Elokuvarekisteri {

    
    private Elokuvat elokuvat = new Elokuvat();
    
    private Nayttelijat nayttelijat = new Nayttelijat();
    
    
    /**
     * Haetaan kaikki elokuvan näyttelijät
     * @param elokuva elokuva jonka näyttelijöitä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin näyttelijöihin
     * @throws SailoException jos tulee ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  Elokuvarekisteri elokuvarekisteri = new Elokuvarekisteri();
     *  Elokuva elokuva1 = new Elokuva(), elokuva2 = new Elokuva(), elokuva3 = new Elokuva();
     *  elokuva1.rekisteroi(); elokuva2.rekisteroi(); elokuva3.rekisteroi();
     *  int id1 = elokuva1.getTunnusNro();
     *  int id2 = elokuva2.getTunnusNro();
     *  Nayttelija elokuva11 = new Nayttelija(id1); elokuvarekisteri.lisaa(elokuva11);
     *  Nayttelija elokuva12 = new Nayttelija(id1); elokuvarekisteri.lisaa(elokuva12);
     *  Nayttelija elokuva21 = new Nayttelija(id2); elokuvarekisteri.lisaa(elokuva21);
     *  Nayttelija elokuva22 = new Nayttelija(id2); elokuvarekisteri.lisaa(elokuva22);
     *  Nayttelija elokuva23 = new Nayttelija(id2); elokuvarekisteri.lisaa(elokuva23);
     *  
     *  List<Nayttelija> loytyneet;
     *  loytyneet = elokuvarekisteri.annaNayttelijat(elokuva3);
     *  loytyneet.size() === 0; 
     *  loytyneet = elokuvarekisteri.annaNayttelijat(elokuva1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == elokuva11 === true;
     *  loytyneet.get(1) == elokuva12 === true;
     *  loytyneet = elokuvarekisteri.annaNayttelijat(elokuva2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == elokuva21 === true;
     * </pre> 
     */
    public List<Nayttelija> annaNayttelijat(Elokuva elokuva) throws SailoException {
        return nayttelijat.naytaNayttelijat(elokuva.getTunnusNro());
    }
    
    
    /**  
     * Palauttaa "taulukossa" hakuehtoon vastaavien elokuvien viitteet  
     * @param hakuehto hakuehto   
     * @param k etsittävän kentän indeksi   
     * @return tietorakenteen löytyneistä elokuvista 
     * @throws SailoException Jos jotakin menee väärin 
     */  
    public Collection<Elokuva> etsi(String hakuehto, int k) throws SailoException {  
        return elokuvat.etsi(hakuehto, k);  
    }
    
    
    /**
     * Korvaa elokuvan tietorakenteessa.  Ottaa elokuvan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva elokuva.  Jos ei löydy,
     * niin lisätään uutena elokuvana.
     * @param elokuva lisättävän elokuvan viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */ 
    public void korvaaTaiLisaa(Elokuva elokuva) throws SailoException { 
        elokuvat.korvaaTaiLisaa(elokuva); 
    }
    
    
    /**
     * Korvaa näyttelijän tietorakenteessa.  Ottaa näyttelijän omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva näyttelijä.  Jos ei löydy,
     * niin lisätään uutena näyttelijänä.
     * @param nayttelija lisättävän näyttelijän viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */ 
    public void korvaaTaiLisaa(Nayttelija nayttelija) throws SailoException { 
        nayttelijat.korvaaTaiLisaa(nayttelija); 
    }
    
    
    /**
     * Lisää elokuvarekisteriin uuden elokuvan
     * @param elokuva lisättävä elokuva
     * @throws SailoException jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Elokuvarekisteri elokuvarekisteri = new Elokuvarekisteri();
     * Elokuva elokuva1 = new Elokuva(), elokuva2 = new Elokuva();
     * elokuvarekisteri.lisaa(elokuva1);  
     * elokuvarekisteri.lisaa(elokuva2);  
     * elokuvarekisteri.lisaa(elokuva1); 
     * Collection<Elokuva> loytyneet = elokuvarekisteri.etsi("",-1);  
     * Iterator<Elokuva> it = loytyneet.iterator(); 
     * it.next() === elokuva1; 
     * it.next() === elokuva2; 
     * it.next() === elokuva1;
     * </pre>
     */
    public void lisaa(Elokuva elokuva) throws SailoException {
        elokuvat.lisaa(elokuva);
    }
    
    
    /**
     * Lisätään uusi näyttelijä elokuvarekisteriin
     * @param nayttelija lisättävä näyttelijä
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Nayttelija nayttelija) throws SailoException {
        nayttelijat.lisaa(nayttelija);
    }
    
    
    /** 
     * Lukee elokuvarekisterin tiedot tiedostosta 
     * @param nimi jota käytetään lukemisessa 
     * @throws SailoException jos lukeminen epäonnistuu 
     *  
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * #import java.io.*; 
     * #import java.util.*; 
     *  
     *  Elokuvarekisteri elokuvarekisteri = new Elokuvarekisteri(); 
     *   
     *  Elokuva elokuva1 = new Elokuva(); elokuva1.elokuvanTiedot(); elokuva1.rekisteroi(); 
     *  Elokuva elokuva2 = new Elokuva(); elokuva2.elokuvanTiedot(); elokuva2.rekisteroi(); 
     *  Nayttelija nayttelija21 = new Nayttelija(); nayttelija21.nayttelijanTiedot(elokuva2.getTunnusNro()); 
     *  Nayttelija nayttelija11 = new Nayttelija(); nayttelija11.nayttelijanTiedot(elokuva1.getTunnusNro()); 
     *  Nayttelija nayttelija22 = new Nayttelija(); nayttelija22.nayttelijanTiedot(elokuva2.getTunnusNro());  
     *  Nayttelija nayttelija12 = new Nayttelija(); nayttelija12.nayttelijanTiedot(elokuva1.getTunnusNro());  
     *  Nayttelija nayttelija23 = new Nayttelija(); nayttelija23.nayttelijanTiedot(elokuva2.getTunnusNro()); 
     *    
     *  String hakemisto = "testielokuvat"; 
     *  File dir = new File(hakemisto); 
     *  File ftied  = new File(hakemisto+"/nimet.dat"); 
     *  File fhtied = new File(hakemisto+"/nayttelijat.dat"); 
     *  dir.mkdir();   
     *  ftied.delete(); 
     *  fhtied.delete(); 
     *  elokuvarekisteri.lueTiedostosta(hakemisto); #THROWS SailoException 
     *  elokuvarekisteri.lisaa(elokuva1); 
     *  elokuvarekisteri.lisaa(elokuva2); 
     *  elokuvarekisteri.lisaa(nayttelija21); 
     *  elokuvarekisteri.lisaa(nayttelija11); 
     *  elokuvarekisteri.lisaa(nayttelija22); 
     *  elokuvarekisteri.lisaa(nayttelija12); 
     *  elokuvarekisteri.lisaa(nayttelija23); 
     *  elokuvarekisteri.tallenna(); 
     *  elokuvarekisteri = new Elokuvarekisteri(); 
     *  elokuvarekisteri.lueTiedostosta(hakemisto); 
     *  Collection<Elokuva> kaikki = elokuvarekisteri.etsi("",-1);  
     *  Iterator<Elokuva> it = kaikki.iterator(); 
     *  it.next() === elokuva1; 
     *  it.next() === elokuva2; 
     *  it.hasNext() === false; 
     *  List<Nayttelija> loytyneet = elokuvarekisteri.annaNayttelijat(elokuva1); 
     *  Iterator<Nayttelija> ih = loytyneet.iterator(); 
     *  ih.next() === nayttelija11; 
     *  ih.next() === nayttelija12; 
     *  ih.hasNext() === false; 
     *  loytyneet = elokuvarekisteri.annaNayttelijat(elokuva2); 
     *  ih = loytyneet.iterator(); 
     *  ih.next() === nayttelija21; 
     *  ih.next() === nayttelija22; 
     *  ih.next() === nayttelija23; 
     *  ih.hasNext() === false; 
     *  elokuvarekisteri.lisaa(elokuva2); 
     *  elokuvarekisteri.lisaa(nayttelija23); 
     *  elokuvarekisteri.tallenna(); 
     *  ftied.delete()  === true; 
     *  fhtied.delete() === true; 
     *  File fbak = new File(hakemisto+"/nimet.bak"); 
     *  File fhbak = new File(hakemisto+"/nayttelijat.bak"); 
     *  fbak.delete() === true; 
     *  fhbak.delete() === true; 
     *  dir.delete() === true; 
     * </pre> 
     */ 
    public void lueTiedostosta(String nimi) throws SailoException {
        elokuvat = new Elokuvat();
        nayttelijat = new Nayttelijat(); 
        
        setTiedosto(nimi); 
        elokuvat.lueTiedostosta(); 
        nayttelijat.lueTiedostosta(); 
    }
    
    
    /**
     * Poistaa elokuvista ja näyttelijöistä elokuvan tiedot
     * @param elokuva elokuva joka poistetaan
     * @return montako elokuvaa poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *  Elokuvarekisteri elokuvarekisteri = new Elokuvarekisteri();
     *   
     *  Elokuva elokuva1 = new Elokuva(); elokuva1.elokuvanTiedot(); elokuva1.rekisteroi(); 
     *  Elokuva elokuva2 = new Elokuva(); elokuva2.elokuvanTiedot(); elokuva2.rekisteroi();
     *  Elokuva elokuva3 = new Elokuva(); elokuva3.elokuvanTiedot(); elokuva3.rekisteroi(); 
     *  Elokuva elokuva4 = new Elokuva(); elokuva4.elokuvanTiedot(); elokuva4.rekisteroi(); 
     *  Elokuva elokuva5 = new Elokuva(); elokuva5.elokuvanTiedot(); elokuva5.rekisteroi(); 
     *   
     *  Nayttelija nayttelija21 = new Nayttelija(); nayttelija21.nayttelijanTiedot(elokuva2.getTunnusNro()); 
     *  Nayttelija nayttelija11 = new Nayttelija(); nayttelija11.nayttelijanTiedot(elokuva1.getTunnusNro()); 
     *  Nayttelija nayttelija22 = new Nayttelija(); nayttelija22.nayttelijanTiedot(elokuva2.getTunnusNro());  
     *  Nayttelija nayttelija12 = new Nayttelija(); nayttelija12.nayttelijanTiedot(elokuva1.getTunnusNro());  
     *  Nayttelija nayttelija23 = new Nayttelija(); nayttelija23.nayttelijanTiedot(elokuva2.getTunnusNro());
     *  
     *  elokuvarekisteri.lisaa(elokuva1); 
     *  elokuvarekisteri.lisaa(elokuva2);
     *  elokuvarekisteri.lisaa(elokuva3); 
     *  elokuvarekisteri.lisaa(elokuva4);
     *  elokuvarekisteri.lisaa(elokuva5); 
     *  
     *  elokuvarekisteri.lisaa(nayttelija21); 
     *  elokuvarekisteri.lisaa(nayttelija11); 
     *  elokuvarekisteri.lisaa(nayttelija22); 
     *  elokuvarekisteri.lisaa(nayttelija12); 
     *  elokuvarekisteri.lisaa(nayttelija23);
     *   
     *  elokuvarekisteri.etsi("*",0).size() === 5;
     *  elokuvarekisteri.annaNayttelijat(elokuva1).size() === 2;
     *  elokuvarekisteri.poista(elokuva1) === 1;
     *  elokuvarekisteri.etsi("*",0).size() === 4;
     *  elokuvarekisteri.annaNayttelijat(elokuva1).size() === 0;
     *  elokuvarekisteri.annaNayttelijat(elokuva2).size() === 3;
     * </pre>
     */
    public int poista(Elokuva elokuva) {
        if ( elokuva == null ) {
            return 0;
        }
        int ret = elokuvat.poista(elokuva.getTunnusNro()); 
        nayttelijat.poistaElokuvanNayttelijat(elokuva.getTunnusNro()); 
        return ret; 
    }  
    
    
    /**
     * Poistaa tämän näyttelijän
     * @param nayttelija poistettava näyttelijä
     * @example
     * <pre name="test">
     * #THROWS Exception
     *  Elokuvarekisteri elokuvarekisteri = new Elokuvarekisteri();
     *   
     *   
     *  Elokuva elokuva1 = new Elokuva(); elokuva1.elokuvanTiedot(); elokuva1.rekisteroi(); 
     *  Elokuva elokuva2 = new Elokuva(); elokuva2.elokuvanTiedot(); elokuva2.rekisteroi(); 
     *  Nayttelija nayttelija21 = new Nayttelija(); nayttelija21.nayttelijanTiedot(elokuva2.getTunnusNro()); 
     *  Nayttelija nayttelija11 = new Nayttelija(); nayttelija11.nayttelijanTiedot(elokuva1.getTunnusNro()); 
     *  Nayttelija nayttelija22 = new Nayttelija(); nayttelija22.nayttelijanTiedot(elokuva2.getTunnusNro());  
     *  Nayttelija nayttelija12 = new Nayttelija(); nayttelija12.nayttelijanTiedot(elokuva1.getTunnusNro());  
     *  Nayttelija nayttelija23 = new Nayttelija(); nayttelija23.nayttelijanTiedot(elokuva2.getTunnusNro()); 
     *  
     *  elokuvarekisteri.lisaa(elokuva1); 
     *  elokuvarekisteri.lisaa(elokuva2); 
     *  elokuvarekisteri.lisaa(nayttelija21); 
     *  elokuvarekisteri.lisaa(nayttelija11); 
     *  elokuvarekisteri.lisaa(nayttelija22); 
     *  elokuvarekisteri.lisaa(nayttelija12); 
     *  elokuvarekisteri.lisaa(nayttelija23);
     *   
     *  elokuvarekisteri.annaNayttelijat(elokuva1).size() === 2;
     *  elokuvarekisteri.poistaNayttelija(nayttelija11);
     *  elokuvarekisteri.annaNayttelijat(elokuva1).size() === 1;
     */ 
    public void poistaNayttelija(Nayttelija nayttelija) { 
        nayttelijat.poista(nayttelija); 
    } 
    
    
    /**   
     * Laitetaan näyttelijät muuttuneeksi, niin pakotetaan tallentamaan.   
     */   
    public void setNayttelijaMuutos() {   
        nayttelijat.setMuutos();   
    }
    
    
    /** 
     * Asettaa tiedostojen perusnimet 
     * @param nimi uusi nimi 
     */ 
    public void setTiedosto(String nimi) { 
        File dir = new File(nimi); 
        dir.mkdirs(); 
        String hakemistonNimi = ""; 
        if ( !nimi.isEmpty() ) {
            hakemistonNimi = nimi +"/"; 
        }
        elokuvat.setTiedostonPerusNimi(hakemistonNimi + "nimet"); 
        nayttelijat.setTiedostonPerusNimi(hakemistonNimi + "nayttelijat"); 
    } 
    
    
    /** 
     * Tallenttaa elokuvarekisterin tiedot tiedostoon.   
     * Vaikka elokuvien tallettamien epäonistuisi, niin yritetään silti tallettaa 
     * näyttelijöitä ennen poikkeuksen heittämistä. 
     * @throws SailoException jos tallettamisessa ongelmia 
     */
    public void tallenna() throws SailoException {
        String virhe = ""; 
        try { 
            elokuvat.tallenna(); 
        } catch ( SailoException ex ) { 
            virhe = ex.getMessage(); 
        } 
    
        try { 
            nayttelijat.tallenna(); 
        } catch ( SailoException ex ) { 
            virhe += ex.getMessage(); 
        } 
        if ( !"".equals(virhe) ) throw new SailoException(virhe); 
    }
}
