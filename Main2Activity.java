package com.example.memesanskar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.divyanshu.colorseekbar.ColorSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    ImageView upload_image;
    EditText Text;
    TextView et1,et2,et3,et4;
    ImageButton download_meme;
    Button texted;
    int countoftext;
    ColorSeekBar colorSeekBar;
    SeekBar seektextsize;

    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        et1 = (TextView) findViewById(R.id.et1);
        et2 = (TextView) findViewById(R.id.et2);
        et3 = (TextView) findViewById(R.id.et3);
        et4 = (TextView) findViewById(R.id.et4);
        Text = (EditText) findViewById(R.id.Text);
        texted = (Button) findViewById(R.id.texted);
        upload_image = (ImageView) findViewById(R.id.uploadedimage);
        colorSeekBar = findViewById(R.id.seektextcolor);
        seektextsize = findViewById(R.id.seektextsize);
        seektextsize.setMax(45);
        seektextsize.setProgress(30);
        download_meme = (ImageButton) findViewById(R.id.download_meme);

        Permission_Check();

        download_meme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Text.setVisibility(View.INVISIBLE);
                texted.setVisibility(View.INVISIBLE);
                colorSeekBar.setVisibility(View.INVISIBLE);
                seektextsize.setVisibility(View.INVISIBLE);
                Bitmap b = Screenshot.takescreenshotOfRootView(upload_image);
                Bitmap resizedbitmap = Bitmap.createBitmap(b,0,230,400,250);
                saveImage(resizedbitmap,"meme");
                download_meme.setVisibility(View.INVISIBLE);
            }
        });


        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i) {
                if(countoftext == 1){
                    et1.setTextColor(i);
                }
                if(countoftext == 2){
                    et2.setTextColor(i);
                }
                if(countoftext == 3){
                    et3.setTextColor(i);
                }
                if(countoftext == 4){
                    et4.setTextColor(i);
                }
            }
        });

        seektextsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(countoftext == 1){
                    et1.setTextSize(progress);
                }
                if(countoftext == 2){
                    et2.setTextSize(progress);
                }
                if(countoftext == 3){
                    et3.setTextSize(progress);
                }
                if(countoftext == 4){
                    et4.setTextSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        et1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView view = (TextView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        et2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView view = (TextView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        et3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView view = (TextView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        et4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView view = (TextView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewTransformation(TextView view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF mid, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        mid.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void textenabler(View view) {
        Text.setVisibility(View.VISIBLE);
        countoftext++;
        if (countoftext == 1) {
            et1.setVisibility(View.VISIBLE);
        }
        if (countoftext == 2) {
            et2.setVisibility(View.VISIBLE);
        }
        if (countoftext == 3) {
            et3.setVisibility(View.VISIBLE);
        }
        if (countoftext == 4) {
            et4.setVisibility(View.VISIBLE);
        }
        Text.setText("");
        Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input;
                input = Text.getText().toString();
                if (et1.getVisibility() == View.VISIBLE && countoftext == 1) {
                    StringBuilder result = new StringBuilder();
                    for (String line : input.split("\n")) {
                        result.append(line).append("\n");
                    }
                    et1.setText(result.toString());
                }
                if (et2.getVisibility() == View.VISIBLE && countoftext == 2) {
                    StringBuilder result = new StringBuilder();
                    for (String line : input.split("\n")) {
                        result.append(line).append("\n");
                    }
                    et2.setText(result.toString());
                }
                if (et3.getVisibility() == View.VISIBLE && countoftext == 3) {
                    StringBuilder result = new StringBuilder();
                    for (String line : input.split("\n")) {
                        result.append(line).append("\n");
                    }
                    et3.setText(result.toString());
                }
                if (et4.getVisibility() == View.VISIBLE && countoftext == 4) {
                    StringBuilder result = new StringBuilder();
                    for (String line : input.split("\n")) {
                        result.append(line).append("\n");
                    }
                    et4.setText(result.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public void Permission_Check(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                pickImageFromGallery();
            }
        }
        else {
            pickImageFromGallery();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            upload_image.setImageURI(data.getData());
        }
    }
}