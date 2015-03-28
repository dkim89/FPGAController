package spring15.ec551.fpgacontroller.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spring15.ec551.fpgacontroller.accelerometer.ControllerVehicleInterfacer;
import spring15.ec551.fpgacontroller.resources.CustomTextView;
import spring15.ec551.fpgacontroller.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerFragment extends Fragment implements ControllerInterfaceListener {

    Context mContext;
    CustomTextView mUnfiltered;
    CustomTextView mFiltered;

    private ControllerVehicleInterfacer mController;

    /** The filtered accelerometer valuesfrom AccelerometerHighPassFilter */

    public static ControllerFragment newInstance() {
        ControllerFragment fragment = new ControllerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControllerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // TODO: Get UserConfigurationObject
        }

        /** This will allow the the filtered values to be outputted to this fragment */
        mController = new ControllerVehicleInterfacer(mContext, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller_setting, container, false);
        mUnfiltered = (CustomTextView) view.findViewById(R.id.unfiltered_vector);
        mFiltered = (CustomTextView) view.findViewById(R.id.filtered_vector);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mController != null) {
            mController.registerSensor();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mController.unregisterSensor();
    }

    @Override
    public void onBaseChangedListener(float valueX, float valueY, float valueZ) {
        if (mUnfiltered != null) {
            mUnfiltered.setText(
                    "X: " + valueX +
                    "\nY: " + valueY +
                    "\nZ: " + valueZ);
        }
    }

    @Override
    public void onFilterChangedListener(float valueX, float valueY, float valueZ) {
        if (mFiltered != null) {
            mFiltered.setText(
                    "filtered X: " + valueX +
                    "\nfiltered Y: " + valueY +
                    "\nfiltered Z: " + valueZ);
        }
    }
}
