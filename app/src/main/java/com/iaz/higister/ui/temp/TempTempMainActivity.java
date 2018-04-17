package com.iaz.higister.ui.temp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.iaz.higister.R;
import com.iaz.higister.data.SyncService;
import com.iaz.higister.data.model.Ribot;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.search.SearchActivity;
import com.iaz.higister.util.DialogFactory;

public class TempTempMainActivity extends BaseActivity implements TempMainMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.TempTempMainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    TempMainPresenter mTempMainPresenter;
    @Inject RibotsAdapter mRibotsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.button)
    Button button;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, TempTempMainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mRibotsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTempMainPresenter.attachView(this);
        mTempMainPresenter.loadRibots();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempTempMainActivity.this, SearchActivity.class);
                TempTempMainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTempMainPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showRibots(List<Ribot> ribots) {
        mRibotsAdapter.setRibots(ribots);
        mRibotsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_ribots))
                .show();
    }

    @Override
    public void showRibotsEmpty() {
        mRibotsAdapter.setRibots(Collections.<Ribot>emptyList());
        mRibotsAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_ribots, Toast.LENGTH_LONG).show();
    }

}
