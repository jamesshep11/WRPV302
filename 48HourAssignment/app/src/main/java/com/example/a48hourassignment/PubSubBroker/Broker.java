package com.example.a48hourassignment.PubSubBroker;

import java.util.*;

public class Broker {

    static private Broker instance = null;              // Singleton instance of the broker.
    private Map<String, Set<Subscriber>> subscribers;   // Collection of subscribers and which topics they're subscribed to

    private Broker() {
        subscribers = new HashMap<>();
    }
    static public Broker getInstance() {
        if(instance == null)
            instance = new Broker();

        return instance;
    }

    public void subscribe(String topic, Subscriber subscriber) {
        // Get subscribers for this topic. If none, create a new set.
        Set<Subscriber> subscriberSet = subscribers.computeIfAbsent(topic, key -> new HashSet<>());
        subscriberSet.add(subscriber);
    }

    public void unsubscribe(String topic, Subscriber subscriber) {
        // Get subscribers for this topic.
        Set<Subscriber> subscriberSet = subscribers.get(topic);

        // If no-one listening, stop.
        if(subscriberSet == null)
            return;

        // Remove from set.
        subscriberSet.remove(subscriber);

        // Empty set? If so, remove the set.
        if(subscriberSet.size() == 0)
            subscribers.remove(topic);
    }

    public void unsubscribe(Subscriber subscriber) {
        /*  Unsubscribing may remove topic from list (if topic has no more subscribers)
            Can't iterate through topics while editing topics
            Therefore, to avoid this, export topics list to new list */
        List<String> topics = new ArrayList<>();
        topics.addAll(subscribers.keySet());

        for (String topic : topics) {
            unsubscribe(topic, subscriber);
        }
    }

    public void publish(Object publisher, String topic, Map<String, Object> params) {
        Set<Subscriber> subscriberSet = subscribers.get(topic);

        // If no subscribers, then done
        if(subscriberSet == null)
            return;

        // Notify all subscribers of the publishing of the message.
        subscriberSet.forEach(subscriber -> subscriber.onPublished(publisher, topic, params));
    }
}
