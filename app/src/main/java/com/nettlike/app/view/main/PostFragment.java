package com.nettlike.app.view.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.nettlike.app.R;
import com.nettlike.app.adapter.ViewPagerAdapter;
import com.nettlike.app.view.main.postInner.AllPostFragment;
import com.nettlike.app.view.main.postInner.FeedPostFragment;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";


    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String token;
    private ExtendedFloatingActionButton fab;
    public JSONArray tagsJSONId = new JSONArray();
    public static ArrayList<String> tagsId;


    public PostFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tagsId = new ArrayList<>();
        token = getActivity().getIntent().getStringExtra("token");
        fab = view.findViewById(R.id.post_fab);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(new AllPostFragment(),"Popular");
        viewPagerAdapter.addFragment(new FeedPostFragment(),"Feed");
        tabLayout = view.findViewById(R.id.tabLayout_post);
        viewPager = view.findViewById(R.id.viewPager_post);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(this::onClick);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.show();
                } else if (position == 1) {
                    fab.show();
                } else if (position == 2){
                    fab.hide();
                }
            }



            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }
    private void onClick(View view) {
        Intent intent = new Intent(getContext(), GetTagsActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("tags_id", tagsId);
        startActivityForResult(intent, 42);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 42 && data != null){
            tagsId = data.getStringArrayListExtra("tags_id");
            for (int i = 0; i < tagsId.size(); i++) {
                tagsJSONId.put(tagsId.get(i));
            }
//            if (tagsJSONId.length() == 0){
               if (tagsId.size() != 0)
                fab.setText("selected");
               if (tagsId.size() == 0)
                    fab.setText("Filter");
//            }
        }
    }
    public static ArrayList<String> getTags(){
        return tagsId;
    }
}
