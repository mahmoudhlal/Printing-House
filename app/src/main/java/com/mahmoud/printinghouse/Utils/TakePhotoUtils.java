package com.mahmoud.printinghouse.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Constants.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TakePhotoUtils {

    public String mCurrentPhotoPath;
    private static Bitmap bitmap;

    private static TakePhotoUtils Instance = null;

    public static TakePhotoUtils getInstance() {
        if (Instance == null)
            Instance = new TakePhotoUtils();
        return Instance;
    }



    //region PHOTO
    public void SelectPhotoDialog(Activity activity , Fragment fragment) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.view_take_photo);

        LinearLayout takePhoto = dialog.findViewById(R.id.takephoto);
        LinearLayout openGallery = dialog.findViewById(R.id.opengallery);

        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.alert_dialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        takePhoto.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.getInstance().check_cameraPermission(activity, fragment)) {
                    ChooseImageCamera(activity, fragment);
                }
            } else {
                ChooseImageCamera(activity, fragment);
            }
            dialog.dismiss();
        });

        openGallery.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.getInstance().check_ReadStoragePermission(activity, fragment)) {
                    ChooseImageGallery(activity, fragment);
                }
            } else {
                ChooseImageGallery(activity, fragment);
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void ChooseImageCamera(Activity context, Fragment fragment) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.mahmoud.printinghouse.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (fragment!=null)
                    fragment.startActivityForResult(takePictureIntent, Constants.CAMERA);
                else
                    context.startActivityForResult(takePictureIntent, Constants.CAMERA);
            }
        }
    }

    public void ChooseImageGallery(Activity context, Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (fragment!=null)
            fragment.startActivityForResult(Intent.createChooser(intent, context.getResources().getString(R.string.choose_photo)), Constants.GALLERY);
        else
            context.startActivityForResult(Intent.createChooser(intent, context.getResources().getString(R.string.choose_photo)), Constants.GALLERY);
    }

    private File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getImagePathFromInputStreamUri(Context context , Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(inputStream , context);

                filePath = photoFile.getPath();

            } catch (IOException e) {
                // log
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private File createTemporalFileFrom(InputStream inputStream , Context context) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile(context);
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private File createTemporalFile(Context context) {
        return new File(context.getExternalCacheDir(), "tempFile.jpg"); // context needed
    }

    private void fixRotate(String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                break;
        }
    }

    private Bitmap rotateImage(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }



    //endregion

    public Bitmap setImage(int type , Uri contentURI , Context context, ImageView imageView) {
        try {
            if (type == Constants.GALLERY) {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                mCurrentPhotoPath = getImagePathFromInputStreamUri(context,contentURI);
            } else
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, new BitmapFactory.Options());

            fixRotate(mCurrentPhotoPath);

            imageView.setPadding(0,0,0,0);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap ;
    }

    public Bitmap getBitmap(int type , Uri contentURI , Context context) {
        try {
            if (type == Constants.GALLERY) {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                mCurrentPhotoPath = getImagePathFromInputStreamUri(context,contentURI);
            } else
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, new BitmapFactory.Options());

            fixRotate(mCurrentPhotoPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public MultipartBody.Part getMultiPartFromBitmap(Bitmap mBitmap, Context context, String fileName) {
        File filesDir = context.getApplicationContext().getFilesDir();
        File file = new File(filesDir, fileName + ".jpg");
        OutputStream os;
        try {

            os = new FileOutputStream(file);
            Log.i("sadsad", mBitmap.getByteCount() + "");

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "select photo", Toast.LENGTH_SHORT).show();
            Log.e("Error writing bitmap", Objects.requireNonNull(e.getMessage()));
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(fileName, file.getName(), requestFile);
    }

}
