package sample.Search;


import sample.DataTime;

import java.io.Serializable;
import java.util.Date;

public class Userid implements Serializable{
    String number;
    String name;
    String friendsCount;
    String followersCount;
    String statusesCount;
    String text;
    Date createdAt;
    String Lang;

    int yyT;
    int MMT;
    int ddT;
    int kkT;
    int mmT;

    public Userid(String number, String name, String friendsCount, String followersCount, String statusesCount, Date createdAt, String text, String Lang) {
        this.number = number;
        this.name = name;
        this.friendsCount = friendsCount;
        this.followersCount = followersCount;
        this.statusesCount = statusesCount;
        this.text = text;
        this.createdAt = createdAt;
        this.Lang = Lang;

        if (createdAt != null) {
            this.yyT = createdAt.getYear();
            this.MMT = createdAt.getMonth();
            this.ddT = createdAt.getDate();
            this.kkT = createdAt.getHours();
            this.mmT = createdAt.getMinutes();
            yyT = yyT - 100;
            MMT = MMT + 1;
        }
    }

    transient DataTime DT = new DataTime();
    int yy = Integer.parseInt(DT.DataTimeYearNow())-2000;
    int MM = Integer.parseInt(DT.DataTimeMonthNow());
    int dd = Integer.parseInt(DT.DataTimeDayNow());
    int kk = Integer.parseInt(DT.DataTimeHourNow());
    int mm = Integer.parseInt(DT.DataTimeMinutsNow());

    public String getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
    public String getFriendsCount() {
        return friendsCount;
    }
    public String getFollowersCount() {
        return followersCount;
    }
    public String getStatusesCount() {
        return statusesCount;
    }
    public String getText() {
        return text;
    }
    public String getCreatedAt() {
        String resul;
        if(yy == yyT){
            if(MM == MMT){
                int resulDate = dd - ddT;
                if (resulDate == 0){
                    int resulkk = kk - kkT;
                    if(resulkk == 0){
                        int resulmm = mm - mmT;
                        if(resulmm == 0){
                            resul = " Только что";
                        } else {
                            resul = resulmm+" минут назад";
                        }
                    } else {
                        resul = resulkk+" часов назад";
                    }

                } else{
                    resul = resulDate+" дней назад";
                }

            } else {
                resul = "Месяц назад";
            }

        } else {
            resul = "Год назад";
        }
        return resul;

    }
    public int getMMT(){
        return MMT;
    }
    public int getddT(){
        return ddT;
    }
    public String getLang(){
        return Lang;
    }
}
