package com.example.inclassapril3rd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView userInput;
    TextView displayText;
    private ArrayList<String> textList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference userRef = database.getReference("test");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userRef.setValue("Hello, World");

        displayText = (TextView) findViewById(R.id.display_text);
        userInput = (EditText) findViewById(R.id.user_input);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    userRef = database.getReference(user.getUid());
                    userRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                         //textList.add(dataSnapshot.getValue(Object.class));
                          //displayText();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                            //Toast.makeText(MainActivity.this,dataSnapshot.getValue(Object.class) + " has changed",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        //Toast.makeText(MainActivity.this,dataSnapshot.getValue(Object.class)+" is removed",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot,@Nullable String s) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                }
        };
        auth.addAuthStateListener(authListener);
    }

    public void send(View view) {

        String input = userInput.getText().toString();
        userRef.child("userInput").push().setValue(input);
        String text = "";
        for (String s : textList) text += s + "\n";
        displayText.setText(input);

    }

    public void logOut(View view){
        auth.signOut();
        textList.clear();
        displayText.setText("");
        userInput.setText("");
        Toast.makeText(MainActivity.this,"Logged out successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authListener !=null){
            auth.removeAuthStateListener(authListener);
        }
    }
}

