package sample.Repley;


import java.io.Serializable;

public class TextRipString implements Serializable{
    String text;
    int number;
    public TextRipString(String text, int number){
        this.text = text;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }
}
