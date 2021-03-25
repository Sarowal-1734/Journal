package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class JournalListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    // Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    // FireStore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Journal> journalList;
    JournalAdapter journalAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        recyclerView = findViewById(R.id.recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        journalList = new ArrayList<>();

        // get data from fireStore and set to the recyclerView
        db.collection("Journal")//.whereEqualTo("userId", firebaseAuth.getUid())  //to get journalList of current user
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Journal journal = documentSnapshot.toObject(Journal.class);
                            journalList.add(journal);
                        }
                        journalAdapter = new JournalAdapter(JournalListActivity.this, journalList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JournalListActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(journalAdapter);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_post:
                if (currentUser != null && firebaseAuth != null){
                    startActivity(new Intent(JournalListActivity.this, PostJournalActivity.class));
                }
                else {
                    startActivity(new Intent(JournalListActivity.this, LoginActivity.class));
                }
                return true;
            case R.id.action_signout:
                if (currentUser != null && firebaseAuth != null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(JournalListActivity.this, LoginActivity.class));
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}