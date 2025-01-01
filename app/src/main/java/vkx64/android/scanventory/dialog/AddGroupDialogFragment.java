package vkx64.android.scanventory.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import vkx64.android.scanventory.R;

public class AddGroupDialogFragment extends DialogFragment {

    public interface AddGroupDialogListener {
        //* Listener interface for form submission *//
        void onSubmit(String groupId, String groupName);
    }

    private AddGroupDialogListener listener;


    public AddGroupDialogFragment(AddGroupDialogListener listener) {
        //* Constructor to set the listener *//
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the dialog
        View view = inflater.inflate(R.layout.dialog_add_group, container, false);

        // Initialize views
        EditText etGroupId = view.findViewById(R.id.etGroupId);
        EditText etGroupName = view.findViewById(R.id.etGroupName);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        // Cancel button closes the dialog
        btnCancel.setOnClickListener(v -> dismiss());

        // Submit button validates input and sends data
        btnSubmit.setOnClickListener(v -> {
            String groupId = etGroupId.getText().toString().trim();
            String groupName = etGroupName.getText().toString().trim();

            if (groupId.isEmpty() || groupName.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (listener != null) listener.onSubmit(groupId, groupName);
            dismiss();
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //* Set the dialog's background to transparent for rounded corners *//
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        //* Set the dialog's width to 75% of the screen width *//
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Get screen width
            DisplayMetrics metrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenWidth = metrics.widthPixels;

            getDialog().getWindow().setLayout((int) (screenWidth * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
