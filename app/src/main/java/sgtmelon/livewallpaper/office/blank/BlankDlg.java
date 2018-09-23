package sgtmelon.livewallpaper.office.blank;

import android.content.DialogInterface;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class BlankDlg extends DialogFragment {


    protected String title;

    public void setTitle(String title) {
        this.title = title;
    }

    private DialogInterface.OnClickListener positiveListener;
    private DialogInterface.OnDismissListener dismissListener;

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    protected final DialogInterface.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (positiveListener != null) {
                positiveListener.onClick(dialogInterface, i);
            }
            dialogInterface.cancel();
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        setEnable();
    }

    protected Button buttonPositive;

    protected void setEnable() {
        AlertDialog dialog = (AlertDialog) getDialog();
        buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) dismissListener.onDismiss(dialog);
    }

}
