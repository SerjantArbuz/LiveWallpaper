package sgtmelon.livewallpaper.app.database;

import java.io.File;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.office.conv.ConvUri;

@Dao
@TypeConverters({ConvUri.class})
public abstract class DaoWallpaper {

    @Insert
    public abstract void insert(ItemWallpaper itemWallpaper);

    @Delete
    public abstract void delete(ItemWallpaper itemWallpaper);

    @Delete
    public abstract void delete(List<ItemWallpaper> itemWallpaper);

    @Query("SELECT * FROM WALLPAPER_TABLE " +
            "ORDER BY WP_ID DESC")
    public abstract List<ItemWallpaper> get();

    //Получение существующих файлов
    public List<ItemWallpaper> getClear() {
        List<ItemWallpaper> listWallpaper = get();

        for (int i = 0; i < listWallpaper.size(); i++) {
            ItemWallpaper itemWallpaper = listWallpaper.get(i);

            String path = itemWallpaper.getPath();
            String name = path.substring(path.lastIndexOf("/") + 1);

            File file = new File(path);

            if (!file.exists()) {
                delete(itemWallpaper);
                listWallpaper.remove(i);
            } else {
                itemWallpaper.setName(name);
                listWallpaper.set(i, itemWallpaper);
            }
        }

        return listWallpaper;
    }

    @Query("SELECT COUNT(WP_ID) FROM WALLPAPER_TABLE")
    public abstract int count();

}
