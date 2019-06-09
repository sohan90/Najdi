package com.najdi.android.najdiapp.utitility;


import com.najdi.android.najdiapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentHelper {
    public static void addFragment(AppCompatActivity context, Fragment fragment, String tag,
                                   int container) {
        FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
        ft.add(container, fragment, tag).commit();
    }


    public static void addFragmentWithAnimation(AppCompatActivity context, Fragment fragment, String tag,
                                                int container, int enter, int exit, int popEnter, int popExit){
        FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
        ft.add(container, fragment, tag);
        if (tag != null) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }
    public static void removeFragment(AppCompatActivity appCompatActivity, Fragment fragment) {
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager()
                .beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /**
     * <p>replace fragment to existing fragment. Provides developer to add this fragment to backstack.</p>
     *
     * @param context           context
     * @param fragment,         fragment to add
     * @param container         , layout id to replace the view.
     * @param canAddBackStrace, is added to backstrace
     */

    public static void replaceFragment(AppCompatActivity context, Fragment fragment, String tag,
                                       boolean canAddBackStrace, int container) {
        FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
        //ft.setCustomAnimations(android.R.anim.slide_in_left, 0);
        ft.replace(container, fragment, tag);
        /*ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);*/
        if (canAddBackStrace) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    public static void replaceFragmentWithAnim(AppCompatActivity context, Fragment fragment, String tag,
                                       boolean canAddBackStrace, int container) {
        FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.slide_right, 0);
        ft.replace(container, fragment, tag);
        /*ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);*/
        if (canAddBackStrace) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    /**
     * <p>Close the number of fragments.</p>
     *
     * @param context       context
     * @param numBackStack, number of fragments to pop up.
     */
    public static void popBackStack(AppCompatActivity context, int numBackStack) {
        FragmentManager manager = context.getSupportFragmentManager();
        int fragCount = manager.getBackStackEntryCount();
        for (int i = 0; i < fragCount - numBackStack; i++) {
            manager.popBackStack();
        }
    }

    /**
     * <p>Close the fragment </p>
     *
     * @param context context
     */
    public static void popBackStack(AppCompatActivity context) {
        FragmentManager manager = context.getSupportFragmentManager();
        manager.popBackStack();
    }

    /**
     * <p> Close the all the fragment till the given tag name</p>
     *
     * @param context context
     * @param tag     if tag name is null then all the fragment will be close or till the given tag name
     */
    public static void popBackStack(AppCompatActivity context, String tag) {
        FragmentManager manager = context.getSupportFragmentManager();
        manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static Fragment getFragmentByTag(AppCompatActivity context, String tag) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(tag);
    }

    public static Fragment getFragmentById(AppCompatActivity context, int id) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        return fragmentManager.findFragmentById(id);
    }
}
