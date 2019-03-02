package server;

import java.io.Serializable;

/**
 * @author Giovanni Calà
**/

public class Film implements Serializable {

    String titolo;
    String genere;
    String regia;
    String anno;
    String nazione;
    String durata;
    
    public Film(String myTitolo, String myGenere, String myRegia, String myAnno, String myNazione, String myDurata) {
        
        this.titolo = myTitolo;
        this.genere = myGenere;
        this.regia = myRegia;
        this.anno = myAnno;
        this.nazione = myNazione;
        this.durata = myDurata;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getRegia() {
        return regia;
    }

    public void setRegia(String regia) {
        this.regia = regia;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }
    
    @Override
    public String toString() {    
        return titolo + "%" + genere + "%" + regia + "%" + anno + "%" + nazione + "%" + durata + "%";
    }
    
}
