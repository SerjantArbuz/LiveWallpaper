package sgtmelon.livewallpaper.element;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.office.annot.DefDlg;
import sgtmelon.livewallpaper.office.blank.BlankDlg;

public class DlgSingle extends BlankDlg {

    public void setArguments(int check) {
        Bundle arg = new Bundle();
        arg.putInt(DefDlg.INIT, check);
        arg.putInt(DefDlg.VALUE, check);
        setArguments(arg);
    }

    private String[] name;
    private int init, check;

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public int getCheck() {
        return check;
    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getInt(DefDlg.INIT);
            check = savedInstanceState.getInt(DefDlg.VALUE);
        } else if (arg != null) {
            init = arg.getInt(DefDlg.INIT);
            check = arg.getInt(DefDlg.VALUE);
        }

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setSingleChoiceItems(name, check, (dialogInterface, i) -> {
                    check = i;
                    setEnable();
                })
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        if (init == check) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDlg.INIT, init);
        outState.putInt(DefDlg.VALUE, check);
    }

}
