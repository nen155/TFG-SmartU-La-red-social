package com.smartu.almacenamiento;

import android.os.Build;

import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by Emilio Chica Jiménez on 29/05/2017.
 */

public class Almacen {

    private static Map<Integer,Proyecto> proyectoHashMap = new HashMap<>();
    private static Map<Integer,Usuario> usuarioHashMap = new HashMap<>();
    private static Map<Integer,Notificacion> notificacionHashMap = new HashMap<>();
    private static Map<Integer,Comentario> comentarioHashMap = new HashMap<>();


    public static void add(Proyecto p){
        proyectoHashMap.put(p.getId(),p);
    }
    public static void add(Notificacion n){
        notificacionHashMap.put(n.getId(),n);
    }
    public static void add(Usuario u){
        usuarioHashMap.put(u.getId(),u);
    }
    public static void add(Comentario c){
        comentarioHashMap.put(c.getId(),c);
    }

    public static void removeProyecto(int id){
        proyectoHashMap.remove(id);
    }
    public static void removeNotificacion(int id){
        notificacionHashMap.remove(id);
    }
    public static void removeUsuario(int id){
        usuarioHashMap.remove(id);
    }
    public static void removeComentario(int id){
        comentarioHashMap.remove(id);
    }

    public static ArrayList<Proyecto> getProyectos(){
        return new ArrayList<Proyecto>(proyectoHashMap.values());
    }
    public static ArrayList<Usuario> getUsuarios(){
        return new ArrayList<Usuario>(usuarioHashMap.values());
    }
    public static ArrayList<Notificacion> getNotificaciones(){
        return new ArrayList<Notificacion>(notificacionHashMap.values());
    }
    public static ArrayList<Comentario> getComentarios(){
        return new ArrayList<Comentario>(comentarioHashMap.values());
    }

    /**
     * Busca un proyecto en el almancen si no lo encuentra devuelve null
     * @param id
     * @return
     */
    public static Proyecto buscarProyecto(int id){
        Proyecto p =proyectoHashMap.get(id);
        if(p!=null )
            return p;
        else
        {
            //TODO HEBRA QUE BUSQUE LOS PROYECTOS Y LOS AÑADA AL ALMACEN
            //CON LOS USUARIOS ASOCIADOS Y NOTIFICACIONES Y COMENTARIOS
            return null;
        }
    }
    /**
     * Busca un proyecto en el almancen si no lo encuentra devuelve null
     * @param id
     * @return
     */
    public static Comentario buscarComentario(int id){
        Comentario c =comentarioHashMap.get(id);
        if(c!=null )
            return c;
        else
        {
            //TODO HEBRA QUE BUSQUE LOS PROYECTOS Y LOS AÑADA AL ALMACEN
            //CON LOS USUARIOS ASOCIADOS Y NOTIFICACIONES Y COMENTARIOS
            return null;
        }
    }
    /**
     * Busca un proyecto en el almancen si no lo encuentra devuelve null
     * @param id
     * @return
     */
    public static Usuario buscarUsuario(int id){
        Usuario u =usuarioHashMap.get(id);
        if(u !=null )
            return u ;
        else
        {
            //TODO HEBRA QUE BUSQUE LOS PROYECTOS Y LOS AÑADA AL ALMACEN
            //CON LOS USUARIOS ASOCIADOS Y NOTIFICACIONES Y COMENTARIOS
            return null;
        }
    }
    /**
     * Busca un proyecto en el almancen si no lo encuentra devuelve null
     * @param id
     * @return
     */
    public static Notificacion buscarNotificacion(int id){
        Notificacion n =notificacionHashMap.get(id);
        if(n !=null )
            return n ;
        else
        {
            //TODO HEBRA QUE BUSQUE LOS PROYECTOS Y LOS AÑADA AL ALMACEN
            //CON LOS USUARIOS ASOCIADOS Y NOTIFICACIONES Y COMENTARIOS
            return null;
        }
    }
    /**
     * Busca un conjunto de ids de proyectos en el almacen y sino los encuentra los busca en
     * el servidor
     * @param idsUsuarios
     * @return
     */
    public static ArrayList<Usuario> buscarUsuarios(ArrayList<Integer> idsUsuarios){
        ArrayList<Integer> usuariosABuscarServer=new ArrayList<>();
        ArrayList<Usuario> usuariosFilt=new ArrayList<>();
        for (int idUsuario: idsUsuarios) {
            Usuario usuario = usuarioHashMap.get(idUsuario);
            if(usuario==null)
                usuariosABuscarServer.add(idUsuario);
            else
                usuariosFilt.add(usuario);
        }
        if(usuariosABuscarServer.size()>0){
            //TODO HEBRA QUE BUSQUE LOS PROYECTOS Y LOS AÑADA AL ALMACEN
            //CON LOS USUARIOS ASOCIADOS Y NOTIFICACIONES Y COMENTARIOS
        }
        return usuariosFilt;
    }
    /**
     * Busca un conjunto de ids de proyectos en el almacen y sino los encuentra los busca en
     * el servidor
     * @param idsProyectos
     * @return
     */
    public static ArrayList<Proyecto> buscarProyectos(ArrayList<Integer> idsProyectos){
        ArrayList<Integer> proyectosABuscarServer=new ArrayList<>();
        ArrayList<Proyecto> proyectosFilt=new ArrayList<>();
        for (int idProyecto: idsProyectos) {
            Proyecto proyecto = proyectoHashMap.get(idProyecto);
            if(proyecto==null)
                proyectosABuscarServer.add(idProyecto);
            else
                proyectosFilt.add(proyecto);
        }
        if(proyectosABuscarServer.size()>0){
            //TODO HEBRA QUE BUSQUE LOS PROYECTOS Y LOS AÑADA AL ALMACEN
            //CON LOS USUARIOS ASOCIADOS Y NOTIFICACIONES Y COMENTARIOS
        }
        return proyectosFilt;
    }



    /**
     * Filtra los proyectos por intereses y por usuarios seguidos
     *
     * @return
     */
    public static ArrayList<Proyecto> proyectosFiltrados(Usuario usuarioSesion) {
        //Sino tengo proyectos o gente que siga y areas de interes devuelvo los proyectos como estan
        if (proyectoHashMap.isEmpty() || usuarioSesion.getMisSeguidos() == null || usuarioSesion.getMisAreasInteres() == null || (usuarioSesion.getMisSeguidos().isEmpty() && usuarioSesion.getMisAreasInteres().isEmpty()))
            return getProyectos();

        ArrayList<Proyecto> proyectosFilter = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Stream<Proyecto> proyectoStream = proyectoHashMap.values().parallelStream()
                    .filter(
                            proyecto -> proyecto.getMisAreas().parallelStream()
                                    .anyMatch(area -> usuarioSesion.getMisAreasInteres().parallelStream()
                                            .anyMatch(areaU -> areaU.getNombre().compareTo(area.getNombre()) == 0))
                                    ||
                                    usuarioSesion.getMisSeguidos().parallelStream().anyMatch(usuario1 -> usuario1 == usuarioSesion.getId())
                    );
            Proyecto[] proyectos = proyectoStream.toArray(Proyecto[]::new);
            proyectosFilter = new ArrayList<Proyecto>(Arrays.asList(proyectos));
        } else {
            proyectosFilter = new ArrayList<>(StreamSupport.parallelStream( proyectoHashMap.values()).filter(
                    proyecto -> StreamSupport.parallelStream(proyecto.getMisAreas())
                            .filter(area -> StreamSupport.parallelStream(usuarioSesion.getMisAreasInteres())
                                    .filter(areaU -> areaU.getNombre().compareTo(area.getNombre()) == 0).findAny().isPresent()).findAny().isPresent()
                            ||
                            StreamSupport.parallelStream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuarioSesion.getId()).findAny().isPresent()
            ).collect(Collectors.toList()));
        }
        return proyectosFilter;
    }

    /**
     * Filtra las notificaciones por intereses y por usuarios a los que sigo
     *
     * @return
     */
    public static ArrayList<Notificacion> notificacionsFiltradas(Usuario usuarioSesion,ArrayList<Proyecto> proyectosFiltrados) {
        //Sino tengo proyectos o no tengo notificaciones gente que siga y areas de interes devuelvo los proyectos como estan
        if (proyectosFiltrados.isEmpty() || notificacionHashMap.isEmpty() || usuarioSesion.getMisSeguidos() == null || usuarioSesion.getMisSeguidos().isEmpty())
            return getNotificaciones();
        ArrayList<Notificacion> nofiticacionesFilter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nofiticacionesFilter = new ArrayList<Notificacion>(Arrays.asList(notificacionHashMap.values().parallelStream()
                    .filter(notificacion -> proyectosFiltrados.parallelStream()
                            .anyMatch(proyecto -> proyecto.getId() == notificacion.getIdProyecto())
                            ||
                            usuarioSesion.getMisSeguidos().parallelStream()
                                    .anyMatch(usuario1 -> usuario1 == notificacion.getIdUsuario())
                    )
                    .toArray(Notificacion[]::new)
            )
            );
        } else {
            nofiticacionesFilter = new ArrayList<>(StreamSupport.parallelStream(notificacionHashMap.values()).filter(notificacion -> StreamSupport.parallelStream(proyectosFiltrados)
                    .filter(proyecto -> proyecto.getId() == notificacion.getIdProyecto()).findAny().isPresent()
                    ||
                    StreamSupport.parallelStream(usuarioSesion.getMisSeguidos())
                            .filter(usuario1 -> usuario1 == notificacion.getIdUsuario()).findAny().isPresent()
            ).collect(Collectors.toList()));
        }
        return nofiticacionesFilter;
    }

    /**
     * Filtra los comentarios por intereses y por usuarios a los que sigo
     *
     * @return
     */
    public static ArrayList<Comentario> comentariosFiltrados(Usuario usuarioSesion,ArrayList<Proyecto> proyectosFiltrados) {
        //Sino tengo proyectos o no tengo notificaciones gente que siga y areas de interes devuelvo los proyectos como estan
        if (proyectosFiltrados.isEmpty() || comentarioHashMap.isEmpty() || usuarioSesion.getMisSeguidos() == null || usuarioSesion.getMisSeguidos().isEmpty())
            return getComentarios();
        ArrayList<Comentario> comentariossFilter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            comentariossFilter = new ArrayList<Comentario>(Arrays.asList(comentarioHashMap.values().parallelStream()
                    .filter(comentario -> proyectosFiltrados.parallelStream()
                            .anyMatch(proyecto -> proyecto.getId() == comentario.getIdProyecto())
                            ||
                            usuarioSesion.getMisSeguidos().parallelStream()
                                    .anyMatch(usuario1 -> usuario1 == comentario.getIdUsuario())
                    )
                    .toArray(Comentario[]::new)));
            ;
        } else {

            comentariossFilter = new ArrayList<Comentario>(StreamSupport.parallelStream(comentarioHashMap.values())
                    .filter(comentario -> StreamSupport.parallelStream(proyectosFiltrados)
                            .filter(proyecto -> proyecto.getId() == comentario.getIdProyecto()).findAny().isPresent()
                            ||
                            StreamSupport.parallelStream(usuarioSesion.getMisSeguidos())
                                    .filter(usuario1 -> usuario1 == comentario.getIdUsuario()).findAny().isPresent()
                    )
                    .collect(Collectors.toList()));
        }

        return comentariossFilter;
    }

    /**
     * Filtra los usuarios por seguidores
     *
     * @return
     */
    public static ArrayList<Usuario> usuariosFiltrados(Usuario usuarioSesion) {
        //Si no tengo gente a la que sigo devuelvo el array como esta
        if (usuarioSesion.getMisSeguidos() == null || usuarioSesion.getMisSeguidos().isEmpty())
            return getUsuarios();
        else
            return Almacen.buscarUsuarios(usuarioSesion.getMisSeguidos());
    }
}
