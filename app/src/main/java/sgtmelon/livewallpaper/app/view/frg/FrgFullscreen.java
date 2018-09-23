package sgtmelon.livewallpaper.app.view.frg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.office.Help;
import sgtmelon.livewallpaper.office.annot.DefDb;

public class FrgFullscreen extends Fragment {

    private static final String TAG = "FrgFullscreen";

    private Context context;

    private Uri uri;
    private ImageView image;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");

        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.frg_fullscreen, container, false);

        Bundle arg = getArguments();
        if (savedInstanceState != null) {
            uri = Uri.parse(savedInstanceState.getString(DefDb.WP_URI));
        } else if (arg != null) {
            uri = Uri.parse(arg.getString(DefDb.WP_URI));
        }

        image = view.findViewById(R.id.frgFullscreen_iv_image);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        int[] resolution = Help.Screen.getResolution(context);

        Picasso.get()
                .load(uri)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(resolution[0], resolution[1])
                .onlyScaleDown()
                .centerInside()
                .into(image);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putString(DefDb.WP_URI, uri.toString());
    }
}
