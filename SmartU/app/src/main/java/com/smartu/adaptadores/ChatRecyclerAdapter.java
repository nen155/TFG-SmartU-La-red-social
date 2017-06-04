package com.smartu.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smartu.R;
import com.smartu.modelos.chat.Mensaje;
import com.smartu.vistas.MensajesActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    private Mensaje friendlyMessage;
    private Context context;
    private List<Mensaje> mChats;
    private ProgressBar mProgress;

    public ChatRecyclerAdapter(List<Mensaje> chats, Context context, ProgressBar mProgress) {
        this.mProgress=mProgress;
        this.mChats = chats;
        this.context =context;
    }

    public void add(Mensaje chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        View viewChatMine = layoutInflater.inflate(R.layout.item_message, parent, false);
        viewHolder = new MessageViewHolder(viewChatMine);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder viewHolder = (MessageViewHolder) holder;
        friendlyMessage = mChats.get(position);
        mProgress.setVisibility(View.INVISIBLE);
        //Muestra el texto si lo hubiese
        if (friendlyMessage.getMessage() != null) {
            viewHolder.messageTextView.setText(friendlyMessage.getMessage());
            viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
            viewHolder.messageImageView.setVisibility(ImageView.GONE);
        } else {//Tenemos una imagen
            String imageUrl = friendlyMessage.getImageUrl();
            //Está subida en el StorageReference de Firebase
            if (imageUrl!=null && imageUrl.startsWith("gs://")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageReference.getDownloadUrl().addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Picasso.with(viewHolder.messageImageView.getContext()).load(downloadUrl).into(viewHolder.messageImageView);
                                } else {
                                    Log.w("Descarga Imagen", "Getting download url was not successful.", task.getException());
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
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.usuario_perfil));
        } else {
            Picasso.with(context).load(friendlyMessage.getPhotoUrl()).into(viewHolder.messengerImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }


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
}
