package com.example.stocki_client.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stocki_client.R;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createToolBar();
        initLinks();
        initText();
    }


    private void createToolBar() {
        Toolbar toolbar = findViewById(R.id.aboutToolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLinks() {
        TextView txtLibraries = findViewById(R.id.tvLibrariesText);
        txtLibraries.setText(Html.fromHtml(getString(R.string.values_libraries), Html.FROM_HTML_MODE_LEGACY));
        txtLibraries.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initText() {
        TextView txtAbout = findViewById(R.id.tvAboutText);
        txtAbout.setText(Html.fromHtml(getString(R.string.about_text), Html.FROM_HTML_MODE_LEGACY));
    }
}
