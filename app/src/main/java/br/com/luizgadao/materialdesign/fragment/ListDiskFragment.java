package br.com.luizgadao.materialdesign.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.luizgadao.materialdesign.DetailsActivity;
import br.com.luizgadao.materialdesign.LoadDisk;
import br.com.luizgadao.materialdesign.R;
import br.com.luizgadao.materialdesign.adapter.DiskAdapter;
import br.com.luizgadao.materialdesign.model.Disk;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListDiskFragment extends Fragment implements DiskAdapter.OnClickDiskListener {

    private static final String TAG = ListDiskFragment.class.getSimpleName();

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    private Disk[] mDisks;
    private DiskDownloadTask mTask;

    public ListDiskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list_disk, container, false);
        ButterKnife.bind(this, view);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTask = new DiskDownloadTask();
                mTask.execute();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mDisks == null){
            if (mTask == null){
                mTask = new DiskDownloadTask();
                mTask.execute();
            }else if (mTask.getStatus() == AsyncTask.Status.RUNNING){
                showProgress();
            }
        }else{
            updateList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view, int position, Disk disk) {
        Log.i(TAG, "click item: " + position);
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_DISK, disk);

        startActivity(intent);
    }

    private void showProgress(){
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
    }

    private void updateList() {
        DiskAdapter adapter = new DiskAdapter(mDisks);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    class DiskDownloadTask extends AsyncTask<Void, Void, Disk[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Disk[] doInBackground(Void... params) {
            return LoadDisk.loadDisk();
        }

        @Override
        protected void onPostExecute(Disk[] disks) {
            super.onPostExecute(disks);
            mSwipeRefresh.setRefreshing(false);
            if (disks != null){
                mDisks = disks;
                updateList();
            }
        }
    }

}
