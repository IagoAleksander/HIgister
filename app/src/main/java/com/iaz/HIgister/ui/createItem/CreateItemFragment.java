package com.iaz.HIgister.ui.createItem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.util.CustomPhotoPickerDialog;
import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.HIgister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class CreateItemFragment extends Fragment implements CreateItemMvpView {

    @BindView(R.id.item_title)
    TextView itemTitle;
    @BindView(R.id.item_description)
    TextView itemDescription;
    @BindView(R.id.listLogoImageLayout)
    RelativeLayout listLogoImageLayout;
    @BindView(R.id.listLogoImageView)
    ImageView listLogoImage;
    @BindView(R.id.base_item_layout)
    LinearLayout baseItemLayout;
    @BindView(R.id.list_type_spinner)
    Spinner listTypeSpinner;
    @BindView(R.id.text_input_list_name)
    TextInputLayout listNameLayout;
    @BindView(R.id.text_input_list_desc)
    TextInputLayout listDescriptionLayout;


    private CustomPhotoPickerDialog photoDialog;

    ListItem listItem;
    CreateItemActivity activity;

    // newInstance constructor for creating fragment with arguments
    public static CreateItemFragment newInstance(ListItem listItem) {
        CreateItemFragment viewItemFragment = new CreateItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("listItem", listItem);
        viewItemFragment.setArguments(args);
        return viewItemFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            listItem = getArguments().getParcelable("listItem");

        activity = (CreateItemActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_create_item, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activity.mCreateItemPresenter.attachView(this);

        if (listItem != null) {

            if (listItem.getBaseItem() != null) {

                if (listItem.getBaseItem().imageUrl != null) {
                    Glide.with(this)
                            .load(listItem.getBaseItem().imageUrl)
                            .into(listLogoImage);
                }

                if (listItem.getBaseItem().title != null)
                    itemTitle.setText(listItem.getBaseItem().title);

                if (listItem.getBaseItem().description != null)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        itemDescription.setText((Html.fromHtml(listItem.getBaseItem().description, Html.FROM_HTML_MODE_LEGACY)).toString());
                    } else {
                        itemDescription.setText((Html.fromHtml(listItem.getBaseItem().description)).toString());
                    }

            } else {
                baseItemLayout.setVisibility(View.GONE);
            }

            if (listItem.getName() != null)
                listNameLayout.getEditText().setText(listItem.getName());

            if (listItem.getDescription() != null)
                listDescriptionLayout.getEditText().setText(listItem.getDescription());

        }

        listNameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activity.listItem.setName(editable.toString());
                activity.fieldsAreOk();
            }
        });

        listDescriptionLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activity.listItem.setDescription(editable.toString());
            }
        });

        listLogoImageLayout.setOnClickListener(v -> {
            photoDialog = new CustomPhotoPickerDialog(activity, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    activity.mCreateItemPresenter.openDialogWindow();
                    photoDialog.dismiss();
                }

                @Override
                public void onCamera() {
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        photoDialog.dismiss();
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                    } else {
                        activity.mCreateItemPresenter.getPhoto();
                        photoDialog.dismiss();
                    }
                }
            });
            photoDialog.show();

        });

        bindSpinnerLabel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activity.mCreateItemPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activity.mCreateItemPresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void callGlide(Uri uri) {
        activity.listItem.getBaseItem().imageUrl = uri.toString();
        try {
            Glide.with(CreateItemFragment.this)
                    .load(uri)
                    .into(listLogoImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissDialog() {
        if (photoDialog != null)
            photoDialog.cancel();
    }

    @Override
    public CreateItemActivity getActivityFromView() {
        return activity;
    }

    private void bindSpinnerLabel() {

        String[] stringArray = getResources().getStringArray(R.array.list_type);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.item_list_type, stringArray) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };

        listTypeSpinner.setAdapter(arrayAdapter);
        listTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    listItem.setType(position);
                else
                    listItem.setType(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (listItem != null) {
            listTypeSpinner.setSelection(listItem.getType() + 1);

            if (listItem.getBaseItem() != null) {
                listTypeSpinner.setSelection(listItem.getBaseItem().getMyType() + 1);
                listTypeSpinner.setEnabled(false);
            }
        }
    }

}
