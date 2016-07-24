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

import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Following implements Runnable {
    String name;
    String oauthtoken;
    String oauthtokensecret;
    String setOAuthConsumerKey = "oINvM4zJ3UUL21YM6Xb7mIF8k";
    String setOAuthConsumerSecret = "OPw7qrXj8v1zkMmgNYYOzkYSeKNRWo8s6hCaMSXI74mcSrLidE";
    String[] massS;

    TextArea textArea;
    int countFollowing;
    ProxyList PL;
    Label labCountProxy;
    double progresInt;
    BarProgress BP;
    ProgressBar progressBar1;
    Label labCountProgressBar;
    int countAkk;
    static int regulAkk = 1;

    public Following(){}
    public int regul(){
        return regulAkk;
    }
    public Following(String name,
                     String oauthtoken,
                     String oauthtokensecret,
                     TextArea textArea,
                     int countFollowing,
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
        this.countFollowing = countFollowing;
        this.PL = PL;
        this.labCountProxy = labCountProxy;
        this.progresInt = progresInt;
        this.BP = BP;
        this.progressBar1 = progressBar;
        this.labCountProgressBar = labCountProgressBar;
        this.countAkk = countAkk;
        regulAkk++;

        String[] massS = new String[100];//============Времено
        massS[0] = "говорить";
        massS[1] = "год";
        massS[2] = "знать";
        massS[3] = "мой";
        massS[4] = "до";
        massS[5] = "или";
        massS[6] = "если";
        massS[7] = "время";
        massS[8] = "рука";
        massS[9] = "нет";
        massS[10] = "самый";
        massS[11] = "ни";
        massS[12] = "стать";
        massS[13] = "большой";
        massS[14] = "даже";
        massS[15] = "другой";
        massS[16] = "наш";
        massS[17] = "свой";
        massS[18] = "ну";
        massS[19] = "под";
        massS[20] = "где";
        massS[21] = "дело";
        massS[22] = "есть";
        massS[23] = "сам";
        massS[24] = "раз";
        massS[25] = "чтобы";
        massS[26] = "два";
        massS[27] = "там";
        massS[28] = "чем";
        massS[29] = "глаз";
        massS[30] = "жизнь";
        massS[31] = "первый";
        massS[32] = "день";
        massS[33] = "тута";
        massS[34] = "ничто";
        massS[35] = "потом";
        massS[36] = "очень";
        massS[37] = "хотеть";
        massS[38] = "ли";
        massS[39] = "при";
        massS[40] = "голова";
        massS[41] = "надо";
        massS[42] = "без";
        massS[43] = "видеть";
        massS[44] = "идти";
        massS[45] = "теперь";
        massS[46] = "тоже";
        massS[47] = "стоять";
        massS[48] = "друг";
        massS[49] = "дом";
        massS[50] = "сейчас";
        massS[51] = "можно";
        massS[52] = "после";
        massS[53] = "слово";
        massS[54] = "здесь";
        massS[55] = "думать";
        massS[56] = "место";
        massS[57] = "спросить";
        massS[58] = "через";
        massS[59] = "лицо";
        massS[60] = "что";
        massS[61] = "тогда";
        massS[62] = "ведь";
        massS[63] = "хороший";
        massS[64] = "каждый";
        massS[65] = "новый";
        massS[66] = "жить";
        massS[67] = "должный";
        massS[68] = "смотреть";
        massS[69] = "почему";
        massS[70] = "потому";
        massS[71] = "сторона";
        massS[72] = "просто";
        massS[73] = "нога";
        massS[74] = "сидеть";
        massS[75] = "понять";
        massS[76] = "иметь";
        massS[77] = "конечный";
        massS[78] = "делать";
        massS[79] = "вдруг";
        massS[80] = "над";
        massS[81] = "взять";
        massS[82] = "никто";
        massS[83] = "сделать";
        massS[84] = "дверь";
        massS[85] = "перед";
        massS[86] = "нужный";
        massS[87] = "понимать";
        massS[88] = "казаться";
        massS[89] = "работа";
        massS[90] = "три";
        massS[91] = "ваш";
        massS[92] = "уж";
        massS[93] = "земля";
        massS[94] = "конец";
        massS[95] = "несколько";
        massS[96] = "час";
        massS[97] = "голос";
        massS[98] = "город";
        massS[99] = "последний";
        this.massS = massS;
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
        Twitter twitter = ConfigTwitter(setOAuthConsumerKey, setOAuthConsumerSecret, oauthtoken, oauthtokensecret, proxy);
        int c=0;
        int i=0;
        int k=1;
        int z=0;
        //=====================================
        while(i<10){
            Search search = new Search(twitter);
            try {
                int rnds = (int)(Math.random() * (100));//************
                String slovo = massS[rnds];//времено
                QueryResult res = search.searchTwit(slovo, countFollowing);
                for ( Status status : res.getTweets()) {
                    int rnd = 3 + (int)(Math.random() * ((15 - 3) + 1));
                    sleep(rnd*1000);
                    while (true){
                        try {
                            User user = twitter.createFriendship(status.getUser().getScreenName());
                            break;
                        } catch (TwitterException e) {
                            z++;
                            System.out.println(name+" Не получилось зафоловить, сплю 1.5 сек - "+z);
                            sleep(1500);
                            if(z==4){
                                z = 0;
                                break;
                            }
                        }
                    }
                    c++;
                    System.out.println("F("+name+") +"+c);
                }
                break;
            } catch (TwitterException e) {
                System.out.println("F("+name+ ") bad "+i);
                i++;
                try {
                    sleep(2000);
                } catch (InterruptedException e1) {e1.printStackTrace();}
            } catch (InterruptedException e) { i++;}
            if(i==5){
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
        //=====================================
        final int finalI = c;
        BP.setFin(progresInt);
        BP.setIn(1);
        final double fin = BP.getFin();
        final int in = BP.getIn();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DataTime d = new DataTime();
                textArea.appendText("F("+d.DataTimeString()+") - "+name+" Following: "+Integer.toString(finalI)+"\n");
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
        regulAkk--;
    }

}
