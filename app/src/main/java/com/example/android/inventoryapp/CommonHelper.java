package com.example.android.inventoryapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by lakshmivineeth on 7/29/16.
 */
public class CommonHelper {

    public final static int PERMISSION_CODE = 123;
    public final static int REQUEST_PERMISSION_SETTING = 456;
    private static final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void checkPermission(final Activity context) {
        boolean granted;
        for (final String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                        permission)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.permission_title));
                    builder.setMessage(context.getString(R.string.message_permission));
                    builder.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, new String[]{permission}, PERMISSION_CODE);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(context, new String[]{permission}, PERMISSION_CODE);
                }
                MainActivity.permissionGranted = false;
                break;
            } else {
                MainActivity.permissionGranted = true;
            }
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean neverAskAgainSet(Activity context) {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[0]);
    }

    public static void takeToSettings(final Activity context) {
        if (!CommonHelper.neverAskAgainSet(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.permission_title));
            builder.setMessage(context.getString(R.string.take_app_settings));
            builder.setPositiveButton(context.getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                }
            });
            builder.show();
        }
    }
}
