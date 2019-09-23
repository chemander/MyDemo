package com.chemander.story.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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

public class LibraryFragment extends Fragment {
    View mainView;
    RecyclerView libraryRecyclerView;
    private HomeRecycleAdapter libraryAdapter;
    private ChapterViewModel viewModel;
    private List<StoryInformation> favoriteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_library, container, false);
        libraryRecyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_home);
        ColumnQty columnQty = new ColumnQty(getContext(), R.layout.item_home_hot_news_update);
        int spanCount = columnQty.calculateNoOfColumns();
        libraryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        int spacing = columnQty.calculateSpacing();
        boolean includeEdge = true;
        libraryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//        libraryRecyclerView.addItemDecoration(new GridSpacing(columnQty.calculateSpacing()));
        viewModel = ViewModelProviders.of(getActivity()).get(ChapterViewModel.class);
//                viewModel.loadAllFavoriteStories();
        favoriteList.addAll(Arrays.asList(viewModel.loadAllFavoriteStories()));
        ProgressDialog progressDialog = SettingsManager.showLoadingDialog(getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                libraryAdapter = new HomeRecycleAdapter(getContext(), favoriteList);
                libraryRecyclerView.setAdapter(libraryAdapter);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        },1000);

        return mainView;
    }


    public static class ColumnQty {
        private int width, height, remaining;
        private DisplayMetrics displayMetrics;

        public ColumnQty(Context context, int viewId) {

            View view = View.inflate(context, viewId, null);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
            displayMetrics = context.getResources().getDisplayMetrics();
        }
        public int calculateNoOfColumns() {

            int numberOfColumns = displayMetrics.widthPixels / width;
            remaining = displayMetrics.widthPixels - (numberOfColumns * width);
//        System.out.println("\nRemaining\t" + remaining + "\nNumber Of Columns\t" + numberOfColumns);
            if (remaining / (2 * numberOfColumns) < 15) {
                numberOfColumns--;
                remaining = displayMetrics.widthPixels - (numberOfColumns * width);
            }
            return numberOfColumns;
        }


        public int calculateSpacing() {

            int numberOfColumns = calculateNoOfColumns();
//        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);
            return remaining / (2 * numberOfColumns)-1;
        }
    }
}
