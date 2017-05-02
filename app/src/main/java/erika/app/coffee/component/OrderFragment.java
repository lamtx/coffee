package erika.app.coffee.component;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import erika.app.coffee.R;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.MenuType;
import erika.app.coffee.state.OrderState;

public class OrderFragment extends BaseFragment<OrderState> {

    @Override
    public OrderState getStateFromStore(AppState appState) {
        return appState.order;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private static final int[] titleIds = {
            R.string.menu,
            R.string.starred,
            R.string.recent,
    };

    private class FragmentAdapter extends FragmentPagerAdapter {
        private Fragment[] caches = new Fragment[MenuType.values().length];
        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = caches[position];
            if (fragment == null) {
                fragment = MenuFragment.newInstance(MenuType.values()[position]);
                caches[position] = fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return MenuType.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titleIds[position]);
        }
    }
}
