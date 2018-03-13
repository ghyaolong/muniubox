package com.taoding.ocr.module;

/**
 * Created by Administrator on 2017/11/23.
 */
public class WordsResult implements Comparable<WordsResult>{
    private Probability probability;
    private Location location;
    private String words;

    public Probability getProbability() {
        return probability;
    }

    public void setProbability(Probability probability) {
        this.probability = probability;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int compareTo(WordsResult o) {
        Integer oldValue = o.getLocation().getTop();
        Integer newValue = this.getLocation().getTop();
        return newValue-oldValue;
    }
}
