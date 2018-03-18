package com.iaz.higister.ui.search;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.data.model.ComicVine.Results;
import com.iaz.higister.data.model.GoodReads.BestBook;
import com.iaz.higister.data.model.LastFM.Track;
import com.iaz.higister.data.model.MyAnimeList.Result;
import com.iaz.higister.data.model.Omdb.Search;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment2 extends Fragment implements SearchMvpView {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    SearchPresenter mSearchPresenter = new SearchPresenter();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    SearchActivity2 activity;

    ResultsAdapter adapter;
    ArrayList<BaseItem> items = new ArrayList<>();
    HashMap<String, ArrayList<BaseItem>> itemsCollection = new HashMap<>();
    HashMap<String, Integer> counterList = new HashMap<>();

    Fragment frag;

    public static RecyclerViewFragment2 newInstance(ArrayList<BaseItem> items) {

        RecyclerViewFragment2 f = new RecyclerViewFragment2();
        Bundle args = new Bundle();
        args.putParcelableArrayList("searchData", items);
        f.setArguments(args);
        return f;
    }

    int counter, i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//
        activity = (SearchActivity2) getActivity();
        Bundle args = getArguments();
        if (args != null && args.getParcelableArrayList("searchData") != null) {
            items = args.getParcelableArrayList("searchData");
        }

        float logicalDensity = Resources.getSystem().getDisplayMetrics().density;
        mSearchPresenter.attachView(this);


        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();


        adapter = new ResultsAdapter(getActivity(), items, settings);
        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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
                counterList.clear();
                i = 0;
                counter = 0;
                itemsCollection.clear();
                mSearchPresenter.loadResults("movie", activity.editText.getText().toString());
                mSearchPresenter.loadResults("series", activity.editText.getText().toString());
                mSearchPresenter.loadResults("anime", activity.editText.getText().toString());
                mSearchPresenter.loadResults("manga", activity.editText.getText().toString());
                mSearchPresenter.loadResults("book", activity.editText.getText().toString());
                mSearchPresenter.loadResults("music", activity.editText.getText().toString());
                mSearchPresenter.loadResults("comics", activity.editText.getText().toString());
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

        final int counter = this.counter++;

        if (itens != null && !itens.isEmpty()) {
            counterList.put(itens.get(0).getMyType(), 0);
        } else {
            this.counter--;
        }

        for (final BaseItem item : itens) {

            String bitmapAddress = "";
            if (item instanceof Result) {
                bitmapAddress = ((Result) item).getImage_url();
            } else if (item instanceof Results) {
                bitmapAddress = ((Results) item).getImage().getSmall_url();
            } else if (item instanceof BestBook) {
                bitmapAddress = ((BestBook) item).getSmall_image_url();
            } else if (item instanceof Search) {
                bitmapAddress = ((Search) item).getPoster();
            } else if (item instanceof Track) {
                itemsCollection.put(item.getMyType(), itens);
                preupdateAdapter();
                break;
            }

            RequestOptions myOptions = new RequestOptions()
                    .override(200, 300)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(this)
                    .asBitmap()
                    .load(bitmapAddress)
                    .apply(myOptions)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            counterList.put(item.getMyType(), counterList.get(item.getMyType()) + 1);
//
                            item.setBit(resource);
                            if (item.getMyType().equals("book")) {
                                Timber.d("sucesso");
                            }
                            if (counterList.get(item.getMyType()) == itens.size()) {
                                if (item.getMyType().equals("book")) {
                                    Timber.d("sucesso");
                                }
                                Timber.d("sucesso");
                                itemsCollection.put(item.getMyType(), itens);
                                preupdateAdapter();
                            }
                        }
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            counterList.put(item.getMyType(), counterList.get(item.getMyType()) + 1);

                            InputStream is = getResources().openRawResource(R.drawable.large_movie_poster);
                            Bitmap pisc = BitmapFactory.decodeStream(new BufferedInputStream(is));

                            item.setBit(pisc);

                            if (item.getMyType().equals("book")) {
                                Timber.d("sucesso");
                            }

                            if (counterList.get(item.getMyType()) == itens.size()) {

                                if (item.getMyType().equals("book")) {
                                    Timber.d("sucesso");
                                }
                                itemsCollection.put(item.getMyType(), itens);
                                preupdateAdapter();
                            }
                        }
                    });

        }
    }

    public void preupdateAdapter() {

        if (counterList.size() == 7
                && itemsCollection.containsKey("movie")
                && itemsCollection.containsKey("series")
                && itemsCollection.containsKey("anime")
                && itemsCollection.containsKey("manga")
                && itemsCollection.containsKey("book")
                && itemsCollection.containsKey("music")
                && itemsCollection.containsKey("comics")) {
            updateAdapter();
        }
    }

    public void updateAdapter() {

        for (Fragment frag : activity.getSupportFragmentManager().getFragments()) {
            if (frag instanceof RecyclerViewFragment2) {
                activity.getSupportFragmentManager().beginTransaction().remove(frag).commit();
            }
        }

        Runtime.getRuntime().gc();
        List<Fragment> frags = activity.getSupportFragmentManager().getFragments();

        activity.mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(activity.getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 8) {
                    case 0:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("movie"));
                    case 1:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("series"));
                    case 2:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("anime"));
                    case 3:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("manga"));
                    case 4:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("book"));
                    case 5:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("music"));
                    case 6:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("comics"));
                    default:
                        return RecyclerViewFragment2.newInstance(itemsCollection.get("movie"));
                }
            }

            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 8) {
                    case 0:
                        return "Movies";
                    case 1:
                        return "Series";
                    case 2:
                        return "Animes";
                    case 3:
                        return "Mangas";
                    case 4:
                        return "Books";
                    case 5:
                        return "Musics";
                    case 6:
                        return "Comics";
                    case 7:
                        return "People";
                }
                return "";
            }

            @Override
            public int getItemPosition(Object object){
                return PagerAdapter.POSITION_NONE;
            }
        });
        activity.mViewPager.getViewPager().getAdapter().notifyDataSetChanged();
        activity.mViewPager.getPagerTitleStrip().setViewPager(activity.mViewPager.getViewPager());
    }


}
