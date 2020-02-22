package com.nettlike.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Payload implements Parcelable {

    protected Payload(Parcel in) {
        byte tmpExists = in.readByte();
        exists = tmpExists == 0 ? null : tmpExists == 1;
        id = in.readString();
        token = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            population = null;
        } else {
            population = in.readInt();
        }
        byte tmpOk = in.readByte();
        ok = tmpOk == 0 ? null : tmpOk == 1;
    }

    public static final Creator<Payload> CREATOR = new Creator<Payload>() {
        @Override
        public Payload createFromParcel(Parcel in) {
            return new Payload(in);
        }

        @Override
        public Payload[] newArray(int size) {
            return new Payload[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return id+" "+name+" "+population + "" +ok;
    }
    @SerializedName("exists")
    private Boolean exists;
    @SerializedName("id")
    private String id;
    @SerializedName("token")
    private String token;
    @SerializedName("name")
    private String name;
    @SerializedName("population")
    private Integer population;
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("account")
    private Account account;
    @SerializedName("dialog")
    @Expose
    private MessageDialog messageDialog;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = new ArrayList<>();

    @SerializedName("id")
    public String getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("population")
    public Integer getPopulation() {
        return population;
    }

    @SerializedName("population")
    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (exists == null ? 0 : exists ? 1 : 2));
        dest.writeString(id);
        dest.writeString(token);
        dest.writeString(name);
        if (population == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(population);
        }
        dest.writeByte((byte) (ok == null ? 0 : ok ? 1 : 2));
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(MessageDialog messageDialog) {
        this.messageDialog = messageDialog;
    }
}
