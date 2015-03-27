package spring15.ec551.fpgacontroller.fragments;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spring15.ec551.fpgacontroller.accelerometer.AccelerometerHighPassFilter;
import spring15.ec551.fpgacontroller.resources.CustomTextView;
import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.resources.UserConfigurationObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerFragment extends Fragment implements
        SensorEventListener, AccelerometerHighPassFilter.FilterListener{

    Context mContext;
    CustomTextView mSubtitle;
    CustomTextView mButton;

    /** The filter object used to remove the pesky noise */
    private AccelerometerHighPassFilter mFilter;
    /** The filtered accelerometer valuesfrom AccelerometerHighPassFilter */
    protected float mFilteredAccelX;
    protected float mFilteredAccelY;
    protected float mFilteredAccelZ;

    // TODO: Rename and change types and number of parameters
    public static ControllerFragment newInstance() {
        ControllerFragment fragment = new ControllerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControllerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mInputMethodService.getCurrentInputBinding().
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller_setting, container, false);
        mSubtitle = (CustomTextView) view.findViewById(R.id.controller_subtitle);
        mButton = (CustomTextView) view.findViewById(R.id.controller_button);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mFilter = new AccelerometerHighPassFilter(this);

//        try {
//            mListener = (ExampleFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


    /** Will initialize the configuration settings */
    public void setConfiguration(UserConfigurationObject userObject) {
        // TODO
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mButton != null) {
            mButton.setText(
                    "X: " + event.values[AccelerometerHighPassFilter.X] +
                            " Y: " + event.values[AccelerometerHighPassFilter.Y] +
                            " Z: " + event.values[AccelerometerHighPassFilter.Z]);
        }
        if (mFilter != null) {
            mFilter.onAccelerometerChanged(
                    event.values[AccelerometerHighPassFilter.X],
                    event.values[AccelerometerHighPassFilter.Y],
                    event.values[AccelerometerHighPassFilter.Z]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ACCURACYCHANGED: ", "Integer: " + accuracy);
    }

    @Override
    public void onFilterChangedListener(float valueX, float valueY, float valueZ) {
        mFilteredAccelX = valueX;
        mFilteredAccelY = valueY;
        mFilteredAccelZ = valueZ;

        if (mSubtitle != null) {
            mSubtitle.setText(
                    "fX: " + (int) mFilteredAccelX + " fY: " + (int) mFilteredAccelY + " fZ: " + (int) mFilteredAccelZ);
        }
    }
}
