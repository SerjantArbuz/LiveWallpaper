package sgtmelon.livewallpaper.app.view.act;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.adapter.AdpPager;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.app.viewModel.VmWallpaper;
import sgtmelon.livewallpaper.office.Help;
import sgtmelon.livewallpaper.office.annot.DefPage;
import sgtmelon.livewallpaper.office.st.StPage;

public class ActFullscreen extends AppCompatActivity {

    private static final String TAG = "ActFullscreen";

    private VmWallpaper vm;
    private FragmentManager fm;

    private StPage stPage;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();

        ItemWallpaper itemWallpaper = vm.getListWallpaper().get(stPage.getPage());
        toolbar.setTitle(itemWallpaper.getName());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fullscreen);
        Log.i(TAG, "onCreate");

        vm = ViewModelProviders.of(this).get(VmWallpaper.class);
        fm = getSupportFragmentManager();

        Bundle bundle = getIntent().getExtras();
        stPage = new StPage(savedInstanceState == null
                ? (bundle != null ? bundle.getInt(DefPage.CURRENT) : 0)
                : savedInstanceState.getInt(DefPage.CURRENT));

        setupToolbar();
        setupPager();
    }

    private Toolbar toolbar;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        toolbar = findViewById(R.id.incToolbar_tb);

        Drawable arrowBack = Help.Draw.get(this, R.drawable.ic_arrow_back, R.color.colorIconAccent);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private ViewPager viewPager;
    private AdpPager adpPager;

    private void setupPager() {
        Log.i(TAG, "setupPager");

        viewPager = findViewById(R.id.actFullscreen_vp);

        adpPager = new AdpPager(fm);
        viewPager.setAdapter(adpPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected");

                ItemWallpaper itemWallpaper = vm.getListWallpaper().get(position);
                toolbar.setTitle(itemWallpaper.getName());

                stPage.setPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<ItemWallpaper> listWallpaper = vm.getListWallpaper();

        adpPager.setListFragment(listWallpaper);
        adpPager.notifyDataSetChanged();

        viewPager.setOffscreenPageLimit(listWallpaper.size() - 1);
        viewPager.setCurrentItem(stPage.getPage());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putInt(DefPage.CURRENT, stPage.getPage());
    }

}
