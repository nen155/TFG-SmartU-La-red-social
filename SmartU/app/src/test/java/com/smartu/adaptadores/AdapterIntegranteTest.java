package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.vistas.FragmentIntegrantes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by Emilio Chica Jiménez on 12/06/2017.
 */
public class AdapterIntegranteTest extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<Usuario> usuarios;
    @Mock
    FragmentIntegrantes.OnIntegranteSelectedListener onIntegranteSelectedListener;
    @Mock
    Usuario usuario;
    @Mock
    Usuario usuarioSesion;
    @Mock
    Button seguirUsuarioEditable;
    @Mock
    TextView seguidoresUsuarioEditable;
    @Mock
    Proyecto proyecto;

    @InjectMocks
    AdapterIntegrante adapterIntegrante;

    public AdapterIntegranteTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterIntegranteTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        AdapterIntegrante.ViewHolder result = adapterIntegrante.onCreateViewHolder(null, 0);
        Assert.assertEquals(new AdapterIntegrante.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        adapterIntegrante.onBindViewHolder(new AdapterIntegrante.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterIntegrante.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterIntegrante.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAddItem() throws Exception {
        adapterIntegrante.addItem(null);
    }

    @Test
    public void testOnBindViewHolder2() throws Exception {
        adapterIntegrante.onBindViewHolder( new AdapterIntegrante.ViewHolder(new View(null), 0), 0, Arrays.<Object>asList(null));
    }

    @Test
    public void testCreateViewHolder() throws Exception {
        AdapterIntegrante.ViewHolder result = adapterIntegrante.createViewHolder(null, 0);
        Assert.assertEquals(new AdapterIntegrante.ViewHolder(new View(null), 0), result);
    }

    @Test
    public void testBindViewHolder() throws Exception {
        adapterIntegrante.bindViewHolder(new AdapterIntegrante.ViewHolder(new View(null),0), 0);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterIntegrante.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewRecycled() throws Exception {
        adapterIntegrante.onViewRecycled(new AdapterIntegrante.ViewHolder(new View(null),0));
    }

    @Test
    public void testOnFailedToRecycleView() throws Exception {
        boolean result = adapterIntegrante.onFailedToRecycleView(new AdapterIntegrante.ViewHolder(new View(null),0));
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewAttachedToWindow() throws Exception {
        adapterIntegrante.onViewAttachedToWindow(new AdapterIntegrante.ViewHolder(new View(null),0));
    }

    @Test
    public void testOnViewDetachedFromWindow() throws Exception {
        adapterIntegrante.onViewDetachedFromWindow(new AdapterIntegrante.ViewHolder(new View(null),0));
    }

    @Test
    public void testHasObservers() throws Exception {
        boolean result = adapterIntegrante.hasObservers();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterAdapterDataObserver() throws Exception {
        adapterIntegrante.registerAdapterDataObserver(null);
    }

    @Test
    public void testUnregisterAdapterDataObserver() throws Exception {
        adapterIntegrante.unregisterAdapterDataObserver(null);
    }

    @Test
    public void testOnAttachedToRecyclerView() throws Exception {
        adapterIntegrante.onAttachedToRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testOnDetachedFromRecyclerView() throws Exception {
        adapterIntegrante.onDetachedFromRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterIntegrante.notifyDataSetChanged();
    }

    @Test
    public void testNotifyItemChanged() throws Exception {
        adapterIntegrante.notifyItemChanged(0);
    }

    @Test
    public void testNotifyItemChanged2() throws Exception {
        adapterIntegrante.notifyItemChanged(0, null);
    }

    @Test
    public void testNotifyItemRangeChanged() throws Exception {
        adapterIntegrante.notifyItemRangeChanged(0, 0);
    }

    @Test
    public void testNotifyItemRangeChanged2() throws Exception {
        adapterIntegrante.notifyItemRangeChanged(0, 0, null);
    }

    @Test
    public void testNotifyItemInserted() throws Exception {
        adapterIntegrante.notifyItemInserted(0);
    }

    @Test
    public void testNotifyItemMoved() throws Exception {
        adapterIntegrante.notifyItemMoved(0, 0);
    }

    @Test
    public void testNotifyItemRangeInserted() throws Exception {
        adapterIntegrante.notifyItemRangeInserted(0, 0);
    }

    @Test
    public void testNotifyItemRemoved() throws Exception {
        adapterIntegrante.notifyItemRemoved(0);
    }

    @Test
    public void testNotifyItemRangeRemoved() throws Exception {
        adapterIntegrante.notifyItemRangeRemoved(0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme