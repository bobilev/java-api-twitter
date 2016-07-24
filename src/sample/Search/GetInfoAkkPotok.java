package sample.Search;


import javafx.application.Platform;
import javafx.scene.control.Label;
import sample.DBwork;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.xml.bind.SchemaOutputResolver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;

public class GetInfoAkkPotok implements Runnable {
    String oauthtoken = "2610039722-Qqf7Xn64U5UPX9bM1r69XnndjeS1b4fuVkuoTVa";
    String oauthtokensecret = "nr5CQih2j4mYOYLeq4nyytKW8RdYq1g0Itw7xmLtK3hYC";
    String setOAuthConsumerKey = "oINvM4zJ3UUL21YM6Xb7mIF8k";
    String setOAuthConsumerSecret = "OPw7qrXj8v1zkMmgNYYOzkYSeKNRWo8s6hCaMSXI74mcSrLidE";

    String[] screen_name;
    Label Label;
    DBUserIdInfo DBU;
    public GetInfoAkkPotok(String[] screen_name, Label Label, DBUserIdInfo DBU){
        this.screen_name = screen_name;
        this.Label = Label;
        this.DBU = DBU;
    }
    public Twitter ConfigTwitter(String setOAuthConsumerKey, String setOAuthConsumerSecret, String oauthtoken, String oauthtokensecret){
        /*int countProxy = List.size();
        int randProxy = (int)(Math.random() * (countProxy));
        String proxy = (String) List.get(randProxy);
        List.remove(randProxy);

        Pattern pt = Pattern.compile(":");
        String[] proxyPort = pt.split(proxy);*/

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(setOAuthConsumerKey)
                .setOAuthConsumerSecret(setOAuthConsumerSecret)
                .setOAuthAccessToken(oauthtoken)
                .setOAuthAccessTokenSecret(oauthtokensecret)
                /*.setHttpProxyHost(proxyPort[0])
                .setHttpProxyPort(Integer.parseInt(proxyPort[1]))*/;
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }

    public ArrayList<Long> GetFollowersIDs() throws SQLException {//Получаем все id в виде long нашей цели
        DBwork dBwork = new DBwork();//создаем запасные токены
        ResultSet rs = dBwork.getAccess();
        ArrayList<String> oauthtokenList0 = new ArrayList<String>();
        while (rs.next()){
            String oauthtoken = rs.getString("oauthtoken");
            String oauthtokensecret = rs.getString("oauthtokensecret");
            oauthtokenList0.add(oauthtoken);
            oauthtokenList0.add(oauthtokensecret);
        }
        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret);
        long cursor = -1;
        int count = 5000;
        IDs ids = null;
        ArrayList<Long> idsList = new ArrayList<Long>();
        int s = 0;

        int i = 0;
        while(true){
            try {
                ids = twitter.getFollowersIDs(screen_name[i], cursor, count);
                cursor = ids.getNextCursor();

                for (long id : ids.getIDs()){
                    idsList.add(id);
                }
                System.out.println("Добавили в базу");
                if(s == (int) cursor){
                    i++;
                    cursor = -1;
                }
                if(i == screen_name.length){
                    break;
                }
            } catch (TwitterException e) {
                System.out.println("Превышен лимит АРI-запросов ");
                String oauthtoken = OauthtokenZapas(oauthtokenList0);
                String oauthtokensecret = OauthtokenZapas(oauthtokenList0);
                twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret);
            }
        }
        return idsList;
    }

    public void GetInfoFollowersIDs(DBUserIdInfo DBU) throws SQLException {//Получаем подробную инфу каждого id полученого выше
        ArrayList<Long> idsList = GetFollowersIDs();
        ArrayList<Userid> DBlookupUsers = new ArrayList<Userid>();
        int mass = Stec(idsList.size());
        long[][] usersIDs = new long[mass][100];
        System.out.println(mass);
        int si = 0;
        int sx = 0;
        while(si < mass){
            int sj = 0;
            while(sj < 100){
                try {
                    usersIDs[si][sj] = idsList.get(sx);
                    sj++;
                    sx++;
                } catch (Exception e) {
                    break;
                }

            }
            si++;
        }
        //создаеться запас токенов доступа
        DBwork dBwork = new DBwork();
        ResultSet rs = dBwork.getAccess();
        ArrayList<String> oauthtokenList = new ArrayList<String>();
        while (rs.next()){
            String oauthtoken = rs.getString("oauthtoken");
            String oauthtokensecret = rs.getString("oauthtokensecret");
            oauthtokenList.add(oauthtoken);
            oauthtokenList.add(oauthtokensecret);
        }

        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret);
        ResponseList<User> users = null;
        int sMas = 0;
        int sMas2 = 1;
        int proverka = 1;//потом удалить
        while(sMas < usersIDs.length) {
            while (true) {
                try {
                    users = twitter.lookupUsers(usersIDs[sMas]);
                    break;
                } catch (TwitterException e) {
                    //e.printStackTrace();
                    System.out.println("Косяк при подключении к твиттеру, повторяю попытку");
                    String oauthtoken = OauthtokenZapas(oauthtokenList);
                    String oauthtokensecret = OauthtokenZapas(oauthtokenList);
                    twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret);
                }
            }

            System.out.println("Начинаем добавлять");
            for(int i=0; i < usersIDs[sMas].length; i++) {
                String number = "" + sMas2;
                sMas2++;
                String name = null;
                try {
                    name = users.get(i).getScreenName();
                } catch (Exception e) {
                    break;
                }
                String friendsCount = Integer.toString(users.get(i).getFriendsCount());
                String followersCount = Integer.toString(users.get(i).getFollowersCount());
                String statusesCount = Integer.toString(users.get(i).getStatusesCount());
                Date createdAt = null;
                String text = null;
                try {
                    createdAt = users.get(i).getStatus().getCreatedAt();
                    text = users.get(i).getStatus().getText();
                } catch (Exception e) {
                    createdAt = null;
                    text = null;
                }
                String Lang = users.get(i).getLang();

                DBlookupUsers.add(new Userid(number, name, friendsCount, followersCount, statusesCount, createdAt, text, Lang));

                final int countDB = DBlookupUsers.size();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Label.setText("Первичная база: " + countDB);
                    }
                });
            }
            sMas++;
            try {
                System.out.println(proverka+" Уснули на 0.1 сек");
                sleep(100);
                proverka++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Уснуть не могу");
            }
        }
        DBU.thisravno(DBlookupUsers);//Важный момент (или главный)
        final int countDB = DBU.getDBUseridListcount();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label.setText("Первичная база: " + countDB);
            }
        });

        int iss = 1;
        for(Userid us: DBlookupUsers){
            System.out.println(iss + " - " + us.getName());
            System.out.println(us.getMMT());
            System.out.println(us.getddT());
            System.out.println("Lang: "+us.getLang());
            System.out.println("text: "+us.getText());
            System.out.println("__________________________");
            iss++;
        }
    }

    public void run() {
        try {
            GetInfoFollowersIDs(DBU);
        } catch (SQLException e) {
            System.out.println("Пробела с запуском потока из-за Базы Данных");
        }
    }

    public Integer Stec(int xx){//Возвращаем вспомогалетьные числа для создание двумерного массива на основе кол-во в базе
        double x = xx;
        double y = 100;
        double a = x/y;
        int o = (int)a;

        double resul = a - o;
        int res;
        if (resul == 0 ){
            res = (int)a;
        } else {
            res = (int)a + 1;
        }
        return res;
    }
    public String OauthtokenZapas(ArrayList<String> List){
        String token = List.get(0);
        List.remove(0);
        return token;
    }
}
