package com.cool.sarthak.hiii;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sarthak on 14/01/18.
 */

class SectioPageAdapter  extends FragmentPagerAdapter{


    public SectioPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                RequestFragment requestFragment =new RequestFragment();
                return requestFragment;

            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;


            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position)
    {

        switch (position)
        {
            case 0:
                return "Requests";
            case 1:
                return "Chats";
            case  2:
                return  "Friends";
            default:
                return  null;

        }
    }
}
