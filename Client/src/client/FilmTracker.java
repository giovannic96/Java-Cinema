package client;

/**
 * @author Giovanni Calà
**/

public class FilmTracker {

    String titolo;
    String genere;
    String regia;
    String anno;
    String nazione;
    String durata;

    public FilmTracker(String titolo, String genere, String regia, String anno, String nazione, String durata) {
    
        this.titolo = titolo;
        this.genere = genere;
        this.regia = regia;
        this.anno = anno;
        this.nazione = nazione;
        this.durata = durata;
    }
    
    public String getTitolo() {
        return titolo;
    }

    public String getGenere() {
        return genere;
    }
    
    public String getDurata() {
        return durata;
    }   
    
    public String getRegia() {
        return regia;
    }
    
    public String getAnno() {
        return anno;
    }
    
    public String getNazione() {
        return nazione;
    }
    
    
}
