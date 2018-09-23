package sgtmelon.livewallpaper.office.annot;

import androidx.annotation.StringDef;

@StringDef({
        DefDb.WP_TB,
        DefDb.WP_ID, DefDb.WP_PH, DefDb.WP_URI
})
public @interface DefDb {

    String WP_TB = "WALLPAPER_TABLE",
            WP_ID = "WP_ID",
            WP_PH = "WP_PATH",
            WP_URI = "WP_URI";

    String name = "LiveWallpaper";

    int version = 1;

}
