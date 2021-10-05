package si.src.naloga.imenik;

import si.src.naloga.kontakt.Kontakt;

import java.util.ArrayList;
import java.util.List;


public class TelefonskiImenik {

    private List<Kontakt> seznamKontaktov;

    public TelefonskiImenik() {
        seznamKontaktov = new ArrayList<>();
    }

    /**
     * Metaoda izpiše vse kontakte
     */
    public void izpisiVseKontakte() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Metaoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta
     */
    public void dodajKontakt() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     */
    public void urediKontakt() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiSteviloKontaktov() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izvozi seznam kontakov CSV datoteko.
     * Naj uporabnik sam izbere ime izhodne datoteke.
     */
    public void izvoziPodatkeVCsvDatoteko() {
        System.out.println("Metoda še ni implementirana");
    }
}
