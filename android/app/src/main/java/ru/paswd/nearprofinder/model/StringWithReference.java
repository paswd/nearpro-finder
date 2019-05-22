package ru.paswd.nearprofinder.model;

public class StringWithReference {
    public String string;
    public int tag;
    public int reference;

    public StringWithReference(int tag, int reference, String string) {
        this.string = string;
        this.tag = tag;
        this.reference = reference;
    }

    @Override
    public String toString() {
        return string;
    }
}
