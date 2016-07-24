package sample.Repley;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class TextRipStringList implements Serializable{
    ArrayList<TextRipString> List = new ArrayList<TextRipString>();

    public TextRipStringList(){

    }
    public void add(TextRipString x){
        List.add(x);
    }
    public String getRnd(){
        int max = List.size();
        int rnd = (int)(Math.random() * (max));
        TextRipString x = List.get(rnd);
        String xx = x.getText();
        return xx;
    }
    public int size(){
        int x = List.size();
        return x;
    }
    public void clear(){
        List.clear();
    }

    public ObservableList<TextRipString> getList(){
        ObservableList<TextRipString> ListO = FXCollections.observableArrayList();
        for(TextRipString TRS: List){
            String text = TRS.getText();
            int number = TRS.getNumber();
            ListO.add(new TextRipString(text,number));
        }
        return ListO;
    }




}
