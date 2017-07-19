package com.koxa;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import com.koxa.model.ProcessItem;
import com.koxa.model.Result;
import com.koxa.translate.GoogleTranslateGateway;
import com.koxa.translate.Translator;

/**
 * Created by Denys on 2017-05-02.
 */
public class Processor extends AbstractActor {

    private ProcessItem processItem;

    public Processor(ProcessItem processItem) {
        this.processItem = processItem;
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(ProcessItem.class, processItem -> startProcessing())
                .match(Result.class, this::finishProcessing)
                .build();
    }

    private void startProcessing() {
        System.out.println("Got: " + processItem.getFilePath());
        final ActorRef parser = getContext().actorOf(Props.create(FileParser.class), "parser");
        final ActorRef aggregator = getContext().actorOf(Props.create(ResultAggregator.class, self(), processItem.isWithTranslation()), "resultAggregator");
        parser.tell(processItem.getFilePath(), aggregator);
    }

    private void finishProcessing(Result result) {
        System.out.println(result.getActiveUsers());
        System.out.println(result.getCommentedItems());
        System.out.println(result.getWords());

        final ActorRef parser = getContext().actorOf(Props.create(FileParser.class), "parser1");
        final ActorRef translator = getContext().actorOf(new RoundRobinPool(100).props(Props.create(Translator.class, new GoogleTranslateGateway()).withMailbox("bounded-mailbox")), "translator1");
//        final ActorRef translator = getContext().actorOf(Props.create(Translator.class), "translator");
        parser.tell(processItem.getFilePath(), translator);
//        context().system().terminate();
    }
}
