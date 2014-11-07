package com.example.nick.notey;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Note extends Activity implements View.OnTouchListener {

    private static final String LIST_FRAGMENT_TAG = "list frag";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataSet;
    private ViewGroup activeButton = null;
    private ViewGroup buttonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        int buttonsSpacing = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int buttonSize = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        Outline circularOutline = new Outline();
        circularOutline.setOval(0, 0, buttonSize, buttonSize);

        mRecyclerView.setHasFixedSize(true);
        myDataSet = new String[] {"test string 1","test string 2"};
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataSet);
        mRecyclerView.setAdapter(mAdapter);

        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.activity_note, null),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        this.buttonsContainer = (ViewGroup) findViewById(R.id.buttonsContainer);
        ViewGroup buttonHost = (ViewGroup) getLayoutInflater().inflate(R.layout.my_circle, buttonsContainer, false);
        TextView button = (TextView) buttonHost.getChildAt(0);


        buttonHost.setOnTouchListener(this);
        buttonsContainer.addView(buttonHost);
        //mLayoutManager.addView(buttonsContainer);

        selectButton(((ViewGroup) buttonsContainer.getChildAt(0)), false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleList() {
        Fragment f = getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
        if (f != null) {
            getFragmentManager().popBackStack();
        } else {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_up,
                            R.animator.slide_down,
                            R.animator.slide_up,
                            R.animator.slide_down)
                    .add(R.id.list_fragment_container, Fragment
                                    .instantiate(this, SlidingRelativeLayout.class.getName()),
                            LIST_FRAGMENT_TAG
                    ).addToBackStack(null).commit();
        }
    }

    private void selectButton(ViewGroup buttonHost, boolean reveal, int startX, int startY) {
        if (buttonHost == activeButton) {
            return;
        }
        if (activeButton != null) {
            activeButton.setSelected(false);
            activeButton = null;
        }
        activeButton = buttonHost;
        activeButton.setSelected(true);
        View button = activeButton.getChildAt(0);
        if (reveal) {
            ViewAnimationUtils.createCircularReveal(button,
                    startX,
                    startY,
                    0,
                    button.getHeight()).start();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ViewGroup) view).getChildAt(0).getBackground().setHotspot(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                selectButton((ViewGroup) view, true, (int) motionEvent.getX(), (int) motionEvent.getY());
                break;
        }
        toggleList();
        return false;
    }

    private void selectButton(ViewGroup buttonHost, boolean reveal) {
        selectButton(buttonHost, reveal, buttonHost.getWidth(), buttonHost.getHeight());
    }
}
