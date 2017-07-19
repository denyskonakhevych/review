package com.koxa.analyzer;

import akka.actor.AbstractActor;
import com.koxa.model.Events;
import com.koxa.model.MostCommentedItems;
import com.koxa.model.MostFrequentWords;
import com.koxa.model.ReviewItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * Created by Denys on 2017-05-03.
 */
public class FrequentlyUsedWordsAnalyzer extends AbstractActor {

    private Map<String, Integer> words = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReviewItem.class, this::processItem)
                .matchEquals(Events.NO_MORE_RECORDS, it -> processResult())
                .build();
    }

    private void processResult() {
        getSender().tell(new MostFrequentWords(words), self());
    }

    private void processItem(ReviewItem reviewItem) {
        String[] words = reviewItem.getText().split("\\W+");
        Map<String, Integer> itemsToAdd = Arrays.stream(words).collect(groupingBy(Function.identity(), summingInt(e -> 1)));
        itemsToAdd.forEach((itemToAdd, valueToAdd) -> mergeItem(this.words, itemToAdd, valueToAdd));
    }

    private Integer mergeItem(Map<String, Integer> items, String userToAdd, Integer valueToAdd) {
        return items.compute(userToAdd, (key, value) -> value == null ? valueToAdd : value + valueToAdd);
    }
}
