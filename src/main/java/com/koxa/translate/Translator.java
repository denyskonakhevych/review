package com.koxa.translate;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;
import com.koxa.model.Events;
import com.koxa.model.ReviewItem;
import com.koxa.model.TranslateRequest;

/**
 * Created by Denys on 2017-05-11.
 */
public class Translator extends AbstractActor implements RequiresMessageQueue<BoundedMessageQueueSemantics> {

    private GoogleTranslateGateway googleTranslateGateway;

    public Translator(GoogleTranslateGateway googleTranslateGateway) {
        this.googleTranslateGateway = googleTranslateGateway;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReviewItem.class, reviewItem -> processItem(new TranslateRequest(reviewItem)))
                .match(TranslateRequest.class, this::processItem)
                .matchEquals(Events.NO_MORE_RECORDS, it -> processResult())
                .build();
    }

    private void processResult() {
        getSender().tell(Events.TRANSLATION_COMPLETE, ActorRef.noSender());
    }

    private void processItem(TranslateRequest reviewItem) throws InterruptedException {
//        long before = Runtime.getRuntime().freeMemory();
//        TranslateRequest reviewItem1 = new TranslateRequest(reviewItem.getInputLanguage(), reviewItem.getOutputLanguage(), reviewItem.getText());
//        long after = Runtime.getRuntime().freeMemory();
//        after - before
        String translatedText = googleTranslateGateway.translate(reviewItem.getText());
        processTranslatedText(translatedText);
    }

    private void processTranslatedText(final String text) {

    }
}
