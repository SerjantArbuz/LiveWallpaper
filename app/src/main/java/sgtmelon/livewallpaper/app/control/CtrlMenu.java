package sgtmelon.livewallpaper.app.control;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.office.Help;

//Класс управления состоянием меню обоев
public class CtrlMenu {

    private final Context context;
    private final Toolbar toolbar;
    private final FloatingActionButton fab;

    public CtrlMenu(Context context, Toolbar toolbar, FloatingActionButton fab) {
        this.context = context;

        this.toolbar = toolbar;
        this.fab = fab;

        setupMenu();
    }

    private MenuItem mDelete, mInfo, mPreference;

    private void setupMenu() {
        Menu menu = toolbar.getMenu();

        mDelete = menu.findItem(R.id.menu_actMain_delete);
        mInfo = menu.findItem(R.id.menu_actMain_info);
        mPreference = menu.findItem(R.id.menu_actMain_preference);

        Help.Draw.tintIcon(context, mDelete);
        Help.Draw.tintIcon(context, mInfo);
        Help.Draw.tintIcon(context, mPreference);
    }

    private View.OnClickListener navClickListener;

    public void setNavClickListener(View.OnClickListener navClickListener) {
        this.navClickListener = navClickListener;
    }

    public void changeMode(boolean active, int count) {
        if (active) {
            Drawable cancel = Help.Draw.get(context, R.drawable.ic_cancel, R.color.colorIconAccent);
            toolbar.setNavigationIcon(cancel);
            changeCount(count);

            toolbar.setNavigationOnClickListener(view -> navClickListener.onClick(view));

            mDelete.setVisible(true);
            mInfo.setVisible(false);
            mPreference.setVisible(false);

            fab.hide();
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.setTitle(context.getString(R.string.title_act_main));

            mDelete.setVisible(false);
            mInfo.setVisible(true);
            mPreference.setVisible(true);

            fab.show();
        }
    }

    public void changeCount(int count) {
        toolbar.setTitle(context.getString(R.string.title_act_main_action) + count);
    }

}
