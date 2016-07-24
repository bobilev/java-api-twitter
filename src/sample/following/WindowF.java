package sample.following;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import sample.BarProgress;
import sample.DBwork;
import sample.ProxyList;
import twitter4j.TwitterException;

import javax.swing.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Thread.sleep;

public class WindowF implements Serializable{
    int seconds = 0;

    int Gminuts;
    int minuts = Gminuts;
    int Ghours;
    int hours = Ghours;

    int interatc = 0;
    int interatcFinal = 1;

    int countFollowing = 0;
    int countFollowinsg = 0;

    public WindowF() {
    }
    public int getinteratcFinal(){
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
    public int getCountFollowing(){
        return countFollowing;
    }

    public void setCountFollowing(int countFollowing) {
        this.countFollowing = countFollowing;
    }

    public void setInteratcFinal(int xx) {
        this.interatcFinal = xx;
        Ghours = 24/xx;
        hours = Ghours;
    }

    public void Rasp(){
        hours = Ghours;
        minuts = Gminuts;
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
                    button.setText("Пуск");
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
                    textArea.appendText("-----Following: "+Integer.toString(interatc)+" начался-----\n");
                    labTime.setText("00:00:00");
                    labTimeInterac.setText(interatc+"|"+interatcFinal);
                }
            });
            try {
                AllStartFollowing(textArea, countFollowing, PL, labCountProxy,progressBar,labCountProgressBar);//Запускаем метод который запускает потоки
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
            minuts = 1;//***************
            seconds = 1;//*************
        }
        seconds--;
    }

    public void AllStartFollowing(final TextArea textArea, int countFollowing, ProxyList PL, Label labCountProxy, final ProgressBar progressBar, Label labCountProgressBar) throws SQLException, TwitterException, InterruptedException {//Постим всем!!!!!!!!! это главный модуль
        DBwork dbwork = new DBwork();
        BarProgress BP = new BarProgress();
        ResultSet rs = dbwork.getAccess();//получаем токены доступа всех акков
        int countAkk = dbwork.getCount();//узнаем колько акков в базе
        double progresInt = 1.0/countAkk;//даем каждому акко равноу долю в прогресс - Баре
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setOpacity(1);
            }
        });
        while(rs.next()) {
            String name = rs.getString("username");
            String oauthtoken = rs.getString("oauthtoken");
            String oauthtokensecret = rs.getString("oauthtokensecret");
            while(true){
                Following F = new Following();
                int regul = F.regul();
                if (regul < 10){
                    System.out.println("Теперь их "+regul+" start = "+name);
                    Thread threads = new Thread(new Following(name,oauthtoken,oauthtokensecret,textArea, countFollowing, PL, labCountProxy, progresInt,BP,progressBar,labCountProgressBar,countAkk));
                    threads.start();
                    break;
                } else{
                    System.out.println(regul+" сейчас работает ждем");
                    sleep(5000);
                }
            }

        }
    }


}
