package si.src.naloga.kontakt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class KontaktDBImpl {

    private static KontaktDBImpl instance; // za implementacijo singelton razreda
    private static final Logger log = Logger.getLogger(KontaktDBImpl.class.getName());

    private Connection con;

    public static KontaktDBImpl getInstance() {

        if (instance == null) { //ce klicemo prvic objek inicializiramo s konsturktorjem
            instance = new KontaktDBImpl();
        }
        return instance;
    }

    public KontaktDBImpl() {
        con = getConnection();
    }

    public Connection getConnection() {
        try {
            // vzpostavitev povezave z bazo
            con = DriverManager.getConnection(
                             "jdbc:postgresql://localhost:5432/kontakti",
                             "postgres",
                             "postgres");
            return con;
        }
        catch (Exception exc) {
            log.severe("Vzpostavítev povezave z bazo ni uspela. " + exc.toString());
        }

        return null;
    }

    /**
     * Pridobi in vrne kontakt z ujemajočim ID-jem
     */
    public Kontakt pridobiPoId(int id) {

        PreparedStatement ps = null;

        try {

            String sql = "SELECT * FROM kontakt WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return pridobiKontaktIzRS(rs);
            } else {
                log.info("Kontakt ne obstaja");
            }

        } catch (SQLException e) {
            log.severe(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.severe(e.toString());
                }
            }
        }
        return null;
    }

    /**
     * Pridobi in vrne vse kontakte iz baze.
     */
    public List<Kontakt> pridobiVse() {

        List<Kontakt> kontaktiList = new ArrayList<Kontakt>();
        Statement st = null;

        try {

            st = con.createStatement();
            String sql = "SELECT * FROM kontakt";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                kontaktiList.add(pridobiKontaktIzRS(rs));
            }
        }
        catch (SQLException exc) {
            log.severe(exc.toString());
        }
        finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException exc) {
                    log.severe(exc.toString());
                }
            }
        }

        return kontaktiList;
    }

    /**
     * Vstavi nov kontakt v bazo.
     */
    public void vstavi(Kontakt kont) {

        PreparedStatement ps = null;

        try {

            //naredimo sql stavek
            String sql = "INSERT INTO kontakt (id, ime, priimek, naslov, elektronskaposta, telefon, mobilnitelefon, opomba) VALUES (?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);

            //nastavimo vrednosti za ? v sql stavku
            ps.setInt(1, kont.getId());
            ps.setString(2, kont.getIme());
            ps.setString(3, kont.getPriimek());
            ps.setString(4, kont.getNaslov());
            ps.setString(5, kont.getElektronskaPosta());
            ps.setString(6, kont.getTelefon());
            ps.setString(7, kont.getMobilniTelefon());
            ps.setString(8, kont.getOpomba());
            ps.executeUpdate();

        }
        catch (SQLException exc) {
            log.severe(exc.toString());
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException exc) {
                    log.severe(exc.toString());
                }
            }
        }

    }

    /**
     * Posodobi kontakt v bazi.
     */
    public void posodobi(Kontakt kont) {

        PreparedStatement ps = null;

        try {

            String sql = "UPDATE kontakt SET ime = ?, priimek = ?, naslov = ?, elektronskaposta = ?, telefon = ?, mobilnitelefon = ?, opomba = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1,kont.getIme());
            ps.setString(2,kont.getPriimek());
            ps.setString(3,kont.getNaslov());
            ps.setString(4,kont.getElektronskaPosta());
            ps.setString(5,kont.getTelefon());
            ps.setString(6,kont.getMobilniTelefon());
            ps.setString(7,kont.getOpomba());
            ps.setInt(8,kont.getId());

            ps.executeUpdate();

        } catch (SQLException exc) {
            log.severe(exc.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException exc) {
                    log.severe(exc.toString());
                }
            }
        }

    }

    /**
     * Iz baze izbriše kontakt z ujemajočim ID-jem
     */
    public void izbrisiPoId(int id) {

        PreparedStatement ps = null;

        try {

            //naredimo sql stavek
            String sql = "DELETE FROM kontakt WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException exc) {
            log.severe(exc.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException exc) {
                    log.severe(exc.toString());
                }
            }
        }

    }


    /**
     * Iz rezultata poizvedbe naredi objek Kontakt.
     */
    private static Kontakt pridobiKontaktIzRS(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String ime = rs.getString("ime");
        String priimek = rs.getString("priimek");
        String naslov = rs.getString("naslov");
        String elektronskaPosta = rs.getString("elektronskaPosta");
        String telefon = rs.getString("telefon");
        String mobilniTelefon = rs.getString("mobilniTelefon");
        String opomba = rs.getString("opomba");


        return new Kontakt(id, ime, priimek, naslov, elektronskaPosta, telefon, mobilniTelefon, opomba);

    }

}
