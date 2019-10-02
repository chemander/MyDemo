package com.chemander.story.home;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chemander.story.R;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.utils.SettingsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {
    View mainView;
    RecyclerView downloadRecyclerView;
    private HomeRecycleAdapter downloadAdapter;
    private ChapterViewModel viewModel;
    private List<StoryInformation> downloadList = new ArrayList<>();

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_download, container, false);
        downloadRecyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_home);
        LibraryFragment.ColumnQty columnQty = new LibraryFragment.ColumnQty(getContext(), R.layout.item_home_hot_news_update);
        int spanCount = columnQty.calculateNoOfColumns();
        downloadRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        int spacing = columnQty.calculateSpacing();
        boolean includeEdge = true;
        downloadRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//        libraryRecyclerView.addItemDecoration(new GridSpacing(columnQty.calculateSpacing()));
        viewModel = ViewModelProviders.of(getActivity()).get(ChapterViewModel.class);
//                viewModel.loadAllFavoriteStories();
        downloadList.addAll(Arrays.asList(viewModel.loadAllDownloadStories()));
        ProgressDialog progressDialog = SettingsManager.showLoadingDialog(getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                downloadAdapter = new HomeRecycleAdapter(getContext(), downloadList);
                downloadRecyclerView.setAdapter(downloadAdapter);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        },1000);

        return mainView;
    }

}
