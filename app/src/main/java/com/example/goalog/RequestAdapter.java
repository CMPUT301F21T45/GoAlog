package com.example.goalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<FollowRequest> {
    public RequestAdapter(Context context, int position, ArrayList<FollowRequest> requests) {
        super(context, position, requests);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FollowRequest followRequest = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_request_list,
                    parent, false);
        }

        TextView senderName = convertView.findViewById(R.id.sender_text_view);
        TextView message = convertView.findViewById(R.id.message_request_text_view);
        Button acceptButton = convertView.findViewById(R.id.accept_request_button);
        ImageButton dismissButton = convertView.findViewById(R.id.dismiss_button);

        senderName.setText(followRequest.getFromUser());
        message.setText(followRequest.getMessage());

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: accept
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: delete from firebase and list
            }
        });



        return convertView;
    }
}
