package sgtmelon.livewallpaper.app.viewModel;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.livewallpaper.app.database.DbRoom;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;

public class VmWallpaper extends AndroidViewModel {

    private final Context context;

    private List<ItemWallpaper> listWallpaper;

    public VmWallpaper(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public List<ItemWallpaper> getListWallpaper() {
        loadData();
        return listWallpaper;
    }

    private void loadData() {
        DbRoom db = DbRoom.provideDb(context);
        listWallpaper = db.daoWallpaper().getClear();
        db.close();
    }

}
