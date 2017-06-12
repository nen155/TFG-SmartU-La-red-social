package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.smartu.modelos.Notificacion;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

/**
 * Created by Emilio Chica Jiménez on 12/06/2017.
 */
public class AdapterNotificacionTest extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<Notificacion> notificaciones;
    @Mock
    Notificacion notificacion;

    @InjectMocks
    AdapterNotificacion adapterNotificacion;

    public AdapterNotificacionTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterNotificacionTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }
    @Test
    public void testOnCreateViewHolder() throws Exception {
        AdapterNotificacion.ViewHolder result = adapterNotificacion.onCreateViewHolder(null, 0);
        Assert.assertEquals(new AdapterNotificacion.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        adapterNotificacion.onBindViewHolder(new AdapterNotificacion.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterNotificacion.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterNotificacion.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReplaceData() throws Exception {
        adapterNotificacion.replaceData(new ArrayList<Notificacion>(Arrays.asList(new Notificacion(0, new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 19).getTime(), "nombre", "descripcion", 0, 0, "proyecto", "usuario"))));
    }

    @Test
    public void testAddItemTop() throws Exception {
        adapterNotificacion.addItemTop(null);
    }

    @Test
    public void testAddItem() throws Exception {
        adapterNotificacion.addItem(null);
    }

    @Test
    public void testOnBindViewHolder2() throws Exception {
        adapterNotificacion.onBindViewHolder(new AdapterNotificacion.ViewHolder(new View(context), 0), 0, Arrays.<Object>asList(null));
    }

    @Test
    public void testCreateViewHolder() throws Exception {
        AdapterNotificacion.ViewHolder result = adapterNotificacion.createViewHolder(null, 0);
        Assert.assertEquals(new AdapterNotificacion.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testBindViewHolder() throws Exception {
        adapterNotificacion.bindViewHolder(new AdapterNotificacion.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterNotificacion.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewRecycled() throws Exception {
        adapterNotificacion.onViewRecycled(new AdapterNotificacion.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnFailedToRecycleView() throws Exception {
        boolean result = adapterNotificacion.onFailedToRecycleView(new AdapterNotificacion.ViewHolder(new View(context), 0));
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewAttachedToWindow() throws Exception {
        adapterNotificacion.onViewAttachedToWindow(new AdapterNotificacion.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnViewDetachedFromWindow() throws Exception {
        adapterNotificacion.onViewDetachedFromWindow(new AdapterNotificacion.ViewHolder(new View(context), 0));
    }

    @Test
    public void testHasObservers() throws Exception {
        boolean result = adapterNotificacion.hasObservers();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterAdapterDataObserver() throws Exception {
        adapterNotificacion.registerAdapterDataObserver(null);
    }

    @Test
    public void testUnregisterAdapterDataObserver() throws Exception {
        adapterNotificacion.unregisterAdapterDataObserver(null);
    }

    @Test
    public void testOnAttachedToRecyclerView() throws Exception {
        adapterNotificacion.onAttachedToRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testOnDetachedFromRecyclerView() throws Exception {
        adapterNotificacion.onDetachedFromRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterNotificacion.notifyDataSetChanged();
    }

    @Test
    public void testNotifyItemChanged() throws Exception {
        adapterNotificacion.notifyItemChanged(0);
    }

    @Test
    public void testNotifyItemChanged2() throws Exception {
        adapterNotificacion.notifyItemChanged(0, null);
    }

    @Test
    public void testNotifyItemRangeChanged() throws Exception {
        adapterNotificacion.notifyItemRangeChanged(0, 0);
    }

    @Test
    public void testNotifyItemRangeChanged2() throws Exception {
        adapterNotificacion.notifyItemRangeChanged(0, 0, null);
    }

    @Test
    public void testNotifyItemInserted() throws Exception {
        adapterNotificacion.notifyItemInserted(0);
    }

    @Test
    public void testNotifyItemMoved() throws Exception {
        adapterNotificacion.notifyItemMoved(0, 0);
    }

    @Test
    public void testNotifyItemRangeInserted() throws Exception {
        adapterNotificacion.notifyItemRangeInserted(0, 0);
    }

    @Test
    public void testNotifyItemRemoved() throws Exception {
        adapterNotificacion.notifyItemRemoved(0);
    }

    @Test
    public void testNotifyItemRangeRemoved() throws Exception {
        adapterNotificacion.notifyItemRangeRemoved(0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme