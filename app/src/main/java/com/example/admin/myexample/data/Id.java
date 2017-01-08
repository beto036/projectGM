package com.example.admin.myexample.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 1/7/2017.
 */

public class Id implements Parcelable{
    @SerializedName("$oid")
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "Id{" +
                "oid='" + oid + '\'' +
                '}';
    }

    protected Id(Parcel in) {
        oid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Id> CREATOR = new Parcelable.Creator<Id>() {
        @Override
        public Id createFromParcel(Parcel in) {
            return new Id(in);
        }

        @Override
        public Id[] newArray(int size) {
            return new Id[size];
        }
    };
}
