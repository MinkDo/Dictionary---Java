package com.example.dictionaryapplication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {
    private static HashMap<String, Word> wordList;
    private Trie searchTree;

    static Utility constants = new Utility();

    public void displayWord(Word word) { //hàm để show từ
        System.out.format("%-5s | %-15s | %-15s\n",
                1,
                word.getWord_target(),
                word.getWord_explain());
    }

    public static Word lookup (String keyword) { //hàm tìm kiếm từ chính xác, giả dụ tìm từ air thì ra mỗi air thôi. O(1).
        if (wordList.containsKey(keyword)) {
            return wordList.get(keyword);
        }
        return constants.error1;
    }

    public void addWord(String word_target, String word_explain, String word_type) throws IOException{
        if (!word_target.contains("/")) {
            word_target += " /";
        }
        String tmp = word_type + "\n\t" + word_explain;
        Word newWord = new Word(word_target, tmp);
        addWord(newWord);
        searchTree.insertTrieNode(word_target);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\FPT\\DictionaryApplication\\src\\main\\java\\com\\example\\dictionaryapplication\\hello.txt", true));

            writer.write("@" + word_target);
            writer.newLine();
            writer.write("*  " + word_type);
            writer.newLine();
            writer.write("- " + word_explain);
            writer.newLine();
            writer.close();
            System.out.println("Added!");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void removeWord(String filePath, String deletedWordTarget) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        // Xóa từ cần xóa khỏi danh sách
        boolean foundTarget = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("@" + deletedWordTarget)) {
                foundTarget = true;
                lines.remove(i);  // Xóa dòng chứa từ cần xóa
                // Xóa các dòng liên quan đến từ đó
                while (i < lines.size() && !lines.get(i).startsWith("@")) {
                    lines.remove(i);
                }
                break;
            }
        }

        if (!foundTarget) {
            System.out.println("Word not found!");
            return; // Từ cần xóa không tồn tại
        }

        // Ghi lại toàn bộ danh sách vào file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }

        System.out.println("Deleted!");
    }

    public ArrayList<String> search(String keyword) { //hàm này để kiếm từ, độ phức tạp cây tìm kiếm của Trie là O(m) với m là độ dài từ cần tìm
        ArrayList<String> result;
        result = searchTree.search(keyword);
        return result;
    }
    public Dictionary() { //hàm khởi tạo
        this.wordList = new HashMap<String, Word>();
        searchTree = new Trie();
    }

    public void addWord(Word word) { //hàm để thêm 1 từ vào wordlist, cần sửa 1 chút vì bây giờ đang dùng để đọc file, cần 1 hàm từ console.
//        System.out.println(word.getWord_target());
        String key = word.getWord_target().substring(0, word.getWord_target().indexOf(" /")); //1 cái word target sẽ bao gồm cả cách phát âm, nên phải cắt phần từ ra để làm key
        wordList.put(key, word);
    }

    public void clear() {
        wordList.clear();
    } // hàm xóa hết phần tử trong wordlist

    public int getSize() {
        return wordList.size();
    } //hàm trả về size của wordlist

    public void searchTreeTraversal() {
        searchTree.trieTraversal();
    } //hàm để duyệt cây Trie, dùng để debug
    public void readFromFile(String link) throws IOException { //hàm để đọc file
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src/main/java/com/example/dictionaryapplication/" + link);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String Line = bufferedReader.readLine();
            String wordTarget = "";
            String wordExplain = "";
            while (Line != null) {
                if (Line.length() == 0) {
                    final Word word = new Word(wordTarget, wordExplain);
                    this.addWord(word);
                    searchTree.insertTrieNode(wordTarget);
                    wordTarget = "";
                    wordExplain = "";
                }
                if (Line.startsWith("@")) {
                    wordTarget = Line.substring(1, Line.length());
                }
                if (Line.startsWith("*")) {
                    wordExplain += "\n" + "\t * " + Line.substring(3, Line.length());
                }
                if (Line.startsWith("-")) {
                    wordExplain += "\n\t" +  "\t + " + Line.substring(2, Line.length());
                }
                if (Line.startsWith("!")) {
                    wordExplain += "\n\t" + "\t(Idiom: )" + Line.substring(1, Line.length());
                }
                if (Line.startsWith("=")) {
                    Line = Line.replace("+", ":");
                    wordExplain += "\n\t" + "\t\t- " + Line.substring(1, Line.length());
                }
                Line = bufferedReader.readLine();
            }
            if (wordTarget != "" || wordExplain != ""){
                Word tmp = new Word (wordTarget, wordExplain);
                searchTree.insertTrieNode (wordTarget);
                this.addWord (tmp);
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Something went wrong...");
        } catch (IOException ex) {
            throw new IOException("Some thing went wrong...");
        } catch (StringIndexOutOfBoundsException ex){
            System.out.println(ex);
        } finally {
            try {
                bufferedReader.close();
                fileInputStream.close();
                System.out.println("Done!, added " + wordList.size() + " words into hashmap");
                System.out.println("Added " + searchTree.getTrieSize() + " words into trie");

            } catch (IOException ex) {
                throw new IOException("Something went wrong...");
            }
        }
    }
    public void insertFromFile() throws IOException {
        readFromFile ("hello.txt");
    }
    public Word getWord(String s) {
        return wordList.get(s);
    } // hàm để tra từ trong hashmap wordlist


}