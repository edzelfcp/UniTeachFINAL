package com.example.samsung.UniTeach;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorApplyActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private Button applyBtn;
    private EditText applyName;
    private EditText applyMajor;
    private EditText applyUniName;
    private EditText applyUniEmail;
    private EditText applySubject;

    private ProgressBar applyProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_apply);

        //add toolbar
        Toolbar setupToolbar = findViewById(R.id.new_tutor_toolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Apply Tutor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        //users can start storing files onto Database -> Tutor Apply -> profile picture

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setupImage = findViewById(R.id.profile_image);

        applyName = findViewById(R.id.new_tutor_name);
        applyMajor = findViewById(R.id.new_tutor_major);
        applyUniName = findViewById(R.id.new_tutor_university);
        applyUniEmail = findViewById(R.id.new_tutor_email);
        applySubject = findViewById(R.id.new_tutor_subjects);
        applyBtn = findViewById(R.id.apply_btn);

        applyProgress = findViewById(R.id.tutor_progress);
        applyProgress.setVisibility(View.VISIBLE);

        applyBtn.setEnabled(false);

        firebaseFirestore.collection("Tutors").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        String applyName = task.getResult().getString("name");
                        String applyMajor = task.getResult().getString("major");
                        String applyUniName = task.getResult().getString("university_name");
                        String applyUniEmail = task.getResult().getString("university_email");
                        String applySubject = task.getResult().getString("subjects");
                        String image = task.getResult().getString("image");

                        mainImageURI = Uri.parse(image);

                        RequestOptions placeholderRequest = new RequestOptions();

                        //look into this error here
                        placeholderRequest.placeholder(R.drawable.web_hi_res_512);
                        Glide.with(TutorApplyActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(TutorApplyActivity.this, "FIRESTORE Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                }

                applyProgress.setVisibility(View.INVISIBLE);
                applyBtn.setEnabled(true);

            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = applyName.getText().toString();
                final String usermajor = applyMajor.getText().toString();
                final String useruni = applyUniName.getText().toString();
                final String usermail = applyUniEmail.getText().toString();
                final String usersubject = applySubject.getText().toString();

                //Map<String, String> userMap = new HashMap<>();
                if ((!TextUtils.isEmpty(username) && (!TextUtils.isEmpty(usermajor) && (!TextUtils.isEmpty(useruni) && (!TextUtils.isEmpty(usermail)
                        && (!TextUtils.isEmpty(usersubject)) && mainImageURI != null))))){

                    if (isChanged) {

                        user_id = firebaseAuth.getCurrentUser().getUid();
                        String major = firebaseAuth.getCurrentUser().getUid();
                        String university_name = firebaseAuth.getCurrentUser().getUid();
                        String university_email = firebaseAuth.getCurrentUser().getUid();
                        String subjects = firebaseAuth.getCurrentUser().getUid();

                        StorageReference image_path = storageReference.child("tutor_profile_images").child(user_id + ".jpg").child(username);
                        image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {

                                    storeFirestore(task, username, usermajor, useruni, usermail, usersubject);

                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(TutorApplyActivity.this, "IMAGE Error : " + error, Toast.LENGTH_LONG).show();

                                    applyProgress.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
                    } else {

                        storeFirestore(null, username, usermajor, useruni, usermail, usersubject);
                    }
                }
            }
        });

                /*File newImageFile = new File(mainImageURI.getPath());
                try {

                    compressedImageFile = new Compressor(TutorApplyActivity.this)
                            .setMaxHeight(125)
                            .setMaxWidth(125)
                            .setQuality(50)
                            .compressToBitmap(newImageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                UploadTask image_path = storageReference.child("tutor_profile_images").child(user_id + ".jpg").putBytes(thumbData);

                image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            storeFirestore(task, username, usermajor, useruni, usermail, usersubject);

                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(TutorApplyActivity.this, "IMAGE Error : " + error, Toast.LENGTH_LONG).show();

                            //setupProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });*/

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(TutorApplyActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(TutorApplyActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(TutorApplyActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }
            }
        });
    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String username, String usermajor, String useruni, String usermail, String usersubject) {

        Uri download_uri;

        if (task != null) {

            download_uri = task.getResult().getDownloadUrl();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("tutor name", username);
        userMap.put("major", usermajor);
        userMap.put("uni name", useruni);
        userMap.put("user mail", usermail);
        userMap.put("subjects", usersubject);
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Tutors").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(TutorApplyActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(TutorApplyActivity.this, TutorFragment.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(TutorApplyActivity.this, "FIRESTORE Error : " + error, Toast.LENGTH_LONG).show();

                }

                applyProgress.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void BringImagePicker() {
        //send users to crop activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(TutorApplyActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);
                isChanged = true;

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



        /*firebaseFirestore = FirebaseFirestore.getInstance();

        applyName = findViewById(R.id.new_tutor_name);
        applyMajor = findViewById(R.id.new_tutor_major);
        applyUniName = findViewById(R.id.new_tutor_university);
        applyUniEmail = findViewById(R.id.new_tutor_email);
        applySubject = findViewById(R.id.new_tutor_subjects);
        applyBtn = findViewById(R.id.apply_btn);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = applyName.getText().toString();
                String usermajor = applyMajor.getText().toString();
                String useruni = applyUniName.getText().toString();
                String usermail = applyUniEmail.getText().toString();
                String usersubject = applySubject.getText().toString();

                Map<String, String> userMap = new HashMap<>();

                userMap.put("tutor name", username);
                userMap.put("major", usermajor);
                userMap.put("uni name", useruni);
                userMap.put("user mail", usermail);
                userMap.put("subjects", usersubject);

                firebaseFirestore.collection("Tutors").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(TutorApplyActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                });

                        /*.addOnSuccessListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();

                        Toast.makeText(TutorApplyActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                    }
                });*/

