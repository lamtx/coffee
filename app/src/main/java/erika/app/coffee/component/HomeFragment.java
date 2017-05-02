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
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.state.HomeState;

public class HomeFragment extends BaseFragment<HomeState> {
    private ViewPager viewPager;

    @Override
    public HomeState getStateFromStore(AppState appState) {
        return appState.home;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager.setAdapter(new TableFragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void bindStateToView(HomeState state) {
        super.bindStateToView(state);
    }
    private static final TableStatus[] tableStatuses = {
            TableStatus.All,
            TableStatus.Busy,
            TableStatus.Tamtinh,
            TableStatus.TamtinhAndBusy,
            TableStatus.Available
    };
    private static final int[] titleIds = {
            R.string.all,
            R.string.busy,
            R.string.tam_tinh,
            R.string.tam_tinh_and_busy,
            R.string.available
    };
    private class TableFragmentAdapter extends FragmentPagerAdapter {
        private Fragment[] caches = new Fragment[tableStatuses.length];
        TableFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = caches[position];
            if (fragment == null) {
                fragment = TableListFragment.newInstance(tableStatuses[position]);
                caches[position] = fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tableStatuses.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titleIds[position]);
        }
    }
}
