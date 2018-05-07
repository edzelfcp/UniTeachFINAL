package com.example.samsung.UniTeach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class TutorApplyActivity extends AppCompatActivity {

    private Toolbar setupToolbar;

    private CircleImageView tutorImage;
    private EditText applyName;
    private EditText applyMajor;
    private EditText applyUniName;
    private EditText applyUniEmail;
    private EditText applySubject;

    private Button applyBtn;

    private Uri mainImageURI = null;

    //private boolean isChanged = false;

    private ProgressBar applyProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String user_id;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_apply);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        //add toolbar
        setupToolbar = findViewById(R.id.new_tutor_toolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Apply Tutor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tutorImage = findViewById(R.id.profile_image);
        applyName = findViewById(R.id.new_tutor_name);
        applyMajor = findViewById(R.id.new_tutor_major);
        applyUniName = findViewById(R.id.new_tutor_university);
        applyUniEmail = findViewById(R.id.new_tutor_email);
        applySubject = findViewById(R.id.new_tutor_subjects);

        applyBtn = findViewById(R.id.apply_btn);
        applyProgress = findViewById(R.id.tutor_progress);

        //applyProgress.setVisibility(View.VISIBLE);
        //applyBtn.setEnabled(false);

        //redo code
        tutorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(600, 600)
                        .setAspectRatio(2, 2)
                        .start(TutorApplyActivity.this);
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String tutorName = applyName.getText().toString();
                final String tutorMajor = applyMajor.getText().toString();
                final String tutorUni = applyUniName.getText().toString();
                final String tutorMail = applyUniEmail.getText().toString();
                final String tutorSubjects = applySubject.getText().toString();

                //Map<String, String> userMap = new HashMap<>();
                if ((!TextUtils.isEmpty(tutorName) && (!TextUtils.isEmpty(tutorMajor) && (!TextUtils.isEmpty(tutorUni) && (!TextUtils.isEmpty(tutorMail)
                        && (!TextUtils.isEmpty(tutorSubjects)) && mainImageURI != null))))) {

                    applyProgress.setVisibility(View.VISIBLE);
                    final String randomName = UUID.randomUUID().toString();

                    //Upload photo
                    File newTutorFile = new File(mainImageURI.getPath());
                    try {

                        compressedImageFile = new Compressor(TutorApplyActivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newTutorFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] tutorData = baos.toByteArray();

                    //Upload photo
                    UploadTask filePath = storageReference.child("tutors_images").child(randomName + ".jpg").putBytes(tutorData);

                    filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri = task.getResult().getDownloadUrl().toString();

                            if (task.isSuccessful()) {

                                File newImageFile = new File(mainImageURI.getPath());

                                try {
                                    compressedImageFile = new Compressor(TutorApplyActivity.this)
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

                                UploadTask uploadTask = storageReference.child("tutor_image/thumbs")
                                        .child(randomName + ".jpg").putBytes(thumbData);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                        Map<String, Object> tutorMap = new HashMap<>();
                                        tutorMap.put("image_url", downloadUri);
                                        tutorMap.put("image_thumb", downloadthumbUri);

                                        tutorMap.put("tutorName", tutorName);
                                        tutorMap.put("tutorMajor", tutorMajor);
                                        tutorMap.put("tutorSubjects", tutorSubjects);
                                        tutorMap.put("tutorMail", tutorMail);
                                        tutorMap.put("tutorUni", tutorUni);

                                        tutorMap.put("user_id", user_id);
                                        tutorMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Tutors").add(tutorMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if (task.isSuccessful()) {

                                                    Toast.makeText(TutorApplyActivity.this, "Tutor Added", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(TutorApplyActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();


                                                } else {

                                                }

                                                applyProgress.setVisibility(View.INVISIBLE);
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
                                applyProgress.setVisibility(View.INVISIBLE);
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

                mainImageURI = result.getUri();
                tutorImage.setImageURI(mainImageURI);

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




