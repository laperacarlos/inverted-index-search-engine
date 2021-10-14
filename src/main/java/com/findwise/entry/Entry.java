package com.findwise.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
public class Entry implements IndexEntry {

    private String id;
    private double score;
    @JsonIgnore
    private final double tfValue;

    public Entry(String id, double tfValue) {
        this.id = id;
        this.tfValue = tfValue;
    }
}
