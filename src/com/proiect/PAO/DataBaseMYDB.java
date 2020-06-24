package com.proiect.PAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


// clasa care e realizeaza servicii care sa expună operații de tip create, read, update, delete
// pentru clasele: Clienti,Teatru, Concert, Opera si Film

public class DataBaseMYDB {
    private static DataBaseMYDB instance;
    private static Connection con;
    private static Statement stmt;

    private DataBaseMYDB() {
    }

    public static DataBaseMYDB getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DataBaseMYDB.class) {
                if (instance == null) {
                    instance = new DataBaseMYDB();
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","Ifucking3cake");
                    stmt = con.createStatement();
                }
            }
        }
        return instance;

    }
    //read,  update, create, delete pentru Clienti
    public static ArrayList<Client> readClienti() throws SQLException {

        ArrayList<Client> vector = new ArrayList<>();
            String read = "SELECT * FROM Clienti";
            ResultSet rs = stmt.executeQuery(read);

            while (rs.next()) {
                vector.add(new Client(rs.getString("Nume"), rs.getInt("Varsta"), rs.getString("Email")));
            }

            return vector;
        }
    public static void updateClienti(String coloana, String valoare, String email) throws SQLException {

        String update_nume = "UPDATE Clienti SET "+coloana+"=? WHERE email=?";
        //String update_email = "UPDATE Clienti SET email=? WHERE idClienti=?";
        //String update_varsta = "UPDATE Clienti SET varsta=? WHERE idClienti=?";
        PreparedStatement pstmt = con.prepareStatement(update_nume);
        pstmt.setString(1, valoare);
        pstmt.setString(2,email);
        pstmt.executeUpdate();
    }

    public static void insertClienti(Client c) throws SQLException {

        String insert = "INSERT INTO Clienti VALUES('"+c.getNume()+"','"+Integer.toString(c.getVarsta())+"','"+c.getMail()+"')";
        stmt.executeUpdate(insert);
    }
    public static void deleteClienti(Client c) throws SQLException {

        String insert = "DELETE FROM Clienti WHERE Nume LIKE '"+c.getNume()+"'";
        stmt.executeUpdate(insert);
    }

    //read, update,create, delete pentru Teatru
    public static Locuri dataCreareLocuri(String locuri){

        String[] locuriString = locuri.split(" ");
        int []int_locuri = new int[8];
        for(int i=0; i<locuriString.length;i++){
            int_locuri[i]=Integer.parseInt(locuriString[i]);
        }
        Locuri l = new Locuri(int_locuri);
        l.creareLocuri();
        return l;
    }

    public static ArrayList<Eveniment> readTeatru() throws SQLException, ParseException {

        ArrayList<Eveniment> vector = new ArrayList<>();
        String read = "SELECT * FROM Teatru";
        ResultSet rs = stmt.executeQuery(read);

        while (rs.next()) {
            String nume = rs.getString("Nume");
            int pret = rs.getInt("Pret");
            String gen = rs.getString("Gen");
            int durata = rs.getInt("durata");
            String data = rs.getString("data");
            Locuri locuri = instance.dataCreareLocuri(rs.getString("sala"));
            String trupa = rs.getString("Trupa");
            String regizor = rs.getString("Regizor");
            vector.add(new Teatru(nume,pret,gen,durata,data,locuri,trupa,regizor));
        }

        return vector;
    }

    public static void updateTeatru(String coloana, String valoare, int id) throws SQLException {

        String update_nume = "UPDATE Teatru SET "+coloana+"=? WHERE idTeatru=?";
        PreparedStatement pstmt = con.prepareStatement(update_nume);
        pstmt.setString(1, valoare);
        pstmt.setInt(2,id+1);
        pstmt.executeUpdate();
    }

    public static void insertTeatru(Teatru t,int pk) throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();
        Locuri l = t.getLocuriEveniment();
        for (int value:l.getProp().values()) {
            stringBuilder.append(Integer.toString(value));
            stringBuilder.append(" ");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String insert = "INSERT INTO Teatru VALUES('"+pk+"','"+t.getNume()+"','"+Integer.toString(t.getPretBilet())+"','"+t.getGen()+"','"+t.getDurata()+"','"+format.format(t.getData())+"','"+stringBuilder+"','"+t.getTrupa()+"','"+t.getRegizor()+"')";
        stmt.executeUpdate(insert);
    }

    public static void deleteTeatru(Teatru t) throws SQLException {
        String delete = "DELETE FROM Teatru WHERE Nume LIKE '"+t.getNume()+"'";
        stmt.executeUpdate(delete);
    }

    public static ArrayList<Eveniment> readOpera() throws SQLException, ParseException {

        ArrayList<Eveniment> vector = new ArrayList<>();
        String read = "SELECT * FROM Opera";
        ResultSet rs = stmt.executeQuery(read);

        while (rs.next()) {
            String nume = rs.getString("Nume");
            int pret = rs.getInt("Pret");
            String gen = rs.getString("Gen");
            int durata = rs.getInt("durata");
            String data = rs.getString("data");
            Locuri locuri = instance.dataCreareLocuri(rs.getString("sala"));
            String trupa = rs.getString("Trupa");
            String dirijor = rs.getString("Dirijor");
            String orchestra = rs.getString("Orchestra");
            vector.add(new Opera(nume,pret,gen,durata,data,locuri,trupa,dirijor,orchestra));
        }

        return vector;
    }

    public static void updateOpera(String coloana, String valoare, int id) throws SQLException {

        String update_nume = "UPDATE Opera SET "+coloana+"=? WHERE idOpera=?";
        PreparedStatement pstmt = con.prepareStatement(update_nume);
        pstmt.setString(1, valoare);
        pstmt.setInt(2,id+1);
        pstmt.executeUpdate();
    }

    public static void insertOpera(Opera o,int pk) throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();
        Locuri l = o.getLocuriEveniment();
        for (int value:l.getProp().values()) {
            stringBuilder.append(Integer.toString(value));
            stringBuilder.append(" ");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String insert = "INSERT INTO Opera VALUES('"+pk+"','"+o.getNume()+"','"+Integer.toString(o.getPretBilet())+"','"+o.getGen()+"','"+o.getDurata()+"','"+format.format(o.getData())+"','"+stringBuilder+"','"+o.getTrupa()+"','"+o.getOrchestra()+"','"+o.getDirijor()+"')";
        stmt.executeUpdate(insert);
    }

    public static void deleteOpera(Opera o) throws SQLException {
        String delete = "DELETE FROM Opera WHERE Nume LIKE '"+o.getNume()+"'";
        stmt.executeUpdate(delete);
    }

    //read, update, inset delete pt concerte
    public static ArrayList<Eveniment> readConcert() throws SQLException, ParseException {

        ArrayList<Eveniment> vector = new ArrayList<>();
        String read = "SELECT * FROM Concert";
        ResultSet rs = stmt.executeQuery(read);

        while (rs.next()) {
            String nume = rs.getString("Nume");
            int pret = rs.getInt("Pret");
            String gen = rs.getString("Gen");
            int durata = rs.getInt("durata");
            String data = rs.getString("data");
            Locuri locuri = instance.dataCreareLocuri(rs.getString("sala"));
            String artist = rs.getString("Artist");
            vector.add(new Concert(nume,pret,gen,durata,data,locuri,artist));
        }

        return vector;
    }

    public static void updateConcert(String coloana, String valoare, int id) throws SQLException {

        String update_nume = "UPDATE Concert SET "+coloana+"=? WHERE idConcert=?";
        PreparedStatement pstmt = con.prepareStatement(update_nume);
        pstmt.setString(1, valoare);
        pstmt.setInt(2,id+1);
        pstmt.executeUpdate();
    }

    public static void insertConcert(Concert o,int pk) throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();
        Locuri l = o.getLocuriEveniment();
        for (int value:l.getProp().values()) {
            stringBuilder.append(Integer.toString(value));
            stringBuilder.append(" ");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String insert = "INSERT INTO Concert VALUES('"+pk+"','"+o.getNume()+"','"+Integer.toString(o.getPretBilet())+"','"+o.getGen()+"','"+o.getDurata()+"','"+format.format(o.getData())+"','"+stringBuilder+"','"+o.getNumeArtist()+"')";
        stmt.executeUpdate(insert);
    }

    public static void deleteConcert(Concert o) throws SQLException {
        String delete = "DELETE FROM Concert WHERE Nume LIKE '"+o.getNume()+"'";
        stmt.executeUpdate(delete);
    }


    public static ArrayList<Eveniment> readFilm() throws SQLException, ParseException {

        ArrayList<Eveniment> vector = new ArrayList<>();
        String read = "SELECT * FROM Film";
        ResultSet rs = stmt.executeQuery(read);

        while (rs.next()) {
            String nume = rs.getString("Nume");
            int pret = rs.getInt("Pret");
            String gen = rs.getString("Gen");
            int durata = rs.getInt("durata");
            String data = rs.getString("data");
            Locuri locuri = instance.dataCreareLocuri(rs.getString("sala"));
            int xd = rs.getInt("xD");
            Boolean dublat = Boolean.parseBoolean(rs.getString("dublat"));
            String limba = rs.getString("limba");
            vector.add(new Film(nume,pret,gen,durata,data,locuri,xd,limba,dublat));
        }

        return vector;
    }

    public static void updateFilm(String coloana, String valoare, int id) throws SQLException {

        String update_nume = "UPDATE Film SET "+coloana+"=? WHERE idFilm=?";
        PreparedStatement pstmt = con.prepareStatement(update_nume);
        pstmt.setString(1, valoare);
        pstmt.setInt(2,id+1);
        pstmt.executeUpdate();
    }

    public static void insertFilm(Film o,int pk) throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();
        Locuri l = o.getLocuriEveniment();
        for (int value:l.getProp().values()) {
            stringBuilder.append(Integer.toString(value));
            stringBuilder.append(" ");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String insert = "INSERT INTO Film VALUES('"+pk+"','"+o.getNume()+"','"+Integer.toString(o.getPretBilet())+"','"+o.getGen()+"','"+o.getDurata()+"','"+format.format(o.getData())+"','"+stringBuilder+"','"+o.getxD()+"','"+o.getSubtitrari()+"','"+o.isDublat()+"')";
        stmt.executeUpdate(insert);
    }

    public static void deleteFilm(Film o) throws SQLException {
        String delete = "DELETE FROM Film WHERE Nume LIKE '"+o.getNume()+"'";
        stmt.executeUpdate(delete);
    }

}