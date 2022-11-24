package com.ridm.eduRIDM.screen.addevaluative;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ridm.eduRIDM.MainActivity;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ridm.eduRIDM.R;
import com.ridm.eduRIDM.databinding.FragmentAddEvaluativeBinding;
import com.ridm.eduRIDM.databinding.FragmentAddEvaluativeBindingImpl;
import com.ridm.eduRIDM.model.room.Eval.Eval;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class AddEvaluativeFragment extends Fragment {

    AddEvaluativeViewModel viewModel;
    FragmentAddEvaluativeBinding binding;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AddEvaluativeViewModel.class);

        viewModel.getMyCourses();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_evaluative, container, false);

        binding.setLifecycleOwner(this);

        binding.setViewModel(viewModel);

        viewModel.getNavigateToMyAcads().observe(getViewLifecycleOwner(), navigateToMyAcads -> {
            if (navigateToMyAcads == Boolean.TRUE) {
                Navigation.findNavController(this.requireView()).navigate(R.id.action_addEvaluativeFragment_to_myAcadsFragment);
                viewModel.doneNavigatingToMyAcads();
            }
        });

        binding.addEvalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eval eval = new Eval();

                String course[] = binding.spinnerSubject.getSelectedItem().toString().split(" ");

                eval.setDeptCode(course[0]);
//                eval.setCourseCode(course[1]);

                String[] dates = binding.addEvalDatePicker.getText().toString().split("/");
                String date = dates[2] + "-" + dates[1] + "-" + dates[0];
                eval.setDate(date);

                eval.setTime(binding.addEvalTimePicker.getText().toString());
                eval.setDuration(Integer.parseInt(binding.addEvalDuration.getText().toString()));
                eval.setType(binding.spinnerType.getSelectedItem().toString());
                eval.setSyllabus(binding.syllabusText.getText().toString());
                eval.setNature(binding.spinnerNature.getSelectedItem().toString());

                viewModel.eval = eval;
                viewModel.onNavigateToMyAcadsClicked();
            }
        });

        binding.addEvalDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String date = day + "/" + (month + 1) + "/" + year;
                                binding.addEvalDatePicker.setText(date);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        binding.addEvalTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        String time = String.format("%02d:%02d", hourOfDay, minutes) + amPm;
                        binding.addEvalTimePicker.setText(time);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        return binding.getRoot();
    }
}
