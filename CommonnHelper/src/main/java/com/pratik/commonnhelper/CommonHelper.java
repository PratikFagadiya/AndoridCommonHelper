package com.pratik.commonnhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonHelper {

    private static String sFormatEmail = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$";

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

    /**
     * Convertim to time format
     *
     * @param unix is time with long value
     * @return string time
     */
    //    GET UNIX TO DATE
    public static String convertUnix2Date(long unix) {
        if (unix == 0) return "";
        String result;
        Date date = new Date(TimeUnit.SECONDS.toMillis(unix));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        f.setTimeZone(TimeZone.getDefault());
        result = f.format(date);
        return result;
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
            if (mContext.getPackageManager().queryIntentActivities(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
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

    // UNLOCK SCREEN ORIENTATION
    public static void unlockScreenOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    // LOCK SCREEN ORIENTATION
    public static void lockScreenOrientation(Activity activity) {
        int currentOrientation = activity.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    // GET JSON OBJECT FROM XML RESPONSE
    public static JSONObject getJsonObjectFromXmlResponse(String xmlString) {
        JSONObject objectJson = new JSONObject();
        //JSONArray arrayJson = new JSONArray();

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(xmlString));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("string")) {
                            String yourValue = parser.nextText();
                            //arrayJson = new JSONArray(yourValue);
                            objectJson = new JSONObject(yourValue);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objectJson;
    }

    // HIDE KEY BOARD
    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static boolean isJellyBeanOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * @return true if Ice Cream or higher
     */
    public static boolean isICSOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * @return true if HoneyComb or higher
     */
    public static boolean isHoneycombOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * @return true if GingerBreak or higher
     */
    public static boolean isGingerbreadOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * @return true if Froyo or higher
     */
    public static boolean isFroyoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Check SdCard
     *
     * @return true if External Strorage available
     */
    public static boolean isExtStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }


    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * Check an email is valid or not
     *
     * @param email the email need to check
     * @return {@code true} if valid, {@code false} if invalid
     */
    public static boolean isValidEmail(Context context, String email) {
        boolean result = false;
        Pattern pt = Pattern.compile(sFormatEmail);
        Matcher mt = pt.matcher(email);
        if (mt.matches()) {
            result = true;
        }
        return result;
    }

    /**
     * Serializes the Bitmap to Base64
     *
     * @return Base64 string value of a {@linkplain android.graphics.Bitmap} passed in as a parameter
     * @throws NullPointerException If the parameter bitmap is null.
     **/
    public static String toBase64(Bitmap bitmap) {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }

        String base64Bitmap = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBitmap = stream.toByteArray();
        base64Bitmap = Base64.encodeToString(imageBitmap, Base64.DEFAULT);

        return base64Bitmap;
    }

    /**
     * Converts the passed in drawable to Bitmap representation
     *
     * @throws NullPointerException If the parameter drawable is null.
     **/
    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable == null) {
            throw new NullPointerException("Drawable to convert should NOT be null");
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 && drawable.getIntrinsicHeight() <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Converts the given bitmap to {@linkplain java.io.InputStream}.
     *
     * @throws NullPointerException If the parameter bitmap is null.
     **/
    public static InputStream bitmapToInputStream(Bitmap bitmap) throws NullPointerException {

        if (bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream inputstream = new ByteArrayInputStream(baos.toByteArray());

        return inputstream;
    }

    /**
     * Gets the version name of the application. For e.g. 1.9.3
     **/
    public static String getApplicationVersionNumber(Context context) {

        String versionName = null;

        if (context == null) {
            return versionName;
        }

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * Checks if the service with the given name is currently running on the device.
     *
     * @param serviceName Fully qualified name of the server. <br/>
     *                    For e.g. nl.changer.myservice.name
     **/
    public static boolean isServiceRunning(Context ctx, String serviceName) {

        if (serviceName == null) {
            throw new NullPointerException("Service name cannot be null");
        }

        // use application level context to avoid unnecessary leaks.
        ActivityManager manager = (ActivityManager) ctx.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(serviceName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the DB with the given name is present on the device.
     *
     * @param packageName
     * @param dbName
     * @return
     */
    public static boolean isDatabasePresent(String packageName, String dbName) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase("/data/data/" + packageName + "/databases/" + dbName, null, SQLiteDatabase.OPEN_READONLY);
            sqLiteDatabase.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            e.printStackTrace();
            Log.e("TAG", "The database does not exist." + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Exception " + e.getMessage());
        }

        return (sqLiteDatabase != null);
    }

    /**
     * Set Mock Location for test device. DDMS cannot be used to mock location on an actual device.
     * So this method should be used which forces the GPS Provider to mock the location on an actual
     * device.
     **/
    public static void setMockLocation(Context ctx, double longitude, double latitude) {
        // use application level context to avoid unnecessary leaks.
        LocationManager locationManager = (LocationManager) ctx.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, "requiresNetwork" == "", "requiresSatellite" == "", "requiresCell" == "", "hasMonetaryCost" == "", "supportsAltitude" == "", "supportsSpeed" == "", "supportsBearing" == "",

                android.location.Criteria.POWER_LOW, android.location.Criteria.ACCURACY_FINE);

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLongitude(longitude);
        newLocation.setLatitude(latitude);
        newLocation.setTime(new Date().getTime());

        newLocation.setAccuracy(500);

        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

        // http://jgrasstechtips.blogspot.it/2012/12/android-incomplete-location-object.html
        makeLocationObjectComplete(newLocation);

        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
    }

    private static void makeLocationObjectComplete(Location newLocation) {
        Method locationJellyBeanFixMethod = null;
        try {
            locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (locationJellyBeanFixMethod != null) {
            try {
                locationJellyBeanFixMethod.invoke(newLocation);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets random color integer
     **/
    public static int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);

        return Color.argb(255, red, green, blue);
    }

    /**
     * Checks if the url is valid
     */
    public static boolean isValidURL(String url) {
        URL urlObj;

        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            urlObj.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public static boolean isImage(String mimeType) {
        if (mimeType != null) {
            return mimeType.startsWith("image/");
        } else {
            return false;
        }
    }

    public static boolean isAudio(String mimeType) {
        if (mimeType != null) {
            return mimeType.startsWith("audio/");
        } else {
            return false;
        }
    }

    public static boolean isVideo(String mimeType) {
        if (mimeType != null) {
            return mimeType.startsWith("video/");
        } else {
            return false;
        }
    }

    @Nullable
    /**
     * Generates SHA-512 hash for given binary data.
     * @param stringToHash
     * @return
     */
    public static String getSha512Hash(String stringToHash) {
        if (stringToHash == null) {
            return null;
        } else {
            return getSha512Hash(stringToHash.getBytes());
        }
    }

    @Nullable
    /**
     * Generates SHA-512 hash for given binary data.
     */
    public static String getSha512Hash(byte[] dataToHash) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (md != null) {
            md.update(dataToHash);
            byte byteData[] = md.digest();
            String base64 = Base64.encodeToString(byteData, Base64.DEFAULT);

            return base64;
        }
        return null;
    }

    @Nullable
    /**
     * Gets the extension of a file.
     */
    public static String getExtension(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }

        return ext;
    }

    /**
     * Hides the activity's action bar
     *
     * @param activity the activity
     */
    public static void hideActionBar(Activity activity) {
        // Call before calling setContentView();
        if (activity != null) {
            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            if (activity.getActionBar() != null) {
                activity.getActionBar().hide();
            }
        }
    }

    /**
     * Sets the activity in Fullscreen mode
     *
     * @param activity the activity
     */
    public static void setFullScreen(Activity activity) {
        // Call before calling setContentView();
        activity.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}