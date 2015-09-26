package com.scau.beyondboy.dianping_client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scau.beyondboy.dianping_client.NearbyMapActivity;
import com.scau.beyondboy.dianping_client.R;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-31
 * Time: 10:31
 */
public class FragmentSearch extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        startActivity(new Intent(getActivity(), NearbyMapActivity.class));
        return inflater.inflate(R.layout.search_index, container, false);
    }
}
