package com.ajpasigado.qpicalculator;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;

public class GradesAdapterHelper extends SimpleCallback {
    private GradesRecyclerItemTouchHelper listener;

    public GradesAdapterHelper(int dragDirs, int swipeDirs, GradesRecyclerItemTouchHelper listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        return false;
    }

    public void onSwiped(ViewHolder viewHolder, int direction) {
        if (this.listener != null) {
            this.listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        }
    }

    public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(((GradesAdapter.GradesViewHolder) viewHolder).foreground);
    }

    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(((GradesAdapter.GradesViewHolder) viewHolder).foreground);
        }
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(c, recyclerView, ((GradesAdapter.GradesViewHolder) viewHolder).foreground, dX, dY, actionState, isCurrentlyActive);
    }

    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(c, recyclerView, ((GradesAdapter.GradesViewHolder) viewHolder).foreground, dX, dY, actionState, isCurrentlyActive);
    }
}
