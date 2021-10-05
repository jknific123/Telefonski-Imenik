package si.src.naloga.kontakt;

import java.io.Serializable;
import java.util.Objects;

public class Kontakt implements Serializable {

    private static final long serialVersionUID = -5462212594138376176L;

    private int id;
    private String ime;
    private String priimek;
    private String naslov;
    private String elektronskaPosta;
    private String telefon;
    private String mobilniTelefon;
    private String opomba;

    public Kontakt(int id, String ime, String priimek, String naslov, String elektronskaPosta, String telefon, String mobilniTelefon, String opomba) {
        this.id = id;
        this.ime = ime;
        this.priimek = priimek;
        this.naslov = naslov;
        this.elektronskaPosta = elektronskaPosta;
        this.telefon = telefon;
        this.mobilniTelefon = mobilniTelefon;
        this.opomba = opomba;
    }

    public Kontakt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getElektronskaPosta() {
        return elektronskaPosta;
    }

    public void setElektronskaPosta(String elektronskaPosta) {
        this.elektronskaPosta = elektronskaPosta;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMobilniTelefon() {
        return mobilniTelefon;
    }

    public void setMobilniTelefon(String mobilniTelefon) {
        this.mobilniTelefon = mobilniTelefon;
    }

    public String getOpomba() {
        return opomba;
    }

    public void setOpomba(String opomba) {
        this.opomba = opomba;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kontakt kontakt = (Kontakt) o;
        return id == kontakt.id &&
                Objects.equals(ime, kontakt.ime) &&
                Objects.equals(priimek, kontakt.priimek) &&
                Objects.equals(naslov, kontakt.naslov) &&
                Objects.equals(elektronskaPosta, kontakt.elektronskaPosta) &&
                Objects.equals(telefon, kontakt.telefon) &&
                Objects.equals(mobilniTelefon, kontakt.mobilniTelefon) &&
                Objects.equals(opomba, kontakt.opomba);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ime, priimek, naslov, elektronskaPosta, telefon, mobilniTelefon, opomba);
    }

    @Override
    public String toString() {
        return "Kontakt{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", priimek='" + priimek + '\'' +
                ", naslov='" + naslov + '\'' +
                ", elektronskaPosta='" + elektronskaPosta + '\'' +
                ", telefon='" + telefon + '\'' +
                ", mobilniTelefon='" + mobilniTelefon + '\'' +
                ", opomba='" + opomba + '\'' +
                '}';
    }
}
