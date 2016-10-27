package com.example.ashik.photopandabeta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    static {
        System.loadLibrary("ndkTest");
    }
    public native String testfun(String examp);

    public native boolean isDark(int width, int height, byte yuv[], int[] rgba);

    public native boolean isBlur(int width, int height, byte yuv[], int[] rgba);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageChecker i = new ImageChecker();
        final ImageView profile = (ImageView) findViewById(R.id.imageView);
        profile.setImageResource(R.drawable.ashik);
//        Picasso.with(this).load(R.drawable.blurredvision).resizeDimen(R.dimen.width, R.dimen.height)
//                .centerCrop().into(profile, new Callback() {
//            @Override
//            public void onSuccess() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {//You will get your bitmap here
//                        Bitmap innerBitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
//
//
//
//                        int bytes = innerBitmap.getByteCount();
//                        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//                        innerBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//
//                        byte[] array = buffer.array();
//                        int[] pixels = new int[innerBitmap.getHeight() * innerBitmap.getWidth()];
//                        innerBitmap.getPixels(pixels, 0, innerBitmap.getWidth(), 0, 0, innerBitmap.getWidth(),
//                                innerBitmap.getHeight());
//                        Log.d("TAG", Boolean.toString(isDark(innerBitmap.getWidth(),innerBitmap.getHeight(),array,pixels)));
//                        Log.d("TAGBLUR", Boolean.toString(isBlur(innerBitmap.getWidth(),innerBitmap.getHeight(),array,pixels)));
//
//
//
//                    }
//                }, 100);
//            }
//
//            @Override
//            public void onError() {
//                Log.d("isDArk","yes");
//
//            }
//        });
        final ImageView profile2 = (ImageView) findViewById(R.id.imageView2);
        profile2.setImageResource(R.drawable.ashik);
        Bitmap innerBitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
        Bitmap innerBitmap2 = ((BitmapDrawable) profile2.getDrawable()).getBitmap();

        if(innerBitmap.getWidth() + innerBitmap.getHeight() >= innerBitmap2.getWidth() + innerBitmap2.getHeight()) {
            i.setOne(innerBitmap);
            i.setTwo(innerBitmap2);
        } else {
            i.setOne(innerBitmap2);
            i.setTwo(innerBitmap);
        }
        Log.d("hashequal", Boolean.toString(i.compareImages()));


        TextView textView= (TextView) findViewById(R.id.helloworld);
        Log.d("TAG", "If this doesn't crash you are a genius:");
        textView.setText(testfun("king ka"));


        //textView.setText();
    }

}
