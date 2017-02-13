package com.example.harpreet.chitchat;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_room extends AppCompatActivity {
    private ImageButton sendButton;
    private TextView tv;
    private EditText et;
    MediaPlayer mp;
    private String username,roomname,temp_key;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendButton= (ImageButton) findViewById(R.id.imageButton);
        tv= (TextView) findViewById(R.id.textView2);
        et= (EditText) findViewById(R.id.editText);

        username= getIntent().getExtras().get("username").toString();
        roomname=getIntent().getExtras().get("room_name").toString();
        setTitle("Room:-"+roomname);
        root= FirebaseDatabase.getInstance().getReference().child(roomname);
         mp=MediaPlayer.create(this, R.raw.button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map=new HashMap<String, Object>();
                temp_key=root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messageroot=root.child(temp_key);
                Map<String,Object> map2=new HashMap<String, Object>();
                map2.put("name",username);
                map2.put("message",et.getText().toString());
                messageroot.updateChildren(map2);
                 mp.start();

                //hides the soft keys
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);




                    //Auto scroll down
                        final ScrollView sv = (ScrollView) findViewById(R.id.scrollView2);
                          sv.fullScroll(sv.FOCUS_DOWN);


            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chatmsg,chatusername;


    private void append_chat(DataSnapshot dataSnapshot) {

        Iterator i=dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            chatmsg=(String)((DataSnapshot)i.next()).getValue();
            chatusername=(String)((DataSnapshot)i.next()).getValue();
            tv.append(chatusername+" :- "+chatmsg +"\n");
            et.setText("");
        }

    }

}
