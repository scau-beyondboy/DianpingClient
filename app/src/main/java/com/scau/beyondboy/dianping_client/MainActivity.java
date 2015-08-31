package com.scau.beyondboy.dianping_client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.scau.beyondboy.dianping_client.fragment.FragmentHome;
import com.scau.beyondboy.dianping_client.fragment.FragmentMy;
import com.scau.beyondboy.dianping_client.fragment.FragmentSearch;
import com.scau.beyondboy.dianping_client.fragment.FragmentTuan;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    @Bind(R.id.main_bottom_tabs)
    RadioGroup mBottomTabsGroup;
    @Bind(R.id.main_home)
    RadioButton mHomeButton;
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //默认选中
        mHomeButton.setChecked(true);
        mFragmentManager=getSupportFragmentManager();
        changeFragment(new FragmentHome(),false);
        mBottomTabsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.main_home:
                        changeFragment(new FragmentHome(),true);
                        break;
                    case R.id.main_search:
                        changeFragment(new FragmentSearch(),true);
                        break;
                    case R.id.main_my:
                        changeFragment(new FragmentMy(),true);
                        break;
                    case R.id.main_tuan:
                        changeFragment(new FragmentTuan(),true);
                        break;
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //切换不同的fragment
    private void changeFragment(Fragment fragment,boolean isAddToStack)
    {
        //开启事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        if (!isAddToStack)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
