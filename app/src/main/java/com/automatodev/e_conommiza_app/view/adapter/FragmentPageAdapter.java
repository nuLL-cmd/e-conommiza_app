package com.automatodev.e_conommiza_app.view.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.view.activity.FragmentHost;

import java.util.List;

public class FragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<PerspectiveEntity> perspectiveEntities;
    private int position;

    public FragmentPageAdapter(@NonNull FragmentManager fm, int b, List<PerspectiveEntity> perspectiveEntities) {
        super(fm, b);
        this.perspectiveEntities = perspectiveEntities;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        this.position = position;
        FragmentHost fragmentTest = new FragmentHost();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragmentTest.setArguments(bundle);
        return fragmentTest;
    }

    @Override
    public int getCount() {
        return perspectiveEntities.size();
    }


    public int getIntemCount(){
        return this.position;
    }
}
