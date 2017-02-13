package com.example.harpreet.chitchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText etChatroom;
    ListView lvChatroom;
    Button btaddroom;
    ArrayAdapter<String> ap;
    String name;
    private ArrayList<String> list_of_rooms=new ArrayList<>();

    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etChatroom= (EditText) findViewById(R.id.editTextCHATROOM);
        lvChatroom= (ListView) findViewById(R.id.listViewCHATROOM);
        btaddroom= (Button) findViewById(R.id.buttonADDROOM);
        ap=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        lvChatroom.setAdapter(ap);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo()==null)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("No Internet Connection");
                builder.setMessage("You need an internet connection to run this application");

                builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        System.exit(0);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
                builder.show();


                Toast.makeText(getApplicationContext(),"You need an internet connection to run this application",Toast.LENGTH_LONG).show();

            }

           else {
                    requestUserName();
                }

        btaddroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String,Object> map=new HashMap<String, Object>();
                map.put(etChatroom.getText().toString(),"");
                root.updateChildren(map);


            //hides the soft keyboard

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                etChatroom.setText("");

            }
        });


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set =new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();

                while(i.hasNext())
                    {
                        set.add(((DataSnapshot)i.next()).getKey());
                    }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                ap.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lvChatroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(),Chat_room.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("username",name);
                startActivity(intent);
            }
        });
    }



    private void requestUserName() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Enter Name:");
        final EditText inputfield=new EditText(this);
        builder.setView(inputfield);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name=inputfield.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                requestUserName();
            }
        });
        builder.show();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
