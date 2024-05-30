package com.example.prethesispractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.prethesispractice.R;

public class ObjectsActivity extends ToolbarMenuActivity {
    private ConstraintLayout objectsToSellButton;
    private ConstraintLayout objectsForRentButton;
    private ConstraintLayout objectsOthersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);
        inflateToolbar(null);

        objectsToSellButton = findViewById(R.id.objectsPageToSellButton);
        objectsForRentButton = findViewById(R.id.objectsPageForRentButton);
        objectsOthersButton = findViewById(R.id.objectsPageOthersButton);

        objectsToSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("Виставлений на продаж");
            }
        });
        objectsForRentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("Виставлений на оренду");
            }
        });
        objectsOthersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("OTHERS");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void menuClickListener(String objectsType) {
        Intent menuNewActivity = new Intent(getApplicationContext(), ObjectsSearchActivity.class);
        menuNewActivity.putExtra("objectsType", objectsType);

        passEmployeeData(menuNewActivity);
    }
}