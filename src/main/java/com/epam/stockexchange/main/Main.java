package com.epam.stockexchange.main;

import com.epam.stockexchange.entity.Participant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String PARTICIPANTS_FILE_PATH = "src/main/resources/participants.json";

    public static void main(String[] args) {
        programExecution();
    }

    private static void programExecution() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Participant> participants = objectMapper.readValue(new File(PARTICIPANTS_FILE_PATH), new TypeReference<List<Participant>>() {
            });
            ExecutorService executorService = Executors.newFixedThreadPool(participants.size());
            participants.forEach(executorService::submit);
            while (true) {
                participants.forEach(System.out::println);
                System.out.println("\n\n\n");
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e);
        }
    }
}
