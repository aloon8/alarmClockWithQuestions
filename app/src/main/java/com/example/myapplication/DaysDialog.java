package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DaysDialog extends DialogFragment {
    private List<String> Days;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        System.out.println("asdasdasdasdasdadsda");
        Days = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Days");
        builder.setMultiChoiceItems(R.array.Days, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] items = getActivity().getResources().getStringArray(R.array.Days);

                if(isChecked){
                    Days.add(items[which]);
                }
                else if(Days.contains(items[which])){
                    Days.remove(items[which]);
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String final_selection = "";

                for(String item : Days){
                    final_selection = final_selection + "\n" + item;
                }
                Toast.makeText(getActivity(), "Selection "+final_selection,Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
