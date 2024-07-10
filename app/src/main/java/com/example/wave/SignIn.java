package com.example.wave;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignIn extends AppCompatActivity {
    private int currentIndex = 0;
    private EditText passwordEdit;
    private AlertDialog progressAlertDialog;
    private final int[] roundedButtonIds = {
            R.id.rounded_1, R.id.rounded_2, R.id.rounded_3, R.id.rounded_4
    };
    private final int[] buttonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setTheme(R.style.Theme_Wave);
        setContentView(R.layout.activity_sign_in);

        setupActionBar();
        setupWindowInsets();

        passwordEdit = findViewById(R.id.password_edit);
        passwordEdit.setInputType(InputType.TYPE_NULL);

        progressAlertDialog = createProgressDialog(this);
        setupButtonListeners();
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

    private void setupButtonListeners() {
        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> handleDeleteButton());
        View.OnClickListener listener = v -> handleNumberButton((Button) v);

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void handleSubmitButton() {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String otpCode = sharedPreferences.getString("otp_code", "");
            if (passwordEdit.getText().toString().equals(otpCode)){
                startActivity(new Intent(SignIn.this, HomeActivity.class));
                finish();
            }
            else{
                View container = findViewById(R.id.linearLayout);
                if (currentIndex > 0) {
                    for (int i = 0; i < roundedButtonIds.length; i++) {
                        View roundedButton = findViewById(roundedButtonIds[i]);
                        roundedButton.setBackgroundResource(R.drawable.unfilled_shape);
                        currentIndex--;
                        passwordEdit.setText("");
                    }
                }
                Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_annimation);
                container.startAnimation(shakeAnimation);
                Toast.makeText(this, "Vrai code est : "+otpCode, Toast.LENGTH_SHORT).show();
            }

    }

    private void handleDeleteButton() {
        if (currentIndex > 0) {
            currentIndex--;
            View roundedButton = findViewById(roundedButtonIds[currentIndex]);
            roundedButton.setBackgroundResource(R.drawable.unfilled_shape);
            int length = passwordEdit.getText().length();
            if (length > 0) {
                passwordEdit.getText().delete(length - 1, length);
            }
        }
    }

    private void handleNumberButton(Button button) {

        if (currentIndex < roundedButtonIds.length) {
            View roundedButton = findViewById(roundedButtonIds[currentIndex]);
            roundedButton.setBackgroundResource(R.drawable.filled_shape);
            String text = button.getText().toString();
            passwordEdit.append(text);
            currentIndex++;
        }
        if (currentIndex == roundedButtonIds.length) {
            handleSubmitButton();
        }
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