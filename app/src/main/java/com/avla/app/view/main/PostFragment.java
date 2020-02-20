package com.avla.app.view.main;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Interface.SearchInterface;
import com.avla.app.R;
import com.avla.app.adapter.ViewPagerAdapter;
import com.avla.app.view.main.postInner.EventFragment;
import com.avla.app.view.main.postInner.FeedPostFragment;
import com.avla.app.view.main.postInner.PopularPostEventFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements SearchInterface {

    private static final String TAG = "PostFragment";
    private EditText searchFragment;

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

    public static Fragment newInstance() {
        return new PostFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        searchFragment = view.findViewById(R.id.search_post);

        searchFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Fragment fragment = getChildFragmentManager().findFragmentByTag("all_post");

                setQuery(s.toString());
            }
        });
        tagsId = new ArrayList<>();
        token = getActivity().getIntent().getStringExtra("token");
        fab = view.findViewById(R.id.post_fab);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(new PopularPostEventFragment(),"Popular");
        viewPagerAdapter.addFragment(new FeedPostFragment(),"Feed");
        viewPagerAdapter.addFragment(new EventFragment(),"Events");
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
        startActivityForResult(intent, 42);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 42 && data != null){
            tagsId = new ArrayList<>();
            tagsId = data.getStringArrayListExtra("tags_id");
            for (int i = 0; i < tagsId.size(); i++) {
                tagsJSONId.put(tagsId.get(i));
            }
        }
    }
    public static ArrayList<String> getTags(){
        return tagsId;
    }

    @Override
    public String getQuery() {
        if (searchFragment.getText().toString() != null)
        return searchFragment.getText().toString();
        else return "";
    }

    @Override
    public String setQuery(String s) {

        return s;
    }
}
