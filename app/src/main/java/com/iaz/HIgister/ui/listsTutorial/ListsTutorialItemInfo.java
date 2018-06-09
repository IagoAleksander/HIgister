package com.iaz.HIgister.ui.listsTutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 28/05/2018.
 */

public class ListsTutorialItemInfo extends Fragment {


    @BindView(R.id.list_type_spinner)
    Spinner listTypeSpinner;

    @BindView(R.id.text_input_list_desc)
    TextInputLayout mDescriptionTextInput;

    ListsTutorialActivity activity;
    int typeSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ListsTutorialActivity) getActivity();
//        activity.fragmentInfo = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lists_tutorial_item_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

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
                    typeSelected = position;
                else
                    typeSelected = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
