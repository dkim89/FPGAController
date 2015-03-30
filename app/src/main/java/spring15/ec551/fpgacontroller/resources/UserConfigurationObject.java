package spring15.ec551.fpgacontroller.resources;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidkim on 3/26/15.
 */
public class UserConfigurationObject implements Parcelable{

    public static final String USER_SAVED_CONFIG = "USER_SAVED_CONFIG";

    float[] mNetValues;
    int mLeftBound;
    int mRightBound;
    float mKFactor;
    float mAngleSensitivity;

    public UserConfigurationObject() {
        mNetValues = new float[]{0.0f,0.0f,0.0f};
        mLeftBound = 0;
        mRightBound = 0;
        mAngleSensitivity = 0.0f;
        mKFactor = 0.65f;
    }

    public UserConfigurationObject(UserConfigurationObject object) {
        mNetValues = object.getNetValues();
        mLeftBound = object.getLeftBound();
        mRightBound = object.getRightBound();
        mAngleSensitivity = object.getAngleSensitivty();
        mKFactor = object.getKFactor();
    }

    public float[] getNetValues() {
        return mNetValues;
    }

    public int getLeftBound() {
        return mLeftBound;
    }

    public int getRightBound() {
        return mRightBound;
    }

    public float getAngleSensitivty() {
        return mAngleSensitivity;
    }

    public float getKFactor() {
        return mKFactor;
    }
    protected UserConfigurationObject(Parcel in) {
        mKFactor = in.readFloat();
        mAngleSensitivity = in.readFloat();
        mNetValues = in.createFloatArray();
        mLeftBound = in.readInt();
        mRightBound = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mKFactor);
        dest.writeFloat(mAngleSensitivity);
        dest.writeFloatArray(mNetValues);
        dest.writeInt(mLeftBound);
        dest.writeInt(mRightBound);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserConfigurationObject> CREATOR = new Parcelable.Creator<UserConfigurationObject>() {
        @Override
        public UserConfigurationObject createFromParcel(Parcel in) {
            return new UserConfigurationObject(in);
        }

        @Override
        public UserConfigurationObject[] newArray(int size) {
            return new UserConfigurationObject[size];
        }
    };
}
