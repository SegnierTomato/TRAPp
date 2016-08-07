package com.training.startandroid.trapp.ui.fragments.words;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.*;
import android.widget.*;

public class CollapsingToolbarFABBehavior extends FloatingActionButton.Behavior {

    private final String LOG_TAG = CollapsingToolbarFABBehavior.class.getSimpleName();

    public CollapsingToolbarFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {

        Log.d(LOG_TAG, "---------- Dependent view ------------");
        Log.d(LOG_TAG, "translationY: " + dependency.getTranslationY());
        Log.d(LOG_TAG, "Y: " + dependency.getY());
        Log.d(LOG_TAG, "scroll Y: " + dependency.getScrollY());
        Log.d(LOG_TAG, "measured height: " + dependency.getMeasuredHeight());
        Log.d(LOG_TAG, "height: " + dependency.getHeight());

//        int dependencyCollapsingDistance = dependency.getHeight() / 2;
        int childHeight = child.getHeight() / 2;

        if (dependency.getY() > childHeight - 10 && child.getVisibility() == View.VISIBLE) {
            child.hide();
        } else if (child.getVisibility() != View.VISIBLE) {
            child.show();
        }


        Log.d(LOG_TAG, "---------- Child view ------------");

        Log.d(LOG_TAG, "translationY: " + child.getTranslationY());
        Log.d(LOG_TAG, "scrollY: " + child.getScrollY());
        Log.d(LOG_TAG, "height: " + child.getHeight());
//        child.hide();

        return true;
    }
}
