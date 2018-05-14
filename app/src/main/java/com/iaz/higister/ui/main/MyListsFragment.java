package com.iaz.higister.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.util.SpacesItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 05/03/2018.
 */

public class MyListsFragment extends Fragment implements MyListsMvpView {

    @Inject
    MyListsPresenter mListsPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.lists_header_text)
    TextView listsHeaderText;

    MainActivity activity;

    public MyListsAdapter mListAdapter;
    public PeopleAdapter mPeopleAdapter;
    String type;

    ListRepository listRepository = new ListRepository();
    UserRepository userRepository = new UserRepository();

    public static MyListsFragment newInstance(String type) {
        MyListsFragment myListsFragment = new MyListsFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        myListsFragment.setArguments(args);
        return myListsFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        activity = (MainActivity) getActivity();
        activity.activityComponent().inject(this);

        mListsPresenter.attachView(this);


        if (type.equals("created")) {
            listRepository.receiveListsOfUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), new ListRepository.OnUpdateLists() {
                @Override
                public void onSuccess(ArrayList<UserList> userLists) {
                    updateDataLists(userLists);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("receiveMyLists: ", "failed", e);
                }
            });

            listsHeaderText.setText("My created lists:");
        } else if (type.equals("favorited")) {
            listRepository.receiveFavoritesOfUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), new ListRepository.OnUpdateLists() {
                @Override
                public void onSuccess(ArrayList<UserList> userLists) {

                    activity.favoritedListsId.clear();

                    for (UserList list : userLists) {
                        if (!activity.favoritedListsId.contains(list.uid))
                            activity.favoritedListsId.add(list.uid);
                    }

                    updateDataLists(userLists);

                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("receiveMyLists: ", "failed", e);
                }
            }, "favorited");

            listsHeaderText.setText("My favorited lists:");
        } else if (type.equals("feed")) {
            listRepository.receiveFeed(new ListRepository.OnUpdateLists() {
                @Override
                public void onSuccess(ArrayList<UserList> userLists) {
                    updateDataLists(userLists);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("receiveMyLists: ", "failed", e);
                }
            });

            listsHeaderText.setText("My feed:");
        } else {
            listsHeaderText.setText("Search Lists:");
        }

    }

    @Override
    public MyListsFragment getFragment() {
        return this;
    }

    @Override
    public void updateDataLists(ArrayList<UserList> lists) {

        if (mListAdapter == null) {
            mListAdapter = new MyListsAdapter(getFragment(), lists, type);
        } else {
            mListAdapter.setLists(lists);
            mListAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25,
                getResources().getDisplayMetrics());
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(space));
        mRecyclerView.setAdapter(mListAdapter);
    }

    public void updateDataPeople(ArrayList<User> peopleList) {

        if (mPeopleAdapter == null) {
            mPeopleAdapter = new PeopleAdapter(this, peopleList);
        } else {
            mPeopleAdapter.setPeople(peopleList);
            mPeopleAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(mPeopleAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}