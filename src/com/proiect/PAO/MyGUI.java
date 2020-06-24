package com.proiect.PAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyGUI extends JFrame{

    private JFrame frame = new JFrame("Aplicatie sala de spectacol");;
    private JPanel firstPanel = new JPanel();
    private JPanel secondPanel = new JPanel();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private GridBagLayout gridBagLayout = new GridBagLayout();
    private GridBagConstraints gridBagConstraints = new GridBagConstraints();

    public MyGUI(){

        tabbedPane.add("Afisare",firstPanel);
        tabbedPane.add("Cautare",secondPanel);
        frame.setSize(new Dimension(600,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(tabbedPane);
    }

    public void createFirst(ArrayList<Eveniment> events){
        JButton buton = new JButton("Afiseaza Toate Evenimentele disponibile");

        ArrayList<Eveniment> e = new ArrayList<>();
        try {
            e = ClientService.AfiseazaToateEvenimenteleDisponibile(events);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        firstPanel.add(buton);
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String[] columnNames = { "Nume", "Data", "Pret bilet","Genul","Durata" };
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        dtm.setColumnIdentifiers(columnNames);
        table.setModel(dtm);
        JScrollPane scrollPane = new JScrollPane( table );
        JLabel label = new JLabel("Nu mai exista spectacole cu locuri disponibile");
        if(e.size()==0)
            table.add(label);
        else{
            for(Eveniment event:e){
                dtm.addRow(new Object[]{event.getNume(),dateFormat.format(event.getData()),Integer.toString(event.getPretBilet()),event.getGen(),Integer.toString(event.getDurata())});
            }
        }
        buton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                firstPanel.add(scrollPane, BorderLayout.CENTER);
            }
        });

    }

    public void adauga(Component comp, int x, int y, int w, int h){

        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = w;
        gridBagConstraints.gridheight = h;
        gridBagLayout.setConstraints(comp,gridBagConstraints);
        secondPanel.add(comp);
    }
    public void createSecond(ArrayList<Eveniment> events){
        secondPanel.setLayout(gridBagLayout);
        gridBagConstraints.insets = new Insets(1,1,1,1);
        gridBagConstraints.weightx=1.0;
        gridBagConstraints.weighty=1.0;
        JButton cautaData = new JButton("Cauta eveniment in data:");
        JButton cautaGen = new JButton("Cauta eveniment dupa gen:");
        JButton cautaNume = new JButton("Cauta eveniment dupa nume:");
        JButton cautaSubclasa = new JButton("Cauta eveniment dupa tip:");
        this.adauga(cautaData,0,0,1,1);
        this.adauga(cautaGen,1,0,1,1);
        this.adauga(cautaNume,2,0,1,1);

        gridBagConstraints.anchor=GridBagConstraints.CENTER;
        gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
        this.adauga(cautaSubclasa,1,1,1,1);

        JTextField text = new JTextField("",10);

        this.adauga(text,1,2,1,1);


        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String[] columnNames = { "Nume", "Data", "Pret bilet","Genul","Durata" };
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        dtm.setColumnIdentifiers(columnNames);
        table.setModel(dtm);
        JScrollPane scrollPane = new JScrollPane(table);
        gridBagConstraints.fill=GridBagConstraints.BOTH;
        adauga(scrollPane,0,4,10,1);
        cautaData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Eveniment> e = new ArrayList<>();
                dtm.getDataVector().removeAllElements();
                try {
                    e.addAll( ClientService.cautareEvenimenteInData(events,text.getText()));
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
                if(e.size()==0)
                    JOptionPane.showMessageDialog(frame, "Nu exista spectacole in aceasta data");
                else{
                    for(Eveniment event:e){
                        dtm.addRow(new Object[]{event.getNume(),dateFormat.format(event.getData()),Integer.toString(event.getPretBilet()),event.getGen(),Integer.toString(event.getDurata())});
                    }
                }
            }
        });
        cautaNume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                dtm.getDataVector().removeAllElements();
                try {
                    Eveniment event = ClientService.cautaEvenimentDupaNume(events,text.getText());

                    if(event==null)
                        JOptionPane.showMessageDialog(frame, "Nu exista spectacole cu acest nume");
                   else
                       dtm.addRow(new Object[]{event.getNume(),dateFormat.format(event.getData()),Integer.toString(event.getPretBilet()),event.getGen(),Integer.toString(event.getDurata())});

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        cautaGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Eveniment> e = new ArrayList<>();
                dtm.getDataVector().removeAllElements();
                try {
                    e.addAll( ClientService.cautareEvenimenteDupaGen(events,text.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if(e.size()==0)
                    JOptionPane.showMessageDialog(frame, "Nu exista spectacole de acest gen");
                else{
                    for(Eveniment event:e){
                        dtm.addRow(new Object[]{event.getNume(),dateFormat.format(event.getData()),Integer.toString(event.getPretBilet()),event.getGen(),Integer.toString(event.getDurata())});
                    }
                }
            }
        });
        cautaSubclasa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Eveniment> e = new ArrayList<>();
                dtm.getDataVector().removeAllElements();
                try {
                    e.addAll( ClientService.cautaDoarOSublcasa(events,text.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if(e.size()==0)
                    JOptionPane.showMessageDialog(frame, "Nu exista spectacole de "+text.getText());
                else{
                    for(Eveniment event:e){
                        dtm.addRow(new Object[]{event.getNume(),dateFormat.format(event.getData()),Integer.toString(event.getPretBilet()),event.getGen(),Integer.toString(event.getDurata())});
                    }
                }
            }
        });
    }


    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JPanel getFirstPanel() {
        return firstPanel;
    }

    public void setFirstPanel(JPanel firstPanel) {
        this.firstPanel = firstPanel;
    }

    public JPanel getSecondPanel() {
        return secondPanel;
    }

    public void setSecondPanel(JPanel secondPanel) {
        this.secondPanel = secondPanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}
