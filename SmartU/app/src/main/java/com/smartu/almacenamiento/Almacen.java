package com.smartu.almacenamiento;

import android.content.Context;
import android.os.Build;

import com.smartu.hebras.HPublicacion;
import com.smartu.hebras.HPublicaciones;
import com.smartu.modelos.Area;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Constantes;
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
    private static Map<Integer,Area> areasHashMap = new HashMap<>();

    public static void add(Area a){
        areasHashMap.put(a.getId(),a);
    }
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

    public static void removeArea(int id){
        areasHashMap.remove(id);
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
    public static ArrayList<Area> getAreas(){
        return new ArrayList<Area>(areasHashMap.values());
    }

    /**
     * Busca un proyecto en el almacen, sino lo encuentra busca en el server
     * @param id
     * @param p
     * @param context
     */
    public static void buscar(int id,Proyecto p,Context context){
        if(proyectoHashMap.get(id)!=null)
            p.clonar(proyectoHashMap.get(id));
        else
        {
            //Busca el proyecto y lo guarda en p, y añade al almacen comentarios y usuarios asociados
            HPublicacion hPublicacion = new HPublicacion(context,p, Constantes.PROYECTO,id);
            hPublicacion.sethPublicaciones(hPublicacion);
            hPublicacion.execute();
        }
    }
    /**
     * Busca un comentario en el almacen, sino lo encuentra busca en el server
     * @param id
     * @param c
     * @param context
     */
    public static void buscar(int id,Comentario c,Context context){
        if(comentarioHashMap.get(id)!=null)
        c.clonar(comentarioHashMap.get(id));
        else
        {
            //Busca el comentario y lo guarda en c, y añade al almacen proyectos y usuarios asociados
            HPublicacion hPublicacion = new HPublicacion(context,c, Constantes.COMENTARIO,id);
            hPublicacion.sethPublicaciones(hPublicacion);
            hPublicacion.execute();
        }
    }
    /**
     * Busca un usuario en el almacen, sino lo encuentra busca en el server
     * @param id
     * @param u
     * @param context
     */
    public static void buscar(int id,Usuario u,Context context){
        if(usuarioHashMap.get(id)!=null)
            u.clonar(usuarioHashMap.get(id));
        else
        {
            //Busca el usuario y lo guarda en u, y añade al almacen comentarios y proyectos asociados
            HPublicacion hPublicacion = new HPublicacion(context,u, Constantes.USUARIO,id);
            hPublicacion.sethPublicaciones(hPublicacion);
            hPublicacion.execute();
        }
    }
    /**
     * Busca una notificacion en el almacen, sino lo encuentra busca en el server
     * @param id
     * @param n
     * @param context
     */
    public static void buscar(int id, Notificacion n, Context context){
        if(notificacionHashMap.get(id)!=null)
            n.clonar(notificacionHashMap.get(id));
        else
        {
            //Busca la notificacion y lo guarda en n, y añade al almacen comentarios y usuarios asociados
            HPublicacion hPublicacion = new HPublicacion(context,n, Constantes.NOTIFICACION,id);
            hPublicacion.sethPublicaciones(hPublicacion);
            hPublicacion.execute();
        }
    }
    /**
     * Busca un conjunto de ids de usuarios en el almacen y sino los encuentra los busca en
     * el servidor
     * @param idsUsuarios
     * @param usuarios
     */
    public static void buscarUsuarios(ArrayList<Integer> idsUsuarios, ArrayList<Usuario> usuarios,Context context){
        ArrayList<Integer> usuariosABuscarServer=new ArrayList<>();
        for (int idUsuario: idsUsuarios) {
            Usuario usuario = usuarioHashMap.get(idUsuario);
            if(usuario==null)
                usuariosABuscarServer.add(idUsuario);
            else {
                boolean esta =StreamSupport.stream(usuarios).filter(usuario1 -> usuario1.getId() == usuario.getId()).findAny().isPresent();
                if(!esta)
                    usuarios.add(usuario);
            }
        }
        //Significa que no he encontrado algunos elementos que tendré que buscar en el server
        if(usuariosABuscarServer.size()>0){
            HPublicaciones hPublicaciones = new HPublicaciones(context);
            hPublicaciones.sethPublicaciones(hPublicaciones);
            hPublicaciones.setIdsPublicaciones(usuariosABuscarServer);
            hPublicaciones.setTipo(Constantes.USUARIO);
        }
    }
    /**
     * Busca un conjunto de ids de proyectos en el almacen y sino los encuentra los busca en
     * el servidor
     * @param idsProyectos
     * @param proyectos
     */
    public static void buscarProyectos(ArrayList<Integer> idsProyectos,ArrayList<Proyecto> proyectos,Context context){
        ArrayList<Integer> proyectosABuscarServer=new ArrayList<>();
        for (int idProyecto: idsProyectos) {
            Proyecto proyecto = proyectoHashMap.get(idProyecto);
            if(proyecto==null)
                proyectosABuscarServer.add(idProyecto);
            else {
                boolean esta =StreamSupport.stream(proyectos).filter(usuario1 -> usuario1.getId() == proyecto.getId()).findAny().isPresent();
                if(!esta)
                    proyectos.add(proyecto);
            }
        }
        //Significa que no he encontrado algunos elementos que tendré que buscar en el server
        if(proyectosABuscarServer.size()>0){
            HPublicaciones hPublicaciones = new HPublicaciones(context);
            hPublicaciones.sethPublicaciones(hPublicaciones);
            hPublicaciones.setIdsPublicaciones(proyectosABuscarServer);
            hPublicaciones.setTipo(Constantes.PROYECTO);
        }
    }

    /**
     * Busca un conjunto de ids de notificaciones en el almacen y sino las encuentra los busca en
     * el servidor
     * @param idsNotificaciones
     * @param notificacions
     */
    public static void buscarNotificaciones(ArrayList<Integer> idsNotificaciones,ArrayList<Notificacion> notificacions,Context context){
        ArrayList<Integer> notificacionesABuscarServer=new ArrayList<>();
        for (int idNotificacion: idsNotificaciones) {
            Notificacion notificacion = notificacionHashMap.get(idNotificacion);
            if(notificacion==null)
                notificacionesABuscarServer.add(idNotificacion);
            else {
                boolean esta =StreamSupport.stream(notificacions).filter(usuario1 -> usuario1.getId() == notificacion.getId()).findAny().isPresent();
                if(!esta)
                    notificacions.add(notificacion);
            }
        }
        //Significa que no he encontrado algunos elementos que tendré que buscar en el server
        if(notificacionesABuscarServer.size()>0){
            HPublicaciones hPublicaciones = new HPublicaciones(context);
            hPublicaciones.sethPublicaciones(hPublicaciones);
            hPublicaciones.setIdsPublicaciones(notificacionesABuscarServer);
            hPublicaciones.setTipo(Constantes.NOTIFICACION);
        }
    }
    /**
     * Busca un conjunto de ids de comentarios en el almacen y sino los encuentra los busca en
     * el servidor
     * @param idsComentarios
     * @param comentarios
     */
    public static void buscarComentarios(ArrayList<Integer> idsComentarios,ArrayList<Comentario> comentarios,Context context){
        ArrayList<Integer> comentariosABuscarServer=new ArrayList<>();
        for (int idComentario: idsComentarios) {
            Comentario comentario = comentarioHashMap.get(idComentario);
            if(comentario==null)
                comentariosABuscarServer.add(idComentario);
            else {
                boolean esta =StreamSupport.stream(comentarios).filter(usuario1 -> usuario1.getId() == comentario.getId()).findAny().isPresent();
                if(!esta)
                    comentarios.add(comentario);
            }
        }
        //Significa que no he encontrado algunos elementos que tendré que buscar en el server
        if(comentariosABuscarServer.size()>0){
            HPublicaciones hPublicaciones = new HPublicaciones(context);
            hPublicaciones.sethPublicaciones(hPublicaciones);
            hPublicaciones.setIdsPublicaciones(comentariosABuscarServer);
            hPublicaciones.setTipo(Constantes.COMENTARIO);
        }
    }

    /**
     * Filtra los proyectos por intereses y por usuarios seguidos
     * @param usuarioSesion
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
            proyectosFilter = new ArrayList<>(Arrays.asList(StreamSupport.stream( proyectoHashMap.values()).filter(
                    proyecto -> StreamSupport.stream(proyecto.getMisAreas())
                            .filter(area -> StreamSupport.stream(usuarioSesion.getMisAreasInteres())
                                    .filter(areaU -> areaU.getNombre().compareTo(area.getNombre()) == 0).findAny().isPresent()).findAny().isPresent()
                            ||
                            StreamSupport.stream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuarioSesion.getId()).findAny().isPresent()
            ).toArray(Proyecto[]::new)));
        }
        return proyectosFilter;
    }

    /**
     * Filtra las notificaciones por intereses y por usuarios a los que sigo
     * @param usuarioSesion
     * @param proyectosFiltrados
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
            nofiticacionesFilter = new ArrayList<>(Arrays.asList(StreamSupport.stream(notificacionHashMap.values()).filter(notificacion -> StreamSupport.stream(proyectosFiltrados)
                    .filter(proyecto -> proyecto.getId() == notificacion.getIdProyecto()).findAny().isPresent()
                    ||
                    StreamSupport.stream(usuarioSesion.getMisSeguidos())
                            .filter(usuario1 -> usuario1 == notificacion.getIdUsuario()).findAny().isPresent()
            ).toArray(Notificacion[]::new)));
        }
        return nofiticacionesFilter;
    }

    /**
     * Filtra los comentarios por intereses y por usuarios a los que sigo
      * @param usuarioSesion
     * @param proyectosFiltrados
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

            comentariossFilter = new ArrayList<Comentario>(Arrays.asList(StreamSupport.stream(comentarioHashMap.values())
                    .filter(comentario -> StreamSupport.stream(proyectosFiltrados)
                            .filter(proyecto -> proyecto.getId() == comentario.getIdProyecto()).findAny().isPresent()
                            ||
                            StreamSupport.stream(usuarioSesion.getMisSeguidos())
                                    .filter(usuario1 -> usuario1 == comentario.getIdUsuario()).findAny().isPresent()
                    )
                    .toArray(Comentario[]::new)));
        }

        return comentariossFilter;
    }

    /**
     * Filtra los usuarios por seguidores, sino están dichos usuarios los busca en el servidor
     * @param usuarioSesion
     * @param usuariosFiltrados
     * @param context
     */
    public static void usuariosFiltrados(Usuario usuarioSesion,ArrayList<Usuario> usuariosFiltrados,Context context) {
        //Si no tengo gente a la que sigo devuelvo el array como esta
        if (usuarioSesion.getMisSeguidos() == null || usuarioSesion.getMisSeguidos().isEmpty())
            usuariosFiltrados = new ArrayList<>(getUsuarios());
        else
            buscarUsuarios(usuarioSesion.getMisSeguidos(),usuariosFiltrados,context);
    }

    /**
     * Comprueba si un usuario está trabajando con otro en un proyecto
     * @param usuarioSesion
     * @param usuario
     * @return
     */
    public static boolean esCompaniero(Usuario usuarioSesion,Usuario usuario){
        boolean flag=false;

        for(int i=0;i<usuarioSesion.getMisProyectos().size() && !flag;++i){
            int idProyecto = usuarioSesion.getMisProyectos().get(i);
            flag=StreamSupport.stream(usuario.getMisProyectos()).filter(integer -> idProyecto==integer).findAny().isPresent();
        }

        return flag;
    }
}
