package sgtmelon.livewallpaper.app.viewModel;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.model.ItemInfo;
import sgtmelon.livewallpaper.office.Help;

public class VmInfo extends AndroidViewModel {

    private final Context context;
    private List<ItemInfo> listInfo;

    public VmInfo(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
    }

    public List<ItemInfo> getListInfo() {
        if (listInfo == null) loadData();
        return listInfo;
    }

    private void loadData() {
        listInfo = new ArrayList<>();

        ItemInfo itemInfo = new ItemInfo(context.getString(R.string.info_android), Build.VERSION.RELEASE);
        listInfo.add(itemInfo);

        itemInfo = new ItemInfo(context.getString(R.string.info_brand), Build.BRAND);
        listInfo.add(itemInfo);

        itemInfo = new ItemInfo(context.getString(R.string.info_manufacturer), Build.MANUFACTURER);
        listInfo.add(itemInfo);

        itemInfo = new ItemInfo(context.getString(R.string.info_model), Build.MODEL);
        listInfo.add(itemInfo);

        DecimalFormat df = new DecimalFormat("#.##");
        double diagonal = Help.Screen.getDiagonal(context);

        itemInfo = new ItemInfo(context.getString(R.string.info_diagonal), df.format(diagonal));
        listInfo.add(itemInfo);

        int[] res = Help.Screen.getResolution(context);
        itemInfo = new ItemInfo(context.getString(R.string.info_resolution), res[0] + " x " + res[1]);
        listInfo.add(itemInfo);

    }

}
