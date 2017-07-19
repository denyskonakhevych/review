package com.koxa;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.koxa.model.Events;
import com.koxa.model.ReviewItem;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Denys on 2017-05-02.
 */
public class FileParser extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::parse)
                .build();
    }

    private void parse(String fileName) {
        int counter = 0;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.NULL_CHARACTER, 1)) {
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                counter++;
                if (counter % 50_000 == 0)
                    System.out.println("P: " + counter);
                getSender().tell(toReviewItem(row), self());
            }
            getSender().tell(Events.FILE_PARSED, ActorRef.noSender());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ReviewItem toReviewItem(String[] row) {
        try {
            return new ReviewItem(
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    Integer.parseInt(row[4]),
                    Integer.parseInt(row[5]),
                    Integer.parseInt(row[6]),
                    Long.parseLong(row[7]),
                    row[8],
                    row[9]
            );
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }
}
