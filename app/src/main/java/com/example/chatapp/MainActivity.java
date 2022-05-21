package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private Button sendButton;
    private Messages messages;
    private TextView errorHandler;
    private LinearLayout mainLayout;
    private Set<String> currMessages;
    private Switch powerSwitch;
    private ScrollView scrollView;
    private ValueEventListener databaseMessagesListener;
    private ValueEventListener databasePowerListener;
    private Boolean turnedOff;
    // TODO: add messages view list
    // TODO: add power button to chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        sendButton = findViewById(R.id.button);
        currMessages = new HashSet<String>();
        errorHandler = (TextView) findViewById(R.id.errorHandler);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        powerSwitch = findViewById(R.id.chatPower);
        scrollView = (ScrollView)findViewById(R.id.ScrollView);
        turnedOff = false;

        addDatabasePowerListener();
        addSwitchListener();

    }

    public void addSwitchListener() {
        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addDataBaseMessagesListener();
                    sendButton.setEnabled(true);
                    addButtonListener();
                    databaseRef.child("power").setValue(true);
                    if (turnedOff){
                        databaseRef.child(getString(R.string.databaseRoot)).removeValue();
                    }
                } else {
                    removeDataBaseMessagesListener();
                    sendButton.setEnabled(false);
                    removeButtonListener();
                    databaseRef.child("power").setValue(false);
                    turnedOff = true;
                }
            }
        });
    }


    public void addDataBaseMessagesListener() {
        databaseMessagesListener = databaseRef.child(getString(R.string.databaseRoot)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "start");
                messages = new Messages(dataSnapshot.getChildren());
                for (Map.Entry<String, Message> entry : messages.getMessages().entrySet()) {
                    if (!currMessages.contains(entry.getKey())) {
                        addMessage(entry.getValue(), entry.getKey());
                        currMessages.add(entry.getKey());
                    }
                }
                Set<String> removedMessages = new HashSet<String>();
                final Set<String> keys = messages.getMessages().keySet();
                for (String msg_id : currMessages) {
                    if (!keys.contains(msg_id)) {
                        removedMessages.add(msg_id);
                    }
                }

                for (String id : removedMessages) {
                    mainLayout.removeView(mainLayout.findViewWithTag(id));
                }
                // TODO: delete and move to view
                Log.d("messages", messages.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorHandler.setText("Server have some troubles. Please Try again later.");
            }
        });

    }

    public void addDatabasePowerListener(){
        databasePowerListener =  databaseRef.child("power").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                powerSwitch.setChecked((boolean)snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorHandler.setText("Server have some troubles. Please Try again later.");
            }
        });
    }

    public void removeDataBaseMessagesListener() {
        databaseRef.removeEventListener(databaseMessagesListener);
        databaseRef.removeEventListener(databasePowerListener);

    }

    public void addMessage(Message msg, String id) {
        View messagesView = getLayoutInflater().inflate(R.layout.message, null);

        TextView sender = (TextView) messagesView.findViewById(R.id.sender);
        sender.setText(msg.getSenderName());
        TextView messageText = (TextView) messagesView.findViewById(R.id.messageText);
        messageText.setText(msg.getContent());
        TextView time = (TextView) messagesView.findViewById(R.id.time);
        time.setText(msg.getTime().toString());
        TextView osType = (TextView) messagesView.findViewById(R.id.osType);
        osType.setText(msg.getSenderOSType().toString());
        messagesView.setTag(id);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mainLayout.getLayoutParams());
        layoutParams.setMargins(0, 0, 0, 5);

        mainLayout.addView(messagesView, layoutParams);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }, 200);
    }

    public void addButtonListener() {

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
                final String messageContent = ((EditText) findViewById(R.id.editTextMessage)).getText().toString();
                if (name.isEmpty()) {
                    errorHandler.setText("Name filed is empty");
                    return;
                }
                if (messageContent.isEmpty()) {
                    errorHandler.setText("Message filed is empty");
                    return;
                }
                int msgId = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
                Message message = new Message(name, messageContent);
                String messageID = databaseRef.child(getString(R.string.databaseRoot)).push().getKey();
                databaseRef.child(getString(R.string.databaseRoot)).child(messageID).setValue(message.toDict());
                databaseRef.push();

                // TODO: delete
                Log.d("name", name);
                Log.d("message", messageContent);
                Log.d("id", UUID.randomUUID().toString());
            }
        });
    }

    public void removeButtonListener(){
        sendButton.setOnClickListener(null);
    }
}