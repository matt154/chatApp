package com.example.chatapp;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private final Map<String, Message> messages = new HashMap<>();

    Messages(Iterable<DataSnapshot> serverMessages){
        for (DataSnapshot serverMessage : serverMessages) {
            Message msg = serverMessage.getValue(Message.class);

            if ( msg != null && !msg.getName().isEmpty() && !msg.getMessage().isEmpty()){
                this.messages.put(serverMessage.getKey(), new Message(msg.getName(), msg.getMessage()));
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
