package com.example.dictionaryapplication;

import java.util.ArrayList;

public class trieNode {
    public int data;
    public ArrayList<trieNode> next;
    boolean isEndOfWord;

    String keyword;

    public trieNode() {
        this.data = 0;
        this.keyword = null;
        this.next = new ArrayList<trieNode>();
        for (int i = 0; i <= 45; i++) {
            this.next.add(null);
            this.isEndOfWord = false;
        }
    }
}

// đây là Trie, một cấu trúc dữ liệu để lưu các xâu. muốn hiểu thêm thì hỏi Đức Nguyễn