package com.example.prethesispractice.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prethesispractice.R;
import com.example.prethesispractice.httpApi.HttpApi;
import com.example.prethesispractice.httpApi.HttpRequest;

public class ToolbarMenuActivity extends AppCompatActivity {

    final HttpRequest httpApi = new HttpApi().getRetrofit().create(HttpRequest.class);
    protected String userEmployeeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent moveToActivity = null;
        int menuOptionId = item.getItemId();

        if (menuOptionId == R.id.toolbarMenuOption1) {
            moveToActivity = new Intent(getApplicationContext(), MainActivity.class);
        } else if (menuOptionId == R.id.toolbarMenuOption2) {
            moveToActivity = new Intent(getApplicationContext(), ClientsActivity.class);
        } else if (menuOptionId == R.id.toolbarMenuOption3) {
            moveToActivity = new Intent(getApplicationContext(), ObjectsActivity.class);
        } else if (menuOptionId == R.id.toolbarMenuOption4) {
            moveToActivity = new Intent(getApplicationContext(), OperationsActivity.class);
        } else {
            moveToActivity = new Intent(getApplicationContext(), EmployeeSearchActivity.class);
        }

        if (moveToActivity != null) {
            passEmployeeData(moveToActivity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void inflateToolbar(String employeeName) {
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        getSupportActionBar().setTitle(R.string.empty_string);
        Drawable menuIcon = getDrawable(R.drawable.baseline_menu_icon);
        toolbar.setOverflowIcon(menuIcon);

        if (employeeName == null || employeeName.isBlank()) {
            userEmployeeName = getIntent().getStringExtra("employeeName");
        } else {
            userEmployeeName = employeeName;
        }

        if (userEmployeeName != null) {
            TextView toolbarLogin = findViewById(R.id.employeeNameTextView);

            toolbarLogin.setText(userEmployeeName);
        }

        ImageView adminPass = findViewById(R.id.companyAvatarImageView);
        ImageView userPass = findViewById(R.id.userAvatarImageView);

        adminPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(EmployeeSearchActivity.class);
            }
        });
        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(MainActivity.class);
            }
        });
    }

    public void passEmployeeData(Intent passTo) {
        String employeeNameExtra = getIntent().getStringExtra("employeeName");

        if (employeeNameExtra != null) {
            passTo.putExtra("employeeName", employeeNameExtra);
        }

        passTo.putExtra("login", getIntent().getStringExtra("login"));
        passTo.putExtra("role", getIntent().getStringExtra("role"));
        passTo.putExtra("token", getIntent().getStringExtra("token"));
        passTo.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

        startActivity(passTo);
    }

    protected void basicActivitySwitch(Class<?> activityClass) {
        Intent moveToActivity = new Intent(getApplicationContext(), activityClass);

        if (moveToActivity != null) {
            passEmployeeData(moveToActivity);
        }
    }

    public HttpRequest getHttpApi() {
        return httpApi;
    }
}