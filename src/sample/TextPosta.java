package sample;

import sample.Serialization.SerializationMy;

import java.io.IOException;
import java.util.ArrayList;

public class TextPosta {
    ArrayList<String> List = new ArrayList<String>();
    public TextPosta(){
        SerializationMy serMy = new SerializationMy();
        try {
            List = (ArrayList<String>) serMy.deserData("textposta");
        } catch (IOException e) {e.printStackTrace();}
    }
    public String getText(){
        int max = List.size();
        int rnd = (int)(Math.random() * (max - 1));
        String text = List.get(rnd);
        return text;
    }
}
