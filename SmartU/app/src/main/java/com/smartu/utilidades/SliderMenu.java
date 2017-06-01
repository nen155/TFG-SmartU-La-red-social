package com.smartu.utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.smartu.R;
import com.smartu.vistas.AreasActivity;
import com.smartu.vistas.ContactoActivity;
import com.smartu.vistas.LoginActivity;
import com.smartu.vistas.MainActivity;
import com.smartu.vistas.ProyectosActivity;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class SliderMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Toolbar toolbar;
    String TITLES[];
    //int ICONS[]={R.drawable.ic_home_black_48dp};
    public DrawerLayout drawerLayout;                                  // Declaring DrawerLayout
    public ActionBarDrawerToggle mDrawerToggle;
    Context context;
    Activity actualActivity;
    NavigationView navigationView;

    public SliderMenu(Context context, Activity v) {
        this.context = context;
        // this.TITLES = new String[]{context.getString(R.string.home),context.getString(R.string.contact_info)};
        actualActivity = v;
        toolbar = (Toolbar) v.findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);        // drawerLayout object Assigned to the view
        navigationView = (NavigationView) v.findViewById(R.id.nav_view);

    }

    public void inicializateToolbar(String titleActivity) {

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        setToolbar();
        //toolbar.inflateMenu(xml);
        toolbar.setTitle(titleActivity);


        mDrawerToggle = new ActionBarDrawerToggle(actualActivity, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(Sesion.getUsuario(context)!=null) {
                    navigationView.getMenu().setGroupVisible(R.id.autentificado, true);
                    navigationView.getMenu().setGroupVisible(R.id.anonimo,false);
                }
                else {
                    navigationView.getMenu().setGroupVisible(R.id.autentificado, false);
                    navigationView.getMenu().setGroupVisible(R.id.anonimo,true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
                return false;
            }


        }; // drawerLayout Toggle Object Made

        drawerLayout.addDrawerListener(mDrawerToggle); // drawerLayout Listener set to the drawerLayout toggle
        mDrawerToggle.syncState();
    }

    private void setToolbar() {
        ((AppCompatActivity) actualActivity).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) actualActivity).getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            // ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
        if(Sesion.getUsuario(context)!=null) {
            navigationView.getMenu().setGroupVisible(R.id.autentificado, true);
            navigationView.getMenu().setGroupVisible(R.id.anonimo,false);
        }
        else {
            navigationView.getMenu().setGroupVisible(R.id.autentificado, false);
            navigationView.getMenu().setGroupVisible(R.id.anonimo,true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            ((AppCompatActivity) actualActivity).getMenuInflater().inflate(R.menu.main, menu);
            if(Sesion.getUsuario(context)!=null) {
                navigationView.getMenu().setGroupVisible(R.id.autentificado, true);
                navigationView.getMenu().setGroupVisible(R.id.anonimo,false);
            }
            else {
                navigationView.getMenu().setGroupVisible(R.id.autentificado, false);
                navigationView.getMenu().setGroupVisible(R.id.anonimo,true);
            }
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent1=null;
        switch (item.getItemId()) {
            case R.id.nav_inicio_anonimo:
            case R.id.nav_inicio:
                intent1=new Intent(context, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            case R.id.nav_cuenta:
                //Enlace a la web para que edite oosas de su cuenta
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://coloredmoon.com/micuenta"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case R.id.nav_proyectos:
                intent1=new Intent(context, ProyectosActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            case R.id.nav_areas:
                intent1=new Intent(context, AreasActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            case R.id.nav_contacto:
                intent1=new Intent(context, ContactoActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            case R.id.nav_out:
                Sesion.logOut(context);
                FirebaseAuth.getInstance().signOut();
                intent1=new Intent(context, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            case R.id.nav_log:
                intent1=new Intent(context, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
        }


        drawerLayout.closeDrawers();

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
