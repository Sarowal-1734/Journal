package com.example.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    // Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    // FireStore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private EditText usernameET, emailET, passwordET;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameET = findViewById(R.id.usernameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        progressBar = findViewById(R.id.create_accountPB);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onCreateAccountButtonClick(View view) {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String userName = usernameET.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userName)){
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                String currentUserId = currentUser.getUid();

                                // Save username to fireStore database
                                Map<String, String> user = new HashMap<>();
                                user.put("UserId", currentUserId);
                                user.put("Username", userName);
                                collectionReference.document(currentUserId).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SignUpActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this, JournalListActivity.class));
                                            }
                                        });
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUpActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}