package com.example.adminapp;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;



public class AddProductActivity extends AppCompatActivity {
    private  String catagaryName,pName,pPrice,pDescription;
    private EditText productName,productDescription,productPrice;
    ImageView selectProductImage;
    Button addNewProduct;
    private Uri selectedImage;
    private String saveCurrentDate,saveCurrentTime;
    private String productRandomKey;
    private StorageReference ProductImageRef;
    private String downloadImageUrl;
    private DatabaseReference ProductDetailRef;
    public ProgressDialog loadingBar;



    public void addNewProductClick(View view){
        loadingBar.setTitle("Sending to database");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        validateData();
    }
    public void validateData(){
        pName = productName.getText().toString();
        pDescription = productDescription.getText().toString();
        pPrice = productPrice.getText().toString();

        if(selectProductImage == null){
            Toast.makeText(this, "please select product image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pName)){
            Toast.makeText(this, "please enter product name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pDescription)){
            Toast.makeText(this, "Please enter product description ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pPrice)){
            Toast.makeText(this, "Please enter product prices...", Toast.LENGTH_SHORT).show();
        }else {
            storeProductInfo();
        }
    }
    private void storeProductInfo(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;

        final StorageReference filepath =ProductImageRef.child(selectedImage.getLastPathSegment() + productRandomKey);

        final UploadTask uploadTask = filepath.putFile(selectedImage);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AddProductActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
             //   Toast.makeText(AddProductActivity.this, "Image is uploadede Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw(task.getException());
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageUrl = task.getResult().toString();
                           // Toast.makeText(AddProductActivity.this, "got Image Url Succefully", Toast.LENGTH_SHORT).show();
                            saveProductIntoDatabase();
                        }
                    }
                });

            }
        });

    }

    private void saveProductIntoDatabase() {
        final HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("image",downloadImageUrl);
        productMap.put("pname",pName);
        productMap.put("description",pDescription);
        productMap.put("price",pPrice);
        productMap.put("catagary",catagaryName);

        ProductDetailRef.child(catagaryName).child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            ProductDetailRef.child("All").child(productRandomKey).updateChildren(productMap);
                            Toast.makeText(AddProductActivity.this, "Your product is successfully added..", Toast.LENGTH_SHORT).show();
                            recreate();
                        }else {
                            Intent intent = new Intent(getApplicationContext(),AddMenuActivity.class);
                            startActivity(intent);
                            Toast.makeText(AddProductActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void getPhoto(){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }catch (Exception e){
            Log.i("Cancle","Product Image Not Selected");
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 ){
            if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getPhoto();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            selectedImage = data.getData();
        }catch (Exception e){
            Log.i("Cancle","Product Image Not Selected");
        }

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                selectProductImage.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.i("Error",""+e.getMessage());

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescreption);
        productPrice = findViewById(R.id.productPrice);
        selectProductImage = findViewById(R.id.select_product_image);
        addNewProduct = findViewById(R.id.addNewProduct);
        catagaryName = getIntent().getStringExtra("catagary").toString();

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product");
        ProductDetailRef = FirebaseDatabase.getInstance().getReference().child("Product");
        loadingBar = new ProgressDialog(this);

        selectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else {
                        getPhoto();
                }
            }
        });
    }


}