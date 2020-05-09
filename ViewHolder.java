package com.example.memesanskar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mClicklistener.onItemClick(view, getAdapterPosition());

            }
        });
    }

    public void SetDetails(Context ctx, String title, String image){
        TextView mTitle = mview.findViewById(R.id.rTitleTv);
        ImageView mImage = mview.findViewById(R.id.rImage);
        mTitle.setText(title);
        Picasso.get().load(image).into(mImage);
    }

    private ViewHolder.ClickListener mClicklistener;

    public interface ClickListener{

        void onItemClick(View view,int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){

        mClicklistener = clickListener;

    }
}
