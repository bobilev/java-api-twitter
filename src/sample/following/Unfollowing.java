package sample.following;


import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import sample.BarProgress;
import sample.DataTime;
import sample.ProxyList;
import sample.Search.Search;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Unfollowing implements Runnable {
    String name;
    String oauthtoken;
    String oauthtokensecret;
    String setOAuthConsumerKey = "oINvM4zJ3UUL21YM6Xb7mIF8k";
    String setOAuthConsumerSecret = "OPw7qrXj8v1zkMmgNYYOzkYSeKNRWo8s6hCaMSXI74mcSrLidE";

    TextArea textArea;
    ProxyList PL;
    Label labCountProxy;
    double progresInt;
    BarProgress BP;
    ProgressBar progressBar1;
    Label labCountProgressBar;
    int countAkk;

    public Unfollowing(String name,
                       String oauthtoken,
                       String oauthtokensecret,
                       TextArea textArea,
                       ProxyList PL,
                       Label labCountProxy,
                       double progresInt,
                       BarProgress BP,
                       ProgressBar progressBar,
                       Label labCountProgressBar,
                       int countAkk){
        this.oauthtoken = oauthtoken;
        this.oauthtokensecret = oauthtokensecret;
        this.name = name;
        this.textArea = textArea;
        this.PL = PL;
        this.labCountProxy = labCountProxy;
        this.progresInt = progresInt;
        this.BP = BP;
        this.progressBar1 = progressBar;
        this.labCountProgressBar = labCountProgressBar;
        this.countAkk = countAkk;
    }
    public Twitter ConfigTwitter(String setOAuthConsumerKey, String setOAuthConsumerSecret, String oauthtoken, String oauthtokensecret, String proxy){
        Pattern pt = Pattern.compile(":");
        String[] proxyPort = pt.split(proxy);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(setOAuthConsumerKey)
                .setOAuthConsumerSecret(setOAuthConsumerSecret)
                .setOAuthAccessToken(oauthtoken)
                .setOAuthAccessTokenSecret(oauthtokensecret)
                .setHttpProxyHost(proxyPort[0])
                .setHttpProxyPort(Integer.parseInt(proxyPort[1]));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }


    public void  run() {
        String proxy = PL.getProxy();
        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret, proxy);

        Set<Long> arrfriendsIDs = new HashSet<Long>();
        ArrayList<Long> arrfollowersIDs = new ArrayList<Long>();
        int rnd = 3 + (int)(Math.random() * ((15 - 3) + 1));
        int unfoll = 0;

        IDs friendsIDs = null;
        IDs followersIDs = null;
        long Cursor = -1;
        int ij = 0;
        int kj = 0;
        while(true){//=================================== Первый запрос
            try {
                if(friendsIDs == null){
                    friendsIDs = twitter.getFriendsIDs(name, Cursor);
                }
                if(followersIDs == null){
                    followersIDs = twitter.getFollowersIDs(name, Cursor);
                }
                break;
            } catch (TwitterException e) {
                try {
                    sleep(4000);
                } catch (InterruptedException e1) {e1.printStackTrace();}
                ij++;
            }
            if(ij == 10){
                PL.delete(proxy, labCountProxy);
                proxy = PL.getProxy();
                twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret, proxy);
                System.out.println(name+" Создан новый объект твиттер "+kj);
                ij = 0;
                kj++;
            }
            if(kj==10){
                System.out.println(name+" - "+kj+" попытки израсходаваны");
                break;
            }

        }
        try {
            sleep(rnd*1000);
        } catch (InterruptedException e) {e.printStackTrace();}

        for ( long id : friendsIDs.getIDs() ) {
            arrfriendsIDs.add(id);
        }
        for ( long id : followersIDs.getIDs() ) {
            arrfollowersIDs.add(id);
        }
        //модуль взаимного фактора----------------------------
        int n = 0;
        for ( long id : followersIDs.getIDs() ) {
            if (arrfriendsIDs.contains(id)) {

            } else {
                try{
                    sleep(rnd*1000);
                    User status = twitter.createFriendship(id);
                    n++;
                    System.out.println(id);
                } catch (TwitterException e) {
                    System.out.println("Неполучилось подписаться, наверное ограничен доступ");
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }
        System.out.println("Неудачных попыток взаимной чистки: "+n);
        System.out.println(name+"В листе friends(мы) - "+arrfriendsIDs.size());
        System.out.println(name+"В листе followers(нас)- "+arrfollowersIDs.size());
        //----------------------------------------------------
        try {
            sleep(rnd*1000);
        } catch (InterruptedException e) {e.printStackTrace();}

        arrfriendsIDs.removeAll(arrfollowersIDs);//выявляем неотзывчивых
        for ( long id : arrfriendsIDs) {
            int rnd1 = 10 + (int)(Math.random() * ((25 - 10) + 1));
            try {
                sleep(rnd1 * 1000);
                User status1 = twitter.destroyFriendship(id);
                unfoll++;
                System.out.println("U "+name+" Убито - "+unfoll);
            } catch (TwitterException e) {
                System.out.println(name+" ошибка "+id);
                e.printStackTrace();
            } catch (InterruptedException e) {e.printStackTrace();}
        }

        System.out.println(name+"В листе friends(мы) - после "+arrfriendsIDs.size());
        System.out.println(name+"В листе followers(нас)- "+arrfollowersIDs.size());

        final int finalUnfoll = unfoll;
        BP.setFin(progresInt);
        BP.setIn(1);
        final double fin = BP.getFin();
        final int in = BP.getIn();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DataTime d = new DataTime();
                textArea.appendText("U("+d.DataTimeString() + ") - " + name + " UNfollowing: " + finalUnfoll + "\n");
                labCountProgressBar.setText(in+"|"+countAkk);
                //=============================== ProressBAR=====================
                progressBar1.setProgress(fin);
                if (fin > 1) {
                    progressBar1.setOpacity(0);
                }
                if(in == countAkk){
                    labCountProgressBar.setText("Готово");
                }
            }
        });
    }
}
