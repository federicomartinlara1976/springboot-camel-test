package com.example.springbootcamel.route;

import com.example.springbootcamel.service.FileProcessingService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileProcessingRoute extends RouteBuilder {

    private final FileProcessingService fileProcessingService;

    public FileProcessingRoute(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public void configure() {
        from("file:{{app.file.inbox}}?autoCreate=true&readLock=changed&move={{app.file.outbox}}&moveFailed={{app.file.error}}")
                .routeId("file-processing-route")
                .log("Fichero detectado: ${file:name}")
                .bean(fileProcessingService, "process")
                .log("${body}");
    }
}
