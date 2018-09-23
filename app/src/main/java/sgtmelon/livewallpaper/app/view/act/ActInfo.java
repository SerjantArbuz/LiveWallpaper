package sgtmelon.livewallpaper.app.view.act;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.adapter.AdpInfo;
import sgtmelon.livewallpaper.app.viewModel.VmInfo;
import sgtmelon.livewallpaper.office.Help;

public class ActInfo extends AppCompatActivity {

    private static final String TAG = "ActInfo";

    private VmInfo vm;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_info);
        Log.i(TAG, "onCreate");

        vm = ViewModelProviders.of(this).get(VmInfo.class);

        setupToolbar();
        setupRecycler();
    }

    private void setupToolbar(){
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_act_info));

        Drawable arrowBack = Help.Draw.get(this, R.drawable.ic_arrow_back, R.color.colorIconAccent);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private AdpInfo adpInfo;

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        RecyclerView recyclerView = findViewById(R.id.frgInfo_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adpInfo = new AdpInfo();
        recyclerView.setAdapter(adpInfo);
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        adpInfo.update(vm.getListInfo());
        adpInfo.notifyDataSetChanged();
    }

}
