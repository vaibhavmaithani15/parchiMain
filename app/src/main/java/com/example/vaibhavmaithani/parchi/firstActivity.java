package com.example.vaibhavmaithani.parchi;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class firstActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialSearchView mySearchView;
    CardView moneyManagement;
    CardView bet;
    CardView scan;
    CardView listOfItems;
    CardView chat;
    CardView addressBook;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        mySearchView=(MaterialSearchView)findViewById(R.id.search_view);
       // mySearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        moneyManagement= (CardView) findViewById(R.id.moneyCard_id);
        bet= (CardView) findViewById(R.id.betCard_id);
        scan= (CardView) findViewById(R.id.scanCard_id);
        listOfItems= (CardView) findViewById(R.id.listitemCard_id);
        chat= (CardView) findViewById(R.id.chatCard_id);
        addressBook= (CardView) findViewById(R.id.addressBookCard_id);
        mAuth=FirebaseAuth.getInstance();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nagivation_view);
        navigationView.setNavigationItemSelectedListener(this);


        Bundle bundle=getIntent().getExtras();
        //userName=bundle.getString("name");
       // navigationName.setText(userName);

//        mySearchView.setBackgroundResource(R.color.trans);
//        mySearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));


        ActionBarDrawerToggle toogle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toogle);
        toogle.syncState();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent intent=new Intent(firstActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };








        mySearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        //For voice search

        mySearchView.setVoiceSearch(true);

        mySearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        moneyManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moneyManagement();
            }
        });

        bet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet();

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemScanned();

            }
        });

        listOfItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfItems();

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
            }
        });


        addressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBook();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.profile_id:
                myProfile();
                Toast.makeText(this, "Profile Click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.moneymanagement_id:
                moneyManagement();
                break;

            case R.id.scanneditems_id:
                itemScanned();
                break;

            case R.id.logout_id:
                mAuth.signOut();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        //if (mySearchView.isSearchOpen()) {
//            mySearchView.closeSearch();
//        } else {
//            super.onBackPressed();
//        }


//        else {
////            Intent a = new Intent(Intent.ACTION_MAIN);
////            a.addCategory(Intent.CATEGORY_HOME);
////            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(a);
      super.onBackPressed();
//        }
    }




    public void moneyManagement()
    {
       Fragment fragment=null;
        fragment=new MoneyFragement();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }
    public void myProfile(){
        Fragment fragment=null;
        fragment=new MyProfileFragememnt();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }
    public void itemScanned(){
        Fragment fragment=null;
        fragment=new ItemScannedFragement();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }

    public void bet(){
        Fragment fragment=null;
        fragment=new BetFragement();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }

    public void listOfItems(){
        Fragment fragment=null;
        fragment=new ListOfItemsFragement();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }

    public void chat(){
        Fragment fragment=null;
        fragment=new ChatFragment();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }
    public void addressBook()
    {
        Fragment fragment=null;
        fragment=new AddressBookFragement();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.first_activity,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mySearchView.setMenuItem(item);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mySearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
