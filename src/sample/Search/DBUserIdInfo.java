package sample.Search;


import java.io.Serializable;
import java.util.ArrayList;

public class DBUserIdInfo implements Serializable{
    int count = 0;
    ArrayList<Userid> DBUseridList = new ArrayList<Userid>();

    public DBUserIdInfo(){

    }
    public void thisravno(ArrayList<Userid> DBUseridList1){
        this.DBUseridList = DBUseridList1;
    }
    public int getDBUseridListcount(){
        int x = DBUseridList.size();
        return x;
    }
    public void addUserid(Userid userid){
        DBUseridList.add(userid);
        count++;
    }
    public void clear(){
        DBUseridList.clear();
        count = 0;
    }
    public int size(){
        int x = DBUseridList.size();
        return x;
    }
    public int getCount(){
        return count;
    }
    public String screen_name(int rnd){
        String x = DBUseridList.get(rnd).getName();
        DBUseridList.remove(rnd);//удаление (потом запилить это в потоке дабы избежать неоправданого удаления)
        return x;
    }
    public void delet(String name){
        DBUseridList.remove(name);
    }
}
