package com.smartu.utilidades;

import com.smartu.modelos.Avance;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;

import java.util.Comparator;

/**
 * Created by Emilio Chica Jiménez on 07/06/2017.
 */

public class Comparador {
    public static class ComparaComentarios implements Comparator<Comentario>
    {
        public int compare(Comentario left, Comentario right) {
            if(left.getFecha().getTime()<right.getFecha().getTime())
                return 1;
            else
                return -1;
        }
    }

    public static class ComparaAvances implements Comparator<Avance>
    {
        public int compare(Avance left, Avance right) {
            if(left.getFecha().getTime()<right.getFecha().getTime())
                return 1;
            else
                return -1;
        }
    }
    public static class ComparaNotificaciones implements Comparator<Notificacion>
    {
        public int compare(Notificacion left, Notificacion right) {
            if(left.getFecha().getTime()<right.getFecha().getTime())
                return 1;
            else
                return -1;
        }
    }

    public static class ComparaProyectos implements Comparator<Proyecto>
    {
        public int compare(Proyecto left, Proyecto right) {
            if(left.getFechaCreacion().getTime()<right.getFechaCreacion().getTime())
                return 1;
            else
                return -1;
        }
    }
}
