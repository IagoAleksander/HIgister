package com.iaz.higister.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.CircularProgressButton;
import com.iaz.higister.R;

public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static Dialog newDialog(Context context, String text) {
        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog);
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(false);

        final TextView dialogText = mDialog.findViewById(R.id.dialog_text);
        dialogText.setText(text);

        final CircularProgressButton progressBar = mDialog.findViewById(R.id.progress_bar);
        progressBar.setIndeterminateProgressMode(true);
        progressBar.setProgress(1);

        return mDialog;

    }

    public static void finalizeDialogOnClick(Dialog mDialog, boolean success, String text, OnDialogButtonClicked onDialogButtonClicked) {

        final TextView dialogText = mDialog.findViewById(R.id.dialog_text);
        final CircularProgressButton progressBar = mDialog.findViewById(R.id.progress_bar);

        dialogText.setText(text);

        if (success) {
            progressBar.setProgress(100);
        }
        else {
            progressBar.setProgress(-1);
        }

        progressBar.setOnClickListener(view -> {
            mDialog.dismiss();
            onDialogButtonClicked.onClick();

        });
    }

    public static void finalizeDialog(Dialog mDialog, boolean success, String text, OnDialogButtonClicked onDialogButtonClicked) {

        final TextView dialogText = mDialog.findViewById(R.id.dialog_text);
        final CircularProgressButton progressBar = mDialog.findViewById(R.id.progress_bar);

        dialogText.setText(text);

        if (success) {
            progressBar.setProgress(100);
        }
        else {
            progressBar.setProgress(-1);
        }
            mDialog.dismiss();
            onDialogButtonClicked.onClick();
    }

    public static MaterialDialog.Builder newMaterialDialogWithInput(Context context, OnDialogInput onDialogInput) {
        return new MaterialDialog.Builder(context)
                .title("Add new comment")
                .cancelable(true)
                .negativeText("cancel")
                .input("Insert a new comment", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        onDialogInput.onInput(input.toString());
                    }
                });
    }

    public static MaterialDialog.Builder newMaterialDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content("Please enter at least 3 letters to perform a search")
                .cancelable(true)
                .positiveText("OK");
    }

    public interface OnDialogButtonClicked {
        void onClick();
    }

    public interface OnDialogInput {
        void onInput(String text);
    }

}
