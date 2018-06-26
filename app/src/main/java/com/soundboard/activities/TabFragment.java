package com.soundboard.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.soundboard.utils.ObservableList;
import com.soundboard.R;
import com.soundboard.utils.SoundStore;
import com.soundboard.utils.Utils;
import com.soundboard.adapters.SoundsAdapter;
import com.soundboard.models.Categories;
import com.soundboard.models.Sound;

import java.util.ArrayList;

public class TabFragment extends Fragment {

    private ObservableList<Sound> sounds;
    private LayoutInflater inflater;
    private Categories category;
    private LinearLayout layout;

    public static TabFragment newInstance(Categories category) {
        TabFragment f = new TabFragment();

        Bundle args = new Bundle();
        args.putSerializable("Category", category);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup tab = (ViewGroup) inflater.inflate(R.layout.tab_fragment, container, false);

        this.layout = (LinearLayout) tab.findViewById(R.id.layout_fragment);
        this.inflater = inflater;
        this.category = (Categories) getArguments().getSerializable("Category");
        this.sounds = SoundStore.getInstance().getSoundsDictionary().get(category);

        if(category == Categories.Recent){
            initializeFragmentRecent();
        }

        AbsListView listView = (GridView) tab.findViewById(R.id.list_view_fragment);
        boolean showCategoryIconInTheButton = category == Categories.Search || category == Categories.Recent;
        listView.setAdapter(new SoundsAdapter(getContext(), sounds, showCategoryIconInTheButton));

        return tab;
    }

    private void initializeFragmentRecent(){
        Button removeAll = (Button)inflater.inflate(R.layout.special_button, layout, false);
        removeAll.setText(R.string.remove_all);
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sounds.clear();
            }
        });
        layout.addView(removeAll);

        Button playAll = (Button)inflater.inflate(R.layout.special_button, layout, false);
        playAll.setText(R.string.play_all);
        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sounds.isEmpty()) {
                    MainActivity.playSoundsSubsequently(sounds);
                }
            }
        });
        layout.addView(playAll);

        Button shareAll = (Button)inflater.inflate(R.layout.special_button, layout, false);
        shareAll.setText(R.string.share_all);
        shareAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sounds.isEmpty()) {
                    ArrayList<Uri> uris = new ArrayList<>();
                    // reversing order
                    for (int i = sounds.size() - 1; i >= 0; i--) {
                        uris.add(Utils.resourceToUri(getContext(), sounds.get(i).getName(), "ogg", sounds.get(i).getAudioID()));
                    }
                    final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    intent.setType("audio/*");
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            }
        });
        layout.addView(shareAll);
    }

    /// called when the users can see this tab
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            // when this tab is showed, do something
        }
    }

}