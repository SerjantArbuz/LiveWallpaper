package sgtmelon.livewallpaper.office.annot;

import androidx.annotation.StringDef;

@StringDef({DefMode.ACTIVE, DefMode.SELECT, DefMode.COUNT})
public @interface DefMode {

    String ACTIVE = "ACTIVE", SELECT = "SELECT", COUNT = "COUNT";

}
