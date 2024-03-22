package com.example.dictionaryapplication;

import java.util.ArrayList;

public class Trie {
    Utility constants = new Utility();
    private trieNode root;
    private int trieSize;
    public Trie() {
        trieSize = 0;
        trieNode node = new trieNode();
        root = node;
    }

    public void trieTraversal() {
        traversal(root);
    }

    public int getTrieSize() {
        return trieSize;
    }

    public void insertTrieNode(String s) throws NullPointerException{ // hàm này để thêm 1 từ vào trie
        try {
            trieNode copy = new trieNode();
            copy = root;
            Utility constants = new Utility();
            for (int i = 0; i < s.length(); i++) {
                if (s.substring(i, i + 1).equals(" ") && s.substring(i + 1, i + 2).equals("/")) {
                    break;
                }
                if (copy.next.get(constants.charToInterger.get(s.substring(i, i + 1))) == null) {
                    trieNode nextNode = new trieNode();
                    nextNode.data = constants.charToInterger.get(s.substring(i, i + 1));
                    copy.next.set(constants.charToInterger.get(s.substring(i, i + 1)), nextNode);
                    copy = nextNode;
                } else {
                    copy = copy.next.get(constants.charToInterger.get(s.substring(i, i + 1)));
                }
            }
            copy.isEndOfWord = true;
            copy.keyword = s.substring(0, s.indexOf(" /"));
            trieSize++;
        } catch (NullPointerException ex) {
            System.out.println(s);
        }
    }

    public ArrayList<String> search(String S) { //hàm này để tìm 1 từ trong Trie.
        ArrayList<String> result = new ArrayList<String>();
        trieNode copy = new trieNode();
        copy = root;
        for (int i = 0; i < S.length(); i++) {
            String tmp = S.substring(i,i+1);
            copy = copy.next.get(constants.charToInterger.get(tmp));
            if (copy == null) {
                return result;
            }
        }
        searchTraversal(copy, result);
        return result;
    }

    private void searchTraversal(trieNode node, ArrayList<String> resultList) { //hàm này để duyệt cây trie
        if (node == null) {
            return;
        }
        if (node.isEndOfWord) {
            resultList.add(node.keyword);
        }
        for (int i = 0; i < node.next.size(); i++) {
            searchTraversal(node.next.get(i), resultList);
        }
    }
    private void traversal(trieNode node) { //hàm này cũng duyệt nma kiểu khác =)))
        if (node == null) {
            return;
        }
        if (node.isEndOfWord) {
            System.out.println(node.keyword);
        }
        for (int i = 0; i < node.next.size(); i++) {
            traversal(node.next.get(i));
        }
    }
}
