package com.training.startandroid.trapp.util;


/*
*   This FragmentHelper working only if
*   we create fragment mark tag.
*   Same tag need using in addToBackStack operation
*   for this created fragment.
*/

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.reflect.Method;

public class FragmentHelper {

    private static final String LOG_TAG = FragmentHelper.class.getSimpleName();


    public static boolean addFragmentInStack(FragmentManager fragmentManager, Fragment addFragment, Integer containerViewId) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = getHigherFragmentInStack(fragmentManager);

        if (currentFragment != null) {

            fragmentTransaction.hide(currentFragment);

            final String currentOperationTag = addFragment.getClass().getName();

            if (containerViewId != null) {

                fragmentTransaction.add(containerViewId, addFragment, currentOperationTag);
            } else {
                fragmentTransaction.add(addFragment, currentOperationTag);
            }
            fragmentTransaction.addToBackStack(currentOperationTag);
            fragmentTransaction.commit();

            return true;
        } else {
            return false;

        }
    }

    public static Fragment getHigherFragmentInStack(FragmentManager fragmentManager) {

        Fragment currentHigherFragment = null;
        int countFragment = fragmentManager.getBackStackEntryCount();

        if (countFragment > 0) {
            final String tag = fragmentManager.getBackStackEntryAt(countFragment - 1).getName();
            currentHigherFragment = fragmentManager.findFragmentByTag(tag);

            Fragment childFragment = getHigherFragmentInStack(currentHigherFragment.getChildFragmentManager());

            if (childFragment != null) {
                currentHigherFragment = childFragment;
            }

        }

        return currentHigherFragment;
    }

    public static Fragment getPreviousFragmentInBackStack(FragmentManager fragmentManager) {

        Fragment higherFragment = getHigherFragmentInStack(fragmentManager);

        if (higherFragment != null) {
            FragmentManager fragmentMangerInHigherLevel = higherFragment.getFragmentManager();

            int countFragmentsInLevel = fragmentMangerInHigherLevel.getBackStackEntryCount();

            if (countFragmentsInLevel > 1) {

                final String tagPrevious = fragmentManager.getBackStackEntryAt(countFragmentsInLevel - 2).getName();
                return fragmentManager.findFragmentByTag(tagPrevious);
            } else {

                return higherFragment.getParentFragment();
            }

        }

        return null;
//        Fragment previousFragment = null;
//        int countFragments = fragmentManager.getBackStackEntryCount();
//
//        if (countFragments > 1) {
//
//            final String tagHigherFragment = fragmentManager.getBackStackEntryAt(countFragments - 1).getName();
//            Fragment higherFragment = fragmentManager.findFragmentByTag(tagHigherFragment);
//
//            Fragment childPreviousFragment = getPreviousFragmentInBackStack(higherFragment.getChildFragmentManager());
//
//            if (childPreviousFragment != null) {
//                higherFragment = childPreviousFragment;
//            }
//
//            final String tagPrevious = fragmentManager.getBackStackEntryAt(countFragments - 2).getName();
//            previousFragment = fragmentManager.findFragmentByTag(tagPrevious);
//        }
//        return previousFragment;
    }

    public static void getBackPreviousFragment(Fragment currentFragment, Fragment previousFragment, FragmentManager fragmentManager) {

        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (currentFragment != null) {
                Fragment parentFragment = currentFragment.getParentFragment();
                fragmentTransaction.remove(currentFragment);

                if (previousFragment != null) {

                    if (parentFragment != null) {
                        if (parentFragment == previousFragment) {

//                          Using reflection in Java for invoking method setVisible(boolean)

                            Method methodSetVisibleView = parentFragment.getClass().getMethod("setVisible", new Class[]{boolean.class});
                            methodSetVisibleView.invoke(parentFragment, true);

//                            parentFragment.setVisible(true);

                        }
                    } else {
                        fragmentTransaction.show(previousFragment);
                    }
                }

            }

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentManager.popBackStackImmediate();
            fragmentTransaction.commit();

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
        }

    }

    public static void closeFragment(FragmentManager fragmentManager) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(this);
        fragmentManager.popBackStackImmediate();
        fragmentTransaction.commit();

    }


}
