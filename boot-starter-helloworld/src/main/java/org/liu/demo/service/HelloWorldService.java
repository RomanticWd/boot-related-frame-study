package org.liu.demo.service;

public class HelloWorldService {

    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String sayHello() {
        return "hello, " + word;
    }

}
