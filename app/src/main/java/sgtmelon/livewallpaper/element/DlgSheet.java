package sgtmelon.livewallpaper.element;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import sgtmelon.livewallpaper.office.annot.DefDlg;

public class DlgSheet extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    public void setArguments(@LayoutRes int layoutId, @IdRes int navigationId) {
        Bundle arg = new Bundle();
        arg.putInt(DefDlg.INIT, layoutId);
        arg.putInt(DefDlg.VALUE, navigationId);
        setArguments(arg);
    }

    private int layoutId, navigationId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt(DefDlg.INIT);
            navigationId = savedInstanceState.getInt(DefDlg.VALUE);
        } else if (arg != null) {
            layoutId = arg.getInt(DefDlg.INIT);
            navigationId = arg.getInt(DefDlg.VALUE);
        }

        View view = inflater.inflate(layoutId, container, false);

        NavigationView navigationView = view.findViewById(navigationId);
        navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    private DialogInterface.OnDismissListener dismissListener;

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;

    public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener) {
        this.navigationItemSelectedListener = navigationItemSelectedListener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationItemSelectedListener.onNavigationItemSelected(menuItem);
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDlg.INIT, layoutId);
        outState.putInt(DefDlg.VALUE, navigationId);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) dismissListener.onDismiss(dialog);
    }
}
