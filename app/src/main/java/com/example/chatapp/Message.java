package com.example.chatapp;

import java.util.Calendar;
import java.util.Date;

enum OsType{
    ANDROID,
    IOS
}

public class Message {
    private final String name;
    private final String message;
    private final Date time;
    private final OsType osType;

    Message(String name, String message){
        this.name = name;
        this.message = message;
        this.time = Calendar.getInstance().getTime();
        this.osType = OsType.ANDROID;
    }
    Message(){
        this.name = "";
        this.message = "";
        this.time = Calendar.getInstance().getTime();
        this.osType = OsType.ANDROID;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public Date getTime() {
        return time;
    }

    public OsType getOsType() {
        return osType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", osType=" + osType +
                '}';
    }
}
