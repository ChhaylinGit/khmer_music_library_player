package com.example.khmer_music_library_player.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.khmer_music_library_player.R;

import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private String[] arrayOfMinutes;
    private NumberPicker numberPicker;
    private TextView textViewMinute,textViewTimer;
    private CardView cardViewAgree,cardViewDisAgree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.time_picker_dialog_lyout, null);
        initView(dialog);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setDisplayedValues(arrayOfMinutes);
        numberPicker.setValue(1);
        textViewMinute.setText(arrayOfMinutes[numberPicker.getValue() -1]);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                textViewMinute.setText(arrayOfMinutes[newVal -1]);
            }
        });
        cardViewAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minutes =  arrayOfMinutes[numberPicker.getValue() -1].replaceAll(" ", "");
                int value = Integer.parseInt(minutes.substring(0,minutes.indexOf("នាទី")));
                listener.onDateSet(null,value,0,0);
                dismiss();
            }
        });
        cardViewDisAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(dialog);
        return builder.create();
    }

    private void initView(View view) {
        arrayOfMinutes = getActivity().getResources().getStringArray(R.array.array_minutes);
        numberPicker = view.findViewById(R.id.picker_minute);
        textViewMinute = view.findViewById(R.id.textViewMinute);
        textViewTimer = view.findViewById(R.id.textViewTimer);
        cardViewAgree = view.findViewById(R.id.cardViewAgree);
        cardViewDisAgree = view.findViewById(R.id.cardViewDisAgree);
    }
}
