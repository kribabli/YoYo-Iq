package com.example.yoyoiq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.yoyoiq.Fragment.CricketFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentManager childFragmentManager;

    //icon for tabLayout
    private int[] tabIcons = {
            R.drawable.ic_baseline_sports_baseball_24
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = rootView.findViewById(R.id.viewpager);
        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        childFragmentManager = getChildFragmentManager();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 0, 0, 0);
            tab.requestLayout();
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return rootView;
    }

    //Call the method for icon in tabLayout
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(childFragmentManager);
//        adapter.addFragment(new CricketFragment(), "CRICKET");
        adapter.addFragment(new CricketFragment(), "Cricket");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}