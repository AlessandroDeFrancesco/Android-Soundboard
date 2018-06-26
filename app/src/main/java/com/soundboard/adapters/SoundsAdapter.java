package com.soundboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundboard.utils.ObservableList;
import com.soundboard.R;
import com.soundboard.models.Sound;
import com.soundboard.utils.Utils;
import com.soundboard.activities.MainActivity;

import java.util.Observable;
import java.util.Observer;

public class SoundsAdapter extends BaseAdapter implements Observer {

    private final Context context;
    private final ObservableList<Sound> sounds;
    private boolean showCategoryIcon;

    public SoundsAdapter(Context context, ObservableList<Sound> sounds, boolean showCategoryIcon){
        super();
        this.context = context;
        this.sounds = sounds;
        this.showCategoryIcon = showCategoryIcon;
        sounds.addObserver(this);
    }

    @Override
    public int getCount() {
        return sounds.size();
    }

    @Override
    public Object getItem(int position) {
        return sounds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.sound_button, null);
        }
        final Sound sound = (Sound) getItem(position);
        TextView textV = (TextView) convertView.findViewById(R.id.name);
        ImageView shareB = (ImageView) convertView.findViewById(R.id.share);

        textV.setText(sound.getName());
        textV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.playSound(sound);
            }
        });
        shareB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Utils.resourceToUri(context, sound.getName(), "ogg", sound.getAudioID());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(share, "Share"));
            }
        });

        ImageView image = (ImageView) convertView.findViewById(R.id.sound_icon);
        if(showCategoryIcon) {
            image.setImageResource(sound.getIconID());
        } else {
            image.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable == sounds)
            notifyDataSetChanged();
    }
}
