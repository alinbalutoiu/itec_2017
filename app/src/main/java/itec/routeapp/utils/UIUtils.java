package itec.routeapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import itec.routeapp.R;

/**
 * Created by Mihaela Ilin on 1/5/2017.
 */

public class UIUtils {

    public static void showOKDialog(Context context, @NonNull String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showDiscardWarningDialog(Context context, DialogInterface.OnClickListener
            positiveButtonClickListener){
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.discard_changes))
                .setMessage(context.getResources().getString(R.string.changes_not_saved_warning))
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton(android.R.string.yes, positiveButtonClickListener)
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * @param show whether to show or hide progress
     * @param underlyingView view over which progress is displayed
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, final View underlyingView, final View progressView){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = underlyingView.getContext()
                    .getResources().getInteger(android.R.integer.config_shortAnimTime);

            underlyingView.setVisibility(show ? View.GONE : View.VISIBLE);
            underlyingView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    underlyingView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            underlyingView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
