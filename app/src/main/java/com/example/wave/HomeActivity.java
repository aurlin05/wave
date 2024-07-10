package com.example.wave;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private final int INTERVAL = 30000;
    private EditText amount;
    private ListView mListView;
    private List<Transaction> mTransactions;
    private ActivityResultLauncher<Intent> scanQrResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setTheme(R.style.Theme_Wave);
        setContentView(R.layout.activity_home);
        setupWindowInsets();
        setupActionBar();

        mListView = findViewById(R.id.list_view_transactions);
        mTransactions = TransactionGenerator.generateTransactions(10);

        TransactionAdapter adapter = new TransactionAdapter(this, mTransactions);
        mListView.setAdapter(adapter);
        amount = findViewById(R.id.amount);
        amount.setInputType(InputType.TYPE_NULL);
        amount.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.home_wave_bg));

        qrCodeImageView = findViewById(R.id.qr_code_image_view);

        scanQrResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ScanIntentResult scanResult = ScanIntentResult.parseActivityResult(result.getResultCode(), result.getData());
                        if (scanResult.getContents() != null) {
                            String qrContents = scanResult.getContents();
                            Toast.makeText(HomeActivity.this, "Scanned: " + qrContents, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                            startActivity(intent);
                            // Handle the scanned QR code here
                        } else {
                            Toast.makeText(HomeActivity.this, getString(R.string.canceled), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        runnable = new Runnable() {
            @Override
            public void run() {
                String text = "Ceci change toutes les 30 secondes " + System.currentTimeMillis();
                Bitmap bitmap = QRCodeUtil.generateQRCode(text, 250, 250);
                qrCodeImageView.setImageBitmap(bitmap);
                handler.postDelayed(this, INTERVAL);
            }
        };

        handler.post(runnable);

        qrCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQrResultLauncher.launch(new ScanContract().createIntent(HomeActivity.this, new ScanOptions()));
            }
        });

        ImageView[] imageViews = {
                findViewById(R.id.transfer_button),
                findViewById(R.id.payments_button),
                findViewById(R.id.credit_button),
                findViewById(R.id.bank_button),
                findViewById(R.id.gifts_button)
                // Add other ImageView IDs as needed
        };

        // Apply the touch effect to each ImageView
        for (ImageView imageView : imageViews) {
            applyTouchEffect(imageView);
        }

        ImageView settingsImageView = findViewById(R.id.setting_btn);
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
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

    private void applyTouchEffect(ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        v.performClick(); // Perform the click action
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true; // Indicate the touch event is handled
            }
        });
    }

}
