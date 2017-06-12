package com.smartu.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.smartu.modelos.Area;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by Emilio Chica Jiénez on 12/06/2017.
 */
public class AdapterAreasInteresTest  extends ActivityInstrumentationTestCase2<Activity> {
    @Mock
    Context context;
    @Mock
    ArrayList<Area> areasBack;
    @Mock
    ArrayList<Area> areasInicales;
    @Mock
    Set<Integer> posicionAreasInicial;
    @Mock
    LayoutInflater inflater;
    @Mock
    ArrayList<Area> areas;
    @Mock
    GridView gridView;
    @InjectMocks
    AdapterAreasInteres adapterAreasInteres;


    public AdapterAreasInteresTest(Class<Activity> activityClass) {
        super(activityClass);
    }
    public AdapterAreasInteresTest() {
        super(Activity.class);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = getActivity();

    }

    @Test
    public void testGetItem() throws Exception {
        Object result = adapterAreasInteres.getItem(0);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = adapterAreasInteres.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetView() throws Exception {
        View result = adapterAreasInteres.getView(0, new View(context), null);
        Assert.assertEquals(new View(context), result);
    }

    @Test
    public void testCheck() throws Exception {
        adapterAreasInteres.check(new View(context), 0);
    }

    @Test
    public void testUnCheckAll() throws Exception {
        adapterAreasInteres.unCheckAll();
    }

    @Test
    public void testCheckElement() throws Exception {
        adapterAreasInteres.checkElement(0);
    }

    @Test
    public void testUncheck() throws Exception {
        adapterAreasInteres.uncheck(new View(context), 0);
    }

    @Test
    public void testHasStableIds() throws Exception {
        boolean result = adapterAreasInteres.hasStableIds();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRegisterDataSetObserver() throws Exception {
        adapterAreasInteres.registerDataSetObserver(null);
    }

    @Test
    public void testUnregisterDataSetObserver() throws Exception {
        adapterAreasInteres.unregisterDataSetObserver(null);
    }

    @Test
    public void testNotifyDataSetChanged() throws Exception {
        adapterAreasInteres.notifyDataSetChanged();
    }

    @Test
    public void testNotifyDataSetInvalidated() throws Exception {
        adapterAreasInteres.notifyDataSetInvalidated();
    }

    @Test
    public void testAreAllItemsEnabled() throws Exception {
        boolean result = adapterAreasInteres.areAllItemsEnabled();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsEnabled() throws Exception {
        boolean result = adapterAreasInteres.isEnabled(0);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetDropDownView() throws Exception {
        View result = adapterAreasInteres.getDropDownView(0, new View(context), null);
        Assert.assertEquals(new View(context), result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = adapterAreasInteres.getItemViewType(0);
        Assert.assertEquals(0, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme