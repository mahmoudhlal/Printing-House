package com.mahmoud.printinghouse.Utils;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.mahmoud.printinghouse.R;

import java.util.ArrayList;
import java.util.List;

public class ReplaceFragment {

    private static List<String> fragments = new ArrayList<String>();
    private static ReplaceFragment Instance ;
    private static Context mContext ;

    public static synchronized ReplaceFragment getInstance(Context context){
        mContext=context;
        if (Instance==null)
            Instance=new ReplaceFragment();
        return Instance;
    }

    public List<String> getFragments() {
        return fragments;
    }

    public void replaceFragment(Fragment fragment, int frameLayout, String title) {
        Log.e("backStateName", title);
        //fragment not in back stack, create it.
        if (fragment.isAdded()) {
            return; //or return false/true, based on where you are calling from
        }
        FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().addToBackStack(title);
        if (!fragments.contains(title)) {
            Log.e("check", "added");
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_out_right, R.anim.enter_from_right, R.anim.exit_out_left);

           /* switch (title) {
                case "Login":
                case "Register":
                    ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up, R.anim.slide_down, R.anim.slide_up);
                    break;
                case "ChooseAccess":
                    ft.setCustomAnimations(R.anim.enter_from_left, R.anim.slide_down, R.anim.enter_from_right, R.anim.slide_down);
                    break;
                default:
                    ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_out_right, R.anim.enter_from_right, R.anim.exit_out_left);
                    break;
            }*/
            ft.replace(frameLayout, fragment);
            ft.commit();

            fragments.add(title);
            System.out.println("backStateName" + fragments);
        } else {
            try {} catch (Exception e) {}
            Log.e("check", "not_added");
            ft.replace(frameLayout, fragment);
            ft.commit();
        }
    }




}
