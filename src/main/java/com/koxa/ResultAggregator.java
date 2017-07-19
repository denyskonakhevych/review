package com.koxa;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.Broadcast;
import akka.routing.RoundRobinPool;
import com.koxa.analyzer.ActiveUsersAnalyzer;
import com.koxa.analyzer.CommentedItemsAnalyzer;
import com.koxa.analyzer.FrequentlyUsedWordsAnalyzer;
import com.koxa.model.*;
import com.koxa.translate.GoogleTranslateGateway;
import com.koxa.translate.Translator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Denys on 2017-05-02.
 */
public class ResultAggregator extends AbstractActor {

    private Map<String, Integer> activeUsers = new HashMap<>();
    private Map<String, Integer> commentedItems = new HashMap<>();
    private Map<String, Integer> words = new HashMap<>();

    private ActorRef activeUsersAnalyzer;
    private ActorRef commentedItemsAnalyzer;
    private ActorRef frequentlyUsedWordsAnalyzer;
    private ActorRef translator;

    private ActorRef onEndListener;
    private boolean withTranslation;

    private AtomicInteger activeUsersLeft;
    private AtomicInteger commentedItemsLeft;
    private AtomicInteger wordsLeft;
    private AtomicInteger translationsLeft;

    private boolean activeUsersComplete;
    private boolean commentedItemsComplete;
    private boolean wordsComplete;
    private boolean translationComplete;
    private boolean resultComplete;

    public ResultAggregator(ActorRef onEndListener, boolean withTranslation) {
        this.onEndListener = onEndListener;
        this.withTranslation = withTranslation;
        int cores = Runtime.getRuntime().availableProcessors();

        activeUsersAnalyzer = getContext().actorOf(new RoundRobinPool(cores).props(Props.create(ActiveUsersAnalyzer.class)), "activeUsersAnalyzer");
        commentedItemsAnalyzer = getContext().actorOf(new RoundRobinPool(cores).props(Props.create(CommentedItemsAnalyzer.class)), "commentedItemsAnalyzer");
        frequentlyUsedWordsAnalyzer = getContext().actorOf(new RoundRobinPool(cores).props(Props.create(FrequentlyUsedWordsAnalyzer.class)), "frequentlyUsedWordsAnalyzer");
        translator = getContext().actorOf(new RoundRobinPool(100).props(Props.create(Translator.class, new GoogleTranslateGateway())), "translator");

        activeUsersLeft = new AtomicInteger(cores);
        commentedItemsLeft = new AtomicInteger(cores);
        wordsLeft = new AtomicInteger(cores);
        translationsLeft = new AtomicInteger(100);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Events.PROCESSING_CHANGED, it -> checkResultReady())
                .match(ReviewItem.class, this::processParsedItem)
                .matchEquals(Events.FILE_PARSED, event -> onFileParsed())
                .match(MostActiveUsers.class, mostActiveUsers -> mergeUsers(mostActiveUsers.getActiveUsers()))
                .match(MostCommentedItems.class, mostCommentedItems -> mergeItems(mostCommentedItems.getItems()))
                .match(MostFrequentWords.class, mostFrequentWords -> mergeWords(mostFrequentWords.getWords()))
                .matchEquals(Events.TRANSLATION_COMPLETE, event -> onTranslationComplete())
                .build();
    }

    private void onTranslationComplete() {
        if(translationsLeft.decrementAndGet() == 0) {
            translationComplete = true;
            getSelf().tell(Events.PROCESSING_CHANGED, ActorRef.noSender());
        }
    }

    private void checkResultReady() {
        if(isReady()) {
            resultComplete = true;
            onEndListener.tell(new Result(getTopUsers(), getTopItems(), getTopWords()), ActorRef.noSender());
        }
    }

    private boolean isReady() {
        return activeUsersComplete && commentedItemsComplete && wordsComplete
                && (!withTranslation || translationComplete) && !resultComplete;
    }

    private void mergeUsers(Map<String, Integer> users) {
        users.forEach((userToAdd, valueToAdd) -> mergeItem(activeUsers, userToAdd, valueToAdd));
        if(activeUsersLeft.decrementAndGet() == 0) {
            activeUsersComplete = true;
            getSelf().tell(Events.PROCESSING_CHANGED, ActorRef.noSender());
        }
    }

    private void mergeItems(Map<String, Integer> items) {
        items.forEach((itemToAdd, valueToAdd) -> mergeItem(commentedItems, itemToAdd, valueToAdd));
        if(commentedItemsLeft.decrementAndGet() == 0) {
            commentedItemsComplete = true;
            getSelf().tell(Events.PROCESSING_CHANGED, ActorRef.noSender());
        }
    }

    private void mergeWords(Map<String, Integer> items) {
        items.forEach((itemToAdd, valueToAdd) -> mergeItem(words, itemToAdd, valueToAdd));
        if(wordsLeft.decrementAndGet() == 0) {
            wordsComplete = true;
            getSelf().tell(Events.PROCESSING_CHANGED, ActorRef.noSender());
        }
    }

    private Integer mergeItem(Map<String, Integer> items, String userToAdd, Integer valueToAdd) {
        return items.compute(userToAdd, (key, value) -> value == null ? valueToAdd : value + valueToAdd);
    }

    private Map<String, Integer> getTopUsers() {
        return getTop(activeUsers);
    }

    private Map<String, Integer> getTopItems() {
        return getTop(commentedItems);
    }

    private Map<String, Integer> getTopWords() {
        return getTop(words);
    }

    private Map<String, Integer> getTop(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(1000)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void onFileParsed() {
        activeUsersAnalyzer.tell(new Broadcast(Events.NO_MORE_RECORDS), self());
        commentedItemsAnalyzer.tell(new Broadcast(Events.NO_MORE_RECORDS), self());
        frequentlyUsedWordsAnalyzer.tell(new Broadcast(Events.NO_MORE_RECORDS), self());
        if(withTranslation)
            translator.tell(new Broadcast(Events.NO_MORE_RECORDS), self());
    }

    private void processParsedItem(ReviewItem reviewItem) {
        activeUsersAnalyzer.tell(reviewItem, self());
        commentedItemsAnalyzer.tell(reviewItem, self());
        frequentlyUsedWordsAnalyzer.tell(reviewItem, self());
        if(withTranslation)
           translator.tell(new TranslateRequest(reviewItem), self());
    }
}
