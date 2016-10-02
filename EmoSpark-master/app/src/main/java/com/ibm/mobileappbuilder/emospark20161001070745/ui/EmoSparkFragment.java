

package com.ibm.mobileappbuilder.emospark20161001070745.ui;

import android.os.Bundle;

import com.ibm.mobileappbuilder.emospark20161001070745.R;

import java.util.ArrayList;
import java.util.List;

import ibmmobileappbuilder.MenuItem;

import ibmmobileappbuilder.actions.StartActivityAction;
import ibmmobileappbuilder.util.Constants;

/**
 * EmoSparkFragment menu fragment.
 */
public class EmoSparkFragment extends ibmmobileappbuilder.ui.MenuFragment {

    /**
     * Default constructor
     */
    public EmoSparkFragment(){
        super();
    }

    // Factory method
    public static EmoSparkFragment newInstance(Bundle args) {
        EmoSparkFragment fragment = new EmoSparkFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
                }

    // Menu Fragment interface
    @Override
    public List<MenuItem> getMenuItems() {
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        items.add(new MenuItem()
            .setLabel("Cherished")
            .setIcon(R.drawable.jpg_2383b8c0a126d1d6e45c69e1f1d8c835756)
            .setAction(new StartActivityAction(CherishedActivity.class, Constants.DETAIL))
        );
        items.add(new MenuItem()
            .setLabel("Peace")
            .setIcon(R.drawable.jpg_0f30586d102c7777e83b5ccc770692f838)
            .setAction(new StartActivityAction(PeaceActivity.class, Constants.DETAIL))
        );
        items.add(new MenuItem()
            .setLabel("Depressed")
            .setIcon(R.drawable.png_tearfulemoticon661)
            .setAction(new StartActivityAction(DepressedActivity.class, Constants.DETAIL))
        );
        items.add(new MenuItem()
            .setLabel("Angry")
            .setIcon(R.drawable.png_brewinganger908)
            .setAction(new StartActivityAction(AngryActivity.class, Constants.DETAIL))
        );
        items.add(new MenuItem()
            .setLabel("Fear")
            .setIcon(R.drawable.png_sorrysmiley760)
            .setAction(new StartActivityAction(FearActivity.class, Constants.DETAIL))
        );
        return items;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_grid;
    }

    @Override
    public int getItemLayout() {
        return R.layout.emospark_item;
    }
}

