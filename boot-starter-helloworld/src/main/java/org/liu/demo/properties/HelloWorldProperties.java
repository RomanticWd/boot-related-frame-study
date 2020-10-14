package org.liu.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hello.world")
public class HelloWorldProperties {

    public static final String DEFAULT_WORDS = "world";

    private String word = DEFAULT_WORDS;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
