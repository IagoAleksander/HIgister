package com.iaz.higister.ui.login;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import java.util.List;
import java.util.regex.Pattern;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.iaz.higister.R;
import com.iaz.higister.util.Rotate;
import com.iaz.higister.util.TextSizeTransition;
import com.iaz.higister.util.TextWatcherAdapter;
import com.iaz.higister.util.ViewUtil;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class SignUpFragment extends AuthFragment{

    @BindView(R.id.email_input)
    TextInputLayout emailLayout;

    @BindView(R.id.password_input)
    TextInputLayout passwordLayout;

    @BindView(R.id.confirm_password)
    TextInputLayout confirmPasswordLayout;

    @BindViews(value = {R.id.email_input_edit,
            R.id.password_input_edit,
            R.id.confirm_password_edit})
    protected List<TextInputEditText> views;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_sign_up));
            caption.setText(getString(R.string.sign_up_label));
            for(TextInputEditText editText:views){
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout= ButterKnife.findById(view,R.id.password_input);
                    final TextInputLayout confirmLayout=ButterKnife.findById(view,R.id.confirm_password);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    confirmLayout.setTypeface(boldTypeface);
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
            caption.setVerticalText(true);
            foldStuff();
            caption.setTranslationX(getTextPadding());

            caption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthActivity activity = (AuthActivity) getActivity();
                    if (activity.pager.getCurrentItem() == 1 && caption.getText().equals(activity.getString(R.string.sign_up_label)))
                        checkFields();
                    else
                        SignUpFragment.super.unfold();
                }
            });

        }
    }

    @Override
    public int authLayout() {
        return R.layout.sign_up_fragment;
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    @Override
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
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(getTextPadding());
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        foldStuff();
        caption.setTranslationX(-caption.getWidth()/8+getTextPadding());
    }

    private void foldStuff(){
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.folded_size));
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.rightToRight=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }

    private float getTextPadding(){
        return getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
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
        else if (confirmPasswordLayout.getEditText().getText().toString().trim().isEmpty()) {
//            confirmPasswordLayout.setErrorEnabled(true);
//            confirmPasswordLayout.setError("field required");
            Toast.makeText(getActivity(), "field required",
                    Toast.LENGTH_SHORT).show();
            focusView = confirmPasswordLayout.getEditText();
            cancel = true;
        }
        else if (!confirmPasswordLayout.getEditText().getText().toString().trim().equals(passwordLayout.getEditText().getText().toString().trim())) {
//            confirmPasswordLayout.setErrorEnabled(true);
//            confirmPasswordLayout.setError("fields does not match");
            Toast.makeText(getActivity(), "fields does not match",
                    Toast.LENGTH_SHORT).show();
            focusView = confirmPasswordLayout.getEditText();
            cancel = true;
        }

        Log.e("aw", "cancel : " + cancel);

        if (cancel) {
            focusView.requestFocus();
            ViewUtil.showKeyboard(getActivity(), focusView);
        } else {

            if (getActivity() instanceof AuthActivity) {
                AuthActivity activity = (AuthActivity) getActivity();

                activity.mAuthPresenter.addAuthStateListener();
                activity.mAuthPresenter.createAccount(views.get(0).getText().toString(), views.get(1).getText().toString());
            }
        }
        return !cancel;
    }
}