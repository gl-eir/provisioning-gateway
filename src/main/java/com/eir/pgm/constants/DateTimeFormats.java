package com.eir.pgm.constants;

import java.time.format.DateTimeFormatter;

public interface DateTimeFormats {

    DateTimeFormatter edrFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter URL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

}
