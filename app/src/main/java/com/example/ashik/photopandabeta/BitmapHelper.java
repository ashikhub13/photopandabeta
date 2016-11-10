package com.example.ashik.photopandabeta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * BitmapHelper :- Helper class for all Bitmap related operations.
 *
 * @author - Rahul Padmakumar
 * @since - 2016-07-04
 */

public class BitmapHelper {

    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String IMAGE_PNG = "image/png";
    private static final int MAX_IMAGE_WIDTH = 1000;
    private static final int MAX_IMAGE_HEIGHT = 1000;

    /**
     * Function to calculate sample size which is used to scale the image.
     * @param options - BitmapFactory Options
     * @param reqWidth - Required width of image.
     * @param reqHeight - Required Height of image.
     * @return - Sample Size
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,
                                             int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * To obtain bitmap from file.
     * @param imgDir - File object from which bitmap is to be obtained.
     * @param reqWidth - Required width of the file.
     * @param reqHeight - Required height of the file.
     * @return - Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String imgDir , int reqWidth, int reqHeight) {

        if(reqHeight == 0 && reqWidth == 0){
            reqHeight = MAX_IMAGE_HEIGHT;
            reqWidth = MAX_IMAGE_WIDTH;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgDir, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgDir, options);
    }

    /**
     * Function to compress the bitmap and write it to the file specified.
     * @param oldFile  Original image file.
     * @param newFile - File to which compressed image is to be written.
     * @param reqWidth - Required width of the image.
     * @param reqheight - Required height of the image.
     * @return File in which compressed image is written.
     * @throws IOException - If file not found.
     */
    public static File compressImageAndWriteToTempFile(File oldFile, File newFile,
                                                       int reqWidth, int reqheight)
            throws IOException {

        // check whether original file i valid.
        if(FileOperationsHelper.isFileValid(oldFile)) {

            // obtain bitmap from file.
            Bitmap bitmap = decodeSampledBitmapFromFile(oldFile.getPath(), reqWidth, reqheight);
            // create output stream from new File.
            FileOutputStream fileOutputStream = FileOperationsHelper.getFileOutputStream(newFile);
            // check original file type and compress accordingly.
            String fileType = PandaUtility.getFileType(oldFile.getPath());
            if(fileType != null) {
                switch (fileType.toLowerCase()) {
                    case IMAGE_PNG:
                        bitmap.compress(Bitmap.CompressFormat.PNG, 75, fileOutputStream);
                        break;
                    case IMAGE_JPEG:
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fileOutputStream);
                }
            }

            fileOutputStream.flush();
            fileOutputStream.close();
            return newFile;
        } else {
            throw new RuntimeException("Invalid File");
        }
    }
}
