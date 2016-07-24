package sample;

import java.sql.*;
import java.util.ArrayList;


public class DBwork {
    String DB_DRIVER = "com.mysql.jdbc.Driver";//только для MySQL
    String DB_NAME = "newdb";
    String DB_CONNECTION = "jdbc:mysql://localhost:3306/"+DB_NAME;
    String DB_USER = "bobilev";
    String DB_PASSWORD = "130215";

    public DBwork() throws SQLException {
        try {
            Class.forName(DB_DRIVER);//определяем драйвер для нашей БД
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String DBconnectio() throws SQLException {
        try{
            Connection c = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);//конектимся (и тупой поймет что это)
            if (!c.isClosed()) {
                String status = "БД подключена";
                return status;
            } else {
                String status = "БД Не Найдена";
                return status;
            }
        } catch (SQLException e){
            String status = "ОШИБКА";
            return status;
        }


    }

//    public String getTextposta() throws SQLException {//получение случайного текста
//        Connection c = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
//        int rnd = 1 + (int)(Math.random() * ((4110 - 1) + 1));
//        String selectTableSQL = "SELECT * FROM textposta WHERE id = '"+rnd+"'";//формируем запрос
//        String text = null;
//        Statement st = null;
//        try {
//            st = c.createStatement();
//            ResultSet rs = st.executeQuery(selectTableSQL);
//            rs.next();
//            text = rs.getString("textposta");
//            return text;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println(rnd);
//        }
//        return text;
//    }

    public ArrayList getAllName() throws SQLException {
        Connection c = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        String selestTableSQL = "SELECT username from access3";//формируем запрос;
        ArrayList<String> ListName = new ArrayList<String>();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(selestTableSQL);
        while(rs.next()){
            ListName.add(rs.getString("username"));
        }

        return ListName;

    }

    public ResultSet getAccess() throws SQLException {//получение доступа к аккам
        Connection c = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        String selestTableSQL = "SELECT * from access3";//формируем запрос;

        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(selestTableSQL);
        return rs;

    }

    public int getCount() throws SQLException {//узнаем сколько акков в таблице access
        try {
            Connection c = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            String selestTableSQL = "SELECT id from access3";//формируем запрос;

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(selestTableSQL);

            rs.last();
            int rowCount = rs.getRow();
            return rowCount;
        } catch (SQLException e){
            int rowCount = -1;
            return rowCount;
        }

    }
    public ResultSet getAppsOAS(String name,Integer id) throws SQLException {
        String db1 = "access3";
        String db2 = "access_2";
        String db3 = "access_3";
        String db = "";
        if(id==0){db = db1;}
        if(id==1){db = db2;}
        if(id==2){db = db3;}
        Connection c = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        String selestTableSQL = "SELECT * from "+db+" where username='"+name+"'";//формируем запрос;

        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(selestTableSQL);
        rs.next();
        return rs;
    }
}
