package com.iaz.higister.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.viewList.ListItemAdapter;

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

    ArrayList<UserList> lists;
    ProfileActivity activity;

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

        activity = (ProfileActivity) getActivity();
        activity.activityComponent().inject(this);

        mListsPresenter.attachView(this);

        mListsPresenter.receiveLists();
    }

    @Override
    public MyListsFragment getFragment() {
        return this;
    }

    @Override
    public void updateData(ArrayList<UserList> lists) {
        UserListsAdapter mListAdapter = new UserListsAdapter(getActivity(), lists);

        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}