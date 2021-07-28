package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.example.adminapp.model.ViewPagerAdapter;
import com.example.adminapp.prevelent.FragmentAll;
import com.example.adminapp.prevelent.FragmentBeverages;
import com.example.adminapp.prevelent.FragmentMainCourse;
import com.example.adminapp.prevelent.FragmentPastas;
import com.example.adminapp.prevelent.FragmentPizzas;
import com.example.adminapp.prevelent.FragmentSandwiches;
import com.example.adminapp.prevelent.FragmentStarter;
import com.google.android.material.tabs.TabLayout;

public class UserViewActivity extends AppCompatActivity {
    static public ProgressDialog loadingBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    Button checkoutBtn;
    static public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        loadingBar = new ProgressDialog(this);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//         pagerAdapter.addFragment(new FragmentAll(),"All");
        pagerAdapter.addFragment(new FragmentStarter(),"starters");
        pagerAdapter.addFragment(new FragmentMainCourse(),"meals");
        pagerAdapter.addFragment(new FragmentSandwiches(),"sandwiches");
        pagerAdapter.addFragment(new FragmentPizzas(),"pizzas");
        pagerAdapter.addFragment(new FragmentPastas(),"pastas");
        pagerAdapter.addFragment(new FragmentBeverages(),"beverages");
        context = UserViewActivity.this;
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}