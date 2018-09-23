package sgtmelon.livewallpaper.app.view.act;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.view.frg.FrgPreference;
import sgtmelon.livewallpaper.office.Help;

public class ActPreference extends AppCompatActivity {

    private static final String TAG = "ActPreference";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_preference);
        Log.i(TAG, "onCreate");

        setupToolbar();

        FrgPreference frgPreference = new FrgPreference();
        getFragmentManager().beginTransaction()
                .replace(R.id.actPreference_fl_container, frgPreference)
                .commit();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_act_preference));

        Drawable arrowBack = Help.Draw.get(this, R.drawable.ic_arrow_back, R.color.colorIconAccent);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

}
