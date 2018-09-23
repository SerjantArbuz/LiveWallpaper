package sgtmelon.livewallpaper.office.annot;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения диалогов после поворота экрана
 */
@StringDef({
        DefDlg.SHEET_MAIN, DefDlg.SHEET_ADD,

        DefDlg.ORDER, DefDlg.TIME
})
public @interface DefDlg {

    //Main
    String SHEET_MAIN = "DLG_SHEET_MAIN",
            SHEET_ADD = "DLG_SHEET_ADD";

    //Settings
    String ORDER = "DLG_ORDER",
            TIME = "DLG_TIME";

    //Значения которое сохраняем в классе диалога
    String INIT = "INIT",
            VALUE = "VALUE",
            OPEN = "OPEN";

}
