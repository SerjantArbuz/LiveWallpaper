package sgtmelon.livewallpaper.office.conv;

import android.net.Uri;

import androidx.room.TypeConverter;

public class ConvUri {

    @TypeConverter
    public Uri fromString(String uri) {
        return Uri.parse(uri);
    }

    @TypeConverter
    public String toString(Uri uri) {
        return uri.toString();
    }

}
