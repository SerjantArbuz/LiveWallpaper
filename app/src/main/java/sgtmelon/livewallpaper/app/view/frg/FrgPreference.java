package sgtmelon.livewallpaper.app.view.frg;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.database.DbRoom;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.app.view.act.ActPreference;
import sgtmelon.livewallpaper.element.DlgSingle;
import sgtmelon.livewallpaper.office.Help;
import sgtmelon.livewallpaper.office.annot.DefCode;
import sgtmelon.livewallpaper.office.annot.DefDlg;
import sgtmelon.livewallpaper.office.st.StOpen;

public class FrgPreference extends PreferenceFragment {

    private static final String TAG = "FrgPreference";

    //region Variable
    private ActPreference activity;
    private FragmentManager fm;
    private SharedPreferences pref;

    private StOpen stOpen;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            View list = view.findViewById(android.R.id.list);
            list.setPadding(0, 0, 0, 0);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        Log.i(TAG, "onCreate");

        activity = (ActPreference) getActivity();
        fm = activity.getSupportFragmentManager();
        pref = PreferenceManager.getDefaultSharedPreferences(activity);

        stOpen = new StOpen();
        if (savedInstanceState != null) stOpen.setOpen(savedInstanceState.getBoolean(DefDlg.OPEN));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        setupWork();
        setupChange();
    }

    private SwitchPreference work;
    private DbRoom db;

    private void setupWork() {
        Log.i(TAG, "setupWork");

        work = (SwitchPreference) findPreference(getString(R.string.pref_key_work));

        db = DbRoom.provideDb(activity);
        int count = db.daoWallpaper().count();
        db.close();

        if (count == 0) {
            pref.edit().putBoolean(getString(R.string.pref_key_work), false).apply();

            work.setEnabled(false);
            work.setChecked(false);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.SET_WALLPAPER}, DefCode.wallpaper);
                } else {
                    work.setEnabled(true);
                }
            } else {
                work.setEnabled(true);
            }
        }

        work.setOnPreferenceChangeListener((preference, o) -> {
            boolean check = work.isChecked();
            pref.edit().putBoolean(getString(R.string.pref_key_work), !check).apply();

            if (!check) {
                db = DbRoom.provideDb(activity);
                List<ItemWallpaper> listWallpaper = db.daoWallpaper().getClear();
                db.close();

                int pCurrent = 0;
                ItemWallpaper itemWallpaper = listWallpaper.get(pCurrent);

                String path = itemWallpaper.getPath();
                new Thread(() -> Help.Data.setWallpaper(activity, path)).start();

                int pNext;
                if (valOrder == getResources().getInteger(R.integer.pref_val_order_def)) {
                    pNext = (pCurrent + 1) % listWallpaper.size();
                } else {
                    pNext = new Random().nextInt(listWallpaper.size());
                }

                itemWallpaper = listWallpaper.get(pNext);
                Help.Data.setAlarm(activity, itemWallpaper.getPath());

                Toast.makeText(activity, getString(R.string.message_wallpaper_set), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult");

        switch (requestCode) {
            case DefCode.wallpaper:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    work.setEnabled(true);
                }
                break;
        }
    }

    private Preference prefOrder;
    private int valOrder;
    private DlgSingle dlgOrder;

    private Preference prefTime;
    private int valTime;
    private DlgSingle dlgTime;

    private void setupChange() {
        Log.i(TAG, "setupChange");

        dlgOrder = (DlgSingle) fm.findFragmentByTag(DefDlg.ORDER);
        if (dlgOrder == null) dlgOrder = new DlgSingle();

        prefOrder = findPreference(getString(R.string.pref_key_order));
        valOrder = pref.getInt(getString(R.string.pref_key_order), getResources().getInteger(R.integer.pref_val_order_def));
        prefOrder.setSummary(getResources().getStringArray(R.array.pref_name_order)[valOrder]);
        prefOrder.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen();

                dlgOrder.setArguments(valOrder);
                dlgOrder.show(fm, DefDlg.ORDER);
            }
            return true;
        });

        dlgOrder.setTitle(getString(R.string.pref_title_order));
        dlgOrder.setName(getResources().getStringArray(R.array.pref_name_order));
        dlgOrder.setPositiveListener(((dialogInterface, i) -> {
            valOrder = dlgOrder.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_order), valOrder).apply();
            prefOrder.setSummary(dlgOrder.getName()[valOrder]);
        }));
        dlgOrder.setDismissListener(dialogInterface -> stOpen.setOpen(false));

        dlgTime = (DlgSingle) fm.findFragmentByTag(DefDlg.TIME);
        if (dlgTime == null) dlgTime = new DlgSingle();

        prefTime = findPreference(getString(R.string.pref_key_time));
        valTime = pref.getInt(getString(R.string.pref_key_time), getResources().getInteger(R.integer.pref_val_time_def));
        prefTime.setSummary(getResources().getStringArray(R.array.pref_name_time)[valTime]);
        prefTime.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen();

                dlgTime.setArguments(valTime);
                dlgTime.show(fm, DefDlg.TIME);
            }
            return true;
        });

        dlgTime.setTitle(getString(R.string.pref_title_time));
        dlgTime.setName(getResources().getStringArray(R.array.pref_name_time));
        dlgTime.setPositiveListener(((dialogInterface, i) -> {
            valTime = dlgTime.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_time), valTime).apply();
            prefTime.setSummary(dlgTime.getName()[valTime]);
        }));
        dlgTime.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefDlg.OPEN, stOpen.isOpen());
    }

}