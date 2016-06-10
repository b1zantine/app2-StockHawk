package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by sudar on 14/4/16.
 * Email : hey@sudar.me
 */
public class QuoteWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new QuoteWidgetRemoteFactory(this.getApplicationContext(), intent);
    }
}
