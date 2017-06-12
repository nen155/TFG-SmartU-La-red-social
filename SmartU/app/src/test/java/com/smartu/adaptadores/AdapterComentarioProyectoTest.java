package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.smartu.modelos.Comentario;
import com.smartu.modelos.Proyecto;

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
public class AdapterComentarioProyectoTest extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<Comentario> comentarios;
    @Mock
    Comentario comentario;
    @Mock
    Proyecto proyectoOrigen;
    @Mock
    TextView holderDescripcionComentario;

    @InjectMocks
    AdapterComentarioProyecto adapterComentarioProyecto;

    public AdapterComentarioProyectoTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterComentarioProyectoTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        AdapterComentarioProyecto.ViewHolder result = adapterComentarioProyecto.onCreateViewHolder(null, 0);
        Assert.assertEquals(new AdapterComentarioProyecto.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        adapterComentarioProyecto.onBindViewHolder(new AdapterComentarioProyecto.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterComentarioProyecto.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterComentarioProyecto.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAddItem() throws Exception {
        adapterComentarioProyecto.addItem(null);
    }

    @Test
    public void testAddItemTop() throws Exception {
        adapterComentarioProyecto.addItemTop(new Comentario(0, "descripcion", new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 19).getTime(), "usuario", "proyecto"));
    }

    @Test
    public void testOnBindViewHolder2() throws Exception {
        adapterComentarioProyecto.onBindViewHolder(new AdapterComentarioProyecto.ViewHolder(new View(context), 0), 0, Arrays.<Object>asList(null));
    }

    @Test
    public void testCreateViewHolder() throws Exception {
        AdapterComentarioProyecto.ViewHolder result = adapterComentarioProyecto.createViewHolder(null, 0);
        Assert.assertEquals(new AdapterComentarioProyecto.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testBindViewHolder() throws Exception {
        adapterComentarioProyecto.bindViewHolder(new AdapterComentarioProyecto.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterComentarioProyecto.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewRecycled() throws Exception {
        adapterComentarioProyecto.onViewRecycled(new AdapterComentarioProyecto.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnFailedToRecycleView() throws Exception {
        boolean result = adapterComentarioProyecto.onFailedToRecycleView(new AdapterComentarioProyecto.ViewHolder(new View(context), 0));
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewAttachedToWindow() throws Exception {
        adapterComentarioProyecto.onViewAttachedToWindow(new AdapterComentarioProyecto.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnViewDetachedFromWindow() throws Exception {
        adapterComentarioProyecto.onViewDetachedFromWindow(new AdapterComentarioProyecto.ViewHolder(new View(context), 0));
    }

    @Test
    public void testHasObservers() throws Exception {
        boolean result = adapterComentarioProyecto.hasObservers();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterAdapterDataObserver() throws Exception {
        adapterComentarioProyecto.registerAdapterDataObserver(null);
    }

    @Test
    public void testUnregisterAdapterDataObserver() throws Exception {
        adapterComentarioProyecto.unregisterAdapterDataObserver(null);
    }

    @Test
    public void testOnAttachedToRecyclerView() throws Exception {
        adapterComentarioProyecto.onAttachedToRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testOnDetachedFromRecyclerView() throws Exception {
        adapterComentarioProyecto.onDetachedFromRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterComentarioProyecto.notifyDataSetChanged();
    }

    @Test
    public void testNotifyItemChanged() throws Exception {
        adapterComentarioProyecto.notifyItemChanged(0);
    }

    @Test
    public void testNotifyItemChanged2() throws Exception {
        adapterComentarioProyecto.notifyItemChanged(0, null);
    }

    @Test
    public void testNotifyItemRangeChanged() throws Exception {
        adapterComentarioProyecto.notifyItemRangeChanged(0, 0);
    }

    @Test
    public void testNotifyItemRangeChanged2() throws Exception {
        adapterComentarioProyecto.notifyItemRangeChanged(0, 0, null);
    }

    @Test
    public void testNotifyItemInserted() throws Exception {
        adapterComentarioProyecto.notifyItemInserted(0);
    }

    @Test
    public void testNotifyItemMoved() throws Exception {
        adapterComentarioProyecto.notifyItemMoved(0, 0);
    }

    @Test
    public void testNotifyItemRangeInserted() throws Exception {
        adapterComentarioProyecto.notifyItemRangeInserted(0, 0);
    }

    @Test
    public void testNotifyItemRemoved() throws Exception {
        adapterComentarioProyecto.notifyItemRemoved(0);
    }

    @Test
    public void testNotifyItemRangeRemoved() throws Exception {
        adapterComentarioProyecto.notifyItemRangeRemoved(0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme