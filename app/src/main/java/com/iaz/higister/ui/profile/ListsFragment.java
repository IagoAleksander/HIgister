package com.iaz.higister.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iaz.higister.R;
import com.iaz.higister.data.model.Profile;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.viewList.ListItemAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 05/03/2018.
 */

public class ListsFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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

        ListItemAdapter mListItemAdapter = new ListItemAdapter(getActivity(), new ArrayList<>());

        mRecyclerView.setAdapter(mListItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}