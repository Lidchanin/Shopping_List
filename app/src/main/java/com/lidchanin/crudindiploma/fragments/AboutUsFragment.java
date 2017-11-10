package com.lidchanin.crudindiploma.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;

/**
 * Class extends {@link Fragment}. Class implements {@link android.view.View.OnClickListener}.
 *
 * @author Lidchanin
 * @see android.support.v4.app.Fragment
 * @see android.view.View.OnClickListener
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((NavigationDrawerActivity) getActivity()).setButtonsToDefault();
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        TextView tvCompanyEmail
                = (TextView) view.findViewById(R.id.tv_company_email_in_about_us_screen);
        tvCompanyEmail.setOnClickListener(this);

        TextView tvSinEmail = (TextView) view.findViewById(R.id.tv_sin_email_in_about_us_screen);
        tvSinEmail.setOnClickListener(this);

        TextView tvFokEmail = (TextView) view.findViewById(R.id.tv_fok_email_in_about_us_screen);
        tvFokEmail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_company_email_in_about_us_screen:
                showDialogForSendEmail(getString(R.string.company_email));
                break;
            case R.id.tv_sin_email_in_about_us_screen:
                showDialogForSendEmail(getString(R.string.sinukovich_email));
                break;
            case R.id.tv_fok_email_in_about_us_screen:
                showDialogForSendEmail(getString(R.string.fokin_email));
                break;
        }
    }

    private void showDialogForSendEmail(final String ourEmail) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.MyDialogTheme);
        // FIXME: 10.11.2017 need string res
        builder.setTitle("SEND EMAIL TITLE");

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText etSubject = new EditText(getContext());
        // FIXME: 10.11.2017 need string res
        etSubject.setHint("Subject");

        final EditText etMessage = new EditText(getContext());
        // FIXME: 10.11.2017 need string res
        etMessage.setHint("Compose email");

        linearLayout.addView(etSubject);
        linearLayout.addView(etMessage);

        builder.setView(linearLayout);

        // FIXME: 10.11.2017 need string res
        builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ourEmail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                emailIntent.setType("message/rfc822");
                // FIXME: 10.11.2017 need string res
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
