package com.ajpasigado.qpicalculator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Grade implements Parcelable {
    public static final Creator<Grade> CREATOR = new C05061();
    String letterGrade;
    int numberOfunits;

    /* renamed from: com.ajpasigado.qpicalculator.Grade$1 */
    static class C05061 implements Creator<Grade> {
        C05061() {
        }

        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    }

    protected Grade(Parcel in) {
        this.letterGrade = in.readString();
        this.numberOfunits = in.readInt();
    }

    public Grade(String letterGrade, int numberOfunits) {
        this.letterGrade = letterGrade;
        this.numberOfunits = numberOfunits;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.letterGrade);
        parcel.writeInt(this.numberOfunits);
    }
}
