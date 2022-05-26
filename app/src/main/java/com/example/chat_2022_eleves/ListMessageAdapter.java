package com.example.chat_2022_eleves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListMessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context mContext;
    private ListMessages mMessageList;
    GlobalState gs;

    public ListMessageAdapter(Context context, ListMessages messageList, GlobalState gs) {
        mContext = context;
        mMessageList = messageList;
        this.gs = gs;
    }

    @Override
    public int getItemCount() {
        return mMessageList.getSize();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.getMessages().get(position);

        if (message.getAuteur().equals(gs.getUsername())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_current_user_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_other_user_item, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.getMessages().get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    // Permit to get the message from the recycler view
    public Message getItem(int position) {
        return mMessageList.getMessages().get(position);
    }

    // Permit to add the message from the recycler view
    public boolean addMessage(Message message) {
        return mMessageList.getMessages().add(message);
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
        }

        void bind(Message message) {
            messageText.setText(message.getContenu());
            messageText.setTextColor(gs.getCouleur(message.getCouleur()));
            nameText.setText(message.getAuteur());


        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
        }


        void bind(Message message) {
            messageText.setText(message.getContenu());
            if(message.getCouleur() == null)
            {
                messageText.setTextColor(gs.getCouleur("white"));
            }
            else
            {
                messageText.setTextColor(gs.getCouleur(message.getCouleur()));
            }

        }


    }
}