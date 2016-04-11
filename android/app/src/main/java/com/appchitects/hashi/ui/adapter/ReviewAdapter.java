package com.appchitects.hashi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private ArrayList<Review> reviews;
    private Context context;
    private int layout;

    public ReviewAdapter(Context context, int layout, ArrayList<Review> reviews) {
        super(context, layout, reviews);
        this.context = context;
        this.layout = layout;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layout, null);
        }

        Review review = reviews.get(position);
        TextView text = (TextView) view.findViewById(R.id.list_item_review_text);
        ImageView image = (ImageView) view.findViewById(R.id.list_item_review_img);

        text.setText(review.text);

        //if (review.authorImagePath != null) {
            Picasso.with(context)
                    .load(Api.getMediaUrl(review.authorImagePath))
                    .error(R.drawable.profile_default)
                    .fit()
                    .into(image);
        //} else {
        //    image.setBackground(context.getDrawable(R.drawable.example_profile));
        //}

        return view;
    }

}
