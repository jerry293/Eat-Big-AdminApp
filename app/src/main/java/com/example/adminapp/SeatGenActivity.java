package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import static com.example.adminapp.HomeActivity.hotel;

public class SeatGenActivity extends AppCompatActivity {
    private Button generate,addtoDb,savaBarcode;
    private ImageView barcodeImage;
    private EditText myText;
    private TextView hotelText;
    OutputStream outputStream = null;
    public  Bitmap bitmap;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_gen);
        generate = findViewById(R.id.generate);
        addtoDb = findViewById(R.id.addtoDb);
        myText = findViewById(R.id.barcode_name);
        barcodeImage = findViewById(R.id.barcode_image);
        savaBarcode = findViewById(R.id.saveBarcode);
        hotelText = findViewById(R.id.hotelTextView);
        hotelText.setText(hotel);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 text = myText.getText().toString();
                if(text != null){
                    try {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                         BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        barcodeImage.setImageBitmap(bitmap);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(SeatGenActivity.this, "Please Enter Seat Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addtoDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> seatTxt = new HashMap<>();
                seatTxt.put("availability","available");
                seatTxt.put("tableNo",text);
                DatabaseReference seatRef = FirebaseDatabase.getInstance().getReference().child("Seats");
                seatRef.child(myText.getText().toString()).updateChildren(seatTxt).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SeatGenActivity.this, "Seat has updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        savaBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BitmapDrawable drawable = (BitmapDrawable) barcodeImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                if (ContextCompat.checkSelfPermission(SeatGenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File file = Environment.getExternalStorageDirectory();
                    File dir = new File(file.getAbsolutePath()+"/Table/");
                    dir.mkdir();
                    File file1 = new File(dir,hotel+myText.getText().toString()+".jpg");
                    try {
                        outputStream =new FileOutputStream(file1);
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                        Toast.makeText(SeatGenActivity.this, "Your barcode has saved successfully in internal storage!!", Toast.LENGTH_SHORT).show();
                        outputStream.flush();
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(SeatGenActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }


            }

        });

    }
}