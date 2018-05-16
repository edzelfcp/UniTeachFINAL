package com.example.samsung.UniTeach;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class BookSellingFragment2 extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton addNewBook;

    private RecyclerView book_list_view;
    private List<BookPost> book_list;
    private List<User> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BookRecyclerAdapter bookRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean FirstPageFirstLoad = true;

    public BookSellingFragment2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BookSellingFragment2 newInstance(String param1, String param2) {
        BookSellingFragment2 fragment = new BookSellingFragment2();
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
        View view = inflater.inflate(R.layout.fragment_book_selling, container, false);

        addNewBook = view.findViewById(R.id.add_new_book);
        addNewBook.setOnClickListener(this);

        book_list = new ArrayList<>();
        user_list = new ArrayList<>();

        book_list_view = view.findViewById(R.id.book_selling_grid_view);

        bookRecyclerAdapter = new BookRecyclerAdapter(book_list, user_list);

        book_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        book_list_view.setAdapter(bookRecyclerAdapter);

        book_list_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            book_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //System.out.println(dx);
                    //System.out.println(dy);
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {

                        loadMorePost();

                    }
                }
            });

            //add .limit(3);
            Query firstQuery = firebaseFirestore.collection("Books").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (FirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            book_list.clear();
                            user_list.clear();
                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String bookPostId = doc.getDocument().getId();
                                final BookPost blogPost = doc.getDocument().toObject(BookPost.class).withId(bookPostId);

                                String bookUserId = doc.getDocument().getString("user_id");
                                firebaseFirestore.collection("Users").document(bookUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){

                                            User user = task.getResult().toObject(User.class);
                                            if (FirstPageFirstLoad) {

                                                user_list.add(user);
                                                book_list.add(blogPost);

                                            } else {

                                                user_list.add(0, user);
                                                book_list.add(0, blogPost);
                                            }

                                            bookRecyclerAdapter.notifyDataSetChanged();
                                        }

                                    }
                                });

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

            Query nextQuery = firebaseFirestore.collection("Books")
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

                                String bookPostId = doc.getDocument().getId();
                                final BookPost bookPost = doc.getDocument().toObject(BookPost.class).withId(bookPostId);
                                String bookUserId = doc.getDocument().getString("user_id");

                                firebaseFirestore.collection("Books").document(bookUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){

                                            User user = task.getResult().toObject(User.class);

                                            user_list.add(user);
                                            book_list.add(bookPost);

                                            bookRecyclerAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });

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
        if (v == addNewBook) {
            Intent newPostIntent = new Intent(getActivity(), NewBookPostActivity.class);
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
