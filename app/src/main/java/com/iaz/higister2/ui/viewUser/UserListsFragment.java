package com.iaz.higister2.ui.viewUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iaz.higister2.R;
import com.iaz.higister2.data.model.UserList;
import com.iaz.higister2.data.repository.ListRepository;
import com.iaz.higister2.data.repository.UserRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 05/03/2018.
 */

public class UserListsFragment extends Fragment implements UserListsMvpView {

    @Inject
    UserListsPresenter mListsPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.lists_header_text)
    TextView listsHeaderText;

    ViewUserActivity activity;

    public UserListsAdapter mListAdapter;
    String type;

    ListRepository listRepository = new ListRepository();
    UserRepository userRepository = new UserRepository();

    public static UserListsFragment newInstance(String type) {
        UserListsFragment userListsFragment = new UserListsFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        userListsFragment.setArguments(args);
        return userListsFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        activity = (ViewUserActivity) getActivity();
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

        activity = (ViewUserActivity) getActivity();
        activity.activityComponent().inject(this);

        mListsPresenter.attachView(this);


        if (type.equals("created")) {
            listRepository.receiveListsOfUser(activity.user.uid, new ListRepository.OnUpdateLists() {
                @Override
                public void onSuccess(ArrayList<UserList> userLists) {
                    updateDataLists(userLists);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("receiveMyLists: ", "failed", e);
                }
            });

            listsHeaderText.setVisibility(View.GONE);
        } else if (type.equals("favorited")) {
            listRepository.receiveFavoritesOfUser(activity.user.uid, new ListRepository.OnUpdateLists() {
                @Override
                public void onSuccess(ArrayList<UserList> userLists) {
                    updateDataLists(userLists);

                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("receiveMyLists: ", "failed", e);
                }
            }, "favorited");

            listsHeaderText.setVisibility(View.GONE);
        }

    }

    @Override
    public UserListsFragment getFragment() {
        return this;
    }

    @Override
    public void updateDataLists(ArrayList<UserList> lists) {

        if (mListAdapter == null) {
            mListAdapter = new UserListsAdapter(this, lists, type);
        } else {
            mListAdapter.setLists(lists);
            mListAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}