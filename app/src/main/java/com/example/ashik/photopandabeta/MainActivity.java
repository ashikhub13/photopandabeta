package com.example.ashik.photopandabeta;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MainActivity extends BasePermissionActivity {


    static {
        System.loadLibrary("ndkTest");
    }

    private double c = 0;
    private Bitmap innerBitmap, innerBitmap2, myBitmap;
    private int imageHeight, imageHeight2, imageWidth, imageWidth2;
    private ArrayList<ArrayList<String>> duplisfull = new ArrayList<ArrayList<String>>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> darkImages = new ArrayList<>();
    private ArrayList<String> blurImages = new ArrayList<>();
    private ArrayList<ArrayList<String>> sectionedImages = new ArrayList<ArrayList<String>>();
    private TextView textView;


    public native String testfun(String examp);

    public native boolean isDark(int width, int height, byte yuv[], int[] rgba);

    public native boolean isBlur(int width, int height, byte yuv[], int[] rgba);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // First decode with inJustDecodeBounds=true to check dimensions


        super.requestWriteExternalStorage(1);


    }

    @Override
    public void onWriteExternalStorageGranted(int mPermissionRequestCode) {
        if (mPermissionRequestCode == 1) {
            Log.d("start of ip", "start of image processing");
            images = getAllShownImagesPath();
            sortByDimensions(images);
            Log.d("images", images.toString());
            Log.d("sectionedimages", sectionedImages.toString());
            if(!images.isEmpty())
            doBackgroundUpdate1();
                Log.d("end of ip", "end of image processing");
//            Picasso.with(this).load(images.get(0).toString()).into(whoamiwith);
//            Log.d("orrgnl image", images.get(0).toString());
//            Log.d("dp", duplis.toString());

        }
    }

    private void sortByDimensions(ArrayList<String> images) {
        ArrayList<String> sortedImages = new ArrayList<>();
        if (!images.isEmpty() && images.get(0) != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(images.get(0), options);
            imageHeight = options.outHeight;
            imageWidth = options.outWidth;
        }
        for (int s = 0; s < images.size(); s++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(images.get(s), options);
            if (options.outHeight == 0 || options.outWidth == 0) {
                images.remove(s);
            } else if (options.outHeight == imageHeight && options.outWidth == imageWidth) {
                sortedImages.add(images.get(s));
            } else {
                sectionedImages.add(sortedImages);
                sortedImages = new ArrayList<>();
                sortedImages.add(images.get(s));
                imageHeight = options.outHeight;
                imageWidth = options.outWidth;
            }
        }
        sectionedImages.add(sortedImages);
    }

    private void checkDark(ArrayList<String> images, int i) {

        Bitmap innerBitmap = getBitmap(images.get(i), i);
        if (innerBitmap != null) {
            int bytes = innerBitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
            innerBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

            byte[] array = buffer.array();
            int[] pixels = new int[innerBitmap.getHeight() * innerBitmap.getWidth()];
            innerBitmap.getPixels(pixels, 0, innerBitmap.getWidth(), 0, 0, innerBitmap.getWidth(),
                    innerBitmap.getHeight());
            if (isDark(innerBitmap.getWidth(), innerBitmap.getHeight(), array, pixels)) {
                Log.d("TAGDARK", "dark");
                Log.d("TAGDARK", images.get(i));
                darkImages.add(images.get(i));
            }
        }
    }

    private void checkBlur(ArrayList<String> images, int i) {

        Bitmap innerBitmap = getBitmap(images.get(i), i);
        if (innerBitmap != null) {
            int bytes = innerBitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
            innerBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

            byte[] array = buffer.array();
            int[] pixels = new int[innerBitmap.getHeight() * innerBitmap.getWidth()];
            innerBitmap.getPixels(pixels, 0, innerBitmap.getWidth(), 0, 0, innerBitmap.getWidth(),
                    innerBitmap.getHeight());
            if (isBlur(innerBitmap.getWidth(), innerBitmap.getHeight(), array, pixels)) {
                Log.d("TAGBLUR", "blur");
                Log.d("TAGBLUR", images.get(i));
                blurImages.add(images.get(i));

            }
        }
    }

    private void compareImages(ArrayList<ArrayList<String>> sectionedImages) {
        Log.d("blurImages", blurImages.toString());
        Log.d("darkImages", darkImages.toString());


        Log.d("TAG of ic", "start of image compare");

        for (int i = 0; i < sectionedImages.size(); i++) {
            //Log.d("TAG", "start of image compare of" + images.get(i));

            ArrayList<String> duplis = new ArrayList<>();
            ArrayList<String> section = new ArrayList<>();
            section = sectionedImages.get(i);
            Log.d("TAG", section.toString());

            for (int k = 0; k < section.size(); k++) {
                String imageDir1 = section.get(k);
                Log.d("TAG by", "by" + section.get(k));

                for (int j = k + 1; j < section.size(); j++) {
                    c = 0;
                    Log.d("TAG with ", "with" + section.get(j));
                    String imageDir2 = section.get(j);

                    innerBitmap = getBitmap(imageDir1, k);
                    innerBitmap2 = getBitmap(imageDir2, j);
                    if (innerBitmap != null && innerBitmap2 != null) {
                        int bytes = innerBitmap.getByteCount();
                        //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
                        //int bytes = b.getWidth()*b.getHeight()*4;

                        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                        innerBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                        byte[] array = buffer.array();

                        int bytes2 = innerBitmap2.getByteCount();
                        //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
                        //int bytes = b.getWidth()*b.getHeight()*4;

                        ByteBuffer buffer2 = ByteBuffer.allocate(bytes2); //Create a new buffer
                        innerBitmap2.copyPixelsToBuffer(buffer2); //Move the byte data to the buffer

                        byte[] array2 = buffer2.array();

                        byte[] differenceArray = new byte[array.length];
                        for (int x = 0; x < array.length; x++) {
                            differenceArray[x] = (byte) (array[x] - array2[x]);
                            if (differenceArray[x] == 0) {
                                c++;
                            }
                        }
                        double ratio = (c / array.length);
                        Log.d("TAG ratio", Double.toString(c / array.length));
                        if (ratio > .8) {
                            Log.d("TAG same?", "same");
                            Log.d("TAG ratio", Double.toString(c / array.length));
                            duplis.add(section.get(j));
                            section.remove(j);
//                        Log.d("similarity level", Integer.toString(c));
//                        Log.d("orrgnl image loop",imageDir1 );
//                        Log.d("length", Integer.toString(array.length));
//                        Log.d("image", images.get(i));

                        }
                    }
                }
                if (!duplis.isEmpty()) {
                    duplis.add(section.get(k));
                    duplisfull.add(duplis);
                }
            }
        }
        Log.d("TAG end of ic", "end of image compare");
        Log.d("duplisfull", duplisfull.toString());

    }

    private Bitmap getBitmap(String imageDir, int position) {

        Bitmap original =BitmapHelper.decodeSampledBitmapFromFile(imageDir,50,50);
        Log.d("TAG imgdir", imageDir);
        return original;

    }

    private boolean compareDimensions(String img1, String img2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(img1, options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
        BitmapFactory.decodeFile(img2, options);
        imageHeight2 = options.outHeight;
        imageWidth2 = options.outWidth;
        if (imageHeight == imageHeight2 && imageWidth == imageWidth2)
            return true;
        return false;
    }

    /**
     * Getting All Images Path.
     *
     * @return ArrayList with images Path
     */
    private ArrayList<String> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = this.getContentResolver().query(uri, projection, MediaStore.MediaColumns.SIZE+">0",
                 null , null);


        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }


    private void doBackgroundUpdate1() {
        Thread backgroundThread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    checkDark(images, i);
                    checkBlur(images, i);
                }
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        backgroundThread.start();
    }

    private void doBackgroundUpdate2() {
        Thread backgroundThread = new Thread() {
            @Override
            public void run() {
                compareImages(sectionedImages);
                // finished second step
                Message msg = Message.obtain();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        };
        backgroundThread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    doBackgroundUpdate2();
                    break;
                case 2:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textView= (TextView) findViewById(R.id.helloworld);
                            textView.setText(duplisfull.toString());
                            ImageView imageView =(ImageView) findViewById(R.id.imageView);
                            ImageView imageView2 =(ImageView) findViewById(R.id.imageView2);
                            imageView.setImageURI(Uri.parse(duplisfull.get(0).get(0)));
                            imageView2.setImageURI(Uri.parse(duplisfull.get(0).get(1)));


                        }
                    });
                    break;
            }
        }
    };

}
