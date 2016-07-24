package sample.Serialization;

import java.io.*;

public class SerializationMy {

    public Object deserData(String filename) throws IOException {//Десериализация (Восстанавливаем данные)
        Object retObject = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename+".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            retObject = in.readObject();
            fileIn.close();
            in.close();
        } catch (FileNotFoundException e){
            System.out.println("Файл не найдет ("+filename+")");
            //System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден ("+filename+")");
            //System.exit(3);
        }
        return retObject;

    }

    public void serData(String filename, Object obj) {//Сериализация (сохраняем данные)
        try {
            FileOutputStream fileOut = new FileOutputStream(filename+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
        } catch (FileNotFoundException e){
            System.out.println("Файл не найдет");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
