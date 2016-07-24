package sample.following;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import sample.BarProgress;
import sample.DBwork;
import sample.ProxyList;
import sample.Repley.TextRipStringList;
import sample.Search.DBUserIdInfo;
import twitter4j.TwitterException;

import javax.swing.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WindowU implements Serializable{
    int seconds = 0;

    int Gminuts;
    int minuts = Gminuts;
    int Ghours;
    int hours = Ghours;

    int Day = 0;//**

    public WindowU() {
    }
    public int getHours(){
        return hours;
    }
    public int getMinuts(){
        return minuts;
    }
    public int getSeconds(){
        return seconds;
    }

    public void setClear() {
        this.Ghours = 0;
        this.Gminuts = 0;
        this.hours = 0;
        this.minuts = 0;
        this.seconds = 0;
        this.Day = 1;
    }
    public String getDay(){
        String TimeDay1 = ""+Day;
        return TimeDay1;
    }
    public String getGhours(){
        String TimeToDay = Ghours+":00";
        if (Ghours < 10){
            TimeToDay = "0"+Ghours+":00";
        }
        return TimeToDay;
    }
    public void setDay(int xx, int h, int m){
        this.Day = xx;
        RaschotDay(h,m);
    }
    public void setHours(int xx, int h, int m) {
        this.Ghours = xx;
        RaschotDay(h,m);
    }
    public void RaschotDay(int hh, int mm){
        hours = ((Ghours - hh) - 1) + Day*24;
        minuts = 60 - mm;
    }

    public void actionTime(final Label labTime, final TextArea textArea, ProxyList PL, Label labCountProxy, ProgressBar progressBar, Label labCountProgressBar) throws TwitterException, SQLException {
        String hh = ""+hours;
        if (hours < 10){hh = "0"+hours;}
        String mm = ""+minuts;
        if (minuts < 10){mm = "0"+minuts;}
        String ss = ""+seconds;
        if (seconds < 10){ss = "0"+seconds;}
        if (seconds >= 0) {//продолжаем таймер
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


        if (hours == 0 && minuts == 0 && seconds == 0) {//если счет = 0 то начинаем заново
            hours = 24;
            minuts = 0;
            seconds = 60;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    textArea.appendText("------Unfollowin началcz-----\n");
                    labTime.setText("00:00:00");

                }
            });
            try {
                AllStartUnfollowing(textArea, PL, labCountProxy,progressBar,labCountProgressBar);//Запускаем метод который запускает потоки
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
            minuts = 1;//**********
            seconds = 1;//*********
        }
        seconds--;
    }
    public void startExtro(final TextArea textArea,ProxyList PL,Label labCountProxy, ProgressBar progressBar,Label labCountProgressBar){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText("-----Un-start: начался Extro-----\n");
            }
        });
        try {
            AllStartUnfollowing(textArea, PL, labCountProxy,progressBar,labCountProgressBar);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void AllStartUnfollowing(final TextArea textArea, ProxyList PL, Label labCountProxy, final ProgressBar progressBar, Label labCountProgressBar) throws SQLException, TwitterException, InterruptedException {//Постим всем!!!!!!!!! это главный модуль
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

            Thread threads = new Thread(new Unfollowing(name, oauthtoken, oauthtokensecret, textArea, PL, labCountProxy, progresInt,BP,progressBar,labCountProgressBar,countAkk));
            threads.start();
        }
    }


}
