package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class UserPotok implements Runnable {
    String name;
    String oauthtoken;
    String oauthtokensecret;
    String setOAuthConsumerKey;
    String setOAuthConsumerSecret;

    String text;
    int interact;
    int maxRand;
    int minRand;
    TextArea textArea;
    ProxyList PL;
    ArrayList ListName;
    Label labCountProxy;
    double progresInt;
    BarProgress BP;
    ProgressBar progressBar1;
    Label labCountProgressBar;
    int countAkk;

    public UserPotok(String text,
                     String name,
                     String oauthtoken,
                     String oauthtokensecret,
                     String setOAuthConsumerKey,
                     String setOAuthConsumerSecret,
                     TextArea textArea,
                     int interatc,
                     int maxRand,
                     int minRand,
                     ProxyList PL,
                     ArrayList ListName,
                     Label labCountProxy,
                     double progresInt,
                     BarProgress BP,
                     ProgressBar progressBar,
                     Label labCountProgressBar,
                     int countAkk
    ) throws TwitterException {
        this.oauthtoken = oauthtoken;
        this.oauthtokensecret = oauthtokensecret;
        this.setOAuthConsumerKey = setOAuthConsumerKey;
        this.setOAuthConsumerSecret = setOAuthConsumerSecret;
        this.name = name;
        this.text = text;
        this.textArea = textArea;
        this.interact = interatc;
        this.maxRand = maxRand;
        this.minRand = minRand;
        this.PL = PL;
        this.ListName = ListName;
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
    public String Regexp1(String text){
        Pattern pattern  = Pattern.compile("(RT\\s)?@([a-zA-Z0-9_])*");
        Matcher matcher = null;
        try {
            matcher = pattern.matcher(text);
        } catch (Exception e) {
            String resul = ":)";
            return resul;
        }
        String name1 = null;
        boolean god = true;
        while(god){
            int rnd = (int)(Math.random() * ListName.size());
            if(!name.equals(ListName.get(rnd))){
                god = false;
                name1 = (String) ListName.get(rnd);
            }
        }

        String result = matcher.replaceFirst("@"+name1);
        return result;
    }
    public String rezka(String text){
        String res = text;
        int count = res.length();
        if(count > 140){
            res = (String) text.subSequence(0, 136);
        }
        return res;
    }

    public void  run() {//отправка твита в потоке
        final int rnd = minRand + (int)(Math.random() * ((maxRand - minRand) + 1));
        long status2;
        Status status;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText(name+" : "+"Проснеться через "+rnd+" минут\n");
            }
        });
        boolean god = true;
        try {
            sleep((rnd*60)*1000);
        } catch (InterruptedException e) {e.printStackTrace();}

        //Просыпаемся
        String proxy = PL.getProxy();
        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret, proxy);

        String text1 = Regexp1(text);
        final String text2 = rezka(text1);
        int i = 1;
        int k = 1;
        while(god) {
            while(i<10){
                try {
                    status = twitter.updateStatus(text2);
                    //status2 = twitter.getUserTimeline().get(0).getId();
                    //status = twitter.destroyStatus(status2);
                    god = false;
                    System.out.println(name+" god"+proxy);
                    break;
                } catch (TwitterException e) {
                    System.out.println(name + " bad "+i);
                    //e.printStackTrace();
                    i++;
                    try {
                        sleep(4000);
                    } catch (InterruptedException e1) {e1.printStackTrace();}
                    //e.printStackTrace();
                } catch (NoSuchElementException e1) {
                    System.out.println(name + " bad "+i);
                    i++;
                    try {
                        sleep(4000);
                    } catch (InterruptedException e2) {e2.printStackTrace();}
                    //e.printStackTrace();
                }
            }
            if(i==10){
                PL.delete(proxy, labCountProxy);
                proxy = PL.getProxy();
                twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret, proxy);
                System.out.println(name+" Создан новый объект твиттер "+k);
                i = 0;
                k++;
            }
            if(k==10){
                System.out.println(name+" - "+k+" попытки израсходаваны");
                break;
            }
        }

        System.out.println(name+") Отправлено: "+text1);
        final int finalI = k;
        BP.setFin(progresInt);
        BP.setIn(1);
        final double fin = BP.getFin();
        final int in = BP.getIn();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DataTime d = new DataTime();
                textArea.appendText(d.DataTimeString() + " : " + finalI + " - " + name + " : " + text2 + "\n");
                labCountProgressBar.setText(in+"|"+countAkk);
                //=============================== ProressBAR=====================
                progressBar1.setProgress(fin);
                if (fin > 1) {
                    progressBar1.setOpacity(0);
                }
                if(in == countAkk){
                    labCountProgressBar.setText("Готово");
                }
                //System.out.println(name+" progress = "+fin);
                //System.out.println(name+" progress = "+in);
                //System.out.println(name+" progress = "+countAkk);

            }
        });
    }
}
