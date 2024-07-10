package com.example.wave;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {
    private CountryCodePicker codePicker;
    private AlertDialog progressAlertDialog;
    private EditText phoneNumberEdit;
    private Button submitButton, btnDelete;
    private final int[] buttonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Wave);
        setContentView(R.layout.activity_register);

        setupActionBar();
        setupWindowInsets();
        initializeViews();
        setupButtonListeners();

        progressAlertDialog = createProgressDialog(this);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        codePicker = findViewById(R.id.country_code);
        submitButton = findViewById(R.id.submit_btn);
        phoneNumberEdit = findViewById(R.id.phone_number);
        btnDelete = findViewById(R.id.btn_delete);

        phoneNumberEdit.setInputType(InputType.TYPE_NULL);
        codePicker.registerCarrierNumberEditText(phoneNumberEdit);
    }

    private void setupButtonListeners() {
        View.OnClickListener numberButtonListener = v -> {
            Button button = (Button) v;
            String text = button.getText().toString();
            phoneNumberEdit.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_wave));
            phoneNumberEdit.append(text);
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(numberButtonListener);
        }

        btnDelete.setOnClickListener(v -> {
            int length = phoneNumberEdit.getText().length();
            if (length > 0) {
                phoneNumberEdit.getText().delete(length - 1, length);
            }
        });

        submitButton.setOnClickListener(v -> {
            displayProgressDialog();
            if (codePicker.isValidFullNumber()) {
                hideProgressDialog();
                String phoneNumber = codePicker.getFullNumberWithPlus();
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phoneNumber", phoneNumber);
                editor.apply();
                startActivity(new Intent(RegisterActivity.this, PasswordActivity.class));
            } else {
                hideProgressDialog();
                Toast.makeText(RegisterActivity.this, "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show();
            }

            logCountryDetails();
        });

    }

    private void logCountryDetails() {
        //String country_code = codePicker.getSelectedCountryCode();
        String country_name = codePicker.getSelectedCountryName();
        //String country_namecode = codePicker.getSelectedCountryNameCode();
        Log.d("TAG", "Country Name: " + country_name);
    }

    private AlertDialog createProgressDialog(AppCompatActivity currentActivity) {
        LinearLayout vLayout = new LinearLayout(currentActivity);
        vLayout.setOrientation(LinearLayout.VERTICAL);
        vLayout.setPadding(50, 50, 50, 50);
        vLayout.addView(new ProgressBar(currentActivity, null, android.R.attr.progressBarStyleLarge));

        return new AlertDialog.Builder(currentActivity)
                .setCancelable(false)
                .setView(vLayout)
                .create();
    }

    public void displayProgressDialog() {
        if (progressAlertDialog != null && !progressAlertDialog.isShowing()) {
            progressAlertDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }
}
