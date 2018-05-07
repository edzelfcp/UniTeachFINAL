package com.example.samsung.UniTeach;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorRecyclerAdapter extends RecyclerView.Adapter<TutorRecyclerAdapter.ViewHolder> {

    public List<TutorPost> tutor_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public String tutName;
    public String image_url;
    public String tutMajor;
    public String tutUni;
    public String tutEmail;

    public String tutVerified;

    public String tutSubs;

    public TutorRecyclerAdapter(List<TutorPost> tutor_list, List<User> user_list) {
        this.tutor_list = tutor_list;
        this.user_list = user_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_list_item, parent, false);
        context = parent.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new TutorRecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String tutorPostId = tutor_list.get(position).TutorPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        tutVerified = tutor_list.get(position).getTutorVerified();
        holder.setTutorVerified(tutVerified);

        tutName = tutor_list.get(position).getTutorName();
        holder.setTutorName(tutName);

        image_url = tutor_list.get(position).getImage_url();
        String thumbUri = tutor_list.get(position).getImage_thumb();
        holder.setTutorImage(image_url, thumbUri);

        tutMajor = tutor_list.get(position).getTutorMajor();
        holder.setTutorMajor(tutMajor);

        tutUni = tutor_list.get(position).getTutorUni();
        holder.setTutorUni(tutUni);

        tutEmail = tutor_list.get(position).getTutorMail();
        holder.setTutorMail(tutEmail);

        tutSubs = tutor_list.get(position).getTutorSubjects();
        holder.setTutorSubjects(tutSubs);

        //String user_id = tutor_list.get(position).getUser_id();

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent clickTutor = new Intent(context, clickTutorActivity.class);

                clickTutor.putExtra("tutorName", tutName);
                clickTutor.putExtra("image_url", image_url);
                clickTutor.putExtra("tutorMajor", tutMajor);
                clickTutor.putExtra("tutorUni",tutUni);
                clickTutor.putExtra("tutorMail", tutEmail);
                clickTutor.putExtra("tutorSubjects", tutSubs);
                clickTutor.putExtra("tutorVerified", tutVerified);

                context.startActivity(clickTutor);

            }
        });

        //Get  Tutor Approval Count
        firebaseFirestore.collection("Tutors/" + tutorPostId + "/Approvals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int count = documentSnapshots.size();
                    holder.updateApprovalCount(count);
                }
                else{
                    holder.updateApprovalCount(0);
                }
            }
        });

        //Get Tutor Approval
        firebaseFirestore.collection("Tutors/" + tutorPostId + "/Approvals").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){

                    holder.tutorApprovalBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_blue));

                } else {

                    holder.tutorApprovalBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));

                }

            }
        });

        holder.tutorApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Tutors/" + tutorPostId + "/Approvals").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){

                            Map<String, Object> approvalsMap = new HashMap<>();
                            approvalsMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Tutors/" + tutorPostId + "/Approvals").document(currentUserId).set(approvalsMap);

                        } else {

                            firebaseFirestore.collection("Tutors/" + tutorPostId + "/Approvals").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        //Get  Tutor Disapproval Count
        firebaseFirestore.collection("Tutors/" + tutorPostId + "/Disapprovals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int count = documentSnapshots.size();
                    holder.updateDisapprovalCount(count);
                }
                else{
                    holder.updateDisapprovalCount(0);
                }
            }
        });

        //Get Tutor Disapproval
        firebaseFirestore.collection("Tutors/" + tutorPostId + "/Disapprovals").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){

                    holder.tutorDisapprovalBtn.setImageDrawable(context.getDrawable(R.drawable.action_dislike_red));

                } else {

                    holder.tutorDisapprovalBtn.setImageDrawable(context.getDrawable(R.drawable.action_dislike_grey));

                }

            }
        });

        holder.tutorDisapprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Tutors/" + tutorPostId + "/Disapprovals").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){

                            Map<String, Object> disapprovalsMap = new HashMap<>();
                            disapprovalsMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Tutors/" + tutorPostId + "/Disapprovals").document(currentUserId).set(disapprovalsMap);

                        } else {

                            firebaseFirestore.collection("Tutors/" + tutorPostId + "/Disapprovals").document(currentUserId).delete();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {

        return tutor_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private CircleImageView tutorImageView;
        private TextView tutorUserName;

        private TextView tutorUniMajor;
        private TextView tutorUniUniversity;
        private TextView tutorUniEmail;

        private TextView tutorApprovalCount;
        private ImageView tutorApprovalBtn;

        private TextView tutorDisapprovalCount;
        private ImageView tutorDisapprovalBtn;

        private TextView tutorUniVerified;

        private TextView tutorUniSubs;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            tutorApprovalBtn = mView.findViewById(R.id.tutor_approval_btn);
            tutorDisapprovalBtn = mView.findViewById(R.id.tutor_disapproval_btn);
        }

        public void setTutorName(String tutorName) {

            tutorUserName = mView.findViewById(R.id.tutorName);
            tutorUserName.setText(tutorName);
        }

        public void setTutorImage(String downloadUri, String thumbUri){

            tutorImageView = mView.findViewById(R.id.tutorImage);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.web_hi_res_512);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(tutorImageView);
        }

        public void setTutorMajor(String tutorMajor) {

            tutorUniMajor = mView.findViewById(R.id.tutorMajor);
            tutorUniMajor.setText(tutorMajor);
        }

        public void setTutorUni(String tutorUni) {

            tutorUniUniversity = mView.findViewById(R.id.tutorUni);
            tutorUniUniversity.setText(tutorUni);

        }

        public void setTutorMail(String tutorMail) {

            tutorUniEmail = mView.findViewById(R.id.tutorEmail);
            tutorUniEmail.setText(tutorMail);
        }

        public void setTutorSubjects(String tutorSubjects) {

            tutorUniSubs = mView.findViewById(R.id.tutorSub);
            tutorUniSubs.setText(tutorSubjects);
        }

        public void setTutorVerified(String tutorVerified) {

            tutorUniVerified = mView.findViewById(R.id.tutorVerified);
            tutorUniVerified.setText(tutorVerified);
        }

        @SuppressLint("SetTextI18n")
        public void updateApprovalCount(int count){

            tutorApprovalCount = mView.findViewById(R.id.tutor_approval_count);
            tutorApprovalCount.setText(count + " Approved");
        }

        @SuppressLint("SetTextI18n")
        public void updateDisapprovalCount(int count){

            tutorDisapprovalCount = mView.findViewById(R.id.tutor_disapproval_count);
            tutorDisapprovalCount.setText(count + " Disapproved");
        }


    }

}
