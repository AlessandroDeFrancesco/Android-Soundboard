package com.soundboard.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundboard.models.Categories;
import com.soundboard.adapters.PagerAdapter;
import com.soundboard.R;
import com.soundboard.models.Sound;
import com.soundboard.utils.ObservableList;
import com.soundboard.utils.SoundPlayer;
import com.soundboard.utils.SoundStore;
import com.soundboard.utils.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RECENT_SOUNDS_MAX_NUMBER = 20;

    private static SoundPlayer soundPlayer;
    private static ViewPager viewPager;
    private static Categories[] orderedCategories;

    private MenuItem searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundPlayer = new SoundPlayer(this);

        // tabs style
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        // add the tabs categories
        orderedCategories = Categories.values();
        for (Categories category : orderedCategories) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setTag(category);
            View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
            ((ImageView)view.findViewById(R.id.tab_icon)).setImageResource(Utils.getDrawableIdByName(this, category.name()));
            ((TextView)view.findViewById(R.id.tab_text)).setText(category.name().toUpperCase());
            tab.setCustomView(view);

            tabLayout.addTab(tab);
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), orderedCategories.length, orderedCategories);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getTag() == Categories.Search)
                        searchView.expandActionView();
                    viewPager.setCurrentItem(tab.getPosition(), true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    if(tab.getTag() != Categories.Search)
                        searchView.collapseActionView();
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if(tab.getTag() == Categories.Search)
                        searchView.expandActionView();
                    viewPager.setCurrentItem(tab.getPosition());
                }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        soundPlayer.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = menu.findItem(R.id.search);
        SearchView actionView = (SearchView) searchView.getActionView();
        actionView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        actionView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0)
                    searchSounds(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.never_interrupt:
                soundPlayer.setNeverInterrupt(!soundPlayer.isNeverInterrupt());
                item.setChecked(soundPlayer.isNeverInterrupt());
                return true;
            case R.id.search:
                item.expandActionView();
                viewPager.setCurrentItem(Utils.ObjectPosition(orderedCategories, Categories.Search), true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchSounds(String query) {
        List<Sound> found = SoundStore.getInstance().searchSounds(query);

        List<Sound> soundsFound = SoundStore.getInstance().getSoundsDictionary().get(Categories.Search);
        soundsFound.clear();
        soundsFound.addAll(found);

        viewPager.setCurrentItem(Utils.ObjectPosition(orderedCategories, Categories.Search), true);
    }

    private static void addRecent(Sound sound) {
        SoundStore.getInstance().getSoundsDictionary().get(Categories.Recent).add(0, sound);
        if(SoundStore.getInstance().getSoundsDictionary().get(Categories.Recent).size() > RECENT_SOUNDS_MAX_NUMBER)
            SoundStore.getInstance().getSoundsDictionary().get(Categories.Recent).remove(RECENT_SOUNDS_MAX_NUMBER);
    }

    public static void playSound(Sound sound) {
        soundPlayer.playSound(sound);
        if(viewPager.getCurrentItem() != Utils.ObjectPosition(orderedCategories, Categories.Recent))
            addRecent(sound);
    }

    public static void playSoundsSubsequently(ObservableList<Sound> sounds) {
        soundPlayer.playSoundsSubsequently(sounds, 0);
    }
}
