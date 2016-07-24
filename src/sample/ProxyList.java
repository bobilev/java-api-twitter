package sample;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ProxyList {
    Set<String> ListProxy = new HashSet<String>();

    public ProxyList(){

    }

    public void add(String x, final Label label){
        ListProxy.add(x);
        final int countFin = size();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label.setText("Proxy: "+countFin);
            }
        });
    }
    public Set<String> getListProxy(){
        return ListProxy;
    }
    public String getProxy(){
        int max = ListProxy.size();
        int rnd = (int)(Math.random() * (max));
        ArrayList<String> massProxy = new ArrayList<String>();
        for(String p: ListProxy){
            massProxy.add(p);
        }
        String x = massProxy.get(rnd);
        return x;
    }
    public void delete(Object x, final Label labCountProxy){
        ListProxy.remove(x);
        final int count = size();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labCountProxy.setText("Proxy: "+count);
            }
        });
        int countP = size();
//        if(countP < 10){
//            try {
//                startInsait(labCountProxy);
//            } catch (SQLException e) {e.printStackTrace();}
//
//        }
    }
    public int size(){
        int x = ListProxy.size();
        return x;
    }

    public Set<String> start() {
        Set<String> ListTest = new HashSet<String>();
        for(String s: ListProxy){
            ListTest.add(s);
        }
        ListProxy.clear();
        Document doc = null;
        ArrayList<String> URL = new ArrayList<String>();
        URL.add("http://exitmatrix.ru/prxnew/proxy_hideme/1.txt");
        URL.add("http://exitmatrix.ru/prxnew/proxy_hideme/2.txt");
        URL.add("http://exitmatrix.ru/prxnew/proxy_hideme/3.txt");
        URL.add("http://exitmatrix.ru/prxnew/proxy_hideme/4.txt");
        for(String url:URL){
            try {
                doc  = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String slp = doc.text();

            Pattern pt = Pattern.compile("\\s");
            String[] mt = pt.split(slp);
            for (String s:mt){
                ListTest.add(s);
            }
        }
        return ListTest;



    }
//    public void startInsait(Label label) throws SQLException {
//        DBwork dbwork = new DBwork();
//        ResultSet rs = dbwork.getAccess();
//        int count = dbwork.getCount();
//        String[][] massOauth = new String[count][2];
//        int o = 0;
//        while(rs.next()){
//            massOauth[o][0] = rs.getString("oauthtoken");
//            massOauth[o][1] = rs.getString("oauthtokensecret");
//            o++;
//        }
//
//        Set<String> mt = start();
//        int i = 0;
//        for ( String proxy : mt){
//            i++;//*********
//            int rnd = (int)(Math.random() * (count));
//            String oauthtoken = massOauth[rnd][0];
//            String oauthtokensecret = massOauth[rnd][1];
//            Thread thread = new Thread(new ProxyListPotok(oauthtoken, oauthtokensecret,proxy,ListProxy,i,label));
//            thread.start();
//        }
//    }
}
