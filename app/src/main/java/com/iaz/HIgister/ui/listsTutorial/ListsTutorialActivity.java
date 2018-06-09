package com.iaz.HIgister.ui.listsTutorial;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.data.repository.ListRepository;
import com.iaz.HIgister.ui.base.BaseActivity;
import com.iaz.HIgister.ui.intro.IntroActivity;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.search.SearchActivity;
import com.iaz.HIgister.util.Constants;
import com.iaz.HIgister.util.CustomViewPager;
import com.iaz.HIgister.util.DialogFactory;
import com.iaz.Higister.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by alksander on 03/06/2018.
 */

public class ListsTutorialActivity extends BaseActivity {

    @Inject
    ListsTutorialPresenter mListsTutorialPresenter;

    UserList list;
    ListItem listItem;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.previous_button)
    TextView previousButton;
    @BindView(R.id.next_button)
    TextView nextButton;

    FragmentPagerAdapter adapterViewPager;

    public ListsTutorialPicture fragmentPicture;
    public ListsTutorialType fragmentType;
    public ListsTutorialInfo fragmentInfo;
    public ListsTutorialItemPicture fragmentItemPicture;
    public ListsTutorialItemInfo fragmentItemInfo;
    public ListsTutorialReview fragmentReview;

    int position = 0;
    private Dialog mDialog;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mListsTutorialPresenter.setActivity(this);

        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        list = new UserList();

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapterViewPager);
        circleIndicator.setViewPager(viewPager);

        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(6);

        setSupportActionBar(mToolbar);

        nextButton.setOnClickListener(v -> setNext());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int positionT) {


                if (positionT == 0) {
                    previousButton.setVisibility(View.GONE);
                } else if (positionT == 3) {
                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);

                    nextButton.setOnClickListener(v -> {

                        Intent intent = new Intent(ListsTutorialActivity.this, SearchActivity.class);
                        intent.putExtra("isFromTutorial", true);
                        intent.putExtra("list", list);
                        startActivityForResult(intent, 1);
                    });
                } else if (positionT == 6) {

                    previousButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);

                    nextButton.setText("Finish");
                    nextButton.setOnClickListener(v -> {

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("showListsTutorial", false);
                        editor.apply();

                        Intent intent = new Intent(ListsTutorialActivity.this, MainActivity.class);
                        startActivity(intent);
                    });

                } else {

                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);

                    previousButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left, 0, 0, 0);
                    previousButton.setText("Previous");
                    previousButton.setOnClickListener(v -> {

                        position--;
                        viewPager.setCurrentItem(position);
                    });

                    nextButton.setText("Next");
                    nextButton.setOnClickListener(v -> {

                        setNext();
                    });
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        ListsTutorialActivity activity;

        public MyPagerAdapter(FragmentManager fragmentManager, ListsTutorialActivity activity) {
            super(fragmentManager);
            this.activity = activity;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return 7;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (activity.fragmentPicture == null)
                        activity.fragmentPicture = new ListsTutorialPicture();
                    return activity.fragmentPicture;
                case 1:
                    if (activity.fragmentType == null)
                        activity.fragmentType = new ListsTutorialType();
                    return activity.fragmentType;
                case 2:
                    if (activity.fragmentInfo == null)
                        activity.fragmentInfo = new ListsTutorialInfo();
                    return activity.fragmentInfo;
                case 3:
                    return new ListsTutorialSearch1();
                case 4:
                    if (activity.fragmentItemPicture == null)
                        activity.fragmentItemPicture = new ListsTutorialItemPicture();
                    return activity.fragmentItemPicture;
                case 5:
                    if (activity.fragmentItemInfo == null)
                        activity.fragmentItemInfo = new ListsTutorialItemInfo();
                    return activity.fragmentItemInfo;
                case 6:
                    if (activity.fragmentReview == null)
                        activity.fragmentReview = new ListsTutorialReview();
                    return activity.fragmentReview;
                case 7:
                    break;
            }
            if (activity.fragmentPicture == null)
                activity.fragmentPicture = new ListsTutorialPicture();
            return activity.fragmentPicture;
        }

    }

    public void setNext() {
        if (viewPager.getCurrentItem() == 0) {

            if (checkInfoList()) {
                list.setName(fragmentPicture.mNameTextInput.getEditText().getText().toString());
                list.setListPictureUri(fragmentPicture.uri);
                position++;
                viewPager.setCurrentItem(position);
            }

        } else if (viewPager.getCurrentItem() == 1) {

            list.setType(fragmentType.typeSelected);

            position++;
            viewPager.setCurrentItem(position);

        } else if (viewPager.getCurrentItem() == 2) {

            list.setDescription(fragmentInfo.mDescriptionTextInput.getEditText().getText().toString());
            list.setVisibleForEveryone(fragmentInfo.isListVisible.isChecked());
            list.setCommentsEnabled(fragmentInfo.areCommentsEnabled.isChecked());

            position++;
            viewPager.setCurrentItem(position);
        } else if (viewPager.getCurrentItem() == 4) {


            if (list.getType() != 0) {
                fragmentItemInfo.listTypeSpinner.setEnabled(false);
                fragmentItemInfo.listTypeSpinner.setSelection(list.getType()+1);

            }

            if (checkInfoItemList()) {

                if (uri != null && !uri.isEmpty() && (listItem.getBaseItem().imageUrl == null || listItem.getBaseItem().imageUrl.isEmpty()))
                    listItem.getBaseItem().imageUrl = uri;

                position++;
                viewPager.setCurrentItem(position);
            }
        } else if (viewPager.getCurrentItem() == 5) {


            if (listItem.getBaseItem() == null)
                listItem.setType(Constants.MISC);
            else
                listItem.setType(listItem.getBaseItem().getMyType());

            listItem.setDescription(fragmentItemInfo.mDescriptionTextInput.getEditText().getText().toString().trim());

            ArrayList<ListItem> listItems = new ArrayList<>();
            listItems.add(listItem);

            list.setListItems(listItems);

            saveList();


        } else if (viewPager.getCurrentItem() == 6) {

            Intent intent = new Intent(ListsTutorialActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mListsTutorialPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 1) {
            listItem = data.getParcelableExtra("listItem");

            if (listItem != null && listItem.getBaseItem() != null
                    && listItem.getBaseItem().imageUrl != null
                    && !listItem.getBaseItem().imageUrl.isEmpty()) {
                fragmentItemPicture.callGlide(Uri.parse(listItem.getBaseItem().imageUrl));
            } else if (listItem.getBaseItem().imageUrl == null || listItem.getBaseItem().imageUrl.isEmpty()) {
                fragmentItemPicture.profileImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.large_movie_poster, null));
            }

            fragmentItemPicture.mNameTextInput.setError(null);

            position++;
            viewPager.setCurrentItem(position);
        } else {
            mListsTutorialPresenter.activityResult(requestCode, resultCode, data);
        }
    }

    public void showSnackBar(String msg) {
        Toast.makeText(ListsTutorialActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void saveList() {

        ListRepository listRepository = new ListRepository();

        mDialog = DialogFactory.newDialog(ListsTutorialActivity.this, "Saving list...");
        mDialog.show();

        list.setCreatorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userName = sharedPref.getString("userName", "---");
        list.setCreatorName(userName);

        listRepository.saveList(list, new ListRepository.OnListSaved() {
            @Override
            public void onSuccess(UserList userList) {
                DialogFactory.finalizeDialog(mDialog, true, "List saved with success", () -> {
                    list = userList;
                    position++;
                    viewPager.setCurrentItem(position);
                });
            }

            @Override
            public void onFailed(Exception exception) {
                Log.d("saveList: ", exception.getMessage());
                DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on list creation", () -> {
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (position > 0 && position < 6) {
            position--;
            viewPager.setCurrentItem(position);
        }
    }

    public boolean checkInfoList() {

        if (fragmentPicture.mNameTextInput.getEditText().getText().toString().trim().isEmpty()) {
            fragmentPicture.mNameTextInput.setError("This field is required");
            fragmentPicture.mNameTextInput.requestFocus();
            return false;
        } else
            fragmentPicture.mNameTextInput.setError(null);

        return true;
    }

    public boolean checkInfoItemList() {


        if (listItem.getName() == null || (listItem.getName() != null && listItem.getName().isEmpty())) {
            if (listItem.getBaseItem() == null || (listItem.getBaseItem() != null && listItem.getBaseItem().title == null)
                    || (listItem.getBaseItem().title != null && listItem.getBaseItem().title.isEmpty())) {
                fragmentItemPicture.mNameTextInput.setError("This field is required");
                return false;
            }
        }
        if (listItem.getName() != null && listItem.getName().length() > 30) {
            fragmentItemPicture.mNameTextInput.setError("The item name cannot have more than 30 characters (it has "
                    + listItem.getName().length() + " )");
            return false;
        }

        fragmentItemPicture.mNameTextInput.setError(null);

        return true;
    }

}
