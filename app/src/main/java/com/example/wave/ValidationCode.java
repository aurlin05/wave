package com.example.wave;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class ValidationCode extends AppCompatActivity {
    private final int[] buttonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9
    };
    private final int[] editTextIds = {
            R.id.editText1, R.id.editText2, R.id.editText3, R.id.editText4
    };
    private int currentIndex = 0;
    private AlertDialog progressAlertDialog;
    String otp = new String(geek_Password());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setTheme(R.style.Theme_Wave);
        setContentView(R.layout.activity_validation_code);

        setupActionBar();
        setupWindowInsets();

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        String slicedPhoneNumber = "";
        if (phoneNumber.length() >= 4) {
            slicedPhoneNumber = phoneNumber.substring(4);
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(getString(R.string.verify_code, slicedPhoneNumber));

        setupButtonListeners();

        progressAlertDialog = createProgressDialog(this);
    }

    private void setupButtonListeners() {
        View.OnClickListener numberButtonListener = v -> {
            Button button = (Button) v;
            String text = button.getText().toString();
            if (currentIndex < 4){
                EditText editText = findViewById(editTextIds[currentIndex]);
                editText.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_wave));
                editText.append(text);
                currentIndex++;
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(numberButtonListener);
        }
        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            if (currentIndex > 0){
                currentIndex--;
                EditText editText = findViewById(editTextIds[currentIndex]);
                int length = editText.getText().length();
                if (length > 0) {
                    editText.getText().delete(length - 1, length);
                }
            }
        });

        Button submitButton = findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(v -> {
            displayProgressDialog();

            EditText editText1 = findViewById(editTextIds[0]);
            EditText editText2 = findViewById(editTextIds[1]);
            EditText editText3 = findViewById(editTextIds[2]);
            EditText editText4 = findViewById(editTextIds[3]);

            String validationCode = editText1.getText().toString() +
                    editText2.getText().toString() +
                    editText3.getText().toString() +
                    editText4.getText().toString();

            if (validationCode.length() == 4 && validationCode.equals(otp)) {
                hideProgressDialog();
                Toast.makeText(ValidationCode.this, "Code valide", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("otp_code", validationCode);
                editor.putBoolean("is_logged_in", true);
                editor.apply();
                startActivity(new Intent(ValidationCode.this, HomeActivity.class));
            } else {
                Toast.makeText(ValidationCode.this, "Le vrai code est : " + otp, Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            actionBar.setElevation(0);

            Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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


    public static void main(String[] args)
    {
        int length = 4;
    }
    static char[] geek_Password()
    {
        String values = "0123456789";
        Random rndm_method = new Random();

        char[] password = new char[4];

        for (int i = 0; i < 4; i++)
        {
            password[i] =
                    values.charAt(rndm_method.nextInt(values.length()));

        }
        return password;
    }
}
