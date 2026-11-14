package net.bounceme.chronos.springbootcamel.service;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.springframework.stereotype.Service;

@Service
public class FileProcessingService {

    public String process(String content, @Header(Exchange.FILE_NAME) String fileName) {
        int lines = content.isEmpty() ? 0 : content.split("\\R").length;
        return "Fichero %s procesado (%d lineas, %d caracteres)".formatted(fileName, lines, content.length());
    }
}
