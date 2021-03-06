package com.example.food.court;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.court.Menu.ShopMenuActivity;
import com.example.food.court.Order.OrdersTerminalActivity;
import com.example.food.court.ProfileWindows.ShopProfileActivity;
import com.example.food.court.ProfileWindows.UserProfileActivity;
import com.example.food.court.Shop.Shop;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Restaurent_homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    private static final String TAG = "Restaurent_homepage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_homepage);

        FirebaseUser cuser=FirebaseAuth.getInstance().getCurrentUser();


        Log.i(TAG, "onCreate: shopid :"+cuser.getUid());
        DatabaseReference m=FirebaseDatabase.getInstance().getReference().child("Restaurents").child(cuser.getUid()).child("Info");
        m.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shop shop=dataSnapshot.getValue(Shop.class);
                Log.i(TAG, "onDataChange: shopname:"+shop.getShopName());
                TextView shopname=findViewById(R.id.name);
                TextView shopemail=findViewById(R.id.email);

                shopname.setText(shop.getShopName());
                shopemail.setText(shop.getShopEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ApplicationMode.currentMode="owner";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MessageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();*/
                drawer.closeDrawer(GravityCompat.START);
                Intent profileIntent = new Intent(Restaurent_homepage.this, ShopProfileActivity.class);
                startActivity(profileIntent);

                break;
            case R.id.nav_chat:
              //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //        new ChatFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                Intent i1 = new Intent(getApplicationContext(), ShopMenuActivity.class);
                ApplicationMode.currentMode="owner";
                startActivity(i1);
                break;
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                  //      new ProfileFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                ApplicationMode.ordersViewer = "owner";
                Intent intent = new Intent(getApplicationContext(), OrdersTerminalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                drawer.closeDrawer(GravityCompat.START);
        FirebaseAuth.getInstance().signOut();
        finish();
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
