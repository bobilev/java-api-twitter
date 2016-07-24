package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTime {

    public String DataTimeString(){//Показывает реальное время
        Date d = new Date();
        SimpleDateFormat kk, mm, ss;

        kk = new SimpleDateFormat("kk");
        mm = new SimpleDateFormat("mm");
        ss = new SimpleDateFormat("ss");

        String kkString = Integer.toString(Integer.parseInt(kk.format(d)));
        String mmString = Integer.toString(Integer.parseInt(mm.format(d)));
        String ssString = Integer.toString(Integer.parseInt(ss.format(d)));
        String result = kkString+":"+mmString+":"+ssString;
        return result;
    }
    //=======================================
    // Год
    //=======================================
    public String DataTimeYearNow(){//Показывает какой сейчас год
        Date d = new Date();
        SimpleDateFormat yyyy;

        yyyy = new SimpleDateFormat("yyyy");

        String yyyyString = Integer.toString(Integer.parseInt(yyyy.format(d)));
        String result = yyyyString;
        return result;
    }
    //=======================================
    // Месяц
    //=======================================
    public String DataTimeMonthNow(){//Показывает какой сейчас месяц
        Date d = new Date();
        SimpleDateFormat MM;

        MM = new SimpleDateFormat("MM");

        String MMString = Integer.toString(Integer.parseInt(MM.format(d)));
        String result = MMString;
        return result;
    }
    //=======================================
    // День
    //=======================================
    public String DataTimeDayNow(){//Показывает какой сейчас день
        Date d = new Date();
        SimpleDateFormat dd;

        dd = new SimpleDateFormat("dd");

        String ddString = Integer.toString(Integer.parseInt(dd.format(d)));
        String result = ddString;
        return result;
    }
    //=======================================
    // Час
    //=======================================
    public String DataTimeHourNow(){//Показывает какой сейчас час
        Date d = new Date();
        SimpleDateFormat kk;

        kk = new SimpleDateFormat("kk");

        String kkString = Integer.toString(Integer.parseInt(kk.format(d)));
        String result = kkString;
        return result;
    }
    //=======================================
    // Минута
    //=======================================
    public String DataTimeMinutsNow(){//Показывает сколько сейчас минут
        Date d = new Date();
        SimpleDateFormat mm;

        mm = new SimpleDateFormat("mm");

        String mmString = Integer.toString(Integer.parseInt(mm.format(d)));
        String result = mmString;
        return result;
    }
    //=======================================
    // Секунды
    //=======================================
    public String DataTimeSecondsNow(){//Показывает сколько сейчас секунд
        Date d = new Date();
        SimpleDateFormat ss;

        ss = new SimpleDateFormat("ss");

        String ssString = Integer.toString(Integer.parseInt(ss.format(d)));
        String result = ssString;
        return result;
    }

    public void DataTimeFormat(final Label label){//Показывает системное время выполнения команд (дополнение)
        final Date d = new Date();
        SimpleDateFormat dd, MM, yyyy, kk, mm, ss;
        dd = new SimpleDateFormat("dd");
        MM = new SimpleDateFormat("MM");
        yyyy = new SimpleDateFormat("yyyy");
        kk = new SimpleDateFormat("kk");
        mm = new SimpleDateFormat("mm");
        ss = new SimpleDateFormat("ss");

        final String ddString = Integer.toString(Integer.parseInt(dd.format(d)));
        final String MMString = Integer.toString(Integer.parseInt(MM.format(d)));
        final String yyyyString = Integer.toString(Integer.parseInt(yyyy.format(d)));

        int k = Integer.parseInt(kk.format(d));
        String kk1 = ""+k;
        if (k < 10){kk1 = "0"+k;}
        int m = Integer.parseInt(mm.format(d));
        String mm1 = ""+m;
        if (m < 10){mm1 = "0"+m;}
        int s = Integer.parseInt(ss.format(d));
        String ss1 = ""+s;
        if (s < 10){ss1 = "0"+s;}

        final String kkString = kk1;
        final String mmString = mm1;
        final String ssString = ss1;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label.setText(ddString+"."+MMString+"."+yyyyString+" "+kkString+":"+mmString+":"+ssString);
            }
        });
    }
}
