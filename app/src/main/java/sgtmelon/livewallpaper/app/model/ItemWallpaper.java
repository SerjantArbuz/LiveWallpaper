package sgtmelon.livewallpaper.app.model;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.livewallpaper.office.annot.DefDb;
import sgtmelon.livewallpaper.office.conv.ConvUri;

@Entity(tableName = DefDb.WP_TB)
@TypeConverters({ConvUri.class})
public class ItemWallpaper {

    @ColumnInfo(name = DefDb.WP_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DefDb.WP_PH)
    private String path;

    @ColumnInfo(name = DefDb.WP_URI)
    private Uri uri;

    @Ignore
    private String name;

    public ItemWallpaper(Uri uri, String path) {
        this.uri = uri;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
