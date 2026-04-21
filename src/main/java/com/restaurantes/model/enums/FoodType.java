package com.restaurantes.model.enums;

import lombok.Getter;

@Getter
public enum FoodType {
    SPANISH("Española"),
    JAPANESE("Japonesa"),
    MEXICAN("Mexicana");

    private final String label;

    FoodType(String label) {
        this.label = label;
    }
}
