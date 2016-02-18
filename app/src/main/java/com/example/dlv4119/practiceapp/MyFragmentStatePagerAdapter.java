package com.example.dlv4119.practiceapp;

/**
 * Created by dlv4119 on 2016/02/05.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /** 得たポジションによりフラグメントの切り替え */
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new Fragment0();
            case 1:
                return new Fragment1();
            case 2:
                return new Fragment2();
            case 3:
                return new Fragment3();
        }
        return null;
    }

    /** ページ数を返す */
    @Override
    public int getCount() {
        return 4;
    }

    /** そのインデックスに応じたページのタイトルを返す */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "WiFi AP 一覧";
        } else if (position == 1) {
            return "WiFi Direct AP 一覧";
        } else if (position == 2) {
            return "Bluetooth 一覧";
        } else if (position == 3){
            return "通知";
        }
        return null;
    }
}