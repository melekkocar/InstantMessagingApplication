package com.SmsApp.melek.SmsApp;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.message_icon);


            holder.nameLabel = (TextView) convertView.findViewById(R.id.sender_label);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.timeLabel);

            convertView.setTag(holder);


        } else {//already exist
            holder = (ViewHolder) convertView.getTag();
        }




        ParseObject message = mMessages.get(position);


        //Mesajın oluşturulma tarihi

        Date createdAt = message.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(

                createdAt.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS

        ).toString();


        holder.timeLabel.setText(convertedDate);
//


        if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.mipmap.ic_action_picture);
        } else if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_VIDEO)) {
            holder.iconImageView.setImageResource(R.mipmap.ic_action_play_over_video);
        } else {
            holder.iconImageView.setImageResource(android.R.drawable.ic_dialog_email);
        }
        holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));

        return convertView;
    }





    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
        TextView timeLabel;

    }
}
