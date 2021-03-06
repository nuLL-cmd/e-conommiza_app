package com.automatodev.e_conommiza_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PerspectiveEntity implements Parcelable {

    private String namePespective;
    private String mounth;
    private int year;
    private BigDecimal totaldDebit;
    private BigDecimal totalCredit;

    private List<DataEntryEntity> itemsPerspective;


    protected PerspectiveEntity(Parcel in) {
        namePespective = in.readString();
        mounth = in.readString();
        year = in.readInt();
    }

    public static final Creator<PerspectiveEntity> CREATOR = new Creator<PerspectiveEntity>() {
        @Override
        public PerspectiveEntity createFromParcel(Parcel in) {
            return new PerspectiveEntity(in);
        }

        @Override
        public PerspectiveEntity[] newArray(int size) {
            return new PerspectiveEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namePespective);
        dest.writeString(mounth);
        dest.writeInt(year);
    }
}
