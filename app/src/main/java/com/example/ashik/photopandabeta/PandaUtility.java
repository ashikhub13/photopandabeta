package com.example.ashik.photopandabeta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.MimeTypeMap;

import permissions.dispatcher.PermissionRequest;

/**
 * Created by ashik on 2/11/16.
 */
public class PandaUtility {

    /**
     * To get type of file from filename with extension.
     * @param string - Filename with extension e.g: myVideo.mp4
     * @return mime type of media.
     */
    public static String getFileType(String string) {

        String extension = getExtensionFromString(string);
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return null;
    }

    /**
     * To get extension from file path.
     * @param filePath - Path of the file
     * @return extension of the file.
     */
    @SuppressWarnings("WeakerAccess")
    public static String getExtensionFromString(String filePath) {

        if (filePath != null) {
            int index = filePath.lastIndexOf('.');
            if (index != 0) {
                return filePath.substring(index + 1);
            }
        }
        return null;
    }

    /**
     * Function to create permission snackbar.
     * @param context - Activity context in which the snackbar is to be shown.
     * @param view - View object for creating snackbar.
     * @param message - Message to be shown in snackbar.
     * @return created snackbar.
     */
    public static Snackbar showPermissionSnackBar(
            final Context context,
            View view,
            String message){

        Snackbar snackbar = getSnackBar(view, message);
        snackbar.show();
        snackbar.setAction(R.string.label_enable, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                if(context instanceof Activity){
                    ((Activity)context).startActivityForResult(intent, 1111);
                }
            }
        });
        return snackbar;
    }

    public static Snackbar getSnackBar(View view, String text){

        return Snackbar.make(view, text, Snackbar.LENGTH_LONG);
    }

    /**
     * Function to create Alert Dialog only for permissions.
     * @param context - Activity context in which dialog is to be shown.
     * @param permissionRequest - PermissionRequest Object.
     * @param message - Message to be shown in PermissionAlertDialog.
     * @return - Created permission alert dialog.
     */
    public static AlertDialog createPermissionAlertDialog(
            Context context,
            final PermissionRequest permissionRequest,
            String message){

        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.label_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionRequest.proceed();
                    }
                })
                .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionRequest.cancel();
                    }
                })
                .create();
    }

    public static boolean isUrl(String path){
        return path.startsWith("http") || path.startsWith("https");
    }
}
