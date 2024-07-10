package com.example.wave;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PasswordActivity extends AppCompatActivity {
    private int currentIndex = 0;
    private EditText passwordEdit;
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
        setContentView(R.layout.activity_password);

        setupActionBar();
        setupWindowInsets();

        passwordEdit = findViewById(R.id.password_edit);
        passwordEdit.setInputType(InputType.TYPE_NULL);


        setupButtonListeners();
    }


    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            actionBar.setElevation(0);

            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
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

    private void setupButtonListeners() {
        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> handleDeleteButton());
        Button submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(v -> handleSubmitButton());
        View.OnClickListener listener = v -> handleNumberButton((Button) v);

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void handleSubmitButton() {
        if (passwordEdit.getText().length() < 4) {
            Toast.makeText(this, "Mot de passe invalide", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(PasswordActivity.this, ValidationCode.class));
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
