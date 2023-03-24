package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class Game extends JFrame implements ActionListener, Runnable {

    Thread watek_czas;
    JPanel panel, panel_czas;
    JButton btn[][] = new JButton[25][25];
    char wyniki[][] = new char[25][25];
    int kolejka = 0;
    JLabel ilosc, ilosc2, x, o, minuty, sekundy, dwukrop;
    JTextField pole, pole2;
    JButton restart;
    boolean czy_wygrana, stoppedThread;
    int s, min;
    
    public static void main(String[] args) {
       Game mojaGra = new Game();
    }
    
    Game() {
        super("Kółko i Krzyżyk na 5");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1370, 740);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(25, 25));
        panel.setPreferredSize(new Dimension(1150,695));
        
        SpringLayout s = new SpringLayout();
        setLayout(s);
        
        for(int i=0; i<25; i++) {
            for(int j=0; j<25; j++) { 
                btn[i][j] = new JButton(" ");
                panel.add(btn[i][j]);
                btn[i][j].addActionListener(this);
                btn[i][j].setFont(new Font("Helvetica", 50, 17)); 
                
            }
        }
        add(panel);
        
        pole = new JTextField("Imię Gracza 1");
        pole.setPreferredSize(new Dimension(150,30));
        s.putConstraint(SpringLayout.WEST,pole, 1180, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,pole, 50, SpringLayout.NORTH, this);
        pole.setFont(new Font("Helvetica", 50, 23)); 
        add(pole);
        ilosc = new JLabel("0");
        s.putConstraint(SpringLayout.WEST,ilosc, 1250, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,ilosc, 80, SpringLayout.NORTH, this);
        ilosc.setFont(new Font("Helvetica", 50, 50)); 
        add(ilosc);
        pole2 = new JTextField("Imię Gracza 2");
        pole2.setPreferredSize(new Dimension(150,30));
        s.putConstraint(SpringLayout.WEST,pole2, 1180, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,pole2, 170, SpringLayout.NORTH, this);
        pole2.setFont(new Font("Helvetica", 50, 23)); 
        add(pole2);
        ilosc2 = new JLabel("0");
        s.putConstraint(SpringLayout.WEST,ilosc2, 1250, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,ilosc2, 200, SpringLayout.NORTH, this);
        ilosc2.setFont(new Font("Helvetica", 50, 50)); 
        add(ilosc2);
        
        restart = new JButton("RESTART");
        restart.setPreferredSize(new Dimension(120,60));
        restart.setBackground(Color.yellow);
        restart.setFont(new Font("Helvetica", 50, 18));
        s.putConstraint(SpringLayout.WEST,restart, 1190, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,restart, 550, SpringLayout.NORTH, this);
        restart.addActionListener(this);
        add(restart);
        
        x = new JLabel("X:");
        s.putConstraint(SpringLayout.WEST,x, 1180, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,x, 90, SpringLayout.NORTH, this);
        x.setFont(new Font("Helvetica", 50, 40));
        x.setForeground(Color.red);
        add(x);
        o = new JLabel("O:");
        s.putConstraint(SpringLayout.WEST,o, 1180, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,o, 208, SpringLayout.NORTH, this);
        o.setFont(new Font("Helvetica", 50, 40));
        o.setForeground(Color.blue);
        add(o);
        
        panel_czas = new JPanel();
        s.putConstraint(SpringLayout.WEST,panel_czas, 1180, SpringLayout.WEST, this);
        s.putConstraint(SpringLayout.NORTH,panel_czas, 400, SpringLayout.NORTH, this);
        minuty = new JLabel();
        minuty.setFont(new Font("Helvetica", 50, 40));
        panel_czas.add(minuty);
        dwukrop = new JLabel(" : ");
        dwukrop.setFont(new Font("Helvetica", 50, 40));
        panel_czas.add(dwukrop);
        sekundy = new JLabel();
        sekundy.setFont(new Font("Helvetica", 50, 40));
        panel_czas.add(sekundy);
        add(panel_czas);
        
        setVisible(true);
        
        startTimeThread();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source == restart) {
            stoppedThread = true;
            btnEnabled();
            try {
                restart();
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(int i=0; i<25; i++) {
            for(int j=0; j<25; j++) { 
                if(source == btn[i][j] && btn[i][j].getText() == " ") {
                    kolejka++;
                    if(kolejka % 2 == 1) {
                        btn[i][j].setForeground(Color.red);
                        btn[i][j].setText("X");
                        wyniki[i][j] = 'X';
                        i=25; j=25;
                        sprawdz('X');
                    }
                    else {
                        btn[i][j].setForeground(Color.blue);
                        btn[i][j].setText("O");
                        wyniki[i][j] = 'O';
                        i=25; j=25;
                        sprawdz('O');
                    }  
                }
            }
        }    
    }
    
    void sprawdz(char z) {
        czy_wygrana = false;
        
            if(!czy_wygrana){
                for(int a=0; a<25; a++) { //sprawdzenie poziomo
                    for(int b=0; b<21; b++) {
                        if(wyniki[a][b]==z && wyniki[a][b+1]==z && wyniki[a][b+2]==z && wyniki[a][b+3]==z && wyniki[a][b+4]==z) {
                            showResultAndMessage(z);
                        }
                    }
                }
            }
            if(!czy_wygrana){
                for(int b=0; b<25; b++) { //sprawdzenie pionowo
                    for(int a=0; a<21; a++) {
                        if(wyniki[a][b]==z && wyniki[a+1][b]==z && wyniki[a+2][b]==z && wyniki[a+3][b]==z && wyniki[a+4][b]==z) {
                            showResultAndMessage(z);
                        }
                    }
                } 
            }
            if(!czy_wygrana){
                for(int a=20; a>=0; a--) { //sprawdzenie w dolnym, lewym trójkącie
                    for(int b=0; b<21-a; b++) {
                        if(wyniki[b+a][b]==z && wyniki[b+a+1][b+1]==z && wyniki[b+a+2][b+2]==z && wyniki[b+a+3][b+3]==z && wyniki[b+a+4][b+4]==z) {
                            showResultAndMessage(z);
                        }
                    }
                }
            }
            if(!czy_wygrana){
                for(int b=20; b>=0; b--) { //sprawdzenie w górnym, prawym trójkącie
                    for(int a=0; a<21-b; a++) {
                        if(wyniki[a][b+a]==z && wyniki[a+1][b+a+1]==z && wyniki[a+2][b+a+2]==z && wyniki[a+3][b+a+3]==z && wyniki[a+4][b+a+4]==z) {
                            showResultAndMessage(z);
                        }
                    }
                }
            }
            if(!czy_wygrana){
                for(int a=4; a<25; a++) { //sprawdzenie w górnym, lewym trójkącie
                    for(int b=0; b<=a-4; b++) {
                        if(wyniki[a-b][b]==z && wyniki[a-b-1][b+1]==z && wyniki[a-b-2][b+2]==z && wyniki[a-b-3][b+3]==z && wyniki[a-b-4][b+4]==z) {
                            showResultAndMessage(z);
                        }
                    }
                }
            }
            if(!czy_wygrana){
                for(int a=20; a>=0; a--) { //sprawdzenie dolnego, prawego trójkąta
                    for(int b=24; b>=a+4; b--) {
                         if(wyniki[a][b]==z && wyniki[a+1][b-1]==z && wyniki[a+2][b-2]==z && wyniki[a+3][b-3]==z && wyniki[a+4][b-4]==z) {
                         showResultAndMessage(z);  
                        }
                    }
                }
            }
    }  
 
    void restart() throws InterruptedException {
        watek_czas.sleep(1000);
        stoppedThread = false;
        //synchronized(watek_czas) {
            s=-1;
            min=0;
            sekundy.setText("00");
            minuty.setText("00");
            //watek_czas.notify();
        //}
        for(int i=0; i<25; i++) {
            for(int j=0; j<25; j++) { 
                btn[i][j].setText(" ");
                wyniki[i][j] = ' ';
            }
        }
        startTimeThread();
    }
    
    void btnEnabled() {
        for(int i=0; i<25; i++) {
            for(int j=0; j<25; j++) {
                btn[i][j].setEnabled(true);
            }
        }
    }
    
    void btnDisabled() {
        for(int i=0; i<25; i++) {
            for(int j=0; j<25; j++) {
                btn[i][j].setEnabled(false);
            }
        }
    }
    
    void showResultAndMessage(char z) {
        stoppedThread = true;

        if(z == 'X') {
            ilosc.setText(Integer.toString(Integer.parseInt(ilosc.getText())+1));
            JOptionPane.showMessageDialog(null, "Wygrywa "+pole.getText());
            btnDisabled();
        }
        else if(z == 'O') {
            ilosc2.setText(Integer.toString(Integer.parseInt(ilosc2.getText())+1));
            JOptionPane.showMessageDialog(null, "Wygrywa "+pole2.getText());
            btnDisabled();
        }
        czy_wygrana = true;   
    }
    
    void startTimeThread() {
        watek_czas = new Thread(this);
        watek_czas.start();
    }
    
    @Override
    public void run() {
        s=0;
        min=0;
        minuty.setText("00");
        sekundy.setText("00");
        while(!stoppedThread) {
            try {
                watek_czas.sleep(1000);
                /*synchronized(watek_czas) {
                    if(stoppedThread) {
                        try {
                            watek_czas.wait();
                        } catch(InterruptedException e) { }
                    }
                } */  
                if(s == 59) {
                    s = 0;
                    min++;
                    if(min<10) minuty.setText("0"+Integer.toString(min));
                    else minuty.setText(Integer.toString(min));
                }
                else s++;

                if(s<10) sekundy.setText("0"+Integer.toString(s));
                else sekundy.setText(Integer.toString(s));                
            } 
            catch (InterruptedException e) {
                System.out.println("Stoper został zatrzymany!");
            }
        }
    }
}
