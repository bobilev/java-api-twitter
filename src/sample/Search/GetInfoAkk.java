package sample.Search;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import sample.DBwork;
import sample.DataTime;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class GetInfoAkk {
    String oauthtoken = "2603958944-lxA91CNiIg4LdbpQT0JEHgglCoNhg23cEgCmowR";
    String oauthtokensecret = "oGJP4xdvKDksqsbumSx37mujngCOEbxoE5k7yN15F696H";
    String setOAuthConsumerKey = "oINvM4zJ3UUL21YM6Xb7mIF8k";
    String setOAuthConsumerSecret = "OPw7qrXj8v1zkMmgNYYOzkYSeKNRWo8s6hCaMSXI74mcSrLidE";



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
    //====================================================
    // Для статистики
    //====================================================
    public ObservableList Usersid() throws SQLException {
        DBwork dbwork = new DBwork();
        ArrayList<String> List = dbwork.getAllName();
        int stec = Stec(List.size());
        String[][] usersNames = new String[stec][100];
        System.out.println("stec = "+stec+" List.size = "+List.size());
        int ix = 0;
        int jx = 0;
        while (ix < stec){
            int j = 0;
            while(j < 100){
                try {
                    usersNames[ix][j] = List.get(jx);
                    j++;
                    jx++;
                } catch (Exception e) {
                    break;
                }
            }
            ix++;
        }

        Twitter twitter = ConfigTwitter(setOAuthConsumerKey,setOAuthConsumerSecret,oauthtoken,oauthtokensecret);
        ObservableList<Userid> Usersid = FXCollections.observableArrayList();
        ResponseList<User> users = null;
        int akkBan = 0;
        int w = 0;
        int j = 0;
        while (w < stec){
            while (true){
                try {
                    users = twitter.lookupUsers(usersNames[w]);
                    break;
                } catch (TwitterException e) {
                    //e.printStackTrace();
                    System.out.println("Косяк при подключении к твиттеру, повторяю попытку");
                }
            }
            int i=0;
            int k=0;
            int j2 = 0;
            while(k < usersNames[w].length) {
                String number = "" + (j + 1);
                String name = null;
                String friendsCount = null;
                String followersCount = null;
                String statusesCount = null;
                Date createdAt = null;
                String text = null;
                String Lang = null;
                name = users.get(i).getScreenName();
                if(usersNames[w][j2]==null){break;}//прерываем потому что массив всегда равенн 100, а это плохо
                if (usersNames[w][j2].equals(name.toLowerCase())) {
                    friendsCount = Integer.toString(users.get(i).getFriendsCount());
                    followersCount = Integer.toString(users.get(i).getFollowersCount());
                    statusesCount = Integer.toString(users.get(i).getStatusesCount());
                    try {
                        createdAt = users.get(i).getStatus().getCreatedAt();
                        text = users.get(i).getStatus().getText();
                        Lang = users.get(i).getLang();
                    } catch (Exception e) {
                        createdAt = null;
                        text = null;
                        Lang = null;
                    }
                    i++;
                } else{
                    name = usersNames[w][j2];
                    friendsCount = "-1";
                    followersCount = "-1";
                    statusesCount = "-1";
                    createdAt = null;
                    text = "Бан";
                    Lang = null;
                    akkBan++;
                    System.out.println(name);
                }
                Usersid.add(new Userid(number, name, friendsCount, followersCount, statusesCount, createdAt, text, Lang));
                j++;
                k++;
                j2++;
            }
            w++;
        }
        System.out.println("Аккаунтов в бане = "+akkBan);

        return Usersid;
    }
    //====================================================
    // Для сбора данных в арбитраже
    //====================================================
    public void UsersGetInfo(String[] screen_name, Label Label, DBUserIdInfo DBU){
        Thread thread;
        thread = new Thread(new GetInfoAkkPotok(screen_name, Label, DBU));
        thread.start();
    }
    //==================================================
    // ФИЛЬТР
    //==================================================
    public void startFiltrDB(int x, DBUserIdInfo DBU,DBUserIdInfo DBUfiltr){
        File file = new File("idsRipFilter.txt");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {e.printStackTrace();}
        DBUfiltr.clear();
        ArrayList<Userid> dbu = DBU.DBUseridList;
        DataTime DT = new DataTime();
        int mm = Integer.parseInt(DT.DataTimeMonthNow());
        int dd = Integer.parseInt(DT.DataTimeDayNow());

        for(Userid us: dbu){
            int usmm = us.getMMT();
            if(usmm == mm){
                int usdd = us.getddT();
                int resuldd = dd - usdd;

                if(resuldd <= x){
//                    PrintStream printStream = null;
//                    try {
//                        printStream = new PrintStream(new FileOutputStream("idsRipFilter.txt", true), true);
//                    } catch (FileNotFoundException e) {e.printStackTrace();}
//                    try {
//                        //Записываем текст у файл
//                        //out.println(us.getName());
//                        printStream.println(us.getName());
//                    } finally {
//                        //После чего мы должны закрыть файл
//                        //Иначе файл не запишется
//                        printStream.close();
//                    }
                    DBUfiltr.addUserid(us);
                    System.out.println(us.getName());
                    System.out.println(us.getddT());
                }
            }

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
}

