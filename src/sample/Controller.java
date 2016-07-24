package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sample.Repley.TextRipString;
import sample.Repley.TextRipStringList;
import sample.Repley.WindowRep;
import sample.Search.DBUserIdInfo;
import sample.Search.GetInfoAkk;
import sample.Search.Userid;
import sample.Serialization.SerializationMy;
import sample.following.WindowF;
import sample.following.WindowU;
import twitter4j.TwitterException;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;


public class Controller implements Serializable {
    SerializationMy serMy = new SerializationMy();

    public Controller(){
        try {
            DBU = (DBUserIdInfo) serMy.deserData("DBU");} catch (IOException e) {DBU = new DBUserIdInfo();}
        try {
            DBUfiltr = (DBUserIdInfo) serMy.deserData("DBUfiltr");} catch (IOException e) {DBUfiltr = new DBUserIdInfo();}
        try {
            RipList = (TextRipStringList) serMy.deserData("RipList");} catch (IOException e) {RipList = new TextRipStringList();}
        try {
            woo = (Window) serMy.deserData("window");} catch (IOException e) {woo = new Window();}
        try {
            wooF = (WindowF) serMy.deserData("windowF");} catch (IOException e) {wooF = new WindowF();}
        try {
            wooU = (WindowU) serMy.deserData("windowU");} catch (IOException e) {wooU = new WindowU();}
        try {
            WR = (WindowRep) serMy.deserData("WR");} catch (IOException e) {WR = new WindowRep();}
        try {
            RD = (RaspDay) serMy.deserData("RD");} catch (IOException e) {RD = new RaspDay();}
    }

    @FXML
    public ProgressBar progressBar1,progressBar2,progressBar3,progressBar4;
    public Label labCountProgressBar1,labCountProgressBar2,labCountProgressBar3,labCountProgressBar4;
    public Button button, buttonw2, buttonUw2,buttonRipStart;//кнопки запуска
    public Button buttonBD, buttonProxy, buttonExit;//кнопки верхнего порядка

    public Label labTimeData;//время в низу
    public Label labBD, labCountProxy;
    public Label labCount;//колво аккво
    public Label labinteratcFinalTweet, labinteratcFinalFollwing, labTimeFinalRip;
    public Label labTime, labTimew2, labTimeUw2, labTimeRip;
    public Label labCounFollowing;//сколько нужно зафоловить
    public Label labFinalDay, labTimeToDay;//Unfollowing
    public TextArea textArea;

    public ChoiceBox<Integer> cbTweetHours = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbTweetMinuts = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbTweetMinRand = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbTweetMaxRand = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbTweetInterac = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbRipHours = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbRipMinuts = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbRipInterac = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbFollowing = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbFollowingInterac = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbUnFollowingDay = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbUnFollowingHaurs = new ChoiceBox<Integer>();
    //=============================
    // Реплеи
    //=============================
    @FXML
    public TextField textFieldLinkRip;
    @FXML
    public TextArea textRip,textAreaid;
    @FXML
    public Button buttonRep;
    @FXML
    public Label followersDB, labFiltrDB, labInfoOstatok, labCountTextRip, labTimeInteracRip;
    @FXML
    public Tab TabRip;
    @FXML
    public ChoiceBox<Integer> cb = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbMinRand = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> cbMaxRand = new ChoiceBox<Integer>();
    //=============================
    // Расписание
    //=============================
    @FXML
    public ChoiceBox<Integer> RaspTweets = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> RaspFollowing = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> RaspUnFollowing = new ChoiceBox<Integer>();
    public ChoiceBox<Integer> RaspRip = new ChoiceBox<Integer>();
    public RadioButton radioButtonTweet, radioButtonRip;

    //===============================================
    // Tabl
    //===============================================
    @FXML
    public Button refreshTabl;
    @FXML
    private TableView<Userid> TableUsers;
    @FXML
    private TableColumn<Userid, String> Number;
    @FXML
    private TableColumn<Userid, String> Name;
    @FXML
    private TableColumn<Userid, String> Twits;
    @FXML
    private TableColumn<Userid, String> Friends;
    @FXML
    private TableColumn<Userid, String> Followers;
    @FXML
    private TableColumn<Userid, String> Date;
    @FXML
    private TableColumn<Userid, String> Text;

    //===============================================
    // Tab2-Rip
    //===============================================
    @FXML
    private TableView<TextRipString> TablRipText;
    @FXML
    private TableColumn<TextRipString ,Integer> N;
    @FXML
    private TableColumn<TextRipString, String> TextRip;

    Window woo = new Window();
    WindowF wooF = new WindowF();
    WindowU wooU = new WindowU();
    GetInfoAkk getInfoAkk = new GetInfoAkk();
    DBUserIdInfo DBU = new DBUserIdInfo();
    DBUserIdInfo DBUfiltr = new DBUserIdInfo();
    TextRipStringList RipList = new TextRipStringList();//список текста для арбитража
    WindowRep WR = new WindowRep();//арбитраж
    DataTime d = new DataTime();//время
    ProxyList PL = new ProxyList();//прокси
    RaspDay RD = new RaspDay();


    @FXML
    public void clickExit() {
        timData.stop();
        timRasp.stop();
        timProxy.stop();
        serMy.serData("window", woo);
        serMy.serData("windowF",wooF);
        serMy.serData("windowU",wooU);
        serMy.serData("DBU",DBU);
        serMy.serData("DBUfiltr",DBUfiltr);
        serMy.serData("WR",WR);
        serMy.serData("RipList",RipList);
        serMy.serData("RD",RD);
        saveText(textArea);

        System.exit(0);

    }
    @FXML

    public void clickProxy() throws SQLException {
        DBwork dbwork = new DBwork();
        ResultSet rs = dbwork.getAccess();
        int count = dbwork.getCount();
        String [] massAccess = new String[count];
        int i = 0;
        while (rs.next()){//создаем массив токенов
            massAccess[i] = rs.getString("username");
            i++;
        }
        List<String> lst = Arrays.asList(massAccess);
        Collections.shuffle(lst);
        massAccess = lst.toArray(massAccess);

        Set<String> mt = PL.start();
        Set<String> ListProxy = PL.getListProxy();
        ResultSet rsApps;
        AppsKey appsKey = new AppsKey();
        int o = 0;
        int j = 0;
        for ( String proxy : mt){
            if(o==3){o = 0;}
            i++;//*********
            rsApps = dbwork.getAppsOAS(massAccess[j], o);
            String oauthtoken = rsApps.getString("oauthtoken");
            String oauthtokensecret = rsApps.getString("oauthtokensecret");
            String setOAuthConsumerKey = appsKey.getoauthtoken(o);
            String setOAuthConsumerSecret = appsKey.getoauthtokensecret(o);
            o++;
            j++;
            if(j==count){j=0;}
            Thread thread = new Thread(new ProxyListPotok(oauthtoken,oauthtokensecret,setOAuthConsumerKey,setOAuthConsumerSecret,proxy,ListProxy,i,labCountProxy));
            thread.start();

        }
    }
    @FXML
    public void clickRefreshTabl() throws SQLException {
        ObservableList<Userid> usersData = getInfoAkk.Usersid();

        // устанавливаем тип и значение которое должно хранится в колонке
        Number.setCellValueFactory(new PropertyValueFactory<Userid, String>("number"));
        Name.setCellValueFactory(new PropertyValueFactory<Userid, String>("name"));
        Twits.setCellValueFactory(new PropertyValueFactory<Userid, String>("statusesCount"));
        Friends.setCellValueFactory(new PropertyValueFactory<Userid, String>("friendsCount"));
        Followers.setCellValueFactory(new PropertyValueFactory<Userid, String>("followersCount"));
        Date.setCellValueFactory(new PropertyValueFactory<Userid, String>("createdAt"));
        Text.setCellValueFactory(new PropertyValueFactory<Userid, String>("text"));

        TableUsers.setItems(usersData);
    }
    //=========================================================
    // ChoicBox Tweet/following/unfollowing/rip
    //=========================================================
    @FXML
    public void ChoiceBoxsAll(){
        //----------------------
        //Tweet
        //---------------------
        cbTweetHours.setItems(FXCollections.observableArrayList(
            0,1,2,3,4,5,6,7,8,9,10
        ));
        cbTweetHours.getSelectionModel().select(0);
        cbTweetHours.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                woo.setGhours(newValue);
                int h = woo.getGHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = woo.getGMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = woo.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTime.setText(hh+":"+mm+":"+ss);
            }
        });
        cbTweetMinuts.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,
                37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59
        ));
        cbTweetMinuts.getSelectionModel().select(0);
        cbTweetMinuts.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                woo.setGminuts(newValue);
                int h = woo.getGHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = woo.getGMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = woo.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTime.setText(hh+":"+mm+":"+ss);
            }
        });
        cbTweetMinRand.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20
        ));
        cbTweetMinRand.getSelectionModel().select(0);
        cbTweetMinRand.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                woo.setMinRand(newValue);
            }
        });
        cbTweetMaxRand.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20
        ));
        cbTweetMaxRand.getSelectionModel().select(0);
        cbTweetMaxRand.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                woo.setMaxRand(newValue);
            }
        });
        cbTweetInterac.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
        ));
        cbTweetInterac.getSelectionModel().select(0);
        cbTweetInterac.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                woo.setInteratcFinal(newValue);
                labinteratcFinalTweet.setText(woo.getInteratc()+"|"+woo.getInteratcFinal());
            }
        });
        //------------------------
        //Rip
        //------------------------
        cb.setTooltip(new Tooltip("Дней назад"));
        cb.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3));
        cb.getSelectionModel().select(0);
        cbRipHours.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12
        ));
        cbRipHours.getSelectionModel().select(0);
        cbRipHours.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                WR.setGhours(newValue);
                int h = WR.getGHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = WR.getGMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = WR.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTimeRip.setText(hh+":"+mm+":"+ss);
            }
        });
        cbRipMinuts.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,
                37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59
        ));
        cbRipMinuts.getSelectionModel().select(0);
        cbRipMinuts.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                WR.setGminuts(newValue);
                int h = WR.getGHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = WR.getGMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = WR.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTimeRip.setText(hh+":"+mm+":"+ss);
            }
        });
        cbMinRand.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30));
        cbMinRand.getSelectionModel().select(0);
        cbMinRand.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                WR.setMinRand(newValue);
            }
        });

        cbMaxRand.setItems(FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30));
        cbMaxRand.getSelectionModel().select(0);
        cbMaxRand.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                WR.setMaxRand(newValue);
            }
        });
        cbRipInterac.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12
        ));
        cbRipInterac.getSelectionModel().select(0);
        cbRipInterac.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                WR.setInteratcFinal(newValue);
                labTimeInteracRip.setText(WR.getInteratc() + "|" + WR.getInteratcFinal());
            }
        });
        //------------------------
        //Following
        //------------------------
        cbFollowing.setItems(FXCollections.observableArrayList(
                5,10,15,20,25,30,35,40,45,50
        ));
        cbFollowing.getSelectionModel().select(0);
        cbFollowing.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                wooF.setCountFollowing(newValue);
                labCounFollowing.setText(""+wooF.getCountFollowing());
            }
        });
        cbFollowingInterac.setItems(FXCollections.observableArrayList(
                0,1,2,3,4
        ));
        cbFollowingInterac.getSelectionModel().select(0);
        cbFollowingInterac.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                wooF.setInteratcFinal(newValue);
                labinteratcFinalFollwing.setText(wooF.getInteratc()+"|"+wooF.getinteratcFinal());
                int h = wooF.getGHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = wooF.getGMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = wooF.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTimew2.setText(hh+":"+mm+":"+ss);
            }
        });
        //------------------------
        //UnFollowing
        //------------------------
        cbUnFollowingDay.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6
        ));
        cbUnFollowingDay.getSelectionModel().select(0);
        cbUnFollowingDay.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                int H = Integer.parseInt(d.DataTimeHourNow());
                int M = Integer.parseInt(d.DataTimeMinutsNow());
                wooU.setDay(newValue, H, M);
                int h = wooU.getHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = wooU.getMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = wooU.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTimeUw2.setText(hh+":"+mm+":"+ss);
                labFinalDay.setText(wooU.getDay());
            }
        });
        cbUnFollowingHaurs.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23
        ));
        cbUnFollowingHaurs.getSelectionModel().select(0);
        cbUnFollowingHaurs.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                int H = Integer.parseInt(d.DataTimeHourNow());
                int M = Integer.parseInt(d.DataTimeMinutsNow());
                wooU.setHours(newValue, H, M);
                int h = wooU.getHours();
                String hh = ""+h;
                if (h < 10){hh = "0"+h;}
                int m = wooU.getMinuts();
                String mm = ""+m;
                if (m < 10){mm = "0"+m;}
                int s = wooU.getSeconds();
                String ss = ""+s;
                if (s < 10){ss = "0"+s;}
                labTimeUw2.setText(hh+":"+mm+":"+ss);
                labTimeToDay.setText(wooU.getGhours());
            }
        });

    }
    //=========================================================
    // Реплеи
    //=========================================================
    @FXML
    public void clickbuttonRep(){//Запуск сбора id followers
        String screen_name = textAreaid.getText();
        Pattern pt = Pattern.compile("\\s");
        String[] mt = pt.split(screen_name);
        followersDB.setText("Первичная база: 0");
        DBU.clear();
        getInfoAkk.UsersGetInfo(mt, followersDB, DBU);
    }
    @FXML
    public void StartFiltr(){//Запуск сбора информации каждого id по фильтру
        int x = cb.getValue();
        getInfoAkk.startFiltrDB(x,DBU,DBUfiltr);
        int size = DBUfiltr.getDBUseridListcount();
        int count = DBUfiltr.getCount();
        labFiltrDB.setText("Отфильтрованная база: "+count);
        labInfoOstatok.setText(size+"|"+count);
    }
    public void FormylaRascheta(){//для расчета остатка времени рассылки реплеев

    }
    public void textRipOnli(){//отслеживание онлайн лимита в 140
        textRip.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int count = textRip.getAnchor() + 1;
                labCountTextRip.setText(""+count);

            }
        });
    }
    public void textRip(){//добовление текста по одному
        String text = textRip.getText();
        textRip.clear();
        int number = RipList.size() + 1;
        RipList.add(new TextRipString(text,number));
        TablRipTextUp();
    }
    public void TablRipTextUp(){
        TabRip.setText("");
        TabRip.setText(""+RipList.size());
        ObservableList<TextRipString> userData = RipList.getList();

        // устанавливаем тип и значение которое должно хранится в колонке
        N.setCellValueFactory(new PropertyValueFactory<TextRipString, Integer>("number"));
        TextRip.setCellValueFactory(new PropertyValueFactory<TextRipString, String>("text"));

        TablRipText.setItems(userData);
    }
    public void DeleteRip(){
        RipList.clear();
        TabRip.setText("0");
    }
    //временно
    public void ExportTXTfile(){
        FileChooser fileChooser = new FileChooser();

        //Устанавливаем фильтр по txt
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)","*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Открываем
        File file = fileChooser.showOpenDialog(null);
        try {
            openFile(file);//запускаем обработку файла
        } catch (NullPointerException e) {

        }
    }
    public void openFile(File file) {//отвечает за открытие выбраного файла и его обработку
        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                TextRipString trs;
                int i = 0;
                while ((s = in.readLine()) != null) {
                    String resul = RegExpLink(s);
                    i++;
                    trs = new TextRipString(resul,i);
                    RipList.add(trs);
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
                TablRipTextUp();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String RegExpLink(String text){
        ArrayList<String> linkList = new ArrayList<String>();
        linkList.add("http://bit.ly/1yFciv7");
        linkList.add("http://bit.ly/1uFW2tv");
        linkList.add("http://bit.ly/15WYUfc");
        linkList.add("http://bit.ly/1yFcESv");
        linkList.add("http://bit.ly/15WZ4TL");
        int rnd = (int)(Math.random() * ((linkList.size())));
        Pattern pattern  = Pattern.compile("\\[\\/\\]");
        Matcher matcher = null;
        matcher = pattern.matcher(text);

        String link = linkList.get(rnd);
        String result = matcher.replaceFirst(link);
        return result;
    }
    //=========================================================
    // Расписание
    //=========================================================
    public void radioButtonStart (){
        if(radioButtonTweet.isHover()){
            boolean res = radioButtonTweet.isSelected();
            RD.setTweet(res);
        }
        if(radioButtonRip.isHover()) {
            boolean res = radioButtonRip.isSelected();
            RD.setRip(res);
        }

    }
    public void choisboxRaspStart(){
        RaspTweets.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        RaspTweets.getSelectionModel().select(0);
        RaspTweets.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                RD.setTweetH(newValue);
            }
        });

        RaspFollowing.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        RaspFollowing.getSelectionModel().select(0);
        RaspFollowing.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                RD.setFollowingH(newValue);
            }
        });

        RaspUnFollowing.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        RaspUnFollowing.getSelectionModel().select(0);
        RaspUnFollowing.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                RD.setUnFollowingH(newValue);
            }
        });
        RaspRip.setItems(FXCollections.observableArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        RaspRip.getSelectionModel().select(0);
        RaspRip.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                RD.setRipH(newValue);
            }
        });

    }
    public void Rasp(){
        int h = Integer.parseInt(d.DataTimeHourNow());
        if(RD.isTweet()){//возобновление Tweet
            if(h == RD.getTweetH()){
                woo.Rasp();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        labinteratcFinalTweet.setText(woo.getInteratc()+"|"+woo.getInteratcFinal());
                        button.setId("buttonStop");
                    }
                });
                startTim(true);
                System.out.println("RASP T STARTS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }
        }
        if(RD.isRip()){
            if(h == RD.getRipH()){
                WR.Rasp();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        labTimeInteracRip.setText(WR.getInteratc() + "|" + WR.getInteratcFinal());
                        buttonRipStart.setId("buttonStop");
                    }
                });
                startTimRip(true);
                System.out.println("RASP R STARTS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }
    //=========================================================
    // STARTS
    //=========================================================
    @FXML//ПУСК/СТОП
    public void onClickMetod(){//Запуск Постинга
        String getText = button.getId();

        if(getText.equals("buttonStart")){
            button.setId("buttonStop");
            startTim(true);
        } else {
            button.setId("buttonStart");
            startTim(false);
        }
    }

    @FXML//ПУСК/СТОП
    public void onClickMetodw2(){//Запуск Followinga
        String getText = buttonw2.getText();

        if(getText.equals("Пуск")){
            buttonw2.setText("Стоп");
            buttonw2.setId("buttonStop");
            startTimw2(true);
        } else {
            buttonw2.setText("Пуск");
            buttonw2.setId("buttonStart");
            startTimw2(false);
        }
    }

    @FXML//ПУСК/СТОП
    public void onClickMetodUw2() throws InterruptedException, SQLException, TwitterException {//Запуск UnFollowinga
        String getText = buttonUw2.getText();
        int h = Integer.parseInt(d.DataTimeHourNow());
        int m = Integer.parseInt(d.DataTimeMinutsNow());

        if(getText.equals("Пуск")){
            buttonUw2.setText("Стоп");
            buttonUw2.setId("buttonStop");
            wooU.RaschotDay(h,m);
            startTimUw2(true);
        } else {
            buttonUw2.setText("Пуск");
            buttonUw2.setId("buttonStart");
            wooU.RaschotDay(h,m);
            startTimUw2(false);
        }
    }
    @FXML//ПУСК/СТОП - Rip
    public void onClickRipStart(){//Запуск Постинга
        String getText = buttonRipStart.getId();

        if(getText.equals("buttonStart")){
            buttonRipStart.setId("buttonStop");
            startTimRip(true);
        } else {
            buttonRipStart.setId("buttonStart");
            startTimRip(false);
        }
    }
    @FXML//ПУСК/СТОП - Rip
    public void onClickRipStartExtro(){//=============================Экстро запуск
        WR.startExtro(textArea,labInfoOstatok, DBUfiltr, PL, RipList, labCountProxy,progressBar1,labCountProgressBar4);
    }
    @FXML//ПУСК/СТОП - Rip
    public void onClickUnStartExtro(){//=============================Экстро запуск
        wooU.startExtro(textArea, PL, labCountProxy,progressBar3,labCountProgressBar3);
    }
    @FXML//Проверка состояния БД
    public void  onClickgetstatusBD() throws SQLException, TwitterException {
        DBwork dbwork = new DBwork();
        labBD.setText("");
        String status = dbwork.DBconnectio();
        labBD.setText(status);
        int xx = dbwork.getCount();
        labCount.setText(Integer.toString(xx));//показываем сколько акков в таблице

    }

    //=========================================================
    // Reset Clear
    //=========================================================
    @FXML
    public void onClickbuttonReset1() {
        woo.setClear();
        WR.setClear();

        int h1 = woo.getGHours();
        String hh1 = ""+h1;
        if (h1 < 10){hh1 = "0"+h1;}
        int m1 = woo.getGMinuts();
        String mm1 = ""+m1;
        if (m1 < 10){mm1 = "0"+m1;}
        int s1 = woo.getSeconds();
        String ss1 = ""+s1;
        if (s1 < 10){ss1 = "0"+s1;}
        labTime.setText(hh1+":"+mm1+":"+ss1);
        cbTweetMinRand.setValue(0);
        cbTweetMaxRand.setValue(0);
        labinteratcFinalTweet.setText(woo.getInteratc()+"|"+woo.getInteratcFinal());
        int h2 = WR.getGHours();
        String hh2 = ""+h2;
        if (h2 < 10){hh2 = "0"+h2;}
        int m2 = WR.getGMinuts();
        String mm2 = ""+m2;
        if (m2 < 10){mm2 = "0"+m2;}
        int s2 = WR.getSeconds();
        String ss2 = ""+s2;
        if (s2 < 10){ss2 = "0"+s2;}
        labTimeRip.setText(hh2 + ":" + mm2 + ":" + ss2);
        labTimeInteracRip.setText(WR.getInteratc() + "|" + WR.getInteratcFinal());
    }
    @FXML
    public void onClickbuttonReset2() {
        wooU.setClear();
        int h3 = wooU.getHours();
        String hh3 = ""+h3;
        if (h3 < 10){hh3 = "0"+h3;}
        int m3 = wooU.getMinuts();
        String mm3 = ""+m3;
        if (m3 < 10){mm3 = "0"+m3;}
        int s3 = wooU.getSeconds();
        String ss3 = ""+s3;
        if (s3 < 10){ss3 = "0"+s3;}
        String TimeToDay = wooU.getGhours();
        labTimeToDay.setText(TimeToDay);
        labTimeUw2.setText(hh3+":"+mm3+":"+ss3);
        String TimeDay = wooU.getDay();
        labFinalDay.setText(TimeDay);

    }
    @FXML
    public void onClickDelettextArea() {
        textArea.clear();
    }

    //=========================================================
    // Timers
    //=========================================================
    public void startTim(boolean xx){//Запуск Постинга
        if(xx){
            tim.start();
        } else {
            tim.stop();
        }
    }

    public void startTimw2(boolean xx){//Запуск Followinga
        if(xx){
            tim2.start();
        } else {
            tim2.stop();
        }
    }

    public void startTimUw2(boolean xx){//Запуск UnFollowinga
        if(xx){
            tim3.start();
        } else {
            tim3.stop();
        }
    }
    public void startTimRip(boolean xx){//Запуск Rip
        if(xx){
            tim4.start();
        } else {
            tim4.stop();
        }
    }
    //-----------------Таймер для POST----------------
    Timer tim = new Timer( 1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                woo.actionTime(labTime,labinteratcFinalTweet,button,tim,textArea,PL,labCountProxy,progressBar1,labCountProgressBar1);
            } catch (TwitterException e1) {e1.printStackTrace();} catch (SQLException e1) {e1.printStackTrace();}
        }
    } );
    //-----------------Таймер для FOLLOWING-----------
    Timer tim2 = new Timer( 1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                wooF.actionTime(labTimew2, labinteratcFinalFollwing, buttonw2, tim2, textArea, PL, labCountProxy,progressBar2,labCountProgressBar2);
            } catch (TwitterException e1) {e1.printStackTrace();} catch (SQLException e1) {e1.printStackTrace();}
        }
    } );
    //-----------------Таймер для UNFOLLOWING---------
    Timer tim3 = new Timer( 1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                wooU.actionTime(labTimeUw2, textArea, PL, labCountProxy,progressBar3,labCountProgressBar3);
            } catch (TwitterException e1) {e1.printStackTrace();} catch (SQLException e1) {e1.printStackTrace();}
        }
    } );
    //-----------------Таймер для RiP---------
    Timer tim4 = new Timer( 1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                WR.actionTime(textArea,labTimeRip,labInfoOstatok,labTimeInteracRip, buttonRipStart, tim4, DBUfiltr, PL, RipList, labCountProxy,progressBar4,labCountProgressBar4);
            } catch (TwitterException e1) {e1.printStackTrace();} catch (SQLException e1) {e1.printStackTrace();}
        }
    } );

    Timer timProxy = new Timer( 35*60*1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                clickProxy();
            } catch (SQLException e1) {e1.printStackTrace();}
        }
    } );
    Timer timData = new Timer( 1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
           d.DataTimeFormat(labTimeData);
        }
    } );
    Timer timRasp = new Timer( 30*60*1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            Rasp();
        }
    } );

    public void saveText(TextArea textArea) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream("text.txt"));
            out.writeObject(textArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String upText() {
        ObjectInputStream in = null;
        String text = "";
        try {
            in = new ObjectInputStream(new FileInputStream("text.txt"));
            text = (String) in.readObject();
            return text;
        } catch (IOException e) {
            //e.printStackTrace();
            return text;
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            return text;
        }
    }

    public void initialize() throws TwitterException, SQLException {
        //------------------------------------------Десириализация переменных------------------------------------------
        System.out.println(DBU.size());
        System.out.println(DBUfiltr.size());

        onClickgetstatusBD();
        timProxy.start();
        timRasp.start();
        textArea.appendText(upText());
        ChoiceBoxsAll();
        choisboxRaspStart();
        timData.start();
        textRipOnli();
        TablRipTextUp();
        //Время первого окна-----
        int h1 = woo.getGHours();
        String hh1 = ""+h1;
        if (h1 < 10){hh1 = "0"+h1;}
        int m1 = woo.getGMinuts();
        String mm1 = ""+m1;
        if (m1 < 10){mm1 = "0"+m1;}
        int s1 = woo.getSeconds();
        String ss1 = ""+s1;
        if (s1 < 10){ss1 = "0"+s1;}
        labTime.setText(hh1+":"+mm1+":"+ss1);

        //время второго окна(following)------
        int h2 = wooF.getGHours();
        String hh2 = ""+h2;
        if (h2 < 10){hh2 = "0"+h2;}
        int m2 = wooF.getGMinuts();
        String mm2 = ""+m2;
        if (m2 < 10){mm2 = "0"+m2;}
        int s2 = wooF.getSeconds();
        String ss2 = ""+s2;
        if (s2 < 10){ss2 = "0"+s2;}
        labTimew2.setText(hh2+":"+mm2+":"+ss2);

        //время третье окна(Unfollowing)------
        int h3 = wooU.getHours();
        String hh3 = ""+h3;
        if (h3 < 10){hh3 = "0"+h3;}
        int m3 = wooU.getMinuts();
        String mm3 = ""+m3;
        if (m3 < 10){mm3 = "0"+m3;}
        int s3 = wooU.getSeconds();
        String ss3 = ""+s3;
        if (s3 < 10){ss3 = "0"+s3;}
        labTimeUw2.setText(hh3+":"+mm3+":"+ss3);

        //окно интерация Tweet------
        labinteratcFinalTweet.setText(woo.getInteratc()+"|"+woo.getInteratcFinal());

        //окно интерация Rip------
        labTimeInteracRip.setText(WR.getInteratc()+"|"+WR.getInteratcFinal());
        //окно интерация Following------
        labinteratcFinalFollwing.setText(wooF.getInteratc()+"|"+wooF.getinteratcFinal());

        //окно интерация Unfollowin)------
        String TimeDay = wooU.getDay();
        String TimeToDay = wooU.getGhours();
        labFinalDay.setText(TimeDay);
        labTimeToDay.setText(TimeToDay);

        //окно номер один Рандом время-------
        //Tweet
        int minRndTweet = woo.getMinRand();
        int maxRndTweet = woo.getMaxRand();
        cbTweetMinRand.setValue(minRndTweet);
        cbTweetMaxRand.setValue(maxRndTweet);
        //Rip
        int minRndRip = WR.getMinRand();
        int maxRndRip = WR.getMaxRand();
        cbMinRand.setValue(minRndRip);
        cbMaxRand.setValue(maxRndRip);

        //окно второе Followingcount-----
        int countFollowing = wooF.getCountFollowing();
        labCounFollowing.setText(Integer.toString(countFollowing));

        //окно РЕПЛЕЯ
        try {
            int countx = DBU.getDBUseridListcount();
            int countf = DBUfiltr.getCount();
            followersDB.setText("Первичная база: "+countx);
            labFiltrDB.setText("Отфильтрованная база: "+countf);
            labInfoOstatok.setText(DBUfiltr.size()+"|"+countf);
        } catch (Exception e) {}

        int hr = WR.getGHours();
        String hhr = ""+hr;
        if (hr < 10){hhr = "0"+hr;}
        int mr = WR.getGMinuts();
        String mmr = ""+mr;
        if (mr < 10){mmr = "0"+mr;}
        int sr = WR.getSeconds();
        String ssr = ""+sr;
        if (sr < 10){ssr = "0"+sr;}
        labTimeRip.setText(hhr+":"+mmr+":"+ssr);

        //Расписание
        int RaspT = RD.getTweetH();
        int RaspF = RD.getFollowingH();
        int RaspU = RD.getUnFollowingH();
        int RaspR = RD.getRipH();
        RaspTweets.setValue(RaspT);
        RaspFollowing.setValue(RaspF);
        RaspUnFollowing.setValue(RaspU);
        RaspRip.setValue(RaspR);

        radioButtonTweet.setSelected(RD.isTweet());
        radioButtonRip.setSelected(RD.isRip());
    }

}
