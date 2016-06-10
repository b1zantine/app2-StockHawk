package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDatabase;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by sudar on 14/4/16.
 * Email : hey@sudar.me
 */
public class QuoteWidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;

    QuoteWidgetRemoteFactory(Context context, Intent intent){
        this.context = context;
    }
    @Override
    public void onCreate() {
        loadCursor();
    }

    @Override
    public void onDataSetChanged() {
        loadCursor();
    }

    public void loadCursor(){
        final long identityToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.SYMBOL, QuoteColumns.CHANGE},
                "is_current = ?", new String[]{"1"}, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        this.cursor.moveToPosition(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_collection_item);
        remoteViews.setTextViewText(R.id.stock_symbol, cursor.getString(0));
        remoteViews.setTextViewText(R.id.change, cursor.getString(1));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
