package com.chemander.mydemo.reading;


import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.ReadJSON;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.utils.SettingsManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReadingActivity extends AppCompatActivity {
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private TextView mainContent;
    private List<Chapter> chapters;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        mainContent = (TextView) findViewById(R.id.textviewMainContent);
        scrollView = (ScrollView) findViewById(R.id.scrollReading);
        scrollView.setVerticalScrollBarEnabled(false);
        chapters = ReadJSON.readChapterFromJSONFile(this);

        mainContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

        initContent();
    }

    @Override
    protected void onPause() {
        SettingsManager.preferenceCurrentPosition = scrollView.getScrollY();
        SettingsManager.saveSettings(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        SettingsManager.loadSettings(this);
        super.onResume();
    }

    private void initContent(){
        mainContent.setText(chapters.get(SettingsManager.preferenceCurrentChapter).getContent());
        setTextSize();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,SettingsManager.preferenceCurrentPosition);
            }
        });
    }
    private void setTextSize(){
        mainContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, SettingsManager.preferenceFontSize);
    }

    private void setFontType(){
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/"+SettingsManager.preferenceFontType+".ttf");
        mainContent.setTypeface(type);
    }
    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_reading, null);
//        (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBottomSheetDialog.hide();
//            }
//        });

//        (view.findViewById(R.id.submit_rating)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Submit Rating", Toast.LENGTH_SHORT).show();
//            }
//        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // set background transparent
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

    }

}
