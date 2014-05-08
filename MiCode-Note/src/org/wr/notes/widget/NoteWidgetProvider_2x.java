package org.wr.notes.widget;

import org.wr.notes.data.Notes;
import org.wr.notes.tool.ResourceParser;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import org.wr.notes.R;


public class NoteWidgetProvider_2x extends NoteWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.update(context, appWidgetManager, appWidgetIds);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.widget_2x;
    }

    @Override
    protected int getBgResourceId(int bgId) {
        return ResourceParser.WidgetBgResources.getWidget2xBgResource(bgId);
    }

    @Override
    protected int getWidgetType() {
        return Notes.TYPE_WIDGET_2X;
    }
}
