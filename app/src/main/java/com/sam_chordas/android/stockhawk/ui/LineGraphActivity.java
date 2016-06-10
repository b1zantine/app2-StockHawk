package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

public class LineGraphActivity extends Activity {

    private LineChart lineChart;
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        lineChart = (LineChart) findViewById(R.id.linechart);

        if(getIntent() != null)
            symbol = getIntent().getStringExtra("symbol");
        else
            finish();

        Cursor cursor = getContentResolver().query(
                QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                "symbol = ?",
                new String[]{symbol},
                null);

//        Toast.makeText(this, cursor.getCount()+" "+symbol, Toast.LENGTH_SHORT).show();
    }
}
