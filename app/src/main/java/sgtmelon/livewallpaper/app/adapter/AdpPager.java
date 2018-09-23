package sgtmelon.livewallpaper.app.adapter;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.app.view.frg.FrgFullscreen;
import sgtmelon.livewallpaper.office.annot.DefDb;

public class AdpPager extends FragmentPagerAdapter {

    private final List<Fragment> listFragment = new ArrayList<>();

    public AdpPager(FragmentManager fm) {
        super(fm);
    }

    public void setListFragment(List<ItemWallpaper> listWallpaper) {
        listFragment.clear();

        for (ItemWallpaper itemWallpaper : listWallpaper) {
            Bundle bundle = new Bundle();
            bundle.putString(DefDb.WP_URI, itemWallpaper.getUri().toString());

            FrgFullscreen frgFullscreen = new FrgFullscreen();
            frgFullscreen.setArguments(bundle);

            listFragment.add(frgFullscreen);
        }
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

}
