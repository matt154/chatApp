package com.example.chatapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

enum OSType {
    ANDROID,
    IOS
}

public class Message {
    private final String senderName;
    private final String content;
    private final OSType senderOSType;
    private final String time;

    Message(String name, String message){
        this.senderName = name;
        this.content = message;
        this.time = Calendar.getInstance().getTime().toString();
        this.senderOSType = OSType.ANDROID;
    }

    Message(Map<String, String> messageDict){
        this.senderName = messageDict.get("senderName");
        this.content = messageDict.get("content");
        this.time = messageDict.get("time");
        this.senderOSType = OSType.valueOf(messageDict.get("senderOSType"));
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getTime() {
        return time;
    }

    public OSType getSenderOSType() {
        return senderOSType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", senderOSType='" + senderOSType + '\'' +
                ", time=" + time +
                '}';
    }

    public Map<String, String> toDict(){
        Map messageMap = new HashMap();
        messageMap.put("senderName", senderName);
        messageMap.put("content", content);
        messageMap.put("senderOSType", senderOSType.name());
        messageMap.put("time", time);
        return messageMap;
    }
}
