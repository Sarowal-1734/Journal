package com.example.journal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class PostJournalActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    private ProgressBar progressBar;
    private ImageView imageView;
    private EditText titleET, thoughtsET;
    private TextView currentUserTV;

    private String currentUserName;

    // Authentication
    private FirebaseAuth firebaseAuth;

    // Connection to fireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        progressBar = findViewById(R.id.postJournalProgressBar);
        titleET = findViewById(R.id.titleET);
        thoughtsET = findViewById(R.id.thoughtsET);
        imageView = findViewById(R.id.imageView);
        currentUserTV = findViewById(R.id.currentUserTV);

        firebaseAuth = FirebaseAuth.getInstance();

        // get username from fireStore and set to the textView
        db.collection("Users").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currentUserName = documentSnapshot.getString("Username");
                        currentUserTV.setText(currentUserName);
                    }
                });

    }

    public void onPostButtonClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String title = titleET.getText().toString().trim();
        String thoughts = thoughtsET.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri!=null){
            // Store image to Firebase Storage
            StorageReference filepath = storageReference.child("journal_images").child(firebaseAuth.getUid()+new Timestamp(new Date()));
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            Journal journal = new Journal();
                                            journal.setTitle(title);
                                            journal.setThought(thoughts);
                                            journal.setImageUrl(imageUrl);
                                            journal.setTimeAdded(new Timestamp(new Date()));
                                            journal.setUserId(firebaseAuth.getUid());
                                            journal.setUsername(currentUserName);
                                            // Creating documents randomly for every user
                                            db.collection("Journal").add(journal)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            startActivity(new Intent(PostJournalActivity.this, JournalListActivity.class));
                                                            finish();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(PostJournalActivity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(this, "Every field must required", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onAddPhotoButtonClick(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    // onAddPhotoButtonClick
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if (data != null){
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }

}