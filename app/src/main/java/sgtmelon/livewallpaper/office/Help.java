package sgtmelon.livewallpaper.office;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.control.CtrlReceiver;
import sgtmelon.livewallpaper.office.annot.DefDb;

public class Help {

    public static class Draw {

        //Покраска Drawable
        public static Drawable get(Context context, @DrawableRes int drawableId, @ColorRes int colorId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);

            int color = ContextCompat.getColor(context, colorId);
            if (drawable != null) drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            return drawable;
        }

        //Покраска элемента меню
        public static void tintIcon(Context context, MenuItem item) {
            Drawable drawable = item.getIcon();
            Drawable drawableTint = DrawableCompat.wrap(drawable);

            int color = ContextCompat.getColor(context, R.color.colorIconAccent);
            DrawableCompat.setTint(drawableTint, color);

            item.setIcon(drawableTint);
        }

    }

    public static class Data {

        public static Uri URI = null;
        public static String PATH = null;

        //Генерация имени для файла
        static String getName(Context context) {
            return new SimpleDateFormat(context.getString(R.string.file_name_format), Locale.getDefault()).format(Calendar.getInstance().getTime());
        }

        //Создание файла в дериктории приложения
        public static File createFile(Context context) throws IOException {
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = File.createTempFile(getName(context), ".jpg", directory);

            PATH = file.getAbsolutePath();

            return file;
        }

        //Копирование файла в директорию приложения
        public static void copyFile(Context context, Uri inputUri) throws IOException {
            InputStream inputStream = context.getContentResolver().openInputStream(inputUri);

            if (inputStream != null) {
                File file = createFile(context);

                URI = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_name), file);

                OutputStream outputStream = new FileOutputStream(file);

                int length;
                byte buffer[] = new byte[1024];
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();

                inputStream.close();
            }
        }

        public static boolean deleteFile(String path) {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            }
            return true;
        }

        public static void setWallpaper(Context context, String path) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            Bitmap bitmap = BitmapFactory.decodeFile(path);

            if (bitmap != null) {
                try {
                    wallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(context, R.string.message_wallpaper_error_set, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, R.string.message_wallpaper_error_find, Toast.LENGTH_SHORT).show();
            }
        }

        public static void setAlarm(Context context, String path) {
            Intent intent = new Intent(context, CtrlReceiver.class);
            intent.putExtra(DefDb.WP_PH, path);

            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarm != null) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

                long timeCurrent = System.currentTimeMillis();
                int timePref = pref.getInt(context.getString(R.string.pref_key_time), context.getResources().getInteger(R.integer.pref_val_time_def));
                int timeAdd = context.getResources().getIntArray(R.array.pref_val_time)[timePref];

                Date futureDate = new Date(timeCurrent + timeAdd);
                alarm.set(AlarmManager.RTC_WAKEUP, futureDate.getTime(), sender);
            }
        }

    }

    public static class Screen {

        public static double getDiagonal(Context context) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(dm);

                int width = dm.widthPixels;
                int height = dm.heightPixels;

                double wi = (double) width / (double) dm.xdpi;
                double hi = (double) height / (double) dm.ydpi;

                double x = Math.pow(wi, 2);
                double y = Math.pow(hi, 2);

                return Math.sqrt(x + y);
            }
            return 0;
        }

        public static int[] getResolution(Context context) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(dm);
                return new int[]{dm.widthPixels, dm.heightPixels};
            }
            return new int[]{0, 0};
        }

    }

}


