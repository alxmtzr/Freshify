package de.alxmtzr.freshify;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCategoryDropDown(view);
        initExpiryDateField(view);
    }

    private void initCategoryDropDown(@NonNull View view) {
        AutoCompleteTextView dropdownMenu = view.findViewById(R.id.categoryDropdownMenu);

        // data source for categories
        String[] items = new String[]{"Option 1", "Option 2", "Option 3"};

        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.category_dropdown_item,
                R.id.categoryDropdownTextView,
                items
        );

        dropdownMenu.setAdapter(adapter);
    }

    private void initExpiryDateField(View view) {
        TextInputEditText expiryDateEditText = view.findViewById(R.id.expiryDateEditText);

        // click listener to show calendar
        expiryDateEditText.setOnClickListener(v -> {
            // current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // create date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        // format date
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        expiryDateEditText.setText(formattedDate);
                    },
                    year, month, day
            );

            // show date picker
            datePickerDialog.show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}