package com.nensi.mirageislands.Islands;

public enum IslandSize {
    SMALL("&eМаленький"),
    MEDIUM("&6Средний"),
    BIG("&6Большой");

    private final String name;

    IslandSize(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
