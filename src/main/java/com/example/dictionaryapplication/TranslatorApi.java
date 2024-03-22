package com.example.dictionaryapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslatorApi {


    /**
     * Translate text from langFrom to langTo.
     *
     * <p><a
     * href="https://stackoverflow.com/questions/8147284/how-to-use-google-translate-api-in-my-java-application">Reference</a>
     *
     * @param langFrom the input language (2 letters (ex: 'en'))
     * @param langTo   the output language (2 letters (ex: 'vi'))
     * @param text     the text to be translated
     * @return the translation text in langTo
     */


    public static String translate (String langFrom, String langTo, String text) {
        try {
            String urlStr = "https://script.google.com/macros/s/AKfycbzmOz2akZRcxkjp8aOA9AdAxiVPvHJYFsXPke8dPVoI_G4gIT7bMwjq4z-eQnnLx_UuQA/exec"
                    + "?q="
                    + URLEncoder.encode (text, StandardCharsets.UTF_8)
                    + "&target="
                    + langTo
                    + "&source="
                    + langFrom;

            URL url = new URL (urlStr);
            StringBuilder response = new StringBuilder ();
            HttpURLConnection con = (HttpURLConnection) url.openConnection ();
            con.setRequestProperty ("User-Agent", "Mozilla/5.0");

            try (BufferedReader in = new BufferedReader (
                    new InputStreamReader (con.getInputStream (), StandardCharsets.UTF_8))) {

                String inputLine;
                while ((inputLine = in.readLine ()) != null) {
                    response.append (inputLine);
                }
            }

            return response.toString ();

        } catch (IOException e) {
            // Xử lý ngoại lệ IOException ở đây
            e.printStackTrace (); // Hoặc xử lý theo cách khác tùy vào yêu cầu của ứng dụng
            return "Translation failed due to an error.";
        }
    }


    public static String langFx(String lang) {
        try {
            Path path = Path.of("C:\\Users\\FPT\\DictionaryApplication\\src\\main\\java\\com\\example\\dictionaryapplication\\lang.txt");
            List<String> inputLanguages = Files.readAllLines(path);
            for (String language : inputLanguages) {
                String[] split = language.split(":");
                if (split.length == 2 && split[1].trim().equalsIgnoreCase(lang.trim())) {
                    return split[0].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return "en-us";
    }

    public static void speak(String word, String lang) {
        try {
            // Mã hóa từ hoặc cụm từ để chúng phù hợp với URL
            String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());

            String mediaUrl = "https://api.voicerss.org/?key=af7b7b09dd7548ceb92055d7becf7fdb&hl=" + langFx(lang) + "&src=" + encodedWord;
            Media sound = new Media(mediaUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



