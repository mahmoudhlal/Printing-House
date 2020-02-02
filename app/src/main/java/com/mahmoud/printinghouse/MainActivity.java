package com.mahmoud.printinghouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mahmoud.printinghouse.Utils.Constants.Constants;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefManager.getInstance(getApplicationContext()).getUserData()!=null)
            Constants.CURRENT_ROLE =
                    SharedPrefManager.getInstance(getApplicationContext()).getUserData().getRole();

        Fresco.initialize(this);
    }
}
