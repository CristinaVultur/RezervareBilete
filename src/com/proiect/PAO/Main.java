package com.proiect.PAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws ParseException, IOException, SQLException {

        DataBaseMYDB instance = DataBaseMYDB.getInstance();
        Client c = new Client("Gigel",29,"gigel.ionescu@yahoo.com");
        ArrayList<Eveniment> events = new ArrayList<>();
        ArrayList<Client> clienti = DataBaseMYDB.getInstance().readClienti();
        clienti.get(0).setVarsta(30);
        DataBaseMYDB.getInstance().updateClienti("Nume","Anna",clienti.get(0).getMail());
        DataBaseMYDB.getInstance().updateClienti("Varsta","30",clienti.get(0).getMail());
        clienti.get(1).setNume("Anna");

        //DataBaseMYDB.getInstance().insertClienti(c);
        DataBaseMYDB.getInstance().deleteClienti(c);

        events.addAll(DataBaseMYDB.getInstance().readTeatru());

        DataBaseMYDB.getInstance().updateTeatru("Nume","Schimbare primul nume",0);
        events.get(0).setNume("Schimbare primul nume");

        Locuri Sala = new Locuri(new int[]{50, 50, 30, 30, 15, 15, 5, 5});
        Sala.creareLocuri();
        Teatru teatru = new Teatru("Nume de Teatru", 100, "drama", 2, "2020-02-24", Sala, "nume trupa", "nume regizor");


        events.addAll(DataBaseMYDB.getInstance().readOpera());

        Locuri Sala1 = new Locuri(new int[]{60, 60, 30, 30, 20, 20, 10, 10});
        Sala1.creareLocuri();

        Opera opera = new Opera("Nume Opera", 200, "tragic", 3, "2020-03-02", Sala1, "Trupa Opera", "Orchestra", "Dirijor");

        DataBaseMYDB.getInstance().updateOpera("Nume","Schimbare nume opera",4);
        events.get(4).setNume("Schimbare nume opera");

        events.addAll(DataBaseMYDB.getInstance().readConcert());

        DataBaseMYDB.getInstance().updateConcert("Nume","Schimbare nume concert",6);
        events.get(4).setNume("Schimbare nume concert");

        Locuri Sala3 = new Locuri(new int[]{50, 50, 30, 30, 15, 15, 5, 5});
        Concert concert = new Concert("Nume Concert", 50, "rock", 4, "2021-03-12", Sala3, "Artist");

        events.addAll(DataBaseMYDB.getInstance().readFilm());

        DataBaseMYDB.getInstance().updateFilm("Nume","Schimbare nume film",9);
        events.get(4).setNume("Schimbare nume film");


        Locuri Sala2 = new Locuri(new int[]{40, 40, 30, 30, 10, 10, 0, 0});
        Sala2.creareLocuri();

        Film film = new Film("Nume Film", 20, "comedie", 1, "2020-04-23", Sala2, 2, "romana", false);

        //DataBaseMYDB.getInstance().insertTeatru(teatru,events.size()+1);
        DataBaseMYDB.getInstance().deleteTeatru(teatru);

        //DataBaseMYDB.getInstance().insertOpera(opera,events.size()+1);
        DataBaseMYDB.getInstance().deleteOpera(opera);

        //DataBaseMYDB.getInstance().insertConcert(concert,events.size()+1);
        DataBaseMYDB.getInstance().deleteConcert(concert);

        //DataBaseMYDB.getInstance().insertFilm(film,events.size()+1);
        DataBaseMYDB.getInstance().deleteFilm(film);

        MyGUI gui = new MyGUI();
        gui.createFirst(events);
        gui.createSecond(events);

        //System.out.println(events.get(4).getNume());

       /* events.addAll(SingletonFiles.getInstance().citireConcert("concert.csv"));

        events.addAll(SingletonFiles.getInstance().citireOpera("opera.csv"));

        events.addAll(SingletonFiles.getInstance().citireFilm("film.csv"));

        Locuri Sala = new Locuri(new int[]{50, 50, 30, 30, 15, 15, 5, 5});
        Sala.creareLocuri();
        Teatru teatru = new Teatru("Nume de Teatru", 100, "drama", 2, "2020-02-24", Sala, "nume trupa", "nume regizor");


        SingletonFiles.getInstance().writeTeatru("teatru.csv",teatru);

        events.add(teatru);

        Locuri Sala1 = new Locuri(new int[]{60, 60, 30, 30, 20, 20, 10, 10});
        Sala1.creareLocuri();

        Opera opera = new Opera("Nume Opera", 200, "tragic", 3, "2020-03-02", Sala1, "Trupa Opera", "Orchestra", "Dirijor");
        events.add(opera);

        SingletonFiles.getInstance().writeOpera("opera.csv",opera);

        Locuri Sala2 = new Locuri(new int[]{40, 40, 30, 30, 10, 10, 0, 0});
        Sala2.creareLocuri();

        Film film = new Film("Nume Film", 20, "comedie", 1, "2020-04-23", Sala2, 2, "romana", false);

       SingletonFiles.getInstance().writeFilm("film.csv",film);
        events.add(film);

        Locuri Sala3 = new Locuri(new int[]{50, 50, 30, 30, 15, 15, 5, 5});
        Concert concert = new Concert("Nume Concert", 50, "rock", 4, "2021-03-12", Sala3, "Artist");

        SingletonFiles.getInstance().writeConcert("concert.csv",concert);

        events.add(concert);
        //caut evenimentul dupa nume
        try {
            Eveniment e = ClientService.cautaEvenimentDupaNume(events, "Nume de Teatru");
            Eveniment e2 = ClientService.cautaEvenimentDupaNume(events, "Nume Film1");

            // afisare locuri disponibile la un anumit spectacol
            ClientService.afisareLocuriDisponibile(e);

            //rezerva bilet
            ClientService.rezervareBilet(e2, 27, "normal", clienti.get(0));
            ClientService.rezervareBilet(e, 26, "normal", clienti.get(1));


            //afisare locuri rezervate de cliente
            ClientService.afisareLocuriRezervate(clienti.get(1));

            //anuleaza rezervarea la un anumit spectacol
            ClientService.anuleazaRezervarea(e,  clienti.get(1));

            ClientService.afisareLocuriRezervate( clienti.get(0));

            //afiseaza evenimentele la care mai sunt locuri disponibile
            ClientService.AfiseazaToateEvenimenteleDisponibile(events);

            //caut evenimente dupa gen
            ClientService.cautareEvenimenteDupaGen(events, "comedie");

            //caut locurile de un anumit tip disponibile la evenimentul e
            ClientService.cautareLocuri(e, "VIP");

            //caut doar evenimentele de un anumit tip: opera, film etc

            ClientService.cautaDoarOSublcasa(events, "Opera");

            //caut evenient dupa data
            ClientService.cautareEvenimenteInData(events, "Feb 30, 2020");


            ClientService.rezervareBilet(e, 26, "normal",  clienti.get(2));

            //schimb un bilet rezervat
            ClientService.schimbaLocul(e, 46, "VIP",  clienti.get(2));
        } catch (Exception e) {
            System.out.println("exception: " + e.getMessage());

        }*/


    }
}
