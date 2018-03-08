package com.alks_ander.higister.ui.search;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alks_ander.higister.R;
import com.alks_ander.higister.data.model.BaseItem;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment2 extends Fragment implements SearchMvpView{

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    SearchPresenter mSearchPresenter = new SearchPresenter();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    ResultsAdapter mResultsAdapter;
    SearchActivity2 activity;

    List<Object> items = new ArrayList<>();
    TestRecyclerViewAdapter adapter;

    public static RecyclerViewFragment2 newInstance() {
        return new RecyclerViewFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

//        final List<Object> items = new ArrayList<>();
//
        for (int i = 0; i < 1; ++i) {
            items.add(new Object());
        }

        float logicalDensity = Resources.getSystem().getDisplayMetrics().density;
        mSearchPresenter.attachView(this);
        //setup materialviewpager

//        if (GRID_LAYOUT) {
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        }
        mRecyclerView.setHasFixedSize(false);

        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();

//        if (mResultsAdapter == null) {
//            mResultsAdapter = new ResultsAdapter(getActivity(), new ArrayList<BaseItem>(), settings);
//            mRecyclerView.setAdapter(mResultsAdapter);
//
//        }

        adapter = new TestRecyclerViewAdapter(items);
        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(adapter);

        activity = (SearchActivity2) getActivity();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



//                Log.d ("height", ""+dx);
//                if (activity.textLayout.getVisibility() == View.VISIBLE) {
                    activity.textLayout.setVisibility(View.GONE);
                    activity.textView.setVisibility(View.VISIBLE);

//                    activity.canChange = false;
//                }
//                else {
//                    activity.canChange = true;
//                }
            }
        });

        activity.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSearchPresenter.loadResults("movie", activity.editText.getText().toString());
//                activity.mSearchPresenter.loadResults(activity.mViewPager.getViewPager().getCurrentItem(), activity.editText.getText().toString());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSearchPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showItems(final ArrayList<BaseItem> itens) {

//        counter = 0;
//        i = 0;
//        targets.clear();
//
//        for (final BaseItem item : itens) {
//
//
//            Timber.d("counter: " +Integer.toString(counter));
//            targets.add (new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
////                    Log.i(TAG, "The image was obtained correctly, now you can do your canvas operation!");
//                    counter++;
//                    Timber.d("counter: " +Integer.toString(counter));
//
//                    item.setBit(bitmap);
//
//                    if (counter == itens.size()) {
//                        Timber.d("sucesso");
//                        mResultsAdapter.setRibots(itens);
//                        mResultsAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
////                    Log.e(TAG, "The image was not obtained");
//                    counter++;
//                    Timber.d("counter: " +Integer.toString(counter));
//
//                    InputStream is = getResources().openRawResource(R.drawable.large_movie_poster);
//                    Bitmap pisc = BitmapFactory.decodeStream(new BufferedInputStream(is));
//
//                    item.setBit(pisc);
//
//                    if (counter == itens.size()) {
//                        Timber.d("sucesso");
//                        mResultsAdapter.setRibots(itens);
//                        mResultsAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
////                    Log.(TAG, "Getting ready to get the image");
//                    //Here you should place a loading gif in the ImageView to
//                    //while image is being obtained.
//                }
//            });
//
//            item.setBackgroundColor(getBackgroundColor(i));
//
//            if (item instanceof Result) {
//                Picasso.with(this).load(((Result) item).getImage_url()).into(targets.get(i++));
//            }
//            else if (item instanceof Results) {
//                Picasso.with(this).load(((Results) item).getImage().getSmall_url()).into(targets.get(i++));
//            }
//            else if (item instanceof BestBook) {
//                Picasso.with(this).load(((BestBook) item).getSmall_image_url()).into(targets.get(i++));
//            }
//            else if (item instanceof Search) {
//                Picasso.with(this).load(((Search) item).getPoster()).into(targets.get(i++));
//            }
//            else if (item instanceof Track) {

        for (int i = 0; i < itens.size(); ++i) {
            items.add(new Object());
        }

        adapter.notifyDataSetChanged();
        activity.mViewPager.refreshDrawableState();

        synchronized (activity.mViewPager) {
            activity.mViewPager.notifyAll();
        }

        synchronized (activity.mViewPager.getViewPager()) {
            activity.mViewPager.getViewPager().notifyAll();
        }


//        mResultsAdapter.setRibots(itens);
//        mResultsAdapter.notifyDataSetChanged();
//        getActivity().
//                break;
//            }

    }


}
