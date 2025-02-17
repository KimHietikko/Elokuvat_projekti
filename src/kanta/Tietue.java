package kanta;

/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa
 * "attribuutit".
 * @author vesal
 * @version Mar 23, 2012
 * @example
 */
public interface Tietue {

   
    /**
     * @return tietueen kenttien lukum��r�
     * @example
     * <pre name="test">
     *   #import kerho.Harrastus;
     *   Harrastus har = new Harrastus();
     *   har.getKenttia() === 5;
     * </pre>
     */
    public abstract int getKenttia();


    /**
     * @return ensimm�inen k�ytt�j�n sy�tett�v�n kent�n indeksi
     * @example
     * <pre name="test">
     *   Harrastus har = new Harrastus();
     *   har.ekaKentta() === 2;
     * </pre>
     */
    public abstract int ekaKentta();


    /**
     * @param k mink� kent�n kysymys halutaan
     * @return valitun kent�n kysymysteksti
     * @example
     * <pre name="test">
     *   Harrastus har = new Harrastus();
     *   har.getKysymys(2) === "ala";
     * </pre>
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Mink� kent�n sis�lt� halutaan
     * @return valitun kent�n sis�lt�
     * @example
     * <pre name="test">
     *   Harrastus har = new Harrastus();
     *   har.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   har.anna(0) === "2";   
     *   har.anna(1) === "10";   
     *   har.anna(2) === "Kalastus";   
     *   har.anna(3) === "1949";   
     *   har.anna(4) === "22";   
     * </pre>
     */
    public abstract String anna(int k);

   
    /**
     * Asetetaan valitun kent�n sis�lt�.  Mik�li asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k mink� kent�n sis�lt� asetetaan
     * @param s asetettava sis�lt� merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Harrastus har = new Harrastus();
     *   har.aseta(3,"kissa") === "aloitusvuosi: Ei kokonaisluku (kissa)";
     *   har.aseta(3,"1940")  === null;
     *   har.aseta(4,"kissa") === "h/vko: Ei kokonaisluku (kissa)";
     *   har.aseta(4,"20")    === null;
     * </pre>
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehd��n identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Harrastus har = new Harrastus();
     *   har.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   Object kopio = har.clone();
     *   kopio.toString() === har.toString();
     *   har.parse("   1   |  11  |   Uinti  | 1949 | 22 t ");
     *   kopio.toString().equals(har.toString()) === false;
     *   kopio instanceof Harrastus === true;
     * </pre>
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Harrastus harrastus = new Harrastus();
     *   harrastus.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   harrastus.toString()    =R= "2\\|10\\|Kalastus\\|1949\\|22.*";
     * </pre>
     */
    @Override
    public abstract String toString();

}
