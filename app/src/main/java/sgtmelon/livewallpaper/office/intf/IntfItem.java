package sgtmelon.livewallpaper.office.intf;

import android.view.View;

public interface IntfItem {

    interface Click {
        void onItemClick(int p);
    }

    interface LongClick {
        void onItemLongClick(int p);
    }

}
