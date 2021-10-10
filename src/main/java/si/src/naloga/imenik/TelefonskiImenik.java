package si.src.naloga.imenik;

import com.sun.deploy.util.StringUtils;
import si.src.naloga.kontakt.Kontakt;
import si.src.naloga.kontakt.KontaktDBImpl;

import java.io.*;
import java.util.*;


public class TelefonskiImenik {

    private List<Kontakt> seznamKontaktov;
    private KontaktDBImpl kontaktDB;

    public TelefonskiImenik() throws IOException, ClassNotFoundException {
        seznamKontaktov = new ArrayList<>();
        kontaktDB = KontaktDBImpl.getInstance();
        posodobiSeznamKontaktov();
    }

    /**
     * Posodobi kontakte v bazi če je povezava vzpostaljena, drugače pa v datoteki
     */
    public void posodobiSeznamKontaktov() throws IOException, ClassNotFoundException {

        if (kontaktDB.preveriPovezavo()) { // povezava z bazo je vspostavljena
            seznamKontaktov = kontaktDB.pridobiVse(); //  pridobimo kontakte iz baze in posodobimo seznam kontaktov
        }
        else if (!kontaktDB.preveriPovezavo()) { // povezava z bazo ni vspostavljena, zato preberemo kontakte iz diska
            naloziSerializiranSeznamKontakotv();
        }
        seznamKontaktov.sort(Comparator.comparing(Kontakt::getId));
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
     * Metoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta
     */
    public void dodajKontakt(Scanner in) throws IOException, ClassNotFoundException {

        System.out.println("Vnesite podatke novega kontakta ločene z vejico!");
        System.out.println("Zahtevani podatki: Id,Ime,Priimek,Naslov,elektronskaPošta,telefon,mobilniTelefon,opomba");
        System.out.println("Primer pravilnega vnosa: 5,Janez,Pokljukar,Naslov 1,elektronska.posta@gmail.com,030111222,040333444,Opomba kontakta.");
        String vnos = in.next();
        //System.out.println(vnos);
        String[] podatkiNovegaKontakta = vnos.split(",");
        boolean vnosOK = preveriVnos(podatkiNovegaKontakta);
        if (vnosOK) {
            boolean check = false;
            Kontakt novKontakt = new Kontakt(Integer.parseInt(podatkiNovegaKontakta[0]),podatkiNovegaKontakta[1],podatkiNovegaKontakta[2],podatkiNovegaKontakta[3],podatkiNovegaKontakta[4],podatkiNovegaKontakta[5],podatkiNovegaKontakta[6],podatkiNovegaKontakta[7]);
            if (kontaktDB.preveriPovezavo()) { // dodamo kontakt v bazo
                kontaktDB.vstavi(novKontakt);
                check = true;
            }
            else if (!kontaktDB.preveriPovezavo()) {
                seznamKontaktov.add(novKontakt);
                serializirajSeznamKontaktov();
                check = true;
            }

            if (check) {
                System.out.println("Dodajanje novega kontakta je bilo uspešno!");
                posodobiSeznamKontaktov();
            }
        }
    }


    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     */
    public void urediKontakt(Scanner in) throws IOException, ClassNotFoundException {

        System.out.println("Vnesite ID kontakta, ki ga hočete urejati: ");
        String vnos = in.next();

        boolean idOk = preveriVnosId(vnos);
        if (idOk) {
            int vnesenID = Integer.parseInt(vnos);
            if (kontaktDB.preveriPovezavo()) {
                try {
                    Kontakt posodobljenKontakt = kontaktDB.pridobiPoId(vnesenID);
                    if (posodobljenKontakt != null) {
                        System.out.println("Vnesite nove podatke kontakta ločene z vejico!");
                        System.out.println("Zahtevani podatki: Ime,Priimek,Naslov,elektronskaPošta,telefon,mobilniTelefon,opomba");
                        System.out.println("Primer pravilnega vnosa: Janez,Pokljukar,Naslov 1,elektronska.posta@gmail.com,030111222,040333444,Opomba kontakta.");
                        vnos = in.next();
                        String[] podatkiPosodobljenegaKontakta = vnos.split(",");
                        posodobiPoljaKontakta(posodobljenKontakt, podatkiPosodobljenegaKontakta);
                        kontaktDB.posodobi(posodobljenKontakt);
                    } else {
                        System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
                    }
                }
                catch (NullPointerException e) {
                    System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
                }
            }
            else if (!kontaktDB.preveriPovezavo()) {
                Kontakt posodobljenKontakt = najdiKontaktZaId(vnesenID);
                if (posodobljenKontakt != null) {
                    System.out.println("Vnesite nove podatke kontakta ločene z vejico!");
                    System.out.println("Zahtevani podatki: Ime,Priimek,Naslov,elektronskaPošta,telefon,mobilniTelefon,opomba");
                    System.out.println("Primer pravilnega vnosa: Janez,Pokljukar,Naslov 1,elektronska.posta@gmail.com,030111222,040333444,Opomba kontakta.");
                    vnos = in.next();
                    String[] podatkiPosodobljenegaKontakta = vnos.split(",");
                    posodobiPoljaKontakta(posodobljenKontakt, podatkiPosodobljenegaKontakta);
                    serializirajSeznamKontaktov();
                }else {
                    System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
                }
            }
            posodobiSeznamKontaktov();
        }
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId(Scanner in) throws IOException, ClassNotFoundException {

        System.out.println("Vnesite ID kontakta, ki ga hočete izbrisati: ");
        String vnos = in.next();

        boolean idOk = preveriVnosId(vnos);
        if (idOk) {
            int id = Integer.parseInt(vnos);
            boolean check = false;
            if (kontaktDB.preveriPovezavo()) {
                try {
                    kontaktDB.izbrisiPoId(id);
                    check = true;
                }
                catch (NullPointerException e) {
                    System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
                }
            }
            else if (!kontaktDB.preveriPovezavo()) {
                boolean tmp = seznamKontaktov.removeIf(kontakt -> kontakt.getId() == id);
                if (tmp) {
                    check = true;
                    serializirajSeznamKontaktov(); // posodobimo zapis v datoteki
                }
                else {
                    System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
                }
            }
            if (check) {
                posodobiSeznamKontaktov();
                System.out.println("Kontakt z dotičnim ID-jem je bil uspešno izbrisan!");
            }
        }
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId(Scanner in) {
        System.out.println("Vnesite ID kontakta, ki ga hočete izpisati: ");
        String vnos = in.next();

        boolean idOk = preveriVnosId(vnos);
        if (idOk) {
            int vnesenID = Integer.parseInt(vnos);
            if (kontaktDB.preveriPovezavo()) {
                try {
                    Kontakt iskaniKontakt = kontaktDB.pridobiPoId(vnesenID);
                    System.out.println(iskaniKontakt.toString());
                }
                catch (NullPointerException e) {
                    System.out.println("Kontakt z dotičnim ID-jem ne obstaja!");
                }
            }
            else if (!kontaktDB.preveriPovezavo()) {
                Kontakt iskaniKontakt = najdiKontaktZaId(vnesenID);
                System.out.println(iskaniKontakt.toString());
            }
        }
    }

    /**
     * Izpis števila kontaktov
     */
    public void izpisiSteviloKontaktov() {

        if (seznamKontaktov.size() != 0) {
            System.out.println("Število kontaktov: " + seznamKontaktov.size());
        } else {
            System.out.println("V imeniku ni kontaktov!");
        }
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() throws IOException {

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
            System.out.println("Podatki so bili naloženi iz serializirane datoteke.");
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

    /**
     * Prikaže kontakte, katerih imean ali priimki ustrezajo iskalnemu nizu ali pa ga vsebujejo kot podniz
     */
    public void isciPoImenuAliPriimku(Scanner in) {

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

    /**
     * Shrani trenutne kontakte v bazo
     */
    public void shraniKontakteVBazo() {

        if (kontaktDB.preveriPovezavo()) {
            List<Kontakt> trenutniKontaktiVBazi = kontaktDB.pridobiVse();
            for (Kontakt kontakt : seznamKontaktov) {
                int count = 0;
                for (Kontakt obstojeciKontakt : trenutniKontaktiVBazi) {
                    if (!kontakt.equals(obstojeciKontakt)) { // poskrbimo da ne pride do podvajanj v bazi
                        count++;
                    }
                }
                if (count == trenutniKontaktiVBazi.size()) { // kontakt lahko dodamo
                    kontaktDB.vstavi(kontakt);
                }
            }
        } else {
            System.out.println("Povzave na bazo ni mogoče vzpostaviti!");
        }
    }

    /**
     * Pridobi shranjene kontakte iz baze
     */
    public void pridobiKontakteIzBaze() {

        if (kontaktDB.preveriPovezavo()) {

            List<Kontakt> trenutniKontaktiVBazi = kontaktDB.pridobiVse();
            for (Kontakt kontaktBaza : trenutniKontaktiVBazi) {
                int count = 0;
                for (Kontakt obstojeciKontakt : seznamKontaktov) {
                    if (!kontaktBaza.equals(obstojeciKontakt) && kontaktBaza.getId() != obstojeciKontakt.getId()) { // poskrbimo da ne pride do podvajanj kontaktov
                        count++;
                    }
                }
                if (count == seznamKontaktov.size()) { // kontakt lahko dodamo
                        seznamKontaktov.add(kontaktBaza);
                }
            }
            System.out.println("Shranjeni kontakti so bili uspešno pridobljeni iz baze!");
        }
        else if (!kontaktDB.preveriPovezavo()) {
            System.out.println("Povezave z bazo ni mogoče vzpostaviti!");
        }
        seznamKontaktov.sort(Comparator.comparing(Kontakt::getId));
    }

    /**----------------Pomožne funkcije--------------**/

    private Boolean preveriVnosId(String vnos) {

        try {
            int vnesenID = Integer.parseInt(vnos);
            return true;
        }
        catch (NumberFormatException e){
            System.out.println(vnos + " ni število!");
            return false;
        }
    }

    private Boolean preveriVnos(String[] vnosniPodatki) {
        /*
        System.out.println("ID: " + vnosniPodatki[0]);
        System.out.println("Ime: " + vnosniPodatki[1]);
        System.out.println("Priimek: " + vnosniPodatki[2]);
        System.out.println("Naslov: " + vnosniPodatki[3]);
        System.out.println("Email: " + vnosniPodatki[4]);
        System.out.println("Telefon: " + vnosniPodatki[5]);
        System.out.println("Mobilni: " + vnosniPodatki[6]);
        System.out.println("Opomba: " + vnosniPodatki[7]);
        */
        if (preveriVnosId(vnosniPodatki[0])) {
            Kontakt novKontakt = null;
            try {
                novKontakt = new Kontakt(Integer.parseInt(vnosniPodatki[0]),vnosniPodatki[1],vnosniPodatki[2],vnosniPodatki[3],vnosniPodatki[4],vnosniPodatki[5],vnosniPodatki[6],vnosniPodatki[7]);
                for (Kontakt kontakt : seznamKontaktov) {

                     if (novKontakt.getId() == kontakt.getId()) {
                        if (kontakt.equals(novKontakt)) {
                            System.out.println("Vneseni kontakt že obstaja v imeniku - podvajanje!");
                        } else {
                            System.out.println("Kontakt z enakim ID-jem že obstaja, prosim vnesite drugačen ID!");
                        }
                        return false;
                    }
                    else if (kontakt.equals(novKontakt)) {
                        System.out.println("Vneseni kontakt že obstaja v imeniku - podvajanje!");
                        return false;
                    }
                }
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Manjkajoči podatki!! Prosim vnesite vse zahtevane podatke!");
            }

        }
        return false;
    }

    private Kontakt najdiKontaktZaId(int id) {

        for (Kontakt kontakt : seznamKontaktov) {
            if (kontakt.getId() == id) {
                return kontakt;
            }
        }
        return null;
    }

    private void posodobiPoljaKontakta(Kontakt kontakt, String[] polja) {

        kontakt.setIme(polja[0]);
        kontakt.setPriimek(polja[1]);
        kontakt.setNaslov(polja[2]);
        kontakt.setElektronskaPosta(polja[3]);
        kontakt.setTelefon(polja[4]);
        kontakt.setMobilniTelefon(polja[5]);
        kontakt.setOpomba(polja[6]);
    }

}
