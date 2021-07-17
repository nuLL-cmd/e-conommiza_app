package com.automatodev.e_conommiza_app.view.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.view.activity.FragmentHost;

import java.util.List;

public class FragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<PerspectiveEntity> perspectiveEntities;
    private FragmentManager fm;


    public FragmentPageAdapter(@NonNull FragmentManager fm, int b, List<PerspectiveEntity> perspectiveEntities) {
        super(fm, b);
        this.perspectiveEntities = perspectiveEntities;
        this.fm = fm;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        if(fm.getFragments().contains(object))
            return POSITION_NONE;
        else
            return POSITION_UNCHANGED;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        FragmentHost fragmentTest = new FragmentHost();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putSerializable("item",perspectiveEntities.get(position));
        fragmentTest.setArguments(bundle);
        return fragmentTest;
    }

    @Override
    public int getCount() {
        return perspectiveEntities.size();
    }


}
