package com.example.samsung.UniTeach;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class TutorFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView tutor_list_view;
    private List<TutorPost> tutor_list;

    private FloatingActionButton addNewTutor;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TutorRecyclerAdapter tutorRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean FirstPageFirstLoad = true;


    public TutorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TutorFragment newInstance(String param1, String param2) {
        TutorFragment fragment = new TutorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor, container, false);

        addNewTutor = view.findViewById(R.id.add_new_tutor);
        addNewTutor.setOnClickListener(this);

        tutor_list = new ArrayList<>();
        tutor_list_view = view.findViewById(R.id.tutor_list_view);

        tutorRecyclerAdapter = new TutorRecyclerAdapter(tutor_list);


        tutor_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        tutor_list_view.setAdapter(tutorRecyclerAdapter);

        tutor_list_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            tutor_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {

                        loadMorePost();

                    }
                }
            });

            Query firstQuery = firebaseFirestore.collection("Tutors").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (FirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            tutor_list.clear();
                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String tutorPostId = doc.getDocument().getId();
                                TutorPost tutorPost = doc.getDocument().toObject(TutorPost.class).withId(tutorPostId);

                                if (FirstPageFirstLoad) {

                                    tutor_list.add(tutorPost);

                                } else {

                                    tutor_list.add(0, tutorPost);
                                }

                                tutorRecyclerAdapter.notifyDataSetChanged();
                            }
                        }

                        FirstPageFirstLoad = false;
                    }
                }

            });
        }

        return view;
    }


    public void loadMorePost() {

        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Tutors")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String tutorPostId = doc.getDocument().getId();
                                TutorPost tutorPost = doc.getDocument().toObject(TutorPost.class).withId(tutorPostId);
                                tutor_list.add(tutorPost);

                                tutorRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v == addNewTutor) {
            Intent newPostIntent = new Intent(getActivity(), TutorApplyActivity.class);
            startActivity(newPostIntent);

        } else {
            Toast.makeText(getActivity(), "Error ", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
