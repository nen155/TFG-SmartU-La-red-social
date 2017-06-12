package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.smartu.hebras.HSeguir;
import com.smartu.modelos.Usuario;
import com.smartu.vistas.FragmentUsuarios;

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
public class AdapterUsuarioTest  extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<Usuario> usuarios;
    @Mock
    FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener;
    @Mock
    Usuario usuario;
    @Mock
    HSeguir hSeguir;
    @Mock
    Usuario usuarioSesion;

    @InjectMocks
    AdapterUsuario adapterUsuario;

    public AdapterUsuarioTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterUsuarioTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        AdapterUsuario.ViewHolder result = adapterUsuario.onCreateViewHolder(null, 0);
        Assert.assertEquals(new AdapterUsuario.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        adapterUsuario.onBindViewHolder(new AdapterUsuario.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterUsuario.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterUsuario.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAddItem() throws Exception {
        adapterUsuario.addItem(null);
    }

    @Test
    public void testOnBindViewHolder2() throws Exception {
        adapterUsuario.onBindViewHolder(new AdapterUsuario.ViewHolder(new View(context), 0), 0, Arrays.<Object>asList(null));
    }

    @Test
    public void testCreateViewHolder() throws Exception {
        AdapterUsuario.ViewHolder result = adapterUsuario.createViewHolder(null, 0);
        Assert.assertEquals(new AdapterUsuario.ViewHolder(new View(context), 0), result);
    }

    @Test
    public void testBindViewHolder() throws Exception {
        adapterUsuario.bindViewHolder(new AdapterUsuario.ViewHolder(new View(context), 0), 0);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterUsuario.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewRecycled() throws Exception {
        adapterUsuario.onViewRecycled(new AdapterUsuario.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnFailedToRecycleView() throws Exception {
        boolean result = adapterUsuario.onFailedToRecycleView(new AdapterUsuario.ViewHolder(new View(context), 0));
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnViewAttachedToWindow() throws Exception {
        adapterUsuario.onViewAttachedToWindow(new AdapterUsuario.ViewHolder(new View(context), 0));
    }

    @Test
    public void testOnViewDetachedFromWindow() throws Exception {
        adapterUsuario.onViewDetachedFromWindow(new AdapterUsuario.ViewHolder(new View(context), 0));
    }

    @Test
    public void testHasObservers() throws Exception {
        boolean result = adapterUsuario.hasObservers();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterAdapterDataObserver() throws Exception {
        adapterUsuario.registerAdapterDataObserver(null);
    }

    @Test
    public void testUnregisterAdapterDataObserver() throws Exception {
        adapterUsuario.unregisterAdapterDataObserver(null);
    }

    @Test
    public void testOnAttachedToRecyclerView() throws Exception {
        adapterUsuario.onAttachedToRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testOnDetachedFromRecyclerView() throws Exception {
        adapterUsuario.onDetachedFromRecyclerView(new RecyclerView(null, null, 0));
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterUsuario.notifyDataSetChanged();
    }

    @Test
    public void testNotifyItemChanged() throws Exception {
        adapterUsuario.notifyItemChanged(0);
    }

    @Test
    public void testNotifyItemChanged2() throws Exception {
        adapterUsuario.notifyItemChanged(0, null);
    }

    @Test
    public void testNotifyItemRangeChanged() throws Exception {
        adapterUsuario.notifyItemRangeChanged(0, 0);
    }

    @Test
    public void testNotifyItemRangeChanged2() throws Exception {
        adapterUsuario.notifyItemRangeChanged(0, 0, null);
    }

    @Test
    public void testNotifyItemInserted() throws Exception {
        adapterUsuario.notifyItemInserted(0);
    }

    @Test
    public void testNotifyItemMoved() throws Exception {
        adapterUsuario.notifyItemMoved(0, 0);
    }

    @Test
    public void testNotifyItemRangeInserted() throws Exception {
        adapterUsuario.notifyItemRangeInserted(0, 0);
    }

    @Test
    public void testNotifyItemRemoved() throws Exception {
        adapterUsuario.notifyItemRemoved(0);
    }

    @Test
    public void testNotifyItemRangeRemoved() throws Exception {
        adapterUsuario.notifyItemRangeRemoved(0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme