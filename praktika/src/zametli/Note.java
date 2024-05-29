package zametli;

import java.util.Formatter;
import java.util.Locale;

/**
 * Класс для работы с заметкой
 * @author Гоенко Игорь
 * @version 1.0
 */

public class Note {
    private int id;
    private String text;
    /**
     * <p>Конструктор с параметрами, инициализирующий заметку.</p>
     * @param id ID заметки.
     * @param text текст заметки
     */
     public Note(int id, String text){
         this.id = id;
         this.text = text;
     }

    /**
     * <p>Метод для получения ID заметки</p>
     * @return id ID заметки
     */
     public int getID(){
         return id;
    }
    /**
     * <p>Метод для записи нового ID заметки</p>
     * @param id новый ID заметки
     */
    public void setID(int id){
         this.id = id;
    }

    /**
     * <p>Метод для получения текста заметки</p>
     * @return id ID заметки
     */
    public String getText(){
         return text;
    }
    /**
     * <p>Метод для записи нового текста заметки</p>
     * @param text новый текст заметки
     */
    public void setText(String text){
        this.text = text;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb,Locale.US);
        formatter.format("%-5s %-1s %-20s",id,"|",text);
        String str = String.valueOf(formatter);
        return str;
    }
}
