package com.nettlike.app.posts;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.nettlike.app.R;
import com.nettlike.app.adapter.ViewPagerAdapter;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.posts.postInner.AllPostFragment;
import com.nettlike.app.posts.postInner.FeedPostFragment;
import com.nettlike.app.view.main.GetTagsActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

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
    public static ArrayList<String> tagsName;
    public static List<PayloadTag> payloadTagList = new ArrayList<>();
    private AppDatabase db;
    private TokenDao tokenDao;
    public PostFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initViews(view);
        return view;
    }

    public static List<PayloadTag> getTags() {

        return payloadTagList;
    }

    public static void setTagsId() {
        payloadTagList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        tagsId = new ArrayList<>();
        tagsName = new ArrayList<>();
//        token = getActivity().getIntent().getStringExtra("token");
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
        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
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
        intent.putParcelableArrayListExtra("selected_tags", (ArrayList<? extends Parcelable>) payloadTagList);
//        intent.putExtra("token", token);
//        intent.putExtra("tags_id", tagsId);
//        intent.putExtra("tags_name", tagsName);
        startActivityForResult(intent, 42);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 42) {
            if (data != null)
                payloadTagList = data.getParcelableArrayListExtra("selected_tags");
            else
                payloadTagList = new ArrayList<>();
            for (int i = 0; i < payloadTagList.size(); i++) {
                tagsJSONId.put(payloadTagList.get(i).getId());
            }
            if (payloadTagList.size() != 0)
                fab.setText("Selected");
            if (payloadTagList.size() == 0)
                fab.setText("Filter");
        }
    }
}
