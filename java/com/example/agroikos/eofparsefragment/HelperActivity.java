package com.example.agroikos.eofparsefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.lang.reflect.Field;

/**
 * Created by root on 24/12/2015.
 */
public class HelperActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    public void helperOnCreate(int activityLayout, boolean has_arrow) {
        setContentView(activityLayout);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar.setTitle("Εισαγωγή");
        mToolBar.setNavigationIcon(R.drawable.ic_arrows);
        setSupportActionBar(mToolBar);

        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            Field mDragger = drawer.getClass().getDeclaredField("mLeftDragger"); //mRightDragger for right obviously
            mDragger.setAccessible(true);

            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(drawer);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);

            int edge = 0;
            edge = mEdgeSize.getInt(draggerObj);
            mEdgeSize.setInt(draggerObj, edge * 5); //optimal value as for me, you may set any constant in dp
            // You can set it even to the value you want like mEdgeSize.setInt(draggerObj, 150); for 150dp

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personal_pharmacy) {
            startActivity(new Intent(getApplicationContext(), Farmakeio.class));
        } else if (id == R.id.nav_register_med) {
            if (!this.getClass().getSimpleName().equals("TwoButtons"))
                startActivity(new Intent(getApplicationContext(), TwoButtons.class));
        } else if (id == R.id.nav_needs) {

        } else if (id == R.id.nav_donations) {
            //startActivity(new Intent(getApplicationContext(), Donations.class));
        } else if (id == R.id.nav_profile) {
            //startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_communication) {
            //startActivity(new Intent(getApplicationContext(), Communication.class));
        } else if (id == R.id.nav_share) {
            //startActivity(new Intent(getApplicationContext(), Share.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
