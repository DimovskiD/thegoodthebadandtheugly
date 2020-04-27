package com.deluxe1.thegoodthebadandtheugly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.deluxe1.thegoodthebadandtheugly.ui.main.TheBadFragment;
import com.deluxe1.thegoodthebadandtheugly.ui.main.TheGoodFragment;
import com.deluxe1.thegoodthebadandtheugly.ui.main.TheUglyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TextView info;
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        info = findViewById(R.id.text);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        navView.getMenu().getItem(0).setCheckable(false);

        setTitle("The Good, The Bad and The Ugly");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else
            info.setText("Permission required to read contacts");
    }

    private void replaceFragment(Fragment fragment) {
        info.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navView.getMenu().getItem(0).setCheckable(true);
        switch (menuItem.getItemId()) {
            case R.id.good:
                replaceFragment(TheGoodFragment.newInstance());
                break;
            case R.id.bad:
                replaceFragment(TheBadFragment.newInstance());
                break;
            case R.id.ugly:
                replaceFragment(TheUglyFragment.newInstance());
                break;
        }
        return true;
    }
}
