package sgtmelon.livewallpaper.app.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.Random;

import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.database.DbRoom;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.office.Help;
import sgtmelon.livewallpaper.office.annot.DefDb;

//Класс установки обоев и назначения следующих
public class CtrlReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean change = pref.getBoolean(context.getString(R.string.pref_key_work), context.getResources().getBoolean(R.bool.pref_val_work_def));

        if (change) {
            int valOrder = pref.getInt(context.getString(R.string.pref_key_order), context.getResources().getInteger(R.integer.pref_val_order_def));

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String path = bundle.getString(DefDb.WP_PH);

                new Thread(() -> Help.Data.setWallpaper(context, path)).start();

                DbRoom db = DbRoom.provideDb(context);
                List<ItemWallpaper> listWallpaper = db.daoWallpaper().getClear();
                db.close();

                if (listWallpaper.size() != 0) {
                    int pCurrent = 0;                                           //Поиск текущей позиции
                    for (int i = 0; i < listWallpaper.size(); i++) {
                        if (listWallpaper.get(i).getPath().equals(path)) {
                            pCurrent = i;
                            break;
                        }
                    }

                    int pNext;                                                  //Установка следующей позиции
                    if (valOrder == context.getResources().getInteger(R.integer.pref_val_order_def)) {                    //Смена по порядку
                        pNext = (pCurrent + 1) % listWallpaper.size();
                    } else {
                        pNext = new Random().nextInt(listWallpaper.size());
                    }

                    ItemWallpaper itemWallpaper = listWallpaper.get(pNext);     //Установка следующей смены обоев
                    Help.Data.setAlarm(context, itemWallpaper.getPath());
                } else {
                    pref.edit().putBoolean(context.getString(R.string.pref_key_work), false).apply();       //Сброс выключателя работы
                }
            }
        }

    }

}
