package com.iaz.higister2.ui.login;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.iaz.higister2.R;
import com.iaz.higister2.util.Rotate;
import com.iaz.higister2.util.TextSizeTransition;
import com.iaz.higister2.util.TextWatcherAdapter;
import com.iaz.higister2.util.ViewUtil;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by alksander on 05/03/2018.
 */

public class LogInFragment extends AuthFragment{

    @BindView(R.id.email_input)
    TextInputLayout emailLayout;

    @BindView(R.id.password_input)
    TextInputLayout passwordLayout;

    @BindViews(value = {R.id.email_input_edit,R.id.password_input_edit})
    protected List<TextInputEditText> views;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            caption.setText(getString(R.string.log_in_label));
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_log_in));
            for(TextInputEditText editText:views){
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout=ButterKnife.findById(view,R.id.password_input);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    editText.addTextChangedListener(new TextWatcherAdapter(){
                        @Override
                        public void afterTextChanged(Editable editable) {
                            inputLayout.setPasswordVisibilityToggleEnabled(editable.length()>0);
                        }
                    });
                }
                editText.setOnFocusChangeListener((temp,hasFocus)->{
                    if(!hasFocus){
                        boolean isEnabled=editText.getText().length()>0;
                        editText.setSelected(isEnabled);
                    }
                });
            }

            caption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthActivity activity = (AuthActivity) getActivity();
                    if (activity.pager.getCurrentItem() == 0 && caption.getText().equals(activity.getString(R.string.log_in_label)))
                        checkFields();
                    else
                        LogInFragment.super.unfold();
                }
            });
        }
    }

    @Override
    public int authLayout() {
        return R.layout.fragment_login;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void fold() {
        lock=false;
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        TransitionSet set=new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds=new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition=new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        final float padding=getResources().getDimension(R.dimen.folded_label_padding)/2;
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(-padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.folded_size));
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params = getParams();
        params.leftToLeft=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
        caption.setTranslationX(caption.getWidth()/8-padding);
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    public boolean checkFields() {
//        emailLayout.setError(null);
//        emailLayout.setErrorEnabled(false);
//        passwordLayout.setError(null);
//        passwordLayout.setErrorEnabled(false);
//        confirmPasswordLayout.setError(null);
//        confirmPasswordLayout.setErrorEnabled(false);

        boolean cancel = false;
        View focusView = null;

        // Standard pattern for email
        final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        if (emailLayout.getEditText().getText().toString().trim().isEmpty()) {
//            emailLayout.setErrorEnabled(true);
//            emailLayout.setError("field required");
            Toast.makeText(getActivity(), "field required",
                    Toast.LENGTH_SHORT).show();
            focusView = emailLayout.getEditText();
            cancel = true;
        }
        else if (!pattern.matcher(emailLayout.getEditText().getText().toString().trim()).matches()) {
//            emailLayout.setErrorEnabled(true);
//            emailLayout.setError("invalid email");
            Toast.makeText(getActivity(), "invalid email",
                    Toast.LENGTH_SHORT).show();
            focusView = emailLayout.getEditText();
            cancel = true;
        }
        else if (passwordLayout.getEditText().getText().toString().trim().isEmpty()) {
//            passwordLayout.setErrorEnabled(true);
//            passwordLayout.setError("field required");
            Toast.makeText(getActivity(), "field required",
                    Toast.LENGTH_SHORT).show();
            focusView = passwordLayout.getEditText();
            cancel = true;
        }
        else if (passwordLayout.getEditText().getText().toString().trim().length() < 6) {
//            passwordLayout.setErrorEnabled(true);
//            passwordLayout.setError("field required");
            Toast.makeText(getActivity(), "password must have at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            focusView = passwordLayout.getEditText();
            cancel = true;
        }

        Log.e("aw", "cancel : " + cancel);

        if (cancel) {
            focusView.requestFocus();
            ViewUtil.showKeyboard(getActivity(), focusView);
        } else {
            AuthActivity activity = (AuthActivity) getActivity();
            activity.mAuthPresenter.signInWithEmailAndPassword(views.get(0).getText().toString(), views.get(1).getText().toString());
        }
        return !cancel;
    }

}