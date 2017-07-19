package com.koxa;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.koxa.model.ProcessItem;

/**
 * Created by Denys on 2017-04-29.
 */
public class Main {

    public static void main(String[] args) {
        ProcessItem processItem = new ProcessItem();
        processItem.setFilePath("C:\\Users\\Denys\\IdeaProjects\\review\\src\\main\\resources\\Reviews.csv");
        processItem.setWithTranslation(false);

        ActorSystem system = ActorSystem.create("review");
        ActorRef processor = system.actorOf(Props.create(Processor.class, processItem), "processor");
        processor.tell(processItem, ActorRef.noSender());
    }
}
