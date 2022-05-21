package com.example.chatapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Messages {
    private final Map<String, Message> messages = new HashMap<>();

    Messages(Iterable<DataSnapshot> serverMessages){
        for (DataSnapshot serverMessage : serverMessages) {
            Map<String, String> msgMessageEntry = (Map<String, String>)serverMessage.getValue();
            if (msgMessageEntry != null){
                Message msg = new Message(msgMessageEntry);

                if (!msg.getSenderName().isEmpty() && !msg.getContent().isEmpty()){
                    this.messages.put(serverMessage.getKey(), new Message(msg.getSenderName(), msg.getContent()));
                }
            }
        }
    }

    Messages(){}

    public Map<String, Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messages=" + messages +
                '}';
    }
}
