package si.src.naloga;

import si.src.naloga.imenik.TelefonskiImenik;
import si.src.naloga.kontakt.Kontakt;
import si.src.naloga.kontakt.KontaktDBImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

        TelefonskiImenik telefonskiImenik = new TelefonskiImenik();

        izpisiMenu();

        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String akcija = "";

        // zanka za izris menija
        while (!"0".equals(akcija)) {  // prej
            akcija = in.next();

            switch (akcija) {
                case "1":
                    telefonskiImenik.izpisiVseKontakte();
                    break;
                case "2":
                    telefonskiImenik.dodajKontakt(in);
                    break;
                case "3":
                    telefonskiImenik.urediKontakt(in);
                    break;
                case "4":
                    telefonskiImenik.izbrisiKontaktPoId(in);
                    break;
                case "5":
                    telefonskiImenik.izpisiKontaktZaId(in);
                    break;
                case "6":
                    telefonskiImenik.izpisiSteviloKontaktov();
                    break;
                case "7":
                    telefonskiImenik.serializirajSeznamKontaktov();
                    break;
                case "8":
                    telefonskiImenik.naloziSerializiranSeznamKontakotv();
                    break;
                case "9":
                    telefonskiImenik.izvoziPodatkeVCsvDatoteko();
                    break;
                case "10":
                    telefonskiImenik.isciPoImenuAliPriimku(in);
                    break;
                case "11":
                    telefonskiImenik.shraniKontakteVBazo();
                    break;
                case "12":
                    telefonskiImenik.pridobiKontakteIzBaze();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Napačna izbira!!!");
                    break;
            }

            izpisiMenu();
        }
    }

    /**
     * Uporabniku izpišemo menu
     */
    public static void izpisiMenu() {

        System.out.println("");
        System.out.println("");
        System.out.println("Aplikacija telefonski imenik:");
        System.out.println("-----------------------------------");
        System.out.println("Akcije:");
        System.out.println("1 - izpiši vse kontakte v imeniku");
        System.out.println("2 - dodaj kontakt v imenik");
        System.out.println("3 - uredi obstoječi kontakt");
        System.out.println("4 - briši kontakt po ID-ju");
        System.out.println("5 - izpiši kontakt po ID-ju");
        System.out.println("6 - izpiši število vseh kontaktov");
        System.out.println("7 - Shrani kontakte na disk (serializacija)");
        System.out.println("8 - Preberi kontake iz serializirane datoteke");
        System.out.println("9 - Izvozi kontakte v csv");
        System.out.println("10 - Iskanje kontaktov glede na ime ali priimek");
        System.out.println("11 - Shranjevanje kontaktov v bazo");
        System.out.println("12 - Uvoz kontaktov iz baze");
        System.out.println("");
        System.out.println("0 - Izhod iz aplikacije");
        System.out.println("----------------------------------");
        System.out.println("Akcija: ");


    }
}
