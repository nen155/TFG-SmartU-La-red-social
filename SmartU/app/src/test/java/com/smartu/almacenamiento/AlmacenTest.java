package com.smartu.almacenamiento;

import com.smartu.modelos.Area;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;

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
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by NeN on 12/06/2017.
 */
public class AlmacenTest {
    @Mock
    Map<Integer, Proyecto> proyectoHashMap;
    @Mock
    Map<Integer, Usuario> usuarioHashMap;
    @Mock
    Map<Integer, Notificacion> notificacionHashMap;
    @Mock
    Map<Integer, Comentario> comentarioHashMap;
    @Mock
    Map<Integer, Area> areasHashMap;
    @Mock
    ArrayList<Proyecto> proyectosFiltrados;
    @Mock
    ArrayList<Notificacion> notificacionesFiltradas;
    @Mock
    ArrayList<Usuario> usuariosFiltrados;
    @Mock
    ArrayList<Comentario> comentariosFiltrados;
    @InjectMocks
    Almacen almacen;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdd() throws Exception {
        Almacen.add(new Area(0, "nombre", "descripcion", "urlImg"));
    }

    @Test
    public void testAdd2() throws Exception {
        Almacen.add(new Proyecto(0, "nombre", "descripcion", new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 20).getTime(), "imagenDestacada", new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 20).getTime(), "localizacion", "web", 0));
    }

    @Test
    public void testAdd3() throws Exception {
        Almacen.add(new Notificacion(0, new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 20).getTime(), "nombre", "descripcion", 0, 0, "proyecto", "usuario"));
    }

    @Test
    public void testAdd4() throws Exception {
        Almacen.add(new Usuario(0, "nombre", "user", "apellidos", "email", "imagenPerfil"));
    }

    @Test
    public void testAdd5() throws Exception {
        Almacen.add(new Comentario(0, "descripcion", new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 20).getTime(), "usuario", "proyecto"));
    }

    @Test
    public void testRemoveArea() throws Exception {
        Almacen.removeArea(0);
    }

    @Test
    public void testRemoveProyecto() throws Exception {
        Almacen.removeProyecto(0);
    }

    @Test
    public void testRemoveNotificacion() throws Exception {
        Almacen.removeNotificacion(0);
    }

    @Test
    public void testRemoveUsuario() throws Exception {
        Almacen.removeUsuario(0);
    }

    @Test
    public void testRemoveComentario() throws Exception {
        Almacen.removeComentario(0);
    }


    @Test
    public void testBuscarUsuarios() throws Exception {
        Almacen.buscarUsuarios(new ArrayList<Integer>(Arrays.asList(0)), new ArrayList<Usuario>(Arrays.asList(new Usuario(0, "nombre", "user", "apellidos", "email", "imagenPerfil"))), null);
    }


    @Test
    public void testBuscarNotificaciones() throws Exception {
        Almacen.buscarNotificaciones(new ArrayList<Integer>(Arrays.asList(0)), new ArrayList<Notificacion>(Arrays.asList(new Notificacion(0, new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 20).getTime(), "nombre", "descripcion", 0, 0, "proyecto", "usuario"))), null);
    }

    @Test
    public void testBuscarComentarios() throws Exception {
        Almacen.buscarComentarios(new ArrayList<Integer>(Arrays.asList(0)), new ArrayList<Comentario>(Arrays.asList(new Comentario(0, "descripcion", new GregorianCalendar(2017, Calendar.JUNE, 12, 17, 20).getTime(), "usuario", "proyecto"))), null);
    }


    @Test
    public void testUsuariosFiltrados() throws Exception {
        Almacen.usuariosFiltrados(new Usuario(0, "nombre", "user", "apellidos", "email", "imagenPerfil"), new ArrayList<Usuario>(Arrays.asList(new Usuario(0, "nombre", "user", "apellidos", "email", "imagenPerfil"))), null);
    }

    @Test
    public void testFiltrarPublicaciones() throws Exception {
        Almacen.filtrarPublicaciones(new Usuario(0, "nombre", "user", "apellidos", "email", "imagenPerfil"), null);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme