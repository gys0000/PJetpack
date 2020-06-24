package com.gystry.pjetpack.ui.publish;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gystry.libnavannotation.ActivityDestination;
import com.gystry.pjetpack.R;

@ActivityDestination(pageUrl = "main/tabs/publish", asStarter = false)
public class PublishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_publish);
    }
}
