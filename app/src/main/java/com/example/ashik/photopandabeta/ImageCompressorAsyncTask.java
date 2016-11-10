package com.example.ashik.photopandabeta;

import android.os.AsyncTask;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ImageCompressorAsyncTask: Extension of async task to compress the image.
 *
 * @author - Rahul Padmakumar
 * @since - 2016-07-05
 */

public class ImageCompressorAsyncTask extends AsyncTask {

    private static final String IMAGE = "image";

    private final List<File> mFileToCompressList;
    private final File mNewFolder;
    private final int mReqWidth;
    private final int mReqHeight;
    private OnImageCompressionCompleted mOnImageCompressionCompletedListener;

    public ImageCompressorAsyncTask(File oldFile, File newFolder,
                                    int reqWidth, int reqHeight){

        mFileToCompressList = new ArrayList<>();
        mFileToCompressList.add(oldFile);
        mNewFolder = newFolder;
        mReqWidth = reqWidth;
        mReqHeight = reqHeight;
    }

    public void setmOnImageCompressionCompletedListener(
            OnImageCompressionCompleted mOnImageCompressionCompletedListener) {

        this.mOnImageCompressionCompletedListener = mOnImageCompressionCompletedListener;
    }

    public void removeOnImageCompressionCompletedListener(){
        this.mOnImageCompressionCompletedListener = null;
    }

    @Override
    protected List<File> doInBackground(Object[] params) {

        for(int i = 0; i < mFileToCompressList.size(); i++){

            File file = mFileToCompressList.get(i);
            String fileType = PandaUtility.getFileType(file.getPath());
            if(fileType != null) {
                if (fileType.contains(IMAGE)) {
                    // create a file to write compressed image in send folder.
                    File newFile = FileOperationsHelper.createFile(
                            mNewFolder,
                            DateFormatHelper.getMillisecInString(DateFormatHelper.TIMESTAMP_FORMAT)
                                    .concat(".jpg")
                    );

                    try {
                        // compress image.
                        newFile = BitmapHelper.compressImageAndWriteToTempFile(file, newFile,
                                mReqWidth, mReqHeight);
                        // Replace the original file with compressed version.
                        mFileToCompressList.remove(i);
                        mFileToCompressList.add(i, newFile);
                    } catch (IOException e) {
                        LogUtil.e(getClass().getSimpleName(), e.getMessage());
                    }
                }
            }
        }
        return mFileToCompressList;
    }

    @Override
    protected void onPostExecute(Object o) {

        if(o instanceof List && mOnImageCompressionCompletedListener != null){
            //noinspection unchecked
            mOnImageCompressionCompletedListener.onImageCompressionSuccess((List<File>) o);
        }
    }

    /**
     * Callback to inform completion of image compression.
     */
    public interface OnImageCompressionCompleted{

        void onImageCompressionSuccess(List<File> fileList);
    }
}
