package ibmmobileappbuilder.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

public class ColorUtils {

    private static final String[] PALETTE_DEFAULT = {"#17B9ED", "#1992B8", "#31D755", "#EDE66D",
            "#F4C745",
            "#E88E38", "#EA4C26", "#FC6CBC", "#E94A86", "#C230D8", "#9A1CF5", "#733BC7", "#8551F3",
            "#5E4FDE", "#4A3DBD", "#173791", "#32769A", "#30A198", "#26BBAF", "#379B7D", "#7DB324",
            "#A0D743", "#A3A153", "#B69042", "#EFAC57", "#EF5E57", "#FC8599", "#DD6C6C", "#4D5EFF",
            "#24DDEB"};

    public static String[] getDefaultPalette() {
        return PALETTE_DEFAULT;
    }

    /**
     * Tints a menu item icon
     * @param item
     */
    public static void tintIcon(MenuItem item, int resId, Context context){
        Drawable drawable = item.getIcon();
        tintIcon(drawable, resId, context);
    }

    public static void tintIcon(Drawable drawable, int resId, Context context){
        if (drawable != null && context != null) {
            // If we don't mutate the drawable, then all drawables with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(context.getResources().getColor(resId),
                    PorterDuff.Mode.SRC_IN);
        }
    }
}
