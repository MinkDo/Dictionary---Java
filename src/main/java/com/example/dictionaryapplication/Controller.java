package com.example.dictionaryapplication;


import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8;




    @FXML
    public void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        try {
           if (event.getSource() == button1) {
                stage = (Stage) button1.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("DictionaryApplication.fxml"));
            } else if (event.getSource() == button2) {
                stage = (Stage) button2.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("Search.fxml"));
            } else if (event.getSource() == button3) {
                stage = (Stage) button3.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("Add.fxml"));
            } else if (event.getSource() == button4) {
                stage = (Stage) button4.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("Delete.fxml"));
            } else if (event.getSource() == button5) {
                stage = (Stage) button5.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("Game.fxml"));
            } else {
                stage = (Stage) button6.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("Exit.fxml"));
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (NullPointerException e) {

        }
    }




    private Dictionary dictionary;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dictionary = new Dictionary();

        // Đọc dữ liệu từ tập tin hoặc thêm dữ liệu mẫu vào từ điển

        try {
            dictionary.insertFromFile ();
            searcher.textProperty().addListener((observable, oldValue, newValue) -> {
                autoComplete ();
            });
            setComboBox();
        } catch (IOException e) {
            throw new RuntimeException (e);
        }


    }

    /*
     * Phần này để translate
     */
    @FXML
    private ComboBox<String> from = new ComboBox<>();

    @FXML
    private ComboBox<String> to = new ComboBox<>();
    @FXML
    ObservableList<String> languages = FXCollections.
            observableArrayList(
                    "Afrikaans",
    "Arabic",
    "Azerbaijani",
   " Belarusian",
    "Bulgarian",
    "Bengali",
    "Bosnian",
    "Catalan",
    "Cebuano",
    "Czech",
    "Welsh",
    "Danish",
    "German",
    "Greek",
    "English",
    "Esperanto",
    "Spanish",
    "Estonian",
    "Basque",
    "Persian",
    "Finnish",
    "French",
    "Irish",
    "Galician",
    "Gujarati",
    "Hausa",
    "Hindi",
    "Hmong",
   "Croatian",
    "Haitian Creole",
    "Hungarian",
    "Armenian",
    "Indonesian",
    "Igbo",
    "Icelandic",
    "Italian",
    "Hebrew",
    "Japanese",
    "Javanese",
    "Georgian",
    "Kazakh",
    "Khmer",
    "Kannada",
    "Korean",
    "Latin",
    "Lao",
    "Lithuanian",
    "Latvian",
    "Punjabi",
    "Malagasy",
    "Maori",
    "Macedonian",
    "Malayalam",
    "Mongolian",
    "Marathi",
    "Malay",
    "Maltese",
    "Myanmar (Burmese)",
    "Nepali",
    "Dutch",
    "Norwegian",
    "Chichewa",
    "Polish",
    "Portuguese",
    "Romanian",
    "Russian",
    "Sinhala",
    "Slovak",
    "Slovenian",
    "Somali",
    "Albanian",
    "Serbian",
    "Sesotho",
    "Sudanese",
    "Swedish",
    "Swahili",
    "Tamil",
    "Telugu",
    "Tajik",
    "Thai",
    "Filipino",
    "Turkish",
    "Ukrainian",
    "Urdu",
    "Uzbek",
    "Vietnamese",
    "Yiddish",
    "Yoruba",
    "Chinese",
    "Chinese Traditional",
    "Zulu"
);
    @FXML
    public void setComboBox() {
        from.setItems(languages);
        to.setItems(languages);
    }


    @FXML
    private TextField inputTextField;

    @FXML
    private TextField outputTextField;

    @FXML
    private Button translateButton;

    public static String getToLanguageFromFile(String lang){
        try {
            Path path = Path.of("src\\main\\java\\com\\example\\dictionaryapplication\\languages.txt");
            List<String> inputLanguages = Files.readAllLines(path);
            for (String language : inputLanguages) {
                String[] split = language.split(": ");
                if(split[1].equals(lang)){
                    return split[0];
                }
            }
        } catch (IOException e) {
            System.out.println("Can not insert from file !");
        }
        return "vi";
    }

    public static String getFromLanguageFromFile(String lang){
        try {
            Path path = Path.of("src\\main\\java\\com\\example\\dictionaryapplication\\languages.txt");
            List<String> inputLanguages = Files.readAllLines(path);
            for (String language : inputLanguages) {
                String[] split = language.split(": ");
                if(split[1].equals(lang)){
                    return split[0];
                }
            }
        } catch (IOException e) {
            System.out.println("Can not insert from file !");
        }
        return "en";
    }
    private String input;
    private String output;
    @FXML
    private void handleTranslateButton(ActionEvent event) {
        String inputText = inputTextField.getText();
        input = inputText;
        String langFrom = from.getValue ();
        String langTo = to.getValue ();
        // Gọi hàm translate từ TranslatorApi
        String translatedText = TranslatorApi.translate(getFromLanguageFromFile (langFrom),getToLanguageFromFile (langTo),inputText);
        // Hiển thị kết quả trong outputTextField
        output = translatedText;
        outputTextField.setText(translatedText);

    }
@FXML
    private void speakLang(ActionEvent event){
        if (event.getSource() == button7){
        TranslatorApi.speak(input, from.getValue ());}
        if (event.getSource() == button8){
            TranslatorApi.speak(output, to.getValue ());}
    }
    /*
     * Search
     */
    @FXML
    private TextArea result = new TextArea();

    @FXML
    private TextField searcher = new TextField();

    @FXML
    private ListView<String> suggestions = new ListView<>();

    @FXML
    Label searcherEmpty = new Label();

    @FXML
    public void clearListView() {
        if (searcher.getText().isEmpty()) {
            suggestions.getItems().clear();
        }
    }

    @FXML
    public void lookUp(ActionEvent event) throws IOException {
        if (searcher.getText().isEmpty()) {
            searcherEmpty.setText("Hãy nhập từ!");
            result.setText("");
        } else {
            Word lookupResult = dictionary.lookup(searcher.getText());
               result.setText (lookupResult.getWord_explain ());
        }
    }
    @FXML
    public void autoComplete() {
        try {
            clearListView();
            searcherEmpty.setText("");
            ObservableList<String> elements = FXCollections.observableArrayList(dictionary.search(searcher.getText()));
            suggestions.setItems(elements);
            suggestions.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                searcher.setText(new_val);
            });
            if (dictionary.search(searcher.getText()).get(0).equals("Word not found!")) {
                searcher.setText("Word not found!");
                suggestions.getItems().clear();
            }

        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        } catch (java.lang.IndexOutOfBoundsException e) {
            searcherEmpty.setText ("Word not found!");

        }
    }


    /*
     * Add word
     */
    @FXML
    private TextArea englishWord = new TextArea();
    @FXML
    private TextArea vietnameseWord = new TextArea();
    @FXML
    private TextArea typeWord = new TextArea();
    @FXML
    Alert notice = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    public void saveNotice() {
        notice.setTitle("Add a new word");
        notice.setHeaderText("Your word '" + englishWord.getText() + "' is added successfully");

        Optional<ButtonType> result = notice.showAndWait();
    }

    @FXML
    public void addNewWord() {
        try {
            String englishWordText = englishWord.getText();
            String vietnameseWordText = vietnameseWord.getText();
            String typeWordText = typeWord.getText();

            // Validate input
            if (englishWordText.isEmpty() || vietnameseWordText.isEmpty() || typeWordText.isEmpty()) {
                showAlert("Please enter values for all fields.");
                return;
            }

            dictionary.addWord(englishWordText, vietnameseWordText, typeWordText);
            saveNotice();
        } catch (IOException e) {
            showAlert("An error occurred while adding a new word.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /*
     * Delete Word
     */
    @FXML
    private TextArea englishDeleteWord = new TextArea();

    @FXML
    Alert noticeDelete = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    public void saveDeleteNotice() {
        noticeDelete.setTitle("Delete new word");
        noticeDelete.setHeaderText("Your word '" + englishDeleteWord.getText() + "' is deleted successfully");

        Optional<ButtonType> result = noticeDelete.showAndWait();
    }

    @FXML
    public void deleteWord() throws IOException {
        String englishDeleteWordText = englishDeleteWord.getText();

        // Validate input
        if (englishDeleteWordText.isEmpty() ) {
            showDeleteAlert ("Please enter values for all fields.");
            return;
        }

        dictionary.removeWord ("C:\\Users\\FPT\\DictionaryApplication\\src\\main\\java\\com\\example\\dictionaryapplication\\hello.txt", englishDeleteWordText);

        saveDeleteNotice();
    }

    private void showDeleteAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
