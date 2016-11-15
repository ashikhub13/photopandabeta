package com.example.ashik.photopandabeta;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.ashik.photopandabeta.databinding.ActivityMainBinding;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends BasePermissionActivity {


    static {
        System.loadLibrary("ndkTest");
    }

    private double c = 0;
    private Bitmap innerBitmap, innerBitmap2, myBitmap;
    private int imageHeight, imageHeight2, imageWidth, imageWidth2;
    private ArrayList<String> duplisfull = new ArrayList<String>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> screenshotImages = new ArrayList<>();
    private ArrayList<String> darkImages = new ArrayList<>();
    private ArrayList<String> blurImages = new ArrayList<>();
    private ArrayList<String> nostalgiaImages = new ArrayList<>();
    private ArrayList<String> finalImages = new ArrayList<>();


    private ArrayList<ArrayList<String>> sectionedImages = new ArrayList<ArrayList<String>>();
    private TextView textView;
    private ImageAdapter mImageAdapter;
    private ActivityMainBinding mActivityMainBinding;
    private GridLayoutManager mGridLayoutManager;
    private Object imagesLock;


    public native String testfun(String examp);

    public native boolean isDark(int width, int height, byte yuv[], int[] rgba);

    public native boolean isBlur(int width, int height, byte yuv[], int[] rgba);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagesLock = new Object();
        initBinding();
        initRecyclerView();

        // First decode with inJustDecodeBounds=true to check dimensions


        super.requestWriteExternalStorage(1);


    }

    @Override
    public void onWriteExternalStorageGranted(int mPermissionRequestCode) {
        if (mPermissionRequestCode == 1) {
            Log.d("start of ip", "start of image processing");
            images = getAllShownImagesPath();
            getallScreenshots();
            sortByDimensions(images);
            Log.d("images", images.toString());
            Log.d("screenshtimages", screenshotImages.toString());
            Log.d("sectionedimages", sectionedImages.toString());
            if (!images.isEmpty())
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

    private void checkDark(ArrayList<String> images, int i, byte[] array, int[] pixels, Bitmap innerBitmap) {

        if (isDark(innerBitmap.getWidth(), innerBitmap.getHeight(), array, pixels)) {
            //        Log.d("TAGDARK", "dark");
            //        Log.d("TAGDARK", images.get(i));
            darkImages.add(images.get(i));
        }

    }

    private void checkBlur(ArrayList<String> images, int i, byte[] array, int[] pixels, Bitmap innerBitmap) {

        if (isBlur(innerBitmap.getWidth(), innerBitmap.getHeight(), array, pixels)) {
            //        Log.d("TAGBLUR", "blur");
            //        Log.d("TAGBLUR", images.get(i));
            blurImages.add(images.get(i));
        }
    }

    private void compareImages(ArrayList<ArrayList<String>> sectionedImages) {
        Log.d("blurImages", blurImages.toString());
        Log.d("darkImages", darkImages.toString());
        Log.d("nostalgiaImages", nostalgiaImages.toString());


        Log.d("TAG of ic", "start of image compare");

        for (int i = 0; i < sectionedImages.size(); i++) {
            //Log.d("TAG", "start of image compare of" + images.get(i));

            ArrayList<String> duplis = new ArrayList<>();
            ArrayList<String> section = new ArrayList<>();
            section = sectionedImages.get(i);
            //        Log.d("TAG", section.toString());

            for (int k = 0; k < section.size(); k++) {
                String imageDir1 = section.get(k);
                //           Log.d("TAG by", "by" + section.get(k));

                for (int j = k + 1; j < section.size(); j++) {
                    c = 0;
                    //                Log.d("TAG with ", "with" + section.get(j));
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
                        //Log.d("TAG ratio", Double.toString(c / array.length));
                        if (ratio > .8) {
                            //                    Log.d("TAG same?", "same");
                            //                    Log.d("TAG ratio", Double.toString(c / array.length));
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
                    duplisfull.addAll(duplis);
                    //duplisfull.add("1");
                }
            }
        }
        Log.d("TAG end of ic", "end of image compare");
        Log.d("duplisfull", duplisfull.toString());
        finalImages.addAll(duplisfull);
        Log.d("finalImages", finalImages.toString());


    }

    private Bitmap getBitmap(String imageDir, int position) {

        Bitmap original = BitmapHelper.decodeSampledBitmapFromFile(imageDir, 50, 50);
        //    Log.d("TAG imgdir", imageDir);
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

        cursor = this.getContentResolver().query(uri, projection, MediaStore.MediaColumns.SIZE + ">0",
                null, null);


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
                    Bitmap innerBitmap = getBitmap(images.get(i), i);
                    if (innerBitmap != null) {
                        int bytes = innerBitmap.getByteCount();
                        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                        innerBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                        byte[] array = buffer.array();
                        int[] pixels = new int[innerBitmap.getHeight() * innerBitmap.getWidth()];
                        innerBitmap.getPixels(pixels, 0, innerBitmap.getWidth(), 0, 0, innerBitmap.getWidth(),
                                innerBitmap.getHeight());
                        checkDark(images, i, array, pixels, innerBitmap);
                        checkBlur(images, i, array, pixels, innerBitmap);
                        getCreatedDate(images.get(i));
                    }
                }
                finalImages.addAll(darkImages);
                finalImages.addAll(blurImages);
                finalImages.addAll(nostalgiaImages);
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
                msg.what = 3;
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (mImageAdapter) {

                                mImageAdapter.notifyDataSetChanged();
                            }


                        }
                    });
                    msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                    break;
                case 2:
                    doBackgroundUpdate2();
                    break;
                case 3:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (mImageAdapter) {

                                mImageAdapter.notifyDataSetChanged();
                            }


                        }
                    });
                    break;
            }
        }
    };

    private void initBinding() {
        View rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        mActivityMainBinding = DataBindingUtil.bind(rootView);
        mActivityMainBinding.executePendingBindings();
        setContentView(rootView);
    }

    private void initRecyclerView() {

        mImageAdapter = new ImageAdapter(this, finalImages);
        mActivityMainBinding.galleryRecyclerView.setAdapter(mImageAdapter);
        //mActivityMainBinding.galleryRecyclerView.setRefreshListener(this);
//        mActivityImageDetailBinding.imageShowcaseRecyclerView.setRefreshingColorResources(
//                android.R.color.holo_orange_light,
//                android.R.color.holo_blue_light,
//                android.R.color.holo_green_light,
//                android.R.color.holo_red_light
//        );
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mActivityMainBinding.galleryRecyclerView
                .getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int viewWidth = mActivityMainBinding.galleryRecyclerView
                                .getMeasuredWidth();
                        if (viewWidth > 0) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mActivityMainBinding.galleryRecyclerView
                                        .getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                //noinspection deprecation
                                mActivityMainBinding.galleryRecyclerView
                                        .getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            float cardViewWidth = getResources().getDimension(R.dimen.width);
                            int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                            mGridLayoutManager.setSpanCount(newSpanCount);
                            mGridLayoutManager.requestLayout();
                        }
                    }
                });

        mActivityMainBinding.galleryRecyclerView
                .setLayoutManager(mGridLayoutManager);
    }

    private ArrayList<String> getallScreenshots() {
        for (String image : images
                ) {
            if (image.contains("/Screenshots/"))
                screenshotImages.add(image);

        }
        return screenshotImages;
    }

    private void getCreatedDate(String image) {
        File file = new File(image);
        Date lastModDate = new Date(file.lastModified());
        if (isToday(lastModDate)) {
            nostalgiaImages.add(image);
        }
    }

    private boolean isToday(Date date) {
        return areDatesEqual(date, new Date());
    }

    /**
     * Checks if two dates are equal based on Year and Day of Year.
     *
     * @param date1 first date
     * @param date2 second date
     * @return whether the dates are equal or not
     */
    public static boolean areDatesEqual(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if (date1 == null || date2 == null) {
            return false;
        }
        calendar1.setTime(date1);
        calendar2.setTime(date2);

        return ((calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
                && (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)));
    }


}
