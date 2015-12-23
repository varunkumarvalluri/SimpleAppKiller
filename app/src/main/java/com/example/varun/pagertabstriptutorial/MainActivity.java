package com.example.varun.pagertabstriptutorial;

/**
 * Created by varun on 12/22/15.
 */
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// public class MainActivity extends FragmentActivity {

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);


        // Locate the viewpager in activity_main.xml
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }

    public void startKill(View view) {
        Button ram_btn = (Button) findViewById(R.id.ram_btn);
        Button network_btn = (Button) findViewById(R.id.network_btn);
        Button ram_network_btn = (Button) findViewById(R.id.ram_network_btn);

        ram_btn.setClickable(false);
        network_btn.setClickable(false);
        ram_network_btn.setClickable(false);

        List<String> db_list = new ArrayList<String>(0);
        String message = "";
        switch (view.getId()) {
            case R.id.network_btn:
                message = "Network";
                db_list.add(getString(R.string.network_list));
                // Toast.makeText(getApplicationContext(),"N",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ram_btn:
                message = "RAM";
                db_list.add(getString(R.string.ram_list));
                // Toast.makeText(getApplicationContext(),"R",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ram_network_btn:
                message = "RAM and Network";
                // Toast.makeText(getApplicationContext(),"NR",Toast.LENGTH_SHORT).show();
                db_list.add(getString(R.string.network_list));
                db_list.add(getString(R.string.ram_list));
                break;
        }

        for (String db: db_list) {
            SharedPreferences data = getSharedPreferences(db,MODE_PRIVATE);

            Map<String,?> map = data.getAll();
            for (Map.Entry<String,?> entry: map.entrySet()) {
                if (entry.getValue().toString().equals("true")) {
                    // Toast.makeText(getApplicationContext(), entry.getKey(), Toast.LENGTH_SHORT).show();
                    ActivityManager mActivityManager =
                            (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                    mActivityManager.killBackgroundProcesses(entry.getKey());
                }
            }
        }
        Toast.makeText(getApplicationContext(), "Killed " + message + " " +
                "Heavy Apps Successfully", Toast.LENGTH_SHORT).show();

        ram_btn.setClickable(true);
        network_btn.setClickable(true);
        ram_network_btn.setClickable(true);
    }

    public void quitApp(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}