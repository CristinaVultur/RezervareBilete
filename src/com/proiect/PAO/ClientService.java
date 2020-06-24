package com.proiect.PAO;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public final class ClientService {

    private ClientService(){}

    public static void rezervareBilet(Eveniment e, int cod , String tip, Client c) throws IOException { //rezerva un bilet la un evenimen

        c.setUnLoc(e,e.getLocuriEveniment().cautareDupaCod(cod));//adaug locul si evenimentul in treemap-ul clientului cu locuri rezervate
        e.getLocuriEveniment().DecreaseOneProp(tip);;//scad nr de locuri disponibile si nr de locuri disponibile de acel tip
        e.getLocuriEveniment().cautareDupaCod(cod).setDisponibil(false);//setez disponibilitatea biletului ca fals
        System.out.println("Bilet rezervat!");
        AuditService.audit("audit.csv", String.format("Clientul %s a rezervat locul %s %d la %s", c.getNume(), tip, cod, e.getNume()));
    }

    public static void cautareLocuri(Eveniment e, String tip) throws IOException { //cauta un loc disponibil dupa tipul sau : loja, normal ,vip
        boolean gasit = false;
        for(Loc loc: e.getLocuriEveniment().getLocuri())
            if (loc.getTip().equals(tip) && loc.getDisponibil()) {
                System.out.print(tip + " " + loc.getCod()+" pret: "+(e.getPretBilet()*loc.getPretLoc()));
                gasit = true;
                System.out.print("\n");
            }

        if(!gasit) System.out.println("Nu mai exista locuri disponibile");
        AuditService.audit("audit.csv", String.format("Cautare Locuri %s", tip));
    }

    public static void anuleazaRezervarea(Eveniment e, Client c) throws IOException { //anularea rezervarii

        Loc l = new Loc(c.getLocuriRezervate().get(e));
        e.getLocuriEveniment().IncreaseOneProp(l.getTip());// cresc nr de locuri disponiblile
        e.getLocuriEveniment().cautareDupaCod(l.getCod()).setDisponibil(true); //setez disponiblilitatea ca fiind avdevarata
        c.stergeUnLoc(e);
        System.out.println("Anulat cu succes");
        AuditService.audit("audit.csv", String.format("Clientul %s a anulat rezervarea la %s", c.getNume(), e.getNume()));
    }

    public static void afisareLocuriRezervate(Client c) throws IOException { //afisare locuri pe care le-a rezervat deja

        for(Eveniment e:c.getLocuriRezervate().keySet()){
            e.afisare();
            System.out.print(": loc rezervat: "+c.getLocuriRezervate().get(e).getTip()+" "+c.getLocuriRezervate().get(e).getCod()+"\n");

        }
        if(c.getLocuriRezervate().isEmpty()) System.out.println("Nu exista rezervari facute.");
        AuditService.audit("audit.csv", String.format("Afisare Locuri Rezervate de %s", c.getNume()));
    }

    public static ArrayList<Eveniment> cautareEvenimenteInData(ArrayList<Eveniment> events, String data) throws ParseException, IOException { //cauta evenimente intr-o anumita data

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        ArrayList<Eveniment> e= new ArrayList<>();
        Date caut = dateFormat.parse(data);
        boolean gasit = false;
        for(Eveniment E:events){
             if(E.getData().compareTo(caut)==0) {
                E.afisare();
                e.add(E);
                System.out.print("\n");
                gasit = true;
            }
        }
        if(!gasit) System.out.println("Nu exista spectacole in aceasta data");
        AuditService.audit("audit.csv", String.format("Cautarea Evenimentelor din data %s", data));
        return e;
    }

    public static ArrayList<Eveniment> cautareEvenimenteDupaGen(ArrayList<Eveniment> events, String gen) throws IOException { //cauta dupa un anum gen

        ArrayList<Eveniment> e = new ArrayList<>();
        boolean gasit = false;
        for(Eveniment E:events) {
            if(E.getGen().equals(gen)){
                gasit = true;
                E.afisare();
                e.add(E);
                System.out.print("\n");
            }
        }
        if(!gasit) System.out.println("Nu exista spectacole de acest gen");
        AuditService.audit("audit.csv", String.format("Cautarea Evenimentelor de %s", gen));
   return e;
    }

    public static ArrayList<Eveniment> AfiseazaToateEvenimenteleDisponibile(ArrayList<Eveniment> events) throws IOException {

        ArrayList<Eveniment> e = new ArrayList<>();
        boolean gasit = false;
        for(Eveniment E:events) {
            if(E.getLocuriEveniment().getOneProp("nrDisponibil")>0){ //daca exista locuri disponibile la evenim il afisez
                gasit = true;
                E.afisare();
                e.add(E);
                System.out.print("\n");
            }
        }
        if(!gasit) System.out.println("Nu mai exista spectacole cu locuri disponibile");
        AuditService.audit("audit.csv","Afisarea Tuturor Evenimentelor Disponibile");
    return e;
    }

    public static void schimbaLocul(Eveniment e, int cod , String tip, Client c) throws IOException {

            ClientService.anuleazaRezervarea(e,c);
            ClientService.rezervareBilet(e,cod,tip,c);
            System.out.println("Schimbare reusita");
        AuditService.audit("audit.csv", String.format("Clientul %s a schimbat locul la %s", c.getNume(), e.getNume()));
    }

    public static void afisareLocuriDisponibile(Eveniment e) throws IOException {

            boolean gasit = false;
            for(Loc l:e.getLocuriEveniment().getLocuri()){
                if(l.getDisponibil()) {
                    System.out.println("Loc: " + l.getTip() + " " + l.getCod() + " pret:" + (e.getPretBilet() * l.getPretLoc()));
                    gasit = true;
                }
            }
            if(!gasit) System.out.println("Nu exista locuri disponibile");
            AuditService.audit("audit.csv", String.format("Afisarea Locurilor Disponibile la %s", e.getNume()));
    }

    public static Eveniment cautaEvenimentDupaNume(ArrayList<Eveniment> events, String nume) throws IOException {

        for(Eveniment E:events) {
            if(E.getNume().equals(nume)){
                AuditService.audit("audit.csv","Cauta Eveniment Dupa Nume");
                return E;
            }
        }
        System.out.println("Nu exista spectacole cu acest nume");
        AuditService.audit("audit.csv", String.format("Cauta Evenimentul %s", nume));
        return null;

    }
    //returneaza doar filmele, opera sau teatru

    public static ArrayList<Eveniment> cautaDoarOSublcasa(ArrayList<Eveniment> events,String subclasa) throws IOException {

        ArrayList<Eveniment> E = new ArrayList<>();
        boolean gasit = false;
        for(Eveniment e:events){
            if(Objects.equals(e.getClass().getName(), "com.proiect.PAO."+subclasa)){
                e.afisare();
                E.add(e);
                System.out.print("\n");
                gasit = true;
            }
        }
        if(!gasit) System.out.println("Nu exista nici un eveniment de tipul: "+subclasa);
        AuditService.audit("audit.csv", String.format("Cauta %s", subclasa));
    return E;
    }




}
