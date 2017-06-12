package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.smartu.modelos.Multimedia;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by NeN on 12/06/2017.
 */
public class AdapterMultimediaTest extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<Multimedia> multimediaList;
    @Mock
    Multimedia multimedia;

    @InjectMocks
    AdapterMultimedia adapterMultimedia;

    public AdapterMultimediaTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterMultimediaTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        AdapterMultimedia.ViewHolder result = adapterMultimedia.onCreateViewHolder(null, 0);
        Assert.assertEquals(new AdapterMultimedia.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        adapterMultimedia.onBindViewHolder(new AdapterMultimedia.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterMultimedia.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterMultimedia.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAddItem() throws Exception {
        adapterMultimedia.addItem(new Multimedia(0, "nombre", "url", "tipo", "urlPreview", "urlSubtitulos"));
    }

    @Test
    public void testOnBindViewHolder2() throws Exception {
        adapterMultimedia.onBindViewHolder(new AdapterMultimedia.ViewHolder(new View(context), 0), 0, Arrays.<Object>asList(null));
    }

    @Test
    public void testCreateViewHolder() throws Exception {
        AdapterMultimedia.ViewHolder result = adapterMultimedia.createViewHolder(null, 0);
        Assert.assertEquals(new AdapterMultimedia.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testBindViewHolder() throws Exception {
        adapterMultimedia.bindViewHolder(new AdapterMultimedia.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterMultimedia.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewRecycled() throws Exception {
        adapterMultimedia.onViewRecycled(new AdapterMultimedia.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnFailedToRecycleView() throws Exception {
        boolean result = adapterMultimedia.onFailedToRecycleView(new AdapterMultimedia.ViewHolder(new View(context), 0));
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewAttachedToWindow() throws Exception {
        adapterMultimedia.onViewAttachedToWindow(new AdapterMultimedia.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnViewDetachedFromWindow() throws Exception {
        adapterMultimedia.onViewDetachedFromWindow(new AdapterMultimedia.ViewHolder(new View(context), 0));
    }

    @Test
    public void testHasObservers() throws Exception {
        boolean result = adapterMultimedia.hasObservers();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterAdapterDataObserver() throws Exception {
        adapterMultimedia.registerAdapterDataObserver(null);
    }

    @Test
    public void testUnregisterAdapterDataObserver() throws Exception {
        adapterMultimedia.unregisterAdapterDataObserver(null);
    }

    @Test
    public void testOnAttachedToRecyclerView() throws Exception {
        adapterMultimedia.onAttachedToRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testOnDetachedFromRecyclerView() throws Exception {
        adapterMultimedia.onDetachedFromRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterMultimedia.notifyDataSetChanged();
    }

    @Test
    public void testNotifyItemChanged() throws Exception {
        adapterMultimedia.notifyItemChanged(0);
    }

    @Test
    public void testNotifyItemChanged2() throws Exception {
        adapterMultimedia.notifyItemChanged(0, null);
    }

    @Test
    public void testNotifyItemRangeChanged() throws Exception {
        adapterMultimedia.notifyItemRangeChanged(0, 0);
    }

    @Test
    public void testNotifyItemRangeChanged2() throws Exception {
        adapterMultimedia.notifyItemRangeChanged(0, 0, null);
    }

    @Test
    public void testNotifyItemInserted() throws Exception {
        adapterMultimedia.notifyItemInserted(0);
    }

    @Test
    public void testNotifyItemMoved() throws Exception {
        adapterMultimedia.notifyItemMoved(0, 0);
    }

    @Test
    public void testNotifyItemRangeInserted() throws Exception {
        adapterMultimedia.notifyItemRangeInserted(0, 0);
    }

    @Test
    public void testNotifyItemRemoved() throws Exception {
        adapterMultimedia.notifyItemRemoved(0);
    }

    @Test
    public void testNotifyItemRangeRemoved() throws Exception {
        adapterMultimedia.notifyItemRangeRemoved(0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme