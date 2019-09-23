package com.chemander.story.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.story.R;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.utils.SettingsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentFragment extends Fragment {
    private View mainView;
    private RecyclerView recentRecycleView;
    private HomeRecycleAdapter recentAdapter;
    private ChapterViewModel viewModel;
    private List<StoryInformation> recentList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_recent_list, container, false);
        recentRecycleView = (RecyclerView) mainView.findViewById(R.id.recycler_home);
        LibraryFragment.ColumnQty columnQty = new LibraryFragment.ColumnQty(getContext(), R.layout.item_home_hot_news_update);
        int spanCount = columnQty.calculateNoOfColumns();
        recentRecycleView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        int spacing = columnQty.calculateSpacing();
        boolean includeEdge = true;
        recentRecycleView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        viewModel = ViewModelProviders.of(getActivity()).get(ChapterViewModel.class);
//                viewModel.loadAllFavoriteStories();
        recentList.addAll(Arrays.asList(viewModel.loadAllRecentStories()));
        ProgressDialog progressDialog = SettingsManager.showLoadingDialog(getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recentAdapter = new HomeRecycleAdapter(getContext(), recentList);
                recentRecycleView.setAdapter(recentAdapter);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        },1000);
        return mainView;
    }
}
