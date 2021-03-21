package com.pratik.commonnhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonHelper {

    //    CHECK NETWORK CONNECTED OR NOT
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    //    CONVERT PIXEL TO DP
    public static int px2dp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    //    CONVERT DP TO PIXEL
    public static int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    //    GET CURRENT DATE
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy , HH:mm", Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    //    CREATE FOLDER IF NOT EXIST|
    private static void createFoldersIfNotExist(String filePath) {
        File tempFile = new File(filePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
    }


    //    FREE SYSTEM MEMORY
    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    //  LIST FILES FROM ASSETS FOLDER
    public static String[] listFilesFromAssetFolder(Context context, String path) {
        String[] list = new String[0];
        try {
            list = context.getAssets().list(path);
        } catch (IOException ignored) {
        }
        return list;
    }


    //  GENERATE BITMAP FROM ASSETS FOLDER
    public static Bitmap generateAssetBitmap(Context context, String selectedImage) {
        AssetManager assetManager = context.getAssets();

        InputStream inputStream;
        Bitmap bitmap = null;

        try {
            inputStream = assetManager.open(selectedImage);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //  DELETE FOLDER
    public static boolean deleteFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteFolder(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    //    ROTATE BITMAP
    public static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                break;
            case 3:
                matrix.setRotate(180.0f);
                break;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 6:
                matrix.setRotate(90.0f);
                break;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 8:
                matrix.setRotate(-90.0f);
                break;
            case 1:
            default:
                return bitmap;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    //   GET RANDOM NUMBER
    public int getRandomIndex(int min, int max) {
        return ((int) (Math.random() * ((double) ((max - min) + 1)))) + min;
    }


    //    GET VERSION CODE
    public int getVersionCode(Context mContext, String packageName) {
        try {
            return mContext.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }


    //        GET REAL PATH FROM URI
    public static String getRealPathFromURI(Uri contentURI, Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                return contentURI.getPath();
            }
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("_data"));
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return contentURI.toString();
        }
    }


    //    REQUEST PERMISSION
    public static void requestPermission(Context context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }


    //    CHECK PERMISSION STATUS
    public static boolean isPermissionsGranted(Context context, String[] permissions) {
        boolean allGranted = false;

        for (String permission : permissions) {
            allGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (!allGranted) {
                break;
            }
        }

        return allGranted;  // If allGranted return true that means all permission granted else permission not granted
    }


    //   GO TO ANOTHER ACTIVITY
    public static void goToAnotherActivity(Context context, Class activityClass, Bundle bundle) {
        Intent intent = new Intent(context, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}