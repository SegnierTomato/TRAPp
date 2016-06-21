package com.training.startandroid.trapp.util;


/*
*   This FragmentHelper working only if
*   we create fragment mark tag.
*   Same tag need using in addToBackStack operation
*   for this created fragment.
*/

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

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

        int countFragment = fragmentManager.getBackStackEntryCount();

        final String tag = fragmentManager.getBackStackEntryAt(countFragment - 1).getName();
        final Fragment currentFragment = fragmentManager.findFragmentByTag(tag);

        return currentFragment;
    }

    public static Fragment getPreviousFragmentInBaclStack(FragmentManager fragmentManager) {

        int countFragments = fragmentManager.getBackStackEntryCount();

        final String tagPrevious = fragmentManager.getBackStackEntryAt(countFragments - 2).getName();
        Fragment previousFragment = fragmentManager.findFragmentByTag(tagPrevious);

        return previousFragment;
    }

    public static void getBackPreviousFragment(Fragment currentFragment, Fragment previousFragment, FragmentManager fragmentManager) {

        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (currentFragment != null) {
//            fragmentTransaction.hide(currentFragment);
                fragmentTransaction.remove(currentFragment);
            }

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);

            if (previousFragment != null) {
                fragmentTransaction.show(previousFragment);
            }

//            int count = fragmentManager.getBackStackEntryCount();
            fragmentManager.popBackStackImmediate();
//            count = fragmentManager.getBackStackEntryCount();

//            fragmentTransaction.addToBackStack(null);
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
