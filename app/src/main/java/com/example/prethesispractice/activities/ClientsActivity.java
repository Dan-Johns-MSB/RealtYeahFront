package com.example.prethesispractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.prethesispractice.R;

public class ClientsActivity extends ToolbarMenuActivity {

    private ConstraintLayout clientsToBuyButton;
    private ConstraintLayout clientsToSellButton;
    private ConstraintLayout clientsToRentButton;
    private ConstraintLayout clientsForRentButton;
    private ConstraintLayout clientsOthersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        inflateToolbar(null);

        clientsToBuyButton = findViewById(R.id.clientsPageToBuyButton);
        clientsToSellButton = findViewById(R.id.clientsPageToSellButton);
        clientsToRentButton = findViewById(R.id.clientsPageToRentButton);
        clientsForRentButton = findViewById(R.id.clientsPageForRentButton);
        clientsOthersButton = findViewById(R.id.clientsPageOthersButton);

        clientsToBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("Покупець");
            }
        });
        clientsToSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("Продавець");
            }
        });
        clientsToRentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("Орендар");
            }
        });
        clientsForRentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuClickListener("Орендодавець");
            }
        });
        clientsOthersButton.setOnClickListener(new View.OnClickListener() {
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

    private void menuClickListener(String clientsType) {
        Intent menuNewActivity = new Intent(getApplicationContext(), ClientsSearchActivity.class);
        menuNewActivity.putExtra("clientsType", clientsType);

        passEmployeeData(menuNewActivity);
    }
}