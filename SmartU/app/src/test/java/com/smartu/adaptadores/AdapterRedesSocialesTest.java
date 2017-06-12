package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.smartu.modelos.RedSocial;

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
 * Created by Emilio Chica Jiménez on 12/06/2017.
 */
public class AdapterRedesSocialesTest  extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<RedSocial> redesSociales;
    @Mock
    RedSocial redSocial;
    @InjectMocks
    AdapterRedesSociales adapterRedesSociales;


    public AdapterRedesSocialesTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterRedesSocialesTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        AdapterRedesSociales.ViewHolder result = adapterRedesSociales.onCreateViewHolder(null, 0);
        Assert.assertEquals(new AdapterRedesSociales.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        adapterRedesSociales.onBindViewHolder(new AdapterRedesSociales.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterRedesSociales.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testOnBindViewHolder2() throws Exception {
        adapterRedesSociales.onBindViewHolder(new AdapterRedesSociales.ViewHolder(new View(context), 0), 0, Arrays.<Object>asList(null));
    }

    @Test
    public void testCreateViewHolder() throws Exception {
        AdapterRedesSociales.ViewHolder result = adapterRedesSociales.createViewHolder(null, 0);
        Assert.assertEquals(new AdapterRedesSociales.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testBindViewHolder() throws Exception {
        adapterRedesSociales.bindViewHolder(new AdapterRedesSociales.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterRedesSociales.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterRedesSociales.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewRecycled() throws Exception {
        adapterRedesSociales.onViewRecycled(new AdapterRedesSociales.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnFailedToRecycleView() throws Exception {
        boolean result = adapterRedesSociales.onFailedToRecycleView(new AdapterRedesSociales.ViewHolder(new View(context), 0));
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewAttachedToWindow() throws Exception {
        adapterRedesSociales.onViewAttachedToWindow(new AdapterRedesSociales.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnViewDetachedFromWindow() throws Exception {
        adapterRedesSociales.onViewDetachedFromWindow(new AdapterRedesSociales.ViewHolder(new View(context), 0));
    }

    @Test
    public void testHasObservers() throws Exception {
        boolean result = adapterRedesSociales.hasObservers();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterAdapterDataObserver() throws Exception {
        adapterRedesSociales.registerAdapterDataObserver(null);
    }

    @Test
    public void testUnregisterAdapterDataObserver() throws Exception {
        adapterRedesSociales.unregisterAdapterDataObserver(null);
    }

    @Test
    public void testOnAttachedToRecyclerView() throws Exception {
        adapterRedesSociales.onAttachedToRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testOnDetachedFromRecyclerView() throws Exception {
        adapterRedesSociales.onDetachedFromRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterRedesSociales.notifyDataSetChanged();
    }

    @Test
    public void testNotifyItemChanged() throws Exception {
        adapterRedesSociales.notifyItemChanged(0);
    }

    @Test
    public void testNotifyItemChanged2() throws Exception {
        adapterRedesSociales.notifyItemChanged(0, null);
    }

    @Test
    public void testNotifyItemRangeChanged() throws Exception {
        adapterRedesSociales.notifyItemRangeChanged(0, 0);
    }

    @Test
    public void testNotifyItemRangeChanged2() throws Exception {
        adapterRedesSociales.notifyItemRangeChanged(0, 0, null);
    }

    @Test
    public void testNotifyItemInserted() throws Exception {
        adapterRedesSociales.notifyItemInserted(0);
    }

    @Test
    public void testNotifyItemMoved() throws Exception {
        adapterRedesSociales.notifyItemMoved(0, 0);
    }

    @Test
    public void testNotifyItemRangeInserted() throws Exception {
        adapterRedesSociales.notifyItemRangeInserted(0, 0);
    }

    @Test
    public void testNotifyItemRemoved() throws Exception {
        adapterRedesSociales.notifyItemRemoved(0);
    }

    @Test
    public void testNotifyItemRangeRemoved() throws Exception {
        adapterRedesSociales.notifyItemRangeRemoved(0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme