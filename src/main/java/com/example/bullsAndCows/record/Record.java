package com.example.bullsAndCows.record;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Record implements Comparable<Record> {
    private String username;
    private Long averageAttempts;

    @Override
    public int compareTo(Record record) {
        return this.averageAttempts.compareTo((record.averageAttempts));
    }
}
