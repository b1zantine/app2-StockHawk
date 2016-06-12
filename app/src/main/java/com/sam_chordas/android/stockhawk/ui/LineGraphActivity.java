package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.model.Quote;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class LineGraphActivity extends Activity {

    private final static String TAG = LineGraphActivity.class.getName();

    private CandleStickChart candleChart;
    private ContentLoadingProgressBar progressBar;
    private String symbol;

    private ArrayList<Quote> quoteList;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        candleChart = (CandleStickChart) findViewById(R.id.candle_chart);
        candleChart.setDescription("OHLC Data of last 30 days");
        candleChart.setDescriptionColor(ContextCompat.getColor(this,R.color.material_blue_500));
        Legend legend = candleChart.getLegend();
        legend.setTextColor(ContextCompat.getColor(this,R.color.material_blue_500));
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);

        if(getIntent() != null)
            symbol = getIntent().getStringExtra("symbol");
        else
            finish();

        okHttpClient = new OkHttpClient();

        if(savedInstanceState == null){
            candleChart.setVisibility(View.GONE);
            quoteList = null;
            try {
                getData();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            quoteList = savedInstanceState.getParcelableArrayList("quotes");
            if (quoteList!= null && !quoteList.isEmpty()) {
                setCandleData();
                progressBar.setVisibility(View.GONE);
            }else  {
                candleChart.setVisibility(View.GONE);
                try {
                    getData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void getData() throws UnsupportedEncodingException{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        String endDateString = df.format(date);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(GregorianCalendar.DATE, -30);
        String startDateString = df.format(cal.getTime());

        StringBuilder url = new StringBuilder();
        url.append("https://query.yahooapis.com/v1/public/yql?q=");
        url.append(URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = \"", "UTF-8"));
        url.append(symbol.toUpperCase());
        url.append("\"%20and%20startDate=\"");
        url.append(startDateString);
        url.append("\"%20and%20endDate=\"");
        url.append(endDateString);
        url.append("\"&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");

        Log.i(TAG, "url: " + url);

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LineGraphActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        candleChart.setVisibility(View.VISIBLE);
                        Toast.makeText(
                                LineGraphActivity.this,
                                "Check your Internet Connection",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                Log.i(TAG, "Response Data : " + responseData);
                final ArrayList<Quote> quotes =  Utils.parseQuoteJson(responseData);
                LineGraphActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        quoteList = quotes;
                        setCandleData();
                        candleChart.setVisibility(View.VISIBLE);
//                        Toast.makeText(LineGraphActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setCandleData(){
        if(quoteList != null && !quoteList.isEmpty()){
            Log.i(TAG, "setCandleData: Setting Data");
            CandleDataSet dataSet = new CandleDataSet(quoteToCandleEntry(quoteList), symbol.toUpperCase());
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            ArrayList<ICandleDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            ArrayList<String> xVals = new ArrayList<>();
            for(Quote quote : quoteList){
                xVals.add(quote.getDate());
            }
            CandleData data = new CandleData(xVals, dataSets);
            candleChart.setData(data);
            candleChart.invalidate();
        }
    }

    private ArrayList<CandleEntry> quoteToCandleEntry(ArrayList<Quote> quotes){
        ArrayList<CandleEntry> entries = new ArrayList<>();
        int count = 0;
        for(Quote quote : quotes){
            entries.add(new CandleEntry(count,quote.getHigh(),quote.getLow(),quote.getOpen(),quote.getClose()));
            count++;
        }
        return entries;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("quotes", quoteList);
    }
}
