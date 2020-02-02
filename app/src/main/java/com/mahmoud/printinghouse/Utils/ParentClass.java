package com.mahmoud.printinghouse.Utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mahmoud.printinghouse.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParentClass extends AppCompatActivity {

    private static final String TAG = "ParentClass";

    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final int STORAGE_PERMISSION_REQUEST=400;

    public static int paginate = 0;
    public static String lat;
    public static String lng;
    public static int time_end = 0;
    static boolean checked = true;
    protected LocationManager locationManager;

    private static ProgressDialog progressDialog;


    // public static SharedPrefManager sharedPrefManager;
    public List<String> list_names;
    public List<Integer> list_idss;
    //    public List<SpinnerData> spinner_list;
//    KProgressHUD hud;
    public static List<String> fragments = new ArrayList<String>();
    static FragmentManager manager;

    public static Typeface typeface_bold;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sharedPrefManager = new SharedPrefManager(this);
        manager = getSupportFragmentManager();
//        hud = KProgressHUD.create(this);
        //typeface_bold = Typeface.createFromAsset(getAssets(), "fonts/Cairo-SemiBold.ttf");

    }


    public void languageConfiguration(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public boolean check_empty(EditText check) {
        if (TextUtils.isEmpty(check.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }


    public static String getLocalization(Context context) {
        return context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE).getString("language", "ar");
    }

    public static void setLanguage(Context ctx, String type, String language) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE).edit();
        editor.putString("type", type);
        editor.putString("language", language);
        editor.apply();
    }

    public boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permisson", "Permission is granted");
                return true;
            } else {

                Log.v("permisson", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permisson", "Permission is granted");
            return true;
        }
    }

    public boolean isStoargePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permisson", "Permission is granted");
                return true;
            } else {

                Log.v("permisson", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permisson", "Permission is granted");
            return true;
        }
    }

    public String getRealPathFromUri(Context context, Uri contentUri) {
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

    public void imageBrowse(int PICK_IMAGE_REQUEST) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }


//    public static void replaceFragment(Fragment fragment, FrameLayout frameLayout,String title) {
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.replace(frameLayout.getId(), fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.addToBackStack(title);
//        ft.commitAllowingStateLoss();
//
//    }




    public static void makeToast(Context context, int msg) {
        Toast.makeText(context, context.getResources().getString(msg), Toast.LENGTH_SHORT).show();
    }

    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static int colour_converter(String hexa_Color) {
        return Color.parseColor(hexa_Color);
    }

    public static void Peform_log(String log_name, String log_msg) {
        Log.e(log_name, log_msg);
    }

    public void time_handler(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                time_end = 1;
            }
        }, time);
    }

    public void configuration_for_language(String language) {
        int currentOrientation = this.getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);

        }

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }
    }



    public static void setAnimate(final Context context, final ViewGroup Container, final ImageView DownArrow, final ViewGroup parent, final int height, float rotation) {
        if (Container.getVisibility() == View.GONE) {
            DownArrow.animate().rotation(rotation).setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    DownArrow.setEnabled(false);
                    Container.setVisibility(View.VISIBLE);
                    int px = Math.round(TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics()));
                    ValueAnimator anim = ValueAnimator.ofInt(parent.getMeasuredHeight(), parent.getMeasuredHeight() + px);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
                            layoutParams.height = val;
                            parent.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(300);
                    anim.start();

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    DownArrow.setEnabled(true);
                }
            });
        } else {
            DownArrow.animate().rotation(0).setDuration(300).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    DownArrow.setEnabled(false);
                    int px = Math.round(TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics()));
                    ValueAnimator anim = ValueAnimator.ofInt(parent.getMeasuredHeight(), parent.getMeasuredHeight() - px);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
                            layoutParams.height = val;
                            parent.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(300);
                    anim.start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Container.setVisibility(View.GONE);
                    DownArrow.setEnabled(true);
                }
            });
        }
    }


    public static void handleException(Context context, Throwable t) {
        if (t instanceof SocketTimeoutException)
            makeToast(context, "خطأ فى الانترنت");
        else if (t instanceof UnknownHostException)
            makeToast(context, "خطأ فى الاتصال");

        else if (t instanceof ConnectException)
            makeToast(context, "خطأ فى الاتصال");
        else
            makeToast(context, t.getLocalizedMessage());

    }

    public void dismiss_keyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public Bitmap roundCornerImage(Bitmap raw, float round) {
        int width = raw.getWidth();
        int height = raw.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, round, round, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        canvas.drawBitmap(raw, rect, rect, paint);

        return result;
    }

//    public void showdialog() {
//        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("برجاء الانتظار")
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();
//    }
//
//    public void dismis_dialog() {
//        hud.dismiss();
//    }


//    /*public static void replaceFragment(Fragment fragment, String title) {
//
//        manager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(title)
//                .commitAllowingStateLoss();
//
////        Log.e("backStateName", title);
////
////        //fragment not in back stack, create it.
////        if (fragment.isAdded()) {
////            return; //or return false/true, based on where you are calling from
////        }
////        FragmentTransaction ft = manager.beginTransaction();
////        if (!fragments.contains(title)) {
////            Log.e("check", "added");
////            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
////          //  ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_out_right, R.anim.enter_from_right, R.anim.exit_out_left);
////            ft.replace(containerID, fragment);
////            ft.addToBackStack(title);
////            ft.commitAllowingStateLoss();
////
////            fragments.add(title);
////            System.out.println("backStateName" + fragments);
////        } else {
////            try {
//////                if (HomeActivity.type.equals("privilages")) {
//////                    HomeActivity.type = "about";
//////                } else if (HomeActivity.type.equals("about")) {
//////                    HomeActivity.type = "privilages";
//////                }
////            } catch (Exception e) {
////
////            }
////
////            Log.e("check", "not_added");
////            ft.replace(containerID, fragment);
////            ft.commit();
////
////        }
//    }
//*/
    public static void showWaiting(Context context) {
        /*progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.view_general_waiting);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        assert context != null;
        //if(progressDialog == null ||  progressDialog.getContext() != context)
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.show();


    }

    public static void dismissDialog() {
        progressDialog.dismiss();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    //check for locations permissions
    public static boolean hasStoragePermission(Activity activity) {

        Log.v(TAG, "Checking Camera For Permissions");

        String[] permissions = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Log.v(TAG, "Camera Permissions Found");
            return true;

        } else {
            Log.v(TAG, "Camera Permissions Not Found and asking fot them");
            ActivityCompat.requestPermissions(activity, permissions, STORAGE_PERMISSION_REQUEST);
            return false;

        }
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean isConnected(Context context, View noInternetView){
        if(isInternetAvailable(context)){
            noInternetView.setVisibility(View.GONE);
            return true;
        }
        else {
            noInternetView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public static boolean isEmptyList(Object[] objects, View emptyView){
        if(objects!=null&&objects.length>0){
            emptyView.setVisibility(View.GONE);
            return false;
        }
        else {
            emptyView.setVisibility(View.VISIBLE);
            return true;
        }
    }


    public static boolean isEmptyString(String content, View emptyView){
        if(content.isEmpty()||content.equals("")){
            emptyView.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            emptyView.setVisibility(View.GONE);
            return false;
        }
    }

    public static boolean isEmptyObject(Object object, View emptyView){
        if(object==null){
            emptyView.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            emptyView.setVisibility(View.GONE);
            return false;
        }
    }


   /* public static void handleActionBarWithBack(Fragment fragment, View actionBarView, String title){

        TextView tv_title = actionBarView.findViewById(R.id.tv_title);
        ImageView iv_back=actionBarView.findViewById(R.id.iv_back);

        Context context;
        tv_title.setText(title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = fragment.getActivity().getSupportFragmentManager();
                fm.popBackStack ();
            }
        });
    }
*//*
    public static void handleActionBar(View actionBarView, String title){
        TextView tv_title = actionBarView.findViewById(R.id.tv_title);
        tv_title.setText(title);
    }*/


    public static String getYoutubeThumbnail(String videoKey){
        return "https://img.youtube.com/vi/"+videoKey+"/mqdefault.jpg";
    }

    public static String getYoutubeVideoKey(String videoUrl){
        return videoUrl.substring(videoUrl.indexOf("=")+1);
    }

    public static int getPxValue(int dbValue, Context context){
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dbValue, context.getResources().getDisplayMetrics()));
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void makeSpinner(ArrayAdapter arrayAdapter,
                                   Spinner spinner, AdapterView.OnItemSelectedListener onItemSelectedListener){
        //modelList = new ArrayList<>();
        //ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, carBrandsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.notifyDataSetChanged();
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static long getRandomNumber() {
        return (long) ((Math.random() * ((100000 - 0) + 1)) + 0);
    }
}
