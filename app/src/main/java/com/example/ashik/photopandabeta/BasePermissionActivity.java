package com.example.ashik.photopandabeta;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public abstract class BasePermissionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void requestWriteExternalStorage(int permissionRequestCode){
        BasePermissionActivityPermissionsDispatcher.onWriteExternalStorageGrantedWithCheck(
                this,
                permissionRequestCode
        );
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public abstract void onWriteExternalStorageGranted(int mPermissionRequestCode);

    /**
     * Function to show snackbar if NeverAskAgain is clicked.
     */
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onNeverAskAgainClicked(){

        Snackbar snackBar = PandaUtility.showPermissionSnackBar(
                this,
                findViewById(android.R.id.content),
                "Enable storage permission"
        );
        if(!isFinishing()){
            snackBar.show();
        }
    }

    /**
     * Function to show toast if permission is denied.
     */
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onPermissionDenied(){
        Toast.makeText(
                BasePermissionActivity.this,
                R.string.message_storage_permission_denied,
                Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Function to create permission dialog
     * which will be shown after denying permission for firt time.
     * @param permissionRequest - Object of type PermissionRequest
     */
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForWriteExtrenalStorage(final PermissionRequest permissionRequest){

        AlertDialog permissionAlertDialog = PandaUtility.createPermissionAlertDialog(
                BasePermissionActivity.this,
                permissionRequest,
                getString(R.string.alert_message_storage_permission)
        );
        if(!isFinishing()){
            permissionAlertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BasePermissionActivityPermissionsDispatcher.onRequestPermissionsResult(
                this,
                requestCode,
                grantResults
        );
    }

}
