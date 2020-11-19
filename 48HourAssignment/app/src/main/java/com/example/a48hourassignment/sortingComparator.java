package com.example.a48hourassignment;

import java.util.Comparator;

public class sortingComparator implements Comparator<Entry>
{
    public int compare(Entry a, Entry b) {
        return b.getDate().compareTo(a.getDate());
    }
}
