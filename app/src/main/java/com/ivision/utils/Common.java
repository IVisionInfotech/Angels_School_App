package com.ivision.utils;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.javiersantos.appupdater.AppUpdater;
import com.google.android.material.snackbar.Snackbar;
import com.ivision.R;
import com.ivision.activity.LoginActivity;
import com.ivision.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

//import com.github.dhaval2404.imagepicker.ImagePicker;

public class Common {

    private static String TAG = "Common";
    public static String ARG_PARAM1 = "param1";
    public static String ARG_PARAM2 = "param2";
    private static Context context = MyApplication.getContext();
    public static int REQUEST_CHECK_SETTINGS = 100;
    public static int LOCATION_REQUEST_CODE = 99;
    private static int AUTOCOMPLETE_REQUEST_CODE = 98;
    public static final int IMAGE_PICKER_REQUEST_CODE = 97;
    public static final int STORAGE_PERMISSION_CODE = 96;
    public static final int REQUEST_CODE_SPEECH_INPUT = 95;
    private static ProgressDialog pDialog;
    private static OnDateSet onDateSet;
    private static Calendar myCalendar = Calendar.getInstance();
    public static Runnable ansTrue = null;
    public static Runnable ansFalse = null;

    public interface OnDateSet {
        void OnDateSet(String date);
    }

    public static String handleIntent(Intent intent) {
        String id = "", type = "";
        if (intent != null) {
            String appLinkAction = intent.getAction();
            Uri appLinkData = intent.getData();
            if (ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
                id = intent.getData().getQueryParameter("id");
                type = intent.getData().getQueryParameter("type");
                id = decodeData(decodeData(id).trim()).trim();
                type = decodeData(decodeData(type).trim()).trim();
                return id + ", " + type;
            }
        }
        return "";
    }

    public void goToActivity(Context context, Class aClass, String id, String title) {
        Intent intent = new Intent(context, aClass);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        ((Activity) context).startActivity(intent);
    }

    public static void loadImage(final Context context, ImageView imageView, String url) {
        if (context == null) return;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .placeholder(R.drawable.logo) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                .error(R.drawable.logo)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //using to load into cache then second time it will load fast.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
    }

    public static void splashLoadImage(final Context context, ImageView imageView, String url) {
        if (context == null) return;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //using to load into cache then second time it will load fast.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
    }

    /*public static void showBadge(Context context, BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.layout_count, bottomNavigationView, false);
        CircularTextView tvCount = badge.findViewById(R.id.tvCount);
        tvCount.setSolidColor(context.getResources().getColor(R.color.colorAccent));
        tvCount.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }*/

    public static void shareProduct(Context context, String id, String title, String type) {

        title = title.replace(' ', '-');

        String url = Constant.detailsBaseURL + "id=" + encodeData(encodeData(id).trim()).trim() + "&type=" + encodeData(encodeData(type).trim()).trim();

        String shareBody = title +
                "\n\nAvailable only at " + context.getResources().getString(R.string.app_name) + "." +
                "\n" + url +
                "\n\n\nLet me recommend you this application\n" +
                "https://play.google.com/store/apps/details?id=" + context.getPackageName();

        String shareSubject = context.getResources().getString(R.string.app_name) + context.getResources().getString(R.string.share_text);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra("productId", id);
        ((Activity) context).startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static String encodeData(String data) {
        return new String(Base64.encode(data.getBytes(), Base64.NO_PADDING));
    }

    public static String decodeData(String data) {
        return new String(Base64.decode(data, Base64.NO_PADDING));
    }

    public static void setStudentId(Context context, String id) {
        Session session = new Session(context);
        session.setStudentId(id);
    }

    public static String getStudentId(Context context) {
        String sId = "";
        Session session = new Session(context);

        if (session.getStudentId().isEmpty()) {
            ArrayList<User> list = new ArrayList<>(RealmController.with(context).getAllUser());
            if (!list.isEmpty()) {
                sId = String.valueOf(list.get(0).getId());
            }
        } else {
            sId = session.getStudentId();
        }

        return sId;
    }

    public static String getUserId() {
        String id = "";
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            id = String.valueOf(user.getId());
        }
        return id;
    }

    public static String getUserName() {
        String id = "";
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            id = String.valueOf(user.getName());
        }
        return id;
    }

    public static String getUsername() {
        Session session = new Session(context);
        return session.getUsername();
    }

    public static String getPassword() {
        Session session = new Session(context);
        return session.getPassword();
    }

    public static void openImagePreview(Context context, ArrayList<String> imageList) {
//        Intent intent = new Intent(context, ImagePreviewActivity.class);
//        intent.putExtra("list", imageList);
//        ((Activity) context).startActivity(intent);
    }

    public static void logout(Context context, String message) {
        Session session = new Session(context);
        if (!message.isEmpty()) showToast(message);
        session.setLoginStatus(false);
        session.clearData();
        RealmController.with(context).clearAllUser();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

    public static void showShimmerLayout(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
    }

    public static void hideShimmerLayout(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
    }

    public static String changeDateFormat(String dateTime, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";
        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String changeDateFormat(String dateTime, String outputPattern) {
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";
        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean compareDates(String enteredDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(enteredDate);
            Date c = Calendar.getInstance().getTime();
            Date date2 = sdf.parse(sdf.format(c));
            if (date.compareTo(date2) == 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareGreaterDates(String enteredDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(enteredDate);
            Date c = Calendar.getInstance().getTime();
            Date date2 = sdf.parse(sdf.format(c));
//            Log.e(BaseActivity.TAG, "compareGreaterDates: " + date + " : " + date2);
            if (date2 != null && date2.compareTo(date) >= 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareSmallerDates(String enteredDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(enteredDate);
            Date c = Calendar.getInstance().getTime();
            Date date2 = sdf.parse(sdf.format(c));
//            Log.e(BaseActivity.TAG, "compareSmallerDates: " + date2 + " : " + date);
            if (date2 != null && date2.compareTo(date) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDaysCount(String startDate, String endDate) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBefore = myFormat.parse(startDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            int daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
            return String.valueOf(daysBetween + 1);
        } catch (Exception exception) {
            return "";
        }
    }

    public static void showDatePicker(Context context, OnDateSet onDateSet) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                onDateSet.OnDateSet(sdf.format(myCalendar.getTime()));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static void appUpdateDialog(Context context) {

        AppUpdater appUpdater = new AppUpdater(context)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = MyApplication.getContext().getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            MyApplication.getContext().startActivity(new Intent(ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            MyApplication.getContext().startActivity(new Intent(ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setButtonDismiss("Maybe later")
                .setButtonDismissClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false); // Dialog could not be dismissable

        appUpdater.start();
    }

    public static void downloadFile(Context context, String url, String fileName) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            String title = URLUtil.guessFileName(url, null, null);
            request.setTitle(title);
            request.setDescription("Downloading File please wait...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, (context.getString(R.string.app_name) + File.separator + fileName));
            DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            manager.enqueue(request);
            showToast("Downloading Completed...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disableButton(Button btnSubmit, int drawable) {
        btnSubmit.setBackground(context.getResources().getDrawable(drawable));
        btnSubmit.setEnabled(false);
    }

    public static void enableButton(Button btnSubmit, int drawable) {
        btnSubmit.setBackground(context.getResources().getDrawable(drawable));
        btnSubmit.setEnabled(true);
    }

    public static void showProgressDialog(Context context, String msg) {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(msg);
        if (!((Activity) context).isFinishing() && !pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean checkLogin(Context context) {
        Session session = new Session(context);
        if (!session.getLoginStatus() || getUserId().isEmpty()) {
            showToast("Please do login first");
            return false;
        } else {
            return true;
        }
    }

    public static boolean confirmationDialog(Activity activity, String message, String cancelBtn, String okBtn, Runnable aProcedure, Runnable bProcedure) {
        ansTrue = aProcedure;
        ansFalse = bProcedure;

        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ansTrue.run();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ansFalse.run();
                    }
                });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
        return true;
    }

    public static boolean confirmationDialog(Context context, String message, String cancelBtn, String okBtn, Runnable aProcedure) {
        ansTrue = aProcedure;

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ansTrue.run();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        dialog.dismiss();
                    }
                });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
        return true;
    }

    /*public static void openCamera(Activity activity) {
        ImagePicker.with(activity)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .cameraOnly()
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    public static void selectImage(Activity activity) {
        ImagePicker.with(activity)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    public static void openGallery(Activity activity) {
        ImagePicker.with(activity)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .galleryOnly()
                .start(IMAGE_PICKER_REQUEST_CODE);
    }*/

    public static Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static void setTintFilter(Context context, ImageView imageView, int colorId) {
        imageView.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public static void openBrowser(Context context, String url) {
        String query = Uri.encode(url, "UTF-8");
        Intent browserIntent = new Intent(CATEGORY_BROWSABLE, Uri.parse(Uri.decode(query)));
        browserIntent.setAction(ACTION_VIEW);
        ((Activity) context).startActivity(browserIntent);
    }

    public static void dialNumber(Context context, String contact) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + contact));
        ((Activity) context).startActivity(callIntent);
    }

    public static void openWhatsApp(Context context, String number, String message) {

        Intent sendIntent = new Intent();

        sendIntent.setAction(ACTION_VIEW);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        if (Common.isPackageExisted(context, "com.whatsapp")) {
            try {
                String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8");
                sendIntent.setData(Uri.parse(url));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sendIntent.setPackage("com.whatsapp");
        } else if (Common.isPackageExisted(context, "com.whatsapp.w4b")) {
            try {
                String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8");
                sendIntent.setData(Uri.parse(url));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sendIntent.setPackage("com.whatsapp.w4b");
        }

        ((Activity) context).startActivity(sendIntent);
    }

    public static void sendEmail(Context context, String emailId) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:" + emailId));
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailId});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello There");
//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Add Message here");


        try {
            ((Activity) context).startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            showToast("No email clients installed.");
        }
    }

    public static boolean isPackageExisted(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String getAddress(Context context, double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String city = "";
            if (addresses.get(0).getLocality() == null) {
                city = addresses.get(0).getSubAdminArea();
            } else {
                city = addresses.get(0).getLocality();
            }
            return city;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void sendBroadcast(Context context) {
        Intent intent = new Intent();
        intent.putExtra(Constant.refreshCount, Constant.refreshCount);
        intent.setAction(Constant.refreshCount);
        BroadCastManager.getInstance().sendBroadCast(context, intent);
    }

    public static float randomFloatBetween(int minYValue, int maxYValue) {
        Random rand = new Random();
        int randomInt = rand.nextInt((maxYValue - minYValue) + 1) + minYValue;
        return randomInt / 1000.00f;
    }

    public static Date convertStringToDate(String dtStart) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dtStart);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Calendar dateToCalendar(String date) {
        Calendar cal = Calendar.getInstance();
        if (convertStringToDate(date) != null) cal.setTime(convertStringToDate(date));
        return cal;
    }

    /*public static void openSortDialog(Context context) {
        SortBottomSheetDialog openBottomSheet = SortBottomSheetDialog.newInstance();
        openBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), SortBottomSheetDialog.TAG);
    }

    public static class PriceComparatorASC implements Comparator<Product> {
        @Override
        public int compare(Product first, Product second) {
            return Integer.compare(Integer.parseInt(first.getPrice()), Integer.parseInt(second.getPrice()));
        }
    }

    public static class PriceComparatorDESC implements Comparator<Product> {
        @Override
        public int compare(Product first, Product second) {
            return Integer.compare(Integer.parseInt(second.getPrice()), Integer.parseInt(first.getPrice()));
        }
    }

    public static class NameComparator implements Comparator<Product> {
        @Override
        public int compare(Product first, Product second) {
            return first.getName().compareToIgnoreCase(second.getName());
        }
    }*/
}
