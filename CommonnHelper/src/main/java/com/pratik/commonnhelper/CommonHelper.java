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
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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


    //    GET SCREEN WIDTH
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    //    GET SCREEN HEIGHT
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    //  GET DEVICE NAME
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }


    //    GET BATTERY CAPACITY
    public static long getBatteryCapacity(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) context.getSystemService(context.BATTERY_SERVICE);
            Long chargeCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                return (long) (((float) chargeCounter / (float) capacity) * 100f);
            }
        }
        return 0;
    }


    //    GET CENTERCROP BITMAP
    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
    }


    //    GET BITMAP FROM URL
    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            return BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }
        return null;
    }


    //    SHARE IMAGE AND TEXT
    public void shareImageAndText(Context mContext, String pathFile, String SUBJECT, String TEXT) {
        try {
            File myFile = new File(pathFile);
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(myFile.getName().substring(myFile.getName().lastIndexOf(".") + 1));
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.setType(type);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile));

            if (SUBJECT != null) {
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
            }

            if (TEXT != null) {
                sharingIntent.putExtra(Intent.EXTRA_TEXT, TEXT);
            }
            if (mContext.getPackageManager().queryIntentActivities(sharingIntent, 65536).size() > 0) {
                mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            } else {
                showDetailApp(mContext, mContext.getPackageName());
            }
        } catch (Exception e) {
            Log.e("", "shareImageAndText error = " + e.toString());
        }
    }

    public void showDetailApp(Context context, String package_name) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + package_name)));
    }
}