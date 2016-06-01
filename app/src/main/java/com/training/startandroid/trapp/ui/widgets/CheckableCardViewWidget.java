package com.training.startandroid.trapp.ui.widgets;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.Checkable;

public class CheckableCardViewWidget extends CardView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mIsChecked;

    public CheckableCardViewWidget(Context context) {
        super(context);
    }

    public CheckableCardViewWidget(Context context, AttributeSet attrSet) {
        super(context, attrSet);
    }

    public CheckableCardViewWidget(Context context, AttributeSet attrSet, int defStyleAttr) {
        super(context, attrSet, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void setChecked(boolean isChecked) {
        boolean wasChecked = isChecked();
        mIsChecked = isChecked;

        if (wasChecked ^ mIsChecked) {
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }
}
