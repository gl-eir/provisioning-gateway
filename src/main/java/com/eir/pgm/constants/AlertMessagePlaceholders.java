package com.eir.pgm.constants;

public enum AlertMessagePlaceholders {
    TID("<TID>"), QUEUE_SIZE("<QUEUE_SIZE>"), REASON_CODE("<REASON_CODE>"), URL("<URL>"), EXCEPTION("<EXCEPTION>");

    String placeholder;

    AlertMessagePlaceholders(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }
}
