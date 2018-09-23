package sgtmelon.livewallpaper.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.office.annot.DefDb;

@Database(entities = {ItemWallpaper.class}, version = DefDb.version, exportSchema = false)
public abstract class DbRoom extends RoomDatabase {

    public abstract DaoWallpaper daoWallpaper();

    public static DbRoom provideDb(Context context) {
        return Room.databaseBuilder(context, DbRoom.class, DefDb.name)
                .allowMainThreadQueries()
                .build();
    }

}
