package com.training.startandroid.trapp.ui.fragments;

/* This interface allow notify
*  parent fragments about them child state (alive or dead)
*  */

public interface NestedFragmentIteraction {
    public void notifyParent2ChildState(boolean isAlive);
}
