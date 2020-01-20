package com.niek125.datasetservice.handler;

import com.jayway.jsonpath.DocumentContext;
import com.niek125.datasetservice.events.RowDeletedEvent;
import com.niek125.datasetservice.file.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RowDeletedMethodHandler extends HandlerMethod<RowDeletedEvent> {
    private final Logger logger = LoggerFactory.getLogger(RowDeletedMethodHandler.class);
    private final FileHandler fileHandler;

    @Autowired
    public RowDeletedMethodHandler(FileHandler fileHandler) {
        super(RowDeletedEvent.class);
        this.fileHandler = fileHandler;
    }

    @Override
    public void handle(RowDeletedEvent event) {
        try {
            logger.info("getting json");
            final DocumentContext json = fileHandler.getJSON(event.getProjectid());
            logger.info("deleting row");
            json.delete("$.items[" + event.getRownumber() + "]");
            logger.info("writing file");
            fileHandler.overwriteFile(json, event.getProjectid());
        } catch (IOException e) {
            logger.error("could not find file: {}", e.getMessage());
        }
    }
}