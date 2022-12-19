package com.example.lab6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class ClockDialog extends AppCompatDialogFragment {
    private TimePicker timePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_timepickerdialog, null);


        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String resultString;

                        AlertDialog.Builder resultBuilder = new AlertDialog.Builder(getActivity());
                        resultString = "The difference in minutes is: " +
                                getTimeDifferenceInMinutes(
                                        timePicker.getHour(),
                                        timePicker.getMinute());
                        resultBuilder
                                .setMessage(resultString)
                                .setTitle("Time difference");
                        resultBuilder.setCancelable(true);

                        Bundle result = new Bundle();
                        result.putString("minutes",resultString);

                        getParentFragmentManager().setFragmentResult("minutes", result);


                        AlertDialog resultAlert = resultBuilder.create();
                        resultAlert.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        timePicker = view.findViewById(R.id.timePicker1);

        return builder.create();

    }

    private int getTimeDifferenceInMinutes(int hours, int minutes){
        Calendar calendar = Calendar.getInstance();

        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        int timeDifference = Math.abs((hours - currentHours) * 60 + minutes - currentMinutes);
        return timeDifference;
    }
}
