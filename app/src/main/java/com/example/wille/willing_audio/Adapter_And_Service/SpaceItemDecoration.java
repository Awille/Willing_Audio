package com.example.wille.willing_audio.Adapter_And_Service;



/**
 * Created by Administrator on 2017/12/31.
 */

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lgla on 2017/12/31.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view)%3!= 2) {
            outRect.right = mSpace;
        }
        if(parent.getChildAdapterPosition(view)>2){
            outRect.top=mSpace*2;
        }

    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}