package com.okworo.cards.enums;

import lombok.Getter;

/**
 * @author Morris.Okworo on 26/08/2023
 */
public enum CardStatus {

    TO_DO("To Do"), IN_PROGRESS("In Progress"), DONE("Done");


    @Getter
    private String status;

    CardStatus(String status) {
        this.status = status;
    }

    public static CardStatus fromStatus(String status) {
        for (CardStatus state : CardStatus.values()) {
            if (state.getStatus().equalsIgnoreCase(status)) {
                return state;
            }
        }
        throw new IllegalArgumentException("No enum constant with status: " + status);
    }
}
