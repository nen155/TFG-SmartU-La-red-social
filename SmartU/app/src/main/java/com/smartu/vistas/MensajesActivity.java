package com.smartu.vistas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.appindexing.Action;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smartu.R;
import com.smartu.adaptadores.ChatRecyclerAdapter;
import com.smartu.modelos.chat.Mensaje;
import com.smartu.modelos.Usuario;
import com.smartu.servicios.FcmNotificationBuilder;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.ControladorPreferencias;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesActivity extends AppCompatActivity {

    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "https://smartu-40a26.firebaseio.com/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private Context context;
    private List<Mensaje> mensajes = new ArrayList<>();


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }
    private ImageView mAddMessageImageView;
    private EditText mMessageEditText;
    private String mUsername;
    private String mPhotoUrl;
    private Button mSendButton;
    private String mChat;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Mensaje, MessageViewHolder> mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Usuario usuarioSesion=null,usuarioChat=null;
    private ChatRecyclerAdapter chatRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        Bundle bundle = getIntent().getExtras();
        context =this;
        if(bundle!=null)
            usuarioChat = bundle.getParcelable("usuario");

        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(usuarioChat.getUser());
        setTitle(usuarioChat.getUser());
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        usuarioSesion = Sesion.getUsuario(this);
        mUsername = usuarioSesion.getUser();
        if(usuarioSesion.getImagenPerfil()!=null && usuarioSesion.getImagenPerfil().compareTo("")!=0)
            mPhotoUrl = ConsultasBBDD.server+usuarioSesion.getImagenPerfil();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        chatRecyclerAdapter = new ChatRecyclerAdapter(mensajes,context,mProgressBar);

        //Le añado un observador para bajar el RecyclerView a la última posicion cuando llegue un mensaje, o al iniciar el chat
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = chatRecyclerAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(chatRecyclerAdapter);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mAddMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);

        //Enviar un mensaje CON IMAGEN en el Activity for Result
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para enviar una imagen
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        mSendButton = (Button) findViewById(R.id.sendButton);

        //Enviar un mensaje SIN IMAGEN
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Envio un mensaje con el texto de mMessageEditeText y con mi foto y nombre de usuario
                Mensaje friendlyMessage = new Mensaje(mMessageEditText.getText().toString(), mUsername, mPhotoUrl, null);
                friendlyMessage.setSenderUid(usuarioSesion.getUid());
                friendlyMessage.setReceiverUid(usuarioChat.getUid());
                friendlyMessage.setTimestamp(System.currentTimeMillis());
                friendlyMessage.setReceiver(usuarioChat.getUser());

                sendMessageToFirebaseUser(friendlyMessage,usuarioChat.getFirebaseToken());
                mMessageEditText.setText("");
            }
        });

        //Comienzo a recibir mensajes
        getMessageFromFirebaseUser(usuarioSesion.getUid(),usuarioChat.getUid(),null);
    }

    /**
     * Recibe mensajes de cualquier tipo y los añade al adaptador
     * @param senderUid
     * @param receiverUid
     * @param messageViewHolder
     */
    public void getMessageFromFirebaseUser(String senderUid, String receiverUid,MessageViewHolder messageViewHolder) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constantes.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    FirebaseDatabase.getInstance().getReference().child(Constantes.ARG_CHAT_ROOMS).child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                            chatRecyclerAdapter.add(mensaje);
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    FirebaseDatabase.getInstance().getReference().child(Constantes.ARG_CHAT_ROOMS).child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                            chatRecyclerAdapter.add(mensaje);
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                } else {}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Envío elementos
     * @param mensaje
     * @param receiverFirebaseToken
     */
    public void sendMessageToFirebaseUser(final Mensaje mensaje, final String receiverFirebaseToken) {
        final String room_type_1 = mensaje.getSenderUid() + "_" + mensaje.getReceiverUid();
        final String room_type_2 = mensaje.getReceiverUid() + "_" + mensaje.getSenderUid();

        mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //El mensaje lo envíe yo y partir de aquí se enviará en ese canal
                if (dataSnapshot.hasChild(room_type_1)) {
                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(mensaje.getTimestamp())).setValue(mensaje);
                    //El primer mensaje me lo enviaron
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(mensaje.getTimestamp())).setValue(mensaje);
                } else {
                    //La primera vez que se envía un mensaje en este chat. Lo hago en mi room
                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(mensaje.getTimestamp())).setValue(mensaje);
                    //Como el primer getMessageFromFirebaseUser no obtiene nada porque esta es la primera vez que se usa este room tengo que volver a pedir mensajes
                    getMessageFromFirebaseUser(mensaje.getSenderUid(),mensaje.getReceiverUid(),null);
                }
                // send push notification to the receiver
                sendPushNotificationToReceiver(mensaje.getSender(), mensaje.getMessage(), mensaje.getSenderUid(), ControladorPreferencias.getTokenFCM(), receiverFirebaseToken);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MENSAJES","Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Enviar una Cloud Messaging a un token concreto en este caso al que le enviado el mensaje
     *
     * @param username
     * @param message
     * @param uid
     * @param firebaseToken
     * @param receiverFirebaseToken
     */
    private void sendPushNotificationToReceiver(String username, String message, String uid, String firebaseToken, String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize().title(username).message(message)
                .username(username).uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }

    /**
     * Sirve para enviar imágenes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Mensajes", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d("Mensajes", "Uri: " + uri.toString());

                    final String room_type_1 = usuarioSesion.getUid() + "_" + usuarioChat.getUid();
                    final String room_type_2 = usuarioChat.getUid() + "_" + usuarioSesion.getUid();

                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(room_type_1)) {
                                crearMensajeEn(uri);
                            } else if (dataSnapshot.hasChild(room_type_2)) {
                                crearMensajeEn(uri);
                            } else {
                                crearMensajeEn(uri);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("MENSAJES","Unable to send message: " + databaseError.getMessage());
                        }
                    });
                }
            }
        }
    }

    /**
     * Establece la ruta donde va a subir el archivo
     * @param uri
     */
    public void crearMensajeEn(Uri uri){
        //Pone la imagen en el StorageReference en la ruta:
        //User UID -> LA RUTA DE LA URI
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(mFirebaseUser.getUid()).child(uri.getLastPathSegment());
        //Esto crea el mensaje realmente
        putImageInStorage(storageReference, uri);

    }

    /**
     * Guardo la imagen en la base de datos de FCM
     * @param storageReference
     * @param uri
     */
    private void putImageInStorage(StorageReference storageReference, Uri uri) {
        storageReference.putFile(uri).addOnCompleteListener(MensajesActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Mensaje friendlyMessage = new Mensaje(null, mUsername, mPhotoUrl, task.getResult().getDownloadUrl().toString());
                            friendlyMessage.setSenderUid(usuarioSesion.getUid());
                            friendlyMessage.setReceiverUid(usuarioChat.getUid());
                            friendlyMessage.setTimestamp(System.currentTimeMillis());
                            friendlyMessage.setReceiver(usuarioChat.getUser());
                            sendMessageToFirebaseUser(friendlyMessage,usuarioChat.getFirebaseToken());
                        } else {
                            Log.w("Mensajes", "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }
}
