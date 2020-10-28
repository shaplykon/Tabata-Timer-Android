package com.example.timer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public  class ResetDialogFragment extends DialogFragment {
    OnDataReset onDataReset;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDataReset = (OnDataReset) context;
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder((SettingsActivity)getActivity());

        return builder
                .setTitle(R.string.reset_dialog_title)
                .setMessage(R.string.reset_dialog_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDataReset.reset();
                    }
                }).show();

    }

}