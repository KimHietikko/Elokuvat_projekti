package elokuvarekisteri;

import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator; 
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;


/**
 * @author kimhi
 * @version 8.3.2018
 * Elokuvarekisterin Elokuvat-luokka, joka osaa luoda uuden elokuvan
 */
public class Elokuvat implements Iterable<Elokuva> {

    
    private static int MAX_ELOKUVAT   = 5;
    private int lkm = 0;
    private String kokoNimi = ""; 
    private String tiedostonPerusNimi = "nimet";
    
    /**
     * Taulukko elokuvista
     */
    public static Elokuva alkiot[] = new Elokuva[MAX_ELOKUVAT];
    
    private boolean muutettu = false;
    
    
    /** 
     * Luokka elokuvien iteroimiseksi. 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * #PACKAGEIMPORT 
     * #import java.util.*; 
     *  
     * Elokuvat elokuvat = new Elokuvat(); 
     * Elokuva kummiseta1 = new Elokuva(), kummiseta2 = new Elokuva(); 
     * kummiseta1.rekisteroi(); kummiseta2.rekisteroi(); 
     * 
     * elokuvat.lisaa(kummiseta1);  
     * elokuvat.lisaa(kummiseta2);  
     * elokuvat.lisaa(kummiseta1);  
     *  
     * StringBuffer ids = new StringBuffer(30); 
     * for (Elokuva elokuva:elokuvat)   // Kokeillaan for-silmukan toimintaa 
     *   ids.append(" "+elokuva.getTunnusNro());            
     *  
     * String tulos = " " + kummiseta1.getTunnusNro() + " " + kummiseta2.getTunnusNro() + " " + kummiseta1.getTunnusNro(); 
     *  
     * ids.toString() === tulos;  
     *  
     * ids = new StringBuffer(30); 
     * for (Iterator<Elokuva>  i=elokuvat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa 
     *   Elokuva elokuva = i.next(); 
     *   ids.append(" "+elokuva.getTunnusNro());            
     * } 
     *  
     * ids.toString() === tulos; 
     *  
     * Iterator<Elokuva>  i=elokuvat.iterator(); 
     * i.next() == kummiseta1  === true; 
     * i.next() == kummiseta2  === true; 
     * i.next() == kummiseta1  === true; 
     *  
     * i.next();  #THROWS NoSuchElementException 
     *   
     * </pre> 
     */ 
    public class ElokuvatIterator implements Iterator<Elokuva> { 
        private int kohdalla = 0; 
 
 
        /** 
         * Onko olemassa vielä seuraavaa elokuvaa 
         * @see java.util.Iterator#hasNext() 
         * @return true jos on vielä elokuvia 
         */ 
        @Override 
        public boolean hasNext() { 
            return kohdalla < getLkm(); 
        } 
 
 
        /** 
         * Annetaan seuraava elokuva
         * @return seuraava elokuva 
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole 
         * @see java.util.Iterator#next() 
         */ 
        @Override 
        public Elokuva next() throws NoSuchElementException { 
            if ( !hasNext() ) {
                throw new NoSuchElementException("Ei oo"); 
            }
            return anna(kohdalla++); 
        } 
 
 
        /** 
         * Tuhoamista ei ole toteutettu 
         * @throws UnsupportedOperationException aina 
         * @see java.util.Iterator#remove() 
         */ 
        @Override 
        public void remove() throws UnsupportedOperationException { 
            throw new UnsupportedOperationException("Me ei poisteta"); 
        } 
    }
    
    
    /**
     * Oletusmuodostaja
     */
    public Elokuvat() {
        // Attribuuttien oma alustus riittää
    }
    
    
    /**
     * Palauttaa viitteen i:teen elokuvaan.
     * @param i monennenko elokuvan viite halutaan
     * @return viite elokuvaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Elokuva anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
        return alkiot[i];
    }
    
    
    /**  
     * Palauttaa "taulukossa" hakuehtoon vastaavien elokuvien viitteet  
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi   
     * @return tietorakenteen löytyneistä elokuvista  
     * @example  
     * <pre name="test">  
     * #THROWS SailoException   
     *   Elokuvat elokuvat = new Elokuvat();  
     *   Elokuva elokuva1 = new Elokuva(); elokuva1.parse("1|Kummisetä I|1972|Rikos, Draama|9.2|Francis Ford Coppola");  
     *   Elokuva elokuva2 = new Elokuva(); elokuva2.parse("2|Kummisetä II|1974|Rikos, Draama|9.0|Francis Ford Coppola");  
     *   Elokuva elokuva3 = new Elokuva(); elokuva3.parse("3|Kummisetä III|1990|Rikos, Draama|7.6|Francis Ford Coppola");  
     *   elokuvat.lisaa(elokuva1); elokuvat.lisaa(elokuva2); elokuvat.lisaa(elokuva3);
     * </pre>  
     */
    public Collection<Elokuva> etsi(String hakuehto, int k) {  
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) {
            ehto = hakuehto; 
        }
        int hk = k; 
        Collection<Elokuva> loytyneet = new ArrayList<Elokuva>(); 
        for (Elokuva elokuva : this) { 
            if (WildChars.onkoSamat(elokuva.anna(hk), ehto)) loytyneet.add(elokuva);   
        }
        return loytyneet;
    }
    
    
    /**
     * Etsii elokuvan id:n perusteella
     * @param id tunnusnumero, jonka mukaan etsitään
     * @return löytyneen elokuvan indeksi tai -1 jos ei löydy
     * <pre name="test">
     * #THROWS SailoException 
     * Elokuvat elokuvat = new Elokuvat();
     * Elokuva elokuva1 = new Elokuva(), elokuva2 = new Elokuva(), elokuva3 = new Elokuva();
     * elokuva1.rekisteroi(); elokuva2.rekisteroi(); elokuva3.rekisteroi();
     * int id1 = elokuva1.getTunnusNro();
     * elokuvat.lisaa(elokuva1); elokuvat.lisaa(elokuva2); elokuvat.lisaa(elokuva3);
     * elokuvat.etsiId(id1+1) === 1;
     * elokuvat.etsiId(id1+2) === 2;
     * </pre>
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) {
            if (id == alkiot[i].getTunnusNro()) {
                return i; 
            }
        }
        return -1; 
    }
    
    
    /** 
     * Palauttaa varakopiotiedoston nimen 
     * @return varakopiotiedoston nimi 
     */ 
    public String getBakNimi() { 
        return tiedostonPerusNimi + ".bak"; 
    }
    
    
    /** 
     * Palauttaa Elokuvarekisterin koko nimen 
     * @return Elokuvarekisterin koko nimi merkkijononna 
     */ 
    public String getKokoNimi() { 
        return kokoNimi; 
    } 
    
    
    /**
     * Palauttaa elokuvarekisterin elokuvien lukumäärän
     * @return elokuvien lukumäärä
     */
    public int getLkm() {
        return lkm;
    } 
    
    
    /** 
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen 
     * @return tallennustiedoston nimi 
     */ 
    public String getTiedostonNimi() { 
        return getTiedostonPerusNimi() + ".dat"; 
    } 
 
 
    /** 
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen 
     * @return tallennustiedoston nimi 
     */ 
    public String getTiedostonPerusNimi() { 
        return tiedostonPerusNimi; 
    }
    
    
    /** 
     * Palautetaan iteraattori elokuvista. 
     * @return elokuva iteraattori 
     */ 
    @Override 
    public Iterator<Elokuva> iterator() { 
        return new ElokuvatIterator(); 
    } 
 
 
    /**
     * Korvaa elokuvan tietorakenteessa.  Ottaa elokuvan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva elokuva.  Jos ei löydy,
     * niin lisätään uutena elokuvan.
     * @param elokuva lisättävän elokuvan viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Elokuvat elokuvat = new Elokuvat();
     * Elokuva elokuva1 = new Elokuva(), elokuva2 = new Elokuva();
     * elokuva1.rekisteroi(); elokuva2.rekisteroi();
     * elokuvat.getLkm() === 0;
     * elokuvat.korvaaTaiLisaa(elokuva1); elokuvat.getLkm() === 1;
     * elokuvat.korvaaTaiLisaa(elokuva2); elokuvat.getLkm() === 2;
     * Elokuva elokuva3 = elokuva1.clone();
     * elokuva3.aseta(3,"kkk");
     * Iterator<Elokuva> it = elokuvat.iterator();
     * it.next() == elokuva1 === true;
     * elokuvat.korvaaTaiLisaa(elokuva3); elokuvat.getLkm() === 2;
     * it = elokuvat.iterator();
     * Elokuva j0 = it.next();
     * j0 === elokuva3;
     * j0 == elokuva3 === true;
     * j0 == elokuva1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Elokuva elokuva) throws SailoException {
        int id = elokuva.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = elokuva;
                muutettu = true;
                return;
            }
        }
        lisaa(elokuva);
    } 
 
 
    /**
     * Lisää uuden elokuvan tietorakenteeseen
     * @param elokuva lisättävän elokuvan viite
     * @example
     * <pre name="test">
     * Elokuvat elokuvat = new Elokuvat();
     * Elokuva elokuva1 = new Elokuva(), elokuva2 = new Elokuva();
     * elokuvat.getLkm() === 0;
     * elokuvat.lisaa(elokuva1); elokuvat.getLkm() === 1;
     * elokuvat.lisaa(elokuva2); elokuvat.getLkm() === 2;
     * elokuvat.lisaa(elokuva1); elokuvat.getLkm() === 3;
     * elokuvat.anna(0) === elokuva1;
     * elokuvat.anna(1) === elokuva2;
     * elokuvat.anna(2) === elokuva1;
     * elokuvat.anna(1) == elokuva1 === false;
     * elokuvat.anna(1) == elokuva2 === true;
     * elokuvat.anna(3) === elokuva1; #THROWS IndexOutOfBoundsException 
     * elokuvat.lisaa(elokuva1); elokuvat.getLkm() === 4;
     * elokuvat.lisaa(elokuva1); elokuvat.getLkm() === 5;
     * elokuvat.lisaa(elokuva1);
     * </pre>
     */
    public void lisaa(Elokuva elokuva) {
        if (lkm >= alkiot.length) {
            alkiot = Arrays.copyOf(alkiot, lkm+20);
        }
        alkiot[lkm] = elokuva;
        lkm++;
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
     * Lukee elokuvat tiedostosta.  
     * @param tied tiedoston perusnimi 
     * @throws SailoException jos lukeminen epäonnistuu 
     *  
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * #import java.io.File; 
     *  
     *  Elokuvat elokuvat = new Elokuvat(); 
     *  Elokuva kummiseta1 = new Elokuva(), kummiseta2 = new Elokuva(); 
     *  kummiseta1.elokuvanTiedot(); 
     *  kummiseta2.elokuvanTiedot(); 
     *  String hakemisto = "testielokuvat"; 
     *  String tiedNimi = hakemisto+"/nimet"; 
     *  File ftied = new File(tiedNimi+".dat"); 
     *  File dir = new File(hakemisto); 
     *  dir.mkdir(); 
     *  ftied.delete(); 
     *  elokuvat.lueTiedostosta(tiedNimi); #THROWS SailoException 
     *  elokuvat.lisaa(kummiseta1); 
     *  elokuvat.lisaa(kummiseta2); 
     *  elokuvat.tallenna(); 
     *  elokuvat = new Elokuvat();            // Poistetaan vanhat luomalla uusi 
     *  elokuvat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta. 
     *  Iterator<Elokuva> i = elokuvat.iterator(); 
     *  i.next() === kummiseta1; 
     *  i.next() === kummiseta2; 
     *  i.hasNext() === false; 
     *  elokuvat.lisaa(kummiseta2); 
     *  elokuvat.tallenna(); 
     *  ftied.delete() === true; 
     *  File fbak = new File(tiedNimi+".bak"); 
     *  fbak.delete() === true; 
     *  dir.delete() === true; 
     * </pre> 
     */ 
    public void lueTiedostosta(String tied) throws SailoException { 
        setTiedostonPerusNimi(tied); 
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) { 
            kokoNimi = fi.readLine(); 
            if ( kokoNimi == null ) {
                throw new SailoException("Elokuvarekisterin nimi puuttuu"); 
            }
            String rivi = fi.readLine(); 
            if ( rivi == null ) {
                throw new SailoException("Maksimikoko puuttuu"); 
            }
    
            while ( (rivi = fi.readLine()) != null ) { 
                rivi = rivi.trim(); 
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) {
                    continue; 
                }
                Elokuva elokuva = new Elokuva(); 
                elokuva.parse(rivi);
                lisaa(elokuva); 
            } 
            muutettu = false;
        } catch ( FileNotFoundException e ) { 
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea"); 
        } catch ( IOException e ) { 
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage()); 
        } 
    } 
 
 
    /**
     * Poistaa elokuvan jolla on valittu tunnusnumero 
     * @param id poistettavan elokuvan tunnusnumero
     * @return 1 jos poistettiin, 0 jos ei löydy
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Elokuvat elokuvat = new Elokuvat();
     * Elokuva elokuva1 = new Elokuva(), elokuva2 = new Elokuva(), elokuva3 = new Elokuva();
     * elokuva1.rekisteroi(); elokuva2.rekisteroi(); elokuva3.rekisteroi();
     * int id1 = elokuva1.getTunnusNro();
     * elokuvat.lisaa(elokuva1); elokuvat.lisaa(elokuva2); elokuvat.lisaa(elokuva3);
     * elokuvat.poista(id1+1) === 1;
     * elokuvat.poista(id1) === 1; elokuvat.getLkm() === 1;
     * elokuvat.poista(id1+3) === 0; elokuvat.getLkm() === 1;
     * </pre>
     * 
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) {
            return 0; 
        }
        lkm--; 
        for (int i = ind; i < lkm; i++) {
            alkiot[i] = alkiot[i + 1]; 
        }
        alkiot[lkm] = null; 
        muutettu = true;
        return 1;
    } 
 
 
    /** 
     * Asettaa tiedoston perusnimen ilman tarkenninta 
     * @param nimi tallennustiedoston perusnimi 
     */ 
    public void setTiedostonPerusNimi(String nimi) { 
        tiedostonPerusNimi = nimi; 
    } 
    
    
    /** 
     * Tallentaa elokuvat tiedostoon.   
     * Tiedoston muoto: 
     * <pre> 
     * Elokuvarekisteri 
     * 10 
     * ; kommenttirivi 
     * |1|Kummisetä I|1972|Rikos, Draama|9.2|Francis Ford Coppola
     * |2|Kummisetä II|1974|Rikos, Draama|9.0|Francis Ford Coppola 
     * </pre> 
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
            fo.println(getKokoNimi()); 
            fo.println(alkiot.length); 
            for (Elokuva elokuva : this) { 
                fo.println(elokuva.toString()); 
            } 
            
        } catch ( FileNotFoundException ex ) { 
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea"); 
        } catch ( IOException ex ) { 
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia"); 
        } 
 
        muutettu = false;
    }
}
