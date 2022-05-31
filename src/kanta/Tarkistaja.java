package kanta;

/**
 * @author kimhi
 * @version 8.3.2018
 *
 */
public class Tarkistaja {
    
    /**
     * Arvotaan satunnainen kokonaisluku v‰lille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yl‰raja
     * @return satunnainen luku v‰lilt‰ [ala,yla]
     */
    public static int rand(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return (int)Math.round(n);
    }
    
    
    /**
     * @param arvosana annettu arvosana
     * @return jos oikein niin null, muuten ilmoittaa virheen
     */
    public String tarkistaArvosana(String arvosana) {
        if (!arvosana.matches("[0-9].[0-9]")) {
            return "Arvosana v‰‰r‰‰ muotoa";
        }
        
        return null;
    }
    
    
    /**
     * @param valmistusvuosi annettu valmistusvuosi
     * @return jos oikein niin null, muuten ilmoittaa virheen
     */
    public String tarkistaValmistusvuosi(String valmistusvuosi) {
        if (!valmistusvuosi.matches("[0-9][0-9][0-9][0-9]")) {
            return "Valmistusvuosi v‰‰r‰‰ muotoa";
        }
        
        return null;
    }
}
