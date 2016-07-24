package sample;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import sample.Serialization.SerializationMy;
import twitter4j.TwitterException;

import javax.swing.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Window implements Serializable{
    int seconds = 0;

    int Gminuts = 0;
    int minuts = Gminuts;
    int Ghours = 0;
    int hours = Ghours;

    int interatc = 0;
    int interatcFinal = 1;

    int maxRand = 1;
    int minRand = 1;

    public Window(){
    }
    public int getInteratcFinal(){
        return interatcFinal;
    }
    public int getInteratc(){
        return interatc;
    }
    public int getGHours(){
        return Ghours;
    }
    public int getGMinuts(){
        return Gminuts;
    }
    public int getSeconds(){
        return seconds;
    }
    public int getMaxRand(){
        return maxRand;
    }
    public int getMinRand(){
        return minRand;
    }

    public void setClear(){
        this.Ghours = 0;
        this.hours = 0;
        this.Gminuts = 0;
        this.minuts = 0;
        this.seconds = 0;
        this.maxRand = 1;
        this.minRand = 1;
        this.interatc = 0;
        this.interatcFinal = 1;
    }

    public void setGhours(int ghours) {
        Ghours = ghours;
        hours = Ghours;
    }
    public void setGminuts(int gminuts) {
        Gminuts = gminuts;
        minuts = Gminuts;
    }
    public void setMinRand(int minRand) {
        this.minRand = minRand;
    }
    public void setMaxRand(int maxRand) {
        this.maxRand = maxRand;
    }

    public void setInteratcFinal(int interatcFinal) {
        this.interatcFinal = interatcFinal;
    }

    public void Rasp(){
        hours = Ghours;
        minuts = Gminuts;
        seconds = 60;
        interatc = 0;
    }

    public void actionTime(final Label labTime, final Label labTimeInterac, final Button button, Timer tim, final TextArea textArea, ProxyList PL, Label labCountProxy, ProgressBar progressBar, Label labCountProgressBar) throws TwitterException, SQLException {
        String hh = ""+hours;
        if (hours < 10){hh = "0"+hours;}
        String mm = ""+minuts;
        if (minuts < 10){mm = "0"+minuts;}
        String ss = ""+seconds;
        if (seconds < 10){ss = "0"+seconds;}
        if (seconds >= 0) {//проолжаем таймер
            final String finalHh = hh;
            final String finalMm = mm;
            final String finalSs = ss;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                        labTime.setText(finalHh +":"+ finalMm +":"+ finalSs);
                }
            });
        }

        if (interatc == interatcFinal) {//если счет равен интерации то заканчиваем таймер
            tim.stop();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    button.setId("buttonStart");
                    labTime.setText("00:00:00");
                }
            });
        }
        if (hours == 0 && minuts == 0 && seconds == 0) {//если счет = 0 то начинаем заного
            interatc++;
            hours = Ghours;
            minuts = Gminuts;
            seconds = 60;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    textArea.appendText("-----Интерация: "+Integer.toString(interatc)+" началась-----\n");
                    labTime.setText("00:00:00");
                    labTimeInterac.setText(interatc+"|"+interatcFinal);
                }
            });
            try {
                AllStartPostTwits(textArea, PL, labCountProxy,progressBar,labCountProgressBar);//Запускаем метод который запускает потоки

            } catch (SQLException e) {e.printStackTrace();} catch (TwitterException e) {e.printStackTrace();} catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (minuts != 0 && seconds == 0){
            minuts--;
            seconds = 60;
        }
        else if (hours != 0 && minuts == 0 && seconds == 0) {
            hours--;
            minuts = 59;
            seconds = 60;
        }
        seconds--;
    }

    public void AllStartPostTwits(final TextArea textArea, ProxyList PL, Label labCountProxy, final ProgressBar progressBar, Label labCountProgressBar) throws SQLException, TwitterException, InterruptedException {//Постим всем!!!!!!!!! это главный модуль
        DBwork dbwork = new DBwork();
        BarProgress BP = new BarProgress();
        TextPosta TP = new TextPosta();
        ResultSet rs = dbwork.getAccess();//получаем токены доступа всех акков
        int countAkk = dbwork.getCount();//узнаем колько акков в базе
        double progresInt = 1.0/countAkk;//даем каждому акко равноу долю в прогресс - Баре
        ArrayList ListName = dbwork.getAllName();
        String [] massAccess = new String[countAkk];
        int i = 0;
        while (rs.next()){//создаем массив токенов
            massAccess[i] = rs.getString("username");
            i++;
        }
        List<String> lst = Arrays.asList(massAccess);
        Collections.shuffle(lst);
        massAccess = lst.toArray(massAccess);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setOpacity(1);
            }
        });
        int o = 0;
        ResultSet rsApps;
        AppsKey appsKey = new AppsKey();
        for(int j=0;j<massAccess.length;j++) {
            if(o==3){o = 0;}
            String text = TP.getText();
            String name = massAccess[j];
            rsApps = dbwork.getAppsOAS(name, o);

            String oauthtoken = rsApps.getString("oauthtoken");
            String oauthtokensecret = rsApps.getString("oauthtokensecret");
            String setOAuthConsumerKey = appsKey.getoauthtoken(o);
            String setOAuthConsumerSecret = appsKey.getoauthtokensecret(o);
            o++;

            Thread threads = new Thread(new UserPotok(text, name, oauthtoken, oauthtokensecret,setOAuthConsumerKey,setOAuthConsumerSecret, textArea ,interatc, maxRand, minRand, PL, ListName, labCountProxy, progresInt,BP,progressBar,labCountProgressBar,countAkk));
            threads.start();
        }
    }


}
