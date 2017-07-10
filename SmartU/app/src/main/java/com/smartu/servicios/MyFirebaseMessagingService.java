package com.smartu.servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Area;
import com.smartu.modelos.BuenaIdea;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.SolicitudUnion;
import com.smartu.modelos.Usuario;
import com.smartu.modelos.Vacante;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentNotificaciones;
import com.smartu.vistas.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import java8.util.Optional;
import java8.util.stream.StreamSupport;

/**
 * Created by Emilio Chica Jiménez on 21/05/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "¡Mensaje recibido!");
        //Si es un mensaje del chat de un usuario
        if(remoteMessage.getData()!=null && remoteMessage.getData().containsKey("fcm_token")) {
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("text");
                String username = remoteMessage.getData().get("username");
                String uid = remoteMessage.getData().get("uid");
                String fcmToken = remoteMessage.getData().get("fcm_token");
                //Muestro la notifiación
                sendNotification(title, message, username, uid, fcmToken);
        }else {
            /// Si es de tipo inserción la muestro sino no.
            //Es una nueva notificación de que alguien ha creado algo
            if(remoteMessage.getData().get("accion")!=null &&
                    remoteMessage.getData().get("accion").compareTo("insert")==0)
                displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
            //Envío los datos al RecyclerView correspondiente para que se actualice
            addNotificacion(remoteMessage);
        }
    }

    /**
     * Crea una notificación cuando recibes un mensaje del chat de FCM
     */
    private void sendNotification(String title, String message, String receiver, String receiverUid, String firebaseToken) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constantes.ARG_RECEIVER, receiver);
        intent.putExtra(Constantes.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constantes.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mensaje)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * Envía la notificación para que la recoja el correspondiente RecyclerView que está esperandola
     * Se encuentra en FragmentNotificaciones en el onCreate.....
     * Actualiza los datos en el Almacen para la sincronización.
     * @param remoteMessage
     */
    private void addNotificacion(RemoteMessage remoteMessage) {
        //Creo un intent parar el fragment de notificaciones
        Intent intent = new Intent(FragmentNotificaciones.ACTION_NOTIFY_NEW_NOTIFICACION);
        intent.putExtra("id", remoteMessage.getData().get("id"));
        intent.putExtra("nombre", remoteMessage.getData().get("nombre"));
        intent.putExtra("descripcion", remoteMessage.getData().get("descripcion"));
        intent.putExtra("fecha", remoteMessage.getData().get("fecha"));
        intent.putExtra("idusuario", remoteMessage.getData().get("idUsuario"));
        intent.putExtra("idproyecto", remoteMessage.getData().get("idProyecto"));
        intent.putExtra("usuario", remoteMessage.getData().get("usuario"));
        intent.putExtra("proyecto", remoteMessage.getData().get("proyecto"));
        Usuario usuarioSeion=Sesion.getUsuario(getBaseContext());
        int idUsuario =Integer.parseInt(remoteMessage.getData().get("idUsuario"));
        //Actualizo el almacen con los datos de la notificacion
        //sino soy el usuario que la envió
        if((usuarioSeion==null || usuarioSeion.getId() != idUsuario) &&
                (remoteMessage.getData().get("tipo")!=null && remoteMessage.getData().get("accion")!=null))
            updateAlmacen(remoteMessage.getData());

        //Creo la notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setId(Integer.parseInt(remoteMessage.getData().get("id")));
        notificacion.setNombre(remoteMessage.getData().get("nombre"));
        notificacion.setDescripcion(remoteMessage.getData().get("descripcion"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            notificacion.setFecha(sdf.parse(remoteMessage.getData().get("fecha")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(remoteMessage.getData().get("idUsuario")!=null)
            notificacion.setIdUsuario(Integer.parseInt(remoteMessage.getData().get("idUsuario")));
        if(remoteMessage.getData().get("idProyecto")!=null)
            notificacion.setIdProyecto(Integer.parseInt(remoteMessage.getData().get("idProyecto")));
        if(remoteMessage.getData().get("usuario")!=null) {
            notificacion.setUsuario(remoteMessage.getData().get("usuario"));
        }
        if(remoteMessage.getData().get("proyecto")!=null)
            notificacion.setProyecto(remoteMessage.getData().get("proyecto"));
        //La añado al almacen
        Almacen.add(notificacion);
        //Envió el intent al Broadcast local para que lo recoja el fragment de notificaciones
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Sincroniza los datos en el Almacen con los datos que trae la notificación
     * @param datos
     */
    private void updateAlmacen(Map<String,String> datos)  {
        int idUsuario = Integer.parseInt(datos.get("idUsuario"));
        //Dependiendo del tipo tendré que actualizar uno u otro
        switch (datos.get("tipo")){
            case "idea": //Sincroniza las buenas ideas dadas a un proyecot
                int idProyectoIdea= Integer.parseInt(datos.get("idProyecto"));
                //Dependiendo de la acción quito o añado
                if(datos.get("accion").compareTo("insert")==0) {
                    Optional<Proyecto> proyectoOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getId() == idProyectoIdea).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(proyectoOptional.isPresent()){
                        proyectoOptional.get().getBuenaIdea().add(new BuenaIdea(idUsuario));
                    }
                }else //Es eliminar la idea
                {
                    Optional<Proyecto> proyectoOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getId() == idProyectoIdea).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(proyectoOptional.isPresent()){
                        Optional<BuenaIdea> buenaIdeaOptional = StreamSupport.stream(proyectoOptional.get().getBuenaIdea()).filter(buenaIdea -> buenaIdea.getIdUsuario() == idUsuario).findAny();
                        if(buenaIdeaOptional.isPresent())
                            proyectoOptional.get().getBuenaIdea().remove(buenaIdeaOptional.get());
                    }
                }
                break;
            case "solicitud": //Sincroniza las solicitudes de union
                int idProyecto= Integer.parseInt(datos.get("idProyecto"));
                //Dependiendo de la acción quito o añado
                if(datos.get("accion").compareTo("insert")==0) {
                    Optional<Proyecto> proyectoOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getId() == idProyecto).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(proyectoOptional.isPresent()){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            SolicitudUnion solicitudUnion = new SolicitudUnion(sdf.parse(datos.get("fecha")),Integer.parseInt(datos.get("idUsuario")),datos.get("descripcion"));
                            solicitudUnion.setIdProyecto(idProyecto);
                            solicitudUnion.setIdVacante(Integer.parseInt(datos.get("idVacante")));
                            proyectoOptional.get().getSolicitudes().add(solicitudUnion);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }else //Es eliminar la solicitud
                {
                    Optional<Proyecto> proyectoOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getId() == idProyecto).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(proyectoOptional.isPresent()){
                        Optional<SolicitudUnion> solicitudUnionOptional = StreamSupport.stream(proyectoOptional.get().getSolicitudes()).filter(solicitudUnion -> solicitudUnion.getIdUsuarioSolicitante() == idUsuario).findAny();
                        if(solicitudUnionOptional.isPresent())
                            proyectoOptional.get().getSolicitudes().remove(solicitudUnionOptional.get());
                    }
                }
                break;
            case "interes": //Sincroniza los intereses de un usuario
                //Dependiendo de la acción quito o añado
                if(datos.get("accion").compareTo("insert")==0) {
                    Optional<Usuario> usuarioOptional = StreamSupport.stream(Almacen.getUsuarios()).filter(usuario -> usuario.getId() == idUsuario).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(usuarioOptional.isPresent()){
                        if( datos.get("idsAreas").contains(",")) {
                            String[] areas = datos.get("idsAreas").split(",");
                            for (int i=0;i<areas.length;++i) {
                                int idArea= Integer.parseInt(areas[i]);
                                Area area1 = StreamSupport.stream(Almacen.getAreas()).filter(area -> area.getId() == idArea).findAny().get();
                                usuarioOptional.get().getMisAreasInteres().add(area1);
                            }
                        }else //Si solo tengo un area la añado
                        {
                            int idArea= Integer.parseInt(datos.get("idsAreas"));
                            Area area1 = StreamSupport.stream(Almacen.getAreas()).filter(area -> area.getId() == idArea).findAny().get();
                            usuarioOptional.get().getMisAreasInteres().add(area1);
                        }
                    }
                }else //Es eliminar el interes
                {
                    Optional<Usuario> usuarioOptional = StreamSupport.stream(Almacen.getUsuarios()).filter(usuario -> usuario.getId() == idUsuario).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(usuarioOptional.isPresent()) {
                        if( datos.get("idsAreas").contains(",")) {
                            String[] areas = datos.get("idsAreas").split(",");
                            for (int i=0;i<areas.length;++i) {
                                int idArea= Integer.parseInt(areas[i]);
                                Area area1 = StreamSupport.stream(Almacen.getAreas()).filter(area -> area.getId() == idArea).findAny().get();
                                usuarioOptional.get().getMisAreasInteres().remove(area1);
                            }
                        }else //Si solo tengo un area la añado
                        {
                            int idArea= Integer.parseInt(datos.get("idsAreas"));
                            Area area1 = StreamSupport.stream(Almacen.getAreas()).filter(area -> area.getId() == idArea).findAny().get();
                            usuarioOptional.get().getMisAreasInteres().remove(area1);
                        }
                    }
                }
                break;
            case "status": //Sincroniza el status al subir de status de un usuario
                Optional<Usuario> usuarioOptional = StreamSupport.stream(Almacen.getUsuarios()).filter(usuario -> usuario.getId() == idUsuario).findAny();
                //Si lo tengo en el almacen lo actualizo
                if(usuarioOptional.isPresent()) {
                    usuarioOptional.get().getMiStatus().setNombre(datos.get("estatus"));
                    usuarioOptional.get().getMiStatus().setNumSeguidores(Integer.parseInt(datos.get("numSeguidores")));
                    usuarioOptional.get().getMiStatus().setPuntos(Integer.parseInt(datos.get("nPuntos")));
                }

                break;
            case "seguir": //Sincroniza el numero de seguidores de un usuario
                //Dependiendo de la acción quito o añado
                if(datos.get("accion").compareTo("insert")==0) {
                    Optional<Usuario> usuarioOptionalSeguir = StreamSupport.stream(Almacen.getUsuarios()).filter(usuario -> usuario.getId() == idUsuario).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if (usuarioOptionalSeguir.isPresent()) {
                        usuarioOptionalSeguir.get().getMiStatus().setNumSeguidores(Integer.parseInt(datos.get("numSeguidores")));
                        usuarioOptionalSeguir.get().getMisSeguidos().add(Integer.parseInt(datos.get("idUsuarioSeguido")));
                    }
                }else
                {
                    Optional<Usuario> usuarioOptionalSeguir = StreamSupport.stream(Almacen.getUsuarios()).filter(usuario -> usuario.getId() == idUsuario).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if (usuarioOptionalSeguir.isPresent()) {
                        usuarioOptionalSeguir.get().getMiStatus().setNumSeguidores(Integer.parseInt(datos.get("numSeguidores")));
                        usuarioOptionalSeguir.get().getMisSeguidos().remove(usuarioOptionalSeguir.get().getMisSeguidos().indexOf(Integer.parseInt(datos.get("idUsuarioSeguido"))));
                    }
                }
                break;
            case "ocupar": //Sincroniza vacantes al ocupar un proyecto
                if(datos.get("accion").compareTo("insert")==0) {
                    int idProyectoO= Integer.parseInt(datos.get("idProyecto"));
                    int idVacante = Integer.parseInt(datos.get("idVacante"));
                    Optional<Proyecto> proyectoOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getId() == idProyectoO).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(proyectoOptional.isPresent())
                    {
                        Proyecto p = proyectoOptional.get();
                        Optional<Vacante> vacanteOptional = StreamSupport.stream(p.getVacantesProyecto()).filter(vacante -> vacante.getId() == idVacante).findAny();
                        boolean esta = StreamSupport.stream(p.getIntegrantes()).filter(integer -> idUsuario == integer).findAny().isPresent();
                        if(!esta)
                            p.getIntegrantes().add(idUsuario);

                        if(vacanteOptional.isPresent()){
                            p.getVacantesProyecto().remove(vacanteOptional.get());
                        }
                    }
                    Optional<Usuario> usuarioOptional1 = StreamSupport.stream(Almacen.getUsuarios()).filter(usuario -> usuario.getId() == idUsuario).findAny();
                    //Si lo tengo en el almacen lo actualizo
                    if(usuarioOptional1.isPresent()){
                        boolean esta = StreamSupport.stream(usuarioOptional1.get().getMisProyectos()).filter(integer -> idProyectoO == integer).findAny().isPresent();
                        if(!esta)
                            usuarioOptional1.get().getMisProyectos().add(idProyectoO);
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * Muestra que ha habido una nueva notificación
     * @param notification
     * @param data
     */
    private void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notificacion","notificacion");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.ic_launch)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
