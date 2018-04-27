package com.example.samsung.UniTeach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewBookPostActivity extends AppCompatActivity {

    private Toolbar newBookToolbar;

    private ImageView newBookImage;
    private EditText newBookDesc;
    private EditText newBookPrice;
    private EditText newBookEdition;

    private Button newBookBtn;

    private Uri bookImageUri = null;

    private ProgressBar newBookProgress;

    private StorageReference storageReference;
    private  FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newBookToolbar = findViewById(R.id.new_book_toolbar);
        setSupportActionBar(newBookToolbar);
        getSupportActionBar().setTitle("Add New Book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newBookImage = findViewById(R.id.new_book_image);
        newBookDesc = findViewById(R.id.new_book_name);
        newBookEdition = findViewById(R.id.new_book_edition);
        newBookPrice = findViewById(R.id.new_book_price);

        newBookBtn = findViewById(R.id.book_post_btn);
        newBookProgress = findViewById(R.id.new_book_progress);

        newBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(600, 600)
                        .setAspectRatio(2, 2)
                        .start(NewBookPostActivity.this);
            }
        });

        newBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String description = newBookDesc.getText().toString();
                final String edition = newBookEdition.getText().toString();
                final String price = newBookPrice.getText().toString();

                if (!TextUtils.isEmpty(description) && !TextUtils.isEmpty(edition) && !TextUtils.isEmpty(price)
                        && bookImageUri != null) {

                    newBookProgress.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    //Upload photo
                    File newImageFile = new File(bookImageUri.getPath());
                    try {

                        compressedImageFile = new Compressor(NewBookPostActivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    //Upload photo
                    UploadTask filePath = storageReference.child("book_images").child(randomName + ".jpg").putBytes(imageData);

                    filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri = task.getResult().getDownloadUrl().toString();

                            if (task.isSuccessful()) {

                                File newImageFile = new File(bookImageUri.getPath());

                                try {
                                    compressedImageFile = new Compressor(NewBookPostActivity.this)
                                            .setMaxHeight(150)
                                            .setMaxWidth(150)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbData = baos.toByteArray();

                                UploadTask uploadTask = storageReference.child("book_image/thumbs")
                                        .child(randomName + ".jpg").putBytes(thumbData);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                        //change specifications
                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put("image_url", downloadUri);
                                        postMap.put("image_thumb", downloadthumbUri);

                                        postMap.put("description", description);
                                        postMap.put("edition", edition);
                                        postMap.put("price", price);

                                        postMap.put("user_id", current_user_id);
                                        postMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Books").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if (task.isSuccessful()) {

                                                    Toast.makeText(NewBookPostActivity.this, "Book Added", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(NewBookPostActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();


                                                } else {


                                                }

                                                newBookProgress.setVisibility(View.INVISIBLE);
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        //error handling
                                    }
                                });


                            } else {

                                newBookProgress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                bookImageUri = result.getUri();
                newBookImage.setImageURI(bookImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ends activity
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
