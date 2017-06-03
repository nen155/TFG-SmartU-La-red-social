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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smartu.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesActivity extends AppCompatActivity {

    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "https://smartu-40a26.firebaseio.com/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private Context context;


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


        String message = mMessageEditText.getText().toString();
        String receiver = usuarioChat.getEmail();
        String receiverUid = usuarioChat.getUid();
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverFirebaseToken = usuarioChat.getFirebaseToken();

        Mensaje mensaje = new Mensaje(message,sender,null,null);
        mensaje.setReceiver(receiver);
        mensaje.setReceiverUid(receiverUid);
        mensaje.setSenderUid(senderUid);
        mensaje.setTimestamp(System.currentTimeMillis());


        final String room_type_1 = mensaje.getSenderUid() + "_" + mensaje.getReceiverUid();
        final String room_type_2 = mensaje.getReceiverUid() + "_" + mensaje.getSenderUid();

        //Recibo elementos....
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Mensaje, MessageViewHolder>(Mensaje.class, R.layout.item_message, MessageViewHolder.class, mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS)) {

            @Override
            protected Mensaje parseSnapshot(DataSnapshot snapshot) {
                Mensaje friendlyMessage = super.parseSnapshot(snapshot);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(snapshot.getKey());
                }
                return friendlyMessage;
            }

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, Mensaje friendlyMessage, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                //Muestra el texto si lo hubiese
                if (friendlyMessage.getMessage() != null) {
                    viewHolder.messageTextView.setText(friendlyMessage.getMessage());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else {//Tenemos una imagen
                    String imageUrl = friendlyMessage.getImageUrl();
                    //Está subida en el StorageReference de Firebase
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            Picasso.with(viewHolder.messageImageView.getContext()).load(downloadUrl).into(viewHolder.messageImageView);
                                        } else {
                                            Log.w("Descarga Imagen", "Getting download url was not successful.",
                                                    task.getException());
                                        }
                                    }
                                });
                        //Tengo que descargarla de donde esté
                    } else {
                        Picasso.with(viewHolder.messageImageView.getContext()).load(friendlyMessage.getImageUrl()).into(viewHolder.messageImageView);
                    }
                    //Muestro la imágen y oculto el texto
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }

                //Muestro el que envia el mensaje
                viewHolder.messengerTextView.setText(friendlyMessage.getSender());
                //Cojo la URL de la foto del mensaje si la tuviese sino muestro una por defecto
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(MensajesActivity.this, R.drawable.usuario_perfil));
                } else {
                    Picasso.with(MensajesActivity.this).load(friendlyMessage.getPhotoUrl()).into(viewHolder.messengerImageView);
                }

                if (friendlyMessage.getMessage() != null) {
                    // write this message to the on-device index
                    FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
                }

                // log a view action on it
                FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
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
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

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
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Envio un mensaje con el texto de mMessageEditeText y con mi foto y nombre de usuario
                Mensaje friendlyMessage = new Mensaje(mMessageEditText.getText().toString(), mUsername, mPhotoUrl, null);
                friendlyMessage.setSenderUid(usuarioSesion.getUid());
                friendlyMessage.setReceiverUid(usuarioChat.getUid());
                friendlyMessage.setTimestamp(System.currentTimeMillis());
                friendlyMessage.setReceiver(usuarioChat.getUser());

                sendMessageToFirebaseUser(friendlyMessage,ControladorPreferencias.cargarToken(context));
                mMessageEditText.setText("");
            }
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
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e("MENSAJES", "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(mensaje.getTimestamp())).setValue(mensaje);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e("MENSAJES", "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(mensaje.getTimestamp())).setValue(mensaje);
                } else {
                    Log.e("MENSAJES", "sendMessageToFirebaseUser: success");
                    mFirebaseDatabaseReference.child(Constantes.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(mensaje.getTimestamp())).setValue(mensaje);
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



    private Action getMessageViewAction(Mensaje friendlyMessage) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(friendlyMessage.getSender(), MESSAGE_URL.concat(friendlyMessage.getId()))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }

    private Indexable getMessageIndexable(Mensaje friendlyMessage) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(mUsername.equals(friendlyMessage.getSender()))
                .setName(friendlyMessage.getSender())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(mUsername)
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(friendlyMessage.getMessage())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId()))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
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
                    //Envio un mensaje con el texto de mMessageEditeText con foto
                    Mensaje tempMessage = new Mensaje(null, mUsername, mPhotoUrl, LOADING_IMAGE_URL);
                    tempMessage.setSenderUid(usuarioSesion.getUid());
                    tempMessage.setReceiverUid(usuarioChat.getUid());
                    tempMessage.setTimestamp(System.currentTimeMillis());
                    tempMessage.setReceiver(usuarioChat.getUser());

                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(tempMessage, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(mFirebaseUser.getUid()).child(key).child(uri.getLastPathSegment());
                                        putImageInStorage(storageReference, uri, key);
                                    } else {
                                        Log.w("Mensajes", "Unable to write message to database.", databaseError.toException());
                                    }
                                }
                    });
                }
            }
        }
    }

    /**
     * Guardo la imagen en la base de datos de FCM
     * @param storageReference
     * @param uri
     * @param key
     */
    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(MensajesActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Mensaje friendlyMessage = new Mensaje(null, mUsername, mPhotoUrl, task.getResult().getDownloadUrl().toString());
                            mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key).setValue(friendlyMessage);
                        } else {
                            Log.w("Mensajes", "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }
}
