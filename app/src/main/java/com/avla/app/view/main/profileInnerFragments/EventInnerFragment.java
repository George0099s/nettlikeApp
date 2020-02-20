package com.avla.app.view.main.profileInnerFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.adapter.EventAdapter;
import com.avla.app.adapter.SwipeToDeleteCallback;
import com.avla.app.model.Event;
import com.avla.app.model.UserSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventInnerFragment extends Fragment {
    private static final String TAG = "EventInnerFragment";
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private String token;
    private UserSingleton userSingleton = UserSingleton.INSTANCE;
    public EventInnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_inner, container, false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        eventRecyclerView = view.findViewById(R.id.user_events);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        enableSwipeToDeleteAndUndo();
        getUserEvent();
    }

    private void getUserEvent() {
        eventAdapter = new EventAdapter(userSingleton.getEvents(), getContext(), token);
        eventRecyclerView.setAdapter(eventAdapter);
    }



    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Event item = eventAdapter.getData().get(position);
                eventAdapter.removeItem(position, item.getId());


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(eventRecyclerView);
    }
}
