package com.example.adminapp;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.paperdb.Paper;

public class AddMenuActivity extends AppCompatActivity {


    private TextView starterTxt,main_courseTxt,pizzaTxt,pastaTxt,bevergasTxt,sandwichTxt;
    private Button logoutBtn;

    public void logout(View view){
        Paper.book().destroy();
        Intent intent = new Intent(AddMenuActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        sandwichTxt = findViewById(R.id.sandwiches_cat);
        pastaTxt = findViewById(R.id.pasta_cat);
        main_courseTxt = findViewById(R.id.main_cat);
        starterTxt = findViewById(R.id.starter_cat);
        pizzaTxt = findViewById(R.id.pizza_cat);
        bevergasTxt = findViewById(R.id.beverg_cat);





        starterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this,AddProductActivity.class);
                intent.putExtra("catagary","Starters");
                startActivity(intent);
            }
        });
        pizzaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this,AddProductActivity.class);
                intent.putExtra("catagary","Pizzas");
                startActivity(intent);
            }
        });

        pastaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this,AddProductActivity.class);
                intent.putExtra("catagary","Pastas");
                startActivity(intent);
            }
        });
        sandwichTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this,AddProductActivity.class);
                intent.putExtra("catagary","Sandwiches");
                startActivity(intent);
            }
        });


        main_courseTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this,AddProductActivity.class);
                intent.putExtra("catagary","Main-Course");
                startActivity(intent);
            }
        });
        bevergasTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this,AddProductActivity.class);
                intent.putExtra("catagary","Beverages");
                startActivity(intent);
            }
        });


    }
}