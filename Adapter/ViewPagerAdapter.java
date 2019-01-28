package com.devmaker.siftkin.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.devmaker.siftkin.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter{

    private Activity activity;
    private ArrayList<String> images;

    public ViewPagerAdapter(Activity activity, ArrayList<String> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = Objects.requireNonNull(inflater).inflate(R.layout.layout_perfilestab_vp,container,false);

        ImageView image = itemView.findViewById(R.id.imageViewPerfilEstab);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        try{
            Picasso.with(activity.getApplicationContext())
                    .load(images.get(position))
                    .placeholder(R.drawable.swipe)
                    .error(R.drawable.swipe)
                    .into(image);
        }

        catch (Exception ex){
            ex.fillInStackTrace();
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View)object);
    }
}