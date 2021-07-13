package com.automatodev.e_conommiza_app.utils;

import android.app.Activity;
import android.widget.TextView;

import com.automatodev.e_conommiza_app.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

public class CustomMarkerView extends MarkerView {

    private TextView txtDataOneMarker;
    private TextView txtDataTwoMarker;
    private List<String> mxData;


    public CustomMarkerView(Activity context, int layoutResource, List<String> mxData) {
        super(context, layoutResource);

        txtDataOneMarker = findViewById(R.id.txtDataOne_markuer);
        txtDataTwoMarker = findViewById(R.id.txtDataTwo_marker);
        this.mxData = mxData;

    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        txtDataOneMarker.setText(""+FormatUtils.numberFormat((e.getY())));
        txtDataTwoMarker.setText(""+mxData.get((int)e.getX()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}