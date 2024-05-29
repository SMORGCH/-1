package zametli;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
/**
 * Класс для работы пользователя с базой данных Заметки
 * @author Гоенко Игорь
 * @version 1.0
 */
public class Main {
    /**
     * <p>Сканер консоли.</p>
     */
    static final Scanner scanner = new Scanner(System.in);
    /**
     * <p>Фаил для сохранения промежуточных данных</p>
     */
    private static final String FILENAME = "notes.xml";

    public static void main(String[] args) {

        DBNotes.connectDatabase();
        DBNotes.createTable();
        DBNotes.initFirstNote();
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Создать заметку");
            System.out.println("2. Показать все заметки");
            System.out.println("3. Редактировать заметку");
            System.out.println("4. Удалить заметку");
            System.out.println("5. Сохранить и выйти");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addNote();
                    break;
                case "2":
                    getAllNotes();
                    break;
                case "3":
                    editNotes();
                    break;
                case "4":
                    deleteNotes();
                    break;
                case "5":
                    scanner.close();
                    safeToFile();
                    System.out.println("Выход...");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
                    break;
            }
        }
    }

    /**
     * <p>Метод для добавления новой заметки</p>
     */
    private static void addNote(){
        System.out.println("Введите текст новой заметки: ");
        String content = scanner.nextLine();
        DBNotes.createNote(content);

    }
    /**
     * <p>Метод для вывода всех заметок</p>
     */
    private static void getAllNotes(){
        List<Note> notes = DBNotes.getAllNotes();
        if (notes.isEmpty()){
            System.out.println("Нет данных");
        } else {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%-5s %-1s %-20s","id","|","text");
            System.out.println(formatter);
            for(int i = 0; i< notes.size(); i++){
                System.out.println("-------------------------------------------------------------");
                System.out.println(notes.get(i));
            }

        }
    }

    /**
     * <p>Метод для редактирования заметки</p>
     */
    private static void editNotes(){
        getAllNotes();
        System.out.println("Введите ID заметки, которую хотите отредактировать: ");
        int id;
        while (true){
            try {
                id = Integer.parseInt(scanner.nextLine());
                break;
            }catch (Exception e){
                System.out.println("Введите число");
            }
        }
        System.out.println("Введите новый текст заметки: ");
        String content = scanner.nextLine();
        DBNotes.editNote(id,content);
    }

    /**
     * <p>Метод для удаления заметки</p>
     */
    private static void deleteNotes(){
        getAllNotes();
        System.out.println("Введите ID заметки, которую хотите отредактировать: ");
        int id;
        while (true){
            try {
                id = Integer.parseInt(scanner.nextLine());
                break;
            }catch (Exception e){
                System.out.println("Введите число");
            }
        }
        DBNotes.deleteNote(id);
    }

    /**
     * <p>Метод для записи и соранения данных в XML</p>
     */
    public static void safeToFile(){
        List<Note> notes = DBNotes.getAllNotes();
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.newDocument();
            org.w3c.dom.Element rootElement = document.createElement("notes");
            document.appendChild(rootElement);
            for(Note note : notes){
                org.w3c.dom.Element noteElement = document.createElement("note");

                org.w3c.dom.Element idElement = document.createElement("id");
                String idstr = Integer.toString(note.getID());
                idElement.appendChild(document.createTextNode(idstr));
                noteElement.appendChild(idElement);

                org.w3c.dom.Element contentElement = document.createElement("content");
                contentElement.appendChild(document.createTextNode(note.getText()));
                noteElement.appendChild(contentElement);


                rootElement.appendChild(noteElement);
            }
            saveDocument(document);
            System.out.println("Данные записаны в XML");
        }catch (ParserConfigurationException | TransformerException e){
            e.printStackTrace();
        }
    }

    /**
     * <p>Метод для сохранения документа</p>
     * @param document
     * @throws TransformerException
     */
    public static void saveDocument(org.w3c.dom.Document document) throws TransformerException{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(FILENAME));
        transformer.transform(source,result);
    }
}
