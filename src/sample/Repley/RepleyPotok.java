package sample.Repley;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import sample.BarProgress;
import sample.DataTime;
import sample.ProxyList;
import sample.Search.DBUserIdInfo;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class RepleyPotok implements Runnable {
    String name;
    String screen_name;
    String oauthtoken;
    String oauthtokensecret;
    String setOAuthConsumerKey;
    String setOAuthConsumerSecret;

    String text;
    int maxRand;
    int minRand;
    TextArea textArea;
    ProxyList PL;
    Label labInfoOstatok;
    Label labCountProxy;
    DBUserIdInfo DBUfiltr;
    double progresInt;
    BarProgress BP;
    ProgressBar progressBar1;
    Label labCountProgressBar;
    int countAkk;

    public RepleyPotok(String screen_name,
                       String text,
                       String name,
                       String oauthtoken,
                       String oauthtokensecret,
                       String setOAuthConsumerKey,
                       String setOAuthConsumerSecret,
                       TextArea textArea,
                       int maxRand,
                       int minRand,
                       ProxyList PL,
                       Label labInfoOstatok,
                       DBUserIdInfo DBUfiltr,
                       Label labCountProxy,
                       double progresInt,
                       BarProgress BP,
                       ProgressBar progressBar,
                       Label labCountProgressBar,
                       int countAkk) throws TwitterException {
        this.screen_name = screen_name;
        this.oauthtoken = oauthtoken;
        this.oauthtokensecret = oauthtokensecret;
        this.setOAuthConsumerKey = setOAuthConsumerKey;
        this.setOAuthConsumerSecret = setOAuthConsumerSecret;
        this.name = name;
        this.text = text;
        this.textArea = textArea;
        this.maxRand = maxRand;
        this.minRand = minRand;
        this.PL = PL;
        this.labInfoOstatok = labInfoOstatok;
        this.DBUfiltr = DBUfiltr;
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

    public String rezka(String text){
        String res = "@"+screen_name+" "+text;
        int countName = screen_name.length();
        int count = res.length();
        System.out.println(count);
        if(count > 140){
            int x = countName + 1+(count-138);
            res = "@"+screen_name+" "+text.subSequence(x, count);
        }
        return res;
    }
    public void  run() {//отправка твита в потоке
        final int rnd = minRand + (int)(Math.random() * ((maxRand - minRand) + 1));
        Status status;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText("R("+name + ") : " + "Проснеться через " + rnd + " минут\n");
            }
        });
        boolean god = true;
        try {
            sleep((rnd*60)*1000);
        } catch (InterruptedException e) {e.printStackTrace();}

        String proxy = PL.getProxy();
        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret, proxy);

        final String textGood = rezka(text);
        int i = 1;
        int k = 1;
        while(god) {
            while(i<10){
                try {
                    status = twitter.updateStatus(textGood);
                    god = false;
                    System.out.println(name+" god"+proxy);
                    //DBUfiltr.delet(screen_name);
                    break;
                } catch (TwitterException e) {
                    System.out.println(name + " bad "+i);
                    i++;
                    try {
                        sleep(4000);
                    } catch (InterruptedException e1) {e1.printStackTrace();}
                    //e.printStackTrace();
                }
            }
            if(i==10){
                PL.delete(proxy,labCountProxy);
                proxy = PL.getProxy();
                twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret, proxy);
                System.out.println(name+" Создан новый объект твиттер "+k);
                i = 1;
                k++;
            }
            if(k==10){
                god = false;
                System.out.println(name+" - "+k+" попытки израсходаваны");
            }
        }

        System.out.println("R("+name+") Отправлено: "+textGood);
        final int finalI = k;
        BP.setFin(progresInt);
        BP.setIn(1);
        final double fin = BP.getFin();
        final int in = BP.getIn();
        final int DBsize = DBUfiltr.size();
        final int DBcount = DBUfiltr.getCount();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DataTime d = new DataTime();
                textArea.appendText("("+d.DataTimeString() + ") : " + finalI + " - " +"["+textGood.length()+"]"+ name + " : " + textGood + "\n");
                labInfoOstatok.setText(DBsize+"|"+DBcount);
                labCountProgressBar.setText(in+"|"+countAkk);
                //=============================== ProressBAR=====================
                progressBar1.setProgress(fin);
                if (fin > 1) {
                    progressBar1.setOpacity(0);
                }
                if(in == countAkk){
                    labCountProgressBar.setText("Готово");
                }
//                System.out.println(name+" progress = "+fin);
//                System.out.println(name+" progress = "+in);
//                System.out.println(name+" progress = "+countAkk);
            }
        });



    }

}
