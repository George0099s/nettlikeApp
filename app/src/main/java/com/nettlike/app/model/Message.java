package com.nettlike.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {
    private static final String TAG = "Message";
    @SerializedName("dialog_id")
    private String dialogId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("type")
    private String type;
    @SerializedName("text")
    private String text;
    @SerializedName("seen_by")
    private List<String> seenBy = null;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("_id")
    private String id;
    public static final int USER_MSG = 0;
    public static final int OTHER_MSG = 1;


    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public String getCreatedAt() {

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        newFormat.setTimeZone(TimeZone.getDefault());
//        compareFormat.setTimeZone(TimeZone.getDefault());
//        Date currentDate = Calendar.getInstance().getTime();
//        Date msgDate = null;
//        String currentDateString = compareFormat.format(currentDate);
//        try {
//            msgDate = df.parse(createdAt);
//            Date date = compareFormat.parse(createdAt);
//            currentDate = compareFormat.parse(currentDateString);
//            String compareDateString = compareFormat.format(msgDate);
//            if (date.before(currentDate))
//                this.createdAt = createdAt;
//                else
//                this.createdAt = newFormat.format(msgDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return createdAt;

    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public static class Builder {
        private final String mType;
        private String mUsername;
        private String mMessage;


        public Builder(int type) {
            mType = String.valueOf(type);
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }
        public Message build() {
            Message message = new Message();
            message.type = mType;
            message.text = mMessage;
            return message;
        }
    }

    public String getType() {
        if (UserSingleton.INSTANCE.getUserId().equals(getCreatedBy())){
            setType(String.valueOf(USER_MSG));
        } else {
            setType(String.valueOf(OTHER_MSG));

        }
            return type;
//        return String.valueOf(UserSingleton.INSTANCE.getUserId() == createdBy ? USER_MSG : OTHER_MSG);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(List<String> seenBy) {
        this.seenBy = seenBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
