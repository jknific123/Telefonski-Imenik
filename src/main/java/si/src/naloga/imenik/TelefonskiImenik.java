package si.src.naloga.imenik;

import com.sun.deploy.util.StringUtils;
import si.src.naloga.kontakt.Kontakt;
import si.src.naloga.kontakt.KontaktDBImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class TelefonskiImenik {

    private List<Kontakt> seznamKontaktov;
    private KontaktDBImpl kontaktDB;

    public TelefonskiImenik() throws IOException, ClassNotFoundException {
        seznamKontaktov = new ArrayList<>();
        kontaktDB = KontaktDBImpl.getInstance();

        if (kontaktDB.preveriPovezavo()) { // povezava z bazo je uspešno vzpostavljena
            seznamKontaktov = kontaktDB.pridobiVse();
            System.out.println("Povezava z bazo je uspešno vzpostavljena!");
        }
        else {
            naloziSerializiranSeznamKontakotv();
            //System.out.println("Podatki so bili prebrani iz serializirane datoteke!");
        }
    }

    public void posodobiSeznamKontaktov() {
        seznamKontaktov = kontaktDB.pridobiVse(); //  posodobimo seznam kontaktov
    }

    /**
     * Metoda izpiše vse kontakte
     */
    public void izpisiVseKontakte() {

        System.out.println("Vsi kontakti: ");
        for(Kontakt kontakt : seznamKontaktov) {
            System.out.println(kontakt.toString());
        }
    }

    /**
     * Metaoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta
     */
    public void dodajKontakt(Scanner in) {

        System.out.println("Vnesite podatke novega kontakta ločene z vejico!");
        System.out.println("Zahtevani podatki: Id,Ime,Priimek,Naslov,elektronskaPošta,telefon,mobilniTelefon,opomba");
        System.out.println("Primer pravilnega vnosa: 5,Janez,Pokljukar,Naslov 1,elektronska.posta@gmail.com,030111222,040333444,Opomba kontakta.");
        String vnos = in.next();
        //System.out.println(vnos);
        String[] podatkiNovegaKontakta = vnos.split(",");
        boolean vnosOK = preveriVnos(podatkiNovegaKontakta);
        if (vnosOK) {
            Kontakt novKontakt = new Kontakt(Integer.parseInt(podatkiNovegaKontakta[0]),podatkiNovegaKontakta[1],podatkiNovegaKontakta[2],podatkiNovegaKontakta[3],podatkiNovegaKontakta[4],podatkiNovegaKontakta[5],podatkiNovegaKontakta[6],podatkiNovegaKontakta[7]);
            kontaktDB.vstavi(novKontakt);
            seznamKontaktov = kontaktDB.pridobiVse(); //  posodobimo seznam kontaktov
        }
    }


    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     */
    public void urediKontakt(Scanner in) {

        System.out.println("Vnesite ID kontakta, ki ga hočete urejati: ");
        String vnos = in.next();
        try {
            int vnesenID = Integer.parseInt(vnos);
            try {
                kontaktDB.pridobiPoId(vnesenID);
            }
            catch (NullPointerException e) {
                System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
            }
            System.out.println("Vnesite nove podatke kontakta ločene z vejico!");
            System.out.println("Zahtevani podatki: Ime,Priimek,Naslov,elektronskaPošta,telefon,mobilniTelefon,opomba");
            System.out.println("Primer pravilnega vnosa: Janez,Pokljukar,Naslov 1,elektronska.posta@gmail.com,030111222,040333444,Opomba kontakta.");
            vnos = in.next();
            String[] podatkiPosodobljenegaKontakta = vnos.split(",");
            Kontakt posodobljenKontakt = kontaktDB.pridobiPoId(vnesenID);
            posodobiPoljaKontakta(posodobljenKontakt, podatkiPosodobljenegaKontakta);
            kontaktDB.posodobi(posodobljenKontakt);
            posodobiSeznamKontaktov();
        }
        catch (NumberFormatException e){
            System.out.println(vnos + " ni število!");
        }
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId(Scanner in) {

        System.out.println("Vnesite ID kontakta, ki ga hočete izbrisati: ");
        String vnos = in.next();
        try {
            int vnesenID = Integer.parseInt(vnos);
            try {
                kontaktDB.izbrisiPoId(vnesenID);
                posodobiSeznamKontaktov();
            }
            catch (NullPointerException e) {
                System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
            }
        }
        catch (NumberFormatException e){
            System.out.println(vnos + " ni število!");
        }
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId(Scanner in) {
        System.out.println("Vnesite ID kontakta, ki ga hočete izpisati: ");
        String vnos = in.next();
        try {
            int vnesenID = Integer.parseInt(vnos);
            try {
                Kontakt iskaniKontakt = kontaktDB.pridobiPoId(vnesenID);
                System.out.println(iskaniKontakt.toString());
            }
            catch (NullPointerException e) {
                System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
            }
        }
        catch (NumberFormatException e){
            System.out.println(vnos + " ni število!");
        }
    }

    /**
     * Izpis števila kontaktov
     */
    public void izpisiSteviloKontaktov() {

        try {
            posodobiSeznamKontaktov();
            System.out.println("Število kontaktov: " + seznamKontaktov.size());
        }
        catch (NullPointerException e) {
            System.out.println("V imeniku ni kontaktov!");
        }
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() throws IOException {

        posodobiSeznamKontaktov();
        // zapisovanje v datoteko
        FileOutputStream fileOutputStream = new FileOutputStream("kontakti.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(seznamKontaktov);
        objectOutputStream.flush();
        objectOutputStream.close();
        System.out.println("Serializacija kontaktov na disk je bila uspešna!");
    }

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() throws IOException, ClassNotFoundException {

        // branje iz datoteke
        String imeDatoteke = "kontakti.ser";
        try {
            FileInputStream fileInputStream = new FileInputStream(imeDatoteke);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            seznamKontaktov = (List<Kontakt>) objectInputStream.readObject();
            objectInputStream.close();
            System.out.println("Podatki so bili naloženi iz diska.");
        }
        catch (FileNotFoundException e) {
            System.out.println("Serializirana datoteka " + imeDatoteke + " za nalaganje kontaktov ne obstaja!");
        }
    }

    /**
     * Izvozi seznam kontakov CSV datoteko.
     * Naj uporabnik sam izbere ime izhodne datoteke.
     */
    public void izvoziPodatkeVCsvDatoteko() {

        posodobiSeznamKontaktov();
        try (FileWriter fw = new FileWriter("Kontakti.csv");
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Kontakt kontakt : seznamKontaktov) {
                bw.write(kontakt.toString());
                bw.newLine();
            }
            System.out.println("Kontakti so bili uspešno izvoženi v obliki .csv datoteke: Kontakti.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void isciPoImenuAliPriimku(Scanner in) {

        posodobiSeznamKontaktov();
        System.out.println("Vnesite iskalni niz: ");
        String vnos = in.next();

        try {
            int tmp = Integer.parseInt(vnos);
            System.out.println("Napačen vnos - vnesli ste število namesto iskalnega niza!");
        } catch (NumberFormatException e) {
            for (Kontakt kontakt : seznamKontaktov) {
                if (kontakt.getIme().toLowerCase().contains(vnos.toLowerCase()) || kontakt.getPriimek().toLowerCase().contains(vnos.toLowerCase())) {
                    System.out.println(kontakt.toString());
                }
            }
        }
    }

    /**----------------Pomožne funkcije--------------**/

    public Boolean preveriVnos(String[] vnosniPodatki) {

        /**
        System.out.println("ID: " + vnosniPodatki[0]);
        System.out.println("Ime: " + vnosniPodatki[1]);
        System.out.println("Priimek: " + vnosniPodatki[2]);
        System.out.println("Naslov: " + vnosniPodatki[3]);
        System.out.println("Email: " + vnosniPodatki[4]);
        System.out.println("Telefon: " + vnosniPodatki[5]);
        System.out.println("Mobilni: " + vnosniPodatki[6]);
        System.out.println("Opomba: " + vnosniPodatki[7]);
        **/
        boolean check = true;
        int vnesenID = 0;
        try {
            vnesenID = Integer.parseInt(vnosniPodatki[0]);
        }
        catch (NumberFormatException e){
            System.out.println(vnosniPodatki[0] + " ni število!");
            check = false;
        }
        System.out.println("Check: " + check);
        if (check) {
            Kontakt novKontakt = new Kontakt(vnesenID,vnosniPodatki[1],vnosniPodatki[2],vnosniPodatki[3],vnosniPodatki[4],vnosniPodatki[5],vnosniPodatki[6],vnosniPodatki[7]);

            for (Kontakt kontakt : seznamKontaktov) {
                if (kontakt.equals(novKontakt)) {
                    check = false;
                    System.out.println("Vneseni kontakt je že zapisan v imeniku - podvajanje!");
                }
            }

        }
        return check;
    }

    public void posodobiPoljaKontakta(Kontakt kontakt,String[] polja) {

        kontakt.setIme(polja[0]);
        kontakt.setPriimek(polja[1]);
        kontakt.setNaslov(polja[2]);
        kontakt.setElektronskaPosta(polja[3]);
        kontakt.setTelefon(polja[4]);
        kontakt.setMobilniTelefon(polja[5]);
        kontakt.setOpomba(polja[6]);
    }

}
