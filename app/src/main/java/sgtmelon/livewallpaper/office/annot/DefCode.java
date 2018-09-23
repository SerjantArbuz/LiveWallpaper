package sgtmelon.livewallpaper.office.annot;

import androidx.annotation.IntDef;

@IntDef({DefCode.camera, DefCode.gallery,
        DefCode.wallpaper})
public @interface DefCode {

    int camera = 0;
    int gallery = 1;

    int wallpaper = 2;

}
