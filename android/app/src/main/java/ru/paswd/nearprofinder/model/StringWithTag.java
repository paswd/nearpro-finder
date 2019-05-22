package ru.paswd.nearprofinder.model;

public class StringWithTag {
    public String string;
    public int tag;

    public StringWithTag(int tag, String string) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }
}
