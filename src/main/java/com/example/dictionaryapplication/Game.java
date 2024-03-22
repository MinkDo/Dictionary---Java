package com.example.dictionaryapplication;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.File;
import java.net.URI;

public class Game implements Initializable {
    public static final String pathDataGame = "src/main/resources/com/example/dictionaryapplication/dataGame/words.txt";
    public static final int DEFAULT_LIFE = 6;
    int scoreCount;
    @FXML
    Label scoreLabel;
    @FXML
    Text describeText;
    @FXML
    ImageView presentationImage;
    int lifeRemain;
    @FXML
    Text guessingText;
    @FXML
    Button buttonA;
    String targetWord, originalWord, guessingWord;
    @FXML
    ImageView okImageView;
    List<String> words = new ArrayList<>();
    @FXML
    ImageView lifeImageView1;
    @FXML
    ImageView lifeImageView2;
    @FXML
    ImageView lifeImageView3;
    Image fullHeartImage, halfHeartImage = null;
    List<Button> clickedButtons = new ArrayList<>();

    public static String spreadWord_getWord(String s) {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                break;
            }
        }
        return s.substring(0, i - 1);
    }

    public static String spreadWord_getDescribe(String s) {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                break;
            }
        }
        return s.substring(i + 1, s.length());
    }

    public static String replaceCharAt(String originalString, int index, String replacementChar) {
        Character tmpChar = replacementChar.charAt(0);
        if (index >= 0 && index < originalString.length()) {
            StringBuilder stringBuilder = new StringBuilder(originalString);
            stringBuilder.setCharAt(index, tmpChar);
            return stringBuilder.toString();
        } else {
            return originalString;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scoreCount = 0;
        lifeRefill();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathDataGame));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        reloadWord(scoreCount);
        okImageView.setVisible(false);
        guessingText.setFill(Color.BLACK);
        guessingText.setFont(Font.font("Fira Code Light", FontWeight.LIGHT, 50));
        try {
            fullHeartImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/dictionaryapplication/dataGame/heart.png")));
            halfHeartImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/dictionaryapplication/dataGame/half_heart.png")));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (fullHeartImage != null && halfHeartImage != null) {
            System.out.println("PASS");
        }

    }

    public void lifeRefill() {
        lifeRemain = DEFAULT_LIFE;
    }

    public void printWords() {
        for (String word :
                words) {
            System.out.println("Word: " + spreadWord_getWord(word));
            System.out.println("Describe: " + spreadWord_getDescribe(word));
        }
    }

    public void reloadWord(int index) {
        lifeRefill();
        String pathImage = "src/main/resources/com/example/dictionaryapplication/dataGame/"
                + Game.spreadWord_getWord(words.get(index)) + ".jpg";
        try {
            File file = new File(pathImage);
            URI uri = file.toURI();
            presentationImage.setImage(new Image(uri.toString()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        describeText.setText(Game.spreadWord_getDescribe(words.get(index)));
        targetWord = Game.spreadWord_getWord(words.get(index));
        guessingWord = targetWord.substring(0, 1);
        for (int i = 1; i < targetWord.length(); i++) {
            guessingWord = guessingWord + "_";
        }
        guessingText.setText(guessingWord);
        originalWord = targetWord;
        targetWord = targetWord.replaceAll(String.valueOf(targetWord.charAt(0)), "");

    }

    public List<Integer> researchCharInWord(String originalWord, String searchWord) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < originalWord.length(); i++) {
            if (originalWord.charAt(i) == searchWord.charAt(0)) {
                res.add(i);
            }
        }
        return res;
    }

    public void clickOnButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        //Avoid button have clicked
        if (!clickedButton.isDisabled() && lifeRemain > 0) {
            clickedButtons.add(clickedButton);
            String charClicked = clickedButton.getText().toLowerCase();
            targetWord = targetWord.replaceAll(charClicked, "");
            //System.out.println(charClicked + " " + targetWord);
            clickedButton.setDisable(true);
            List<Integer> tmpList = researchCharInWord(originalWord, charClicked);
            for (Integer i : tmpList) {
                System.out.println(i);
                guessingWord = replaceCharAt(guessingWord, i, charClicked);
                guessingText.setText(guessingWord);
            }
            if (tmpList.isEmpty()) {
                lifeRemain--;
                updateImageLife();
            }


            //Increase point and roll
            if (targetWord.isEmpty()) {
                guessingText.setFill(Color.DARKGREEN);
                guessingText.setFont(Font.font("Fira Code Light", FontWeight.BOLD, 50));
                okImageView.setVisible(true);
            }
            if (lifeRemain == 0 && !targetWord.isEmpty()) {
                lose();
            }
        }
    }

    public void updateLevel() {
        lifeRefill();
        updateImageLife();
        guessingText.setFill(Color.BLACK);
        guessingText.setFont(Font.font("Fira Code Light", FontWeight.LIGHT, 50));
        okImageView.setVisible(false);

        for (Button tmpButton :
                clickedButtons) {
            tmpButton.setDisable(false);
        }
        scoreCount++;
        reloadWord(scoreCount);
        scoreLabel.setText("Score: " + scoreCount + "/16");

        if (scoreCount == 16) win();

    }

    public void win() {
        //update soon
        guessingText.setText("100/100");
        guessingText.setFill(Color.YELLOW);
        guessingText.setFont(Font.font("Fira Code Light", FontWeight.BOLD, 50));
        okImageView.setVisible(false);
    }

    public void lose() {
        //update soon
        guessingText.setText(originalWord);
        guessingText.setFill(Color.RED);
        guessingText.setFont(Font.font("Fira Code Light", FontWeight.BOLD, 50));
        okImageView.setVisible(false);
    }

    //Quay lại màn hình chính
    public void returnBack(Event event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DictionaryApplication.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    //Lười quá r
    public void updateImageLife() {
        if (lifeRemain == 0) {
            lifeImageView3.setVisible(false);
            lifeImageView2.setVisible(false);
            lifeImageView1.setVisible(false);
        }
        if (lifeRemain == 1) {
            lifeImageView3.setImage(halfHeartImage);
            lifeImageView2.setVisible(false);
            lifeImageView1.setVisible(false);
        }
        if (lifeRemain == 2) {
            lifeImageView3.setImage(fullHeartImage);
            lifeImageView2.setVisible(false);
            lifeImageView1.setVisible(false);
        }
        if (lifeRemain == 3) {
            lifeImageView3.setImage(fullHeartImage);
            lifeImageView2.setImage(halfHeartImage);
            lifeImageView1.setVisible(false);
        }
        if (lifeRemain == 4) {
            lifeImageView3.setImage(fullHeartImage);
            lifeImageView2.setImage(fullHeartImage);
            lifeImageView1.setVisible(false);
        }
        if (lifeRemain == 5) {
            lifeImageView3.setImage(fullHeartImage);
            lifeImageView2.setImage(fullHeartImage);
            lifeImageView1.setImage(halfHeartImage);
        }
        if (lifeRemain == DEFAULT_LIFE) {
            lifeImageView3.setVisible(true);
            lifeImageView2.setVisible(true);
            lifeImageView1.setVisible(true);
            lifeImageView3.setImage(fullHeartImage);
            lifeImageView2.setImage(fullHeartImage);
            lifeImageView1.setImage(fullHeartImage);
        }
    }

}
