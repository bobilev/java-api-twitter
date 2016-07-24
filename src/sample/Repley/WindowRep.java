package sample.Repley;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import sample.AppsKey;
import sample.BarProgress;
import sample.DBwork;
import sample.ProxyList;
import sample.Search.DBUserIdInfo;
import twitter4j.TwitterException;

import javax.swing.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WindowRep implements Serializable {
    int seconds = 0;

    int Gminuts = 0;
    int minuts = Gminuts;
    int Ghours = 0;
    int hours = Ghours;

    int interatc = 0;
    int interatcFinal = 0;

    int maxRand = 1;
    int minRand = 1;


    public WindowRep() {
    }

    public int getInteratcFinal() {
        return interatcFinal;
    }

    public int getInteratc() {
        return interatc;
    }

    public int getGHours() {
        return Ghours;
    }

    public int getGMinuts() {
        return Gminuts;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinRand() {
        return minRand;
    }

    public int getMaxRand() {
        return maxRand;
    }

    public void setClear() {
        this.Ghours = 0;
        this.hours = 0;
        this.Gminuts = 0;
        this.minuts = 0;
        this.seconds = 0;
        this.maxRand = 0;
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

    public void Rasp() {
        hours = Ghours;
        minuts = Gminuts;
        seconds = 60;
        interatc = 0;

    }

    public void actionTime(final TextArea textArea, final Label labTime, Label labInfoOstatok, final Label labTimeInteracRip, final Button button, Timer tim, DBUserIdInfo DBUfiltr, ProxyList PL, TextRipStringList RipList, Label labCountProxy, ProgressBar progressBar, Label labCountProgressBar) throws TwitterException, SQLException {
        int DBUsize = DBUfiltr.size();
        String hh = "" + hours;
        if (hours < 10) {
            hh = "0" + hours;
        }
        String mm = "" + minuts;
        if (minuts < 10) {
            mm = "0" + minuts;
        }
        String ss = "" + seconds;
        if (seconds < 10) {
            ss = "0" + seconds;
        }
        if (seconds >= 0) {//продолжаем таймер
            final String finalHh = hh;
            final String finalMm = mm;
            final String finalSs = ss;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labTime.setText(finalHh + ":" + finalMm + ":" + finalSs);
                    //labTime2.setText(finalHh + ":" + finalMm + ":" + finalSs);
                }
            });
        }

        if (DBUsize == 0) {//если счет равен интерации то заканчиваем таймер
            tim.stop();
            interatcFinal = 0;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    button.setId("buttonStart");
                    labTime.setText("00:00:00");
                }
            });
        } else if (interatc == interatcFinal) {//если счет равен интерации то заканчиваем таймер
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
            hours = Ghours;
            minuts = Gminuts;
            seconds = 60;
            System.out.println("Ghours- " + Ghours);
            System.out.println("hours- " + hours);
            interatc++;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    textArea.appendText("-----R-start: начался-----\n");
                    labTime.setText("00:00:00");
                    labTimeInteracRip.setText(interatc + "|" + interatcFinal);
                }
            });
            try {
                AllStartFollowing(RipList, textArea, DBUfiltr, PL, labInfoOstatok, labCountProxy, progressBar, labCountProgressBar);//Запускаем метод который запускает потоки
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (TwitterException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (minuts != 0 && seconds == 0) {
            minuts--;
            seconds = 60;
        } else if (hours != 0 && minuts == 0 && seconds == 0) {
            hours--;
            minuts = 59;
            seconds = 60;
        }
        seconds--;
    }

    public void startExtro(final TextArea textArea, Label labInfoOstatok, DBUserIdInfo DBUfiltr, ProxyList PL, TextRipStringList RipList, Label labCountProxy, final ProgressBar progressBar, Label labCountProgressBar) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText("-----R-start: начался Extro-----\n");
            }
        });
        try {
            AllStartFollowing(RipList, textArea, DBUfiltr, PL, labInfoOstatok, labCountProxy, progressBar, labCountProgressBar);//Запускаем метод который запускает потоки

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void AllStartFollowing(TextRipStringList RipList, final TextArea textArea, DBUserIdInfo DBUfiltr, ProxyList PL, Label labInfoOstatok, Label labCountProxy, final ProgressBar progressBar, Label labCountProgressBar) throws SQLException, TwitterException, InterruptedException {//Постим всем!!!!!!!!! это главный модуль
        DBwork dbwork = new DBwork();
        BarProgress BP = new BarProgress();
        ResultSet rs = dbwork.getAccess();//получаем токены доступа всех акков
        int countAkk = dbwork.getCount();//узнаем колько акков в базе
        double progresInt = 1.0 / countAkk;//даем каждому акко равноу долю в прогресс - Баре
        //ArrayList ListName = dbwork.getAllName();
        String[] massAccess = new String[countAkk];
        int i = 0;
        while (rs.next()) {//создаем массив токенов
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
            if (o == 3) {o = 0;}
            int max = DBUfiltr.size();
            int rnd = (int) (Math.random() * (max));

            String screen_name;
            try {
                screen_name = DBUfiltr.screen_name(rnd);}
            catch (IndexOutOfBoundsException e) {break;}//останавливает цикл если лист пуст
            String text = RipList.getRnd();
            String name = massAccess[j];
            rsApps = dbwork.getAppsOAS(name, o);

            String oauthtoken = rsApps.getString("oauthtoken");
            String oauthtokensecret = rsApps.getString("oauthtokensecret");
            String setOAuthConsumerKey = appsKey.getoauthtoken(o);
            String setOAuthConsumerSecret = appsKey.getoauthtokensecret(o);
            o++;

            Thread threads = new Thread(new RepleyPotok(screen_name, text, name, oauthtoken, oauthtokensecret,setOAuthConsumerKey,setOAuthConsumerSecret, textArea, maxRand, minRand, PL, labInfoOstatok, DBUfiltr, labCountProxy, progresInt, BP, progressBar, labCountProgressBar, countAkk));
            threads.start();
        }
    }
}
