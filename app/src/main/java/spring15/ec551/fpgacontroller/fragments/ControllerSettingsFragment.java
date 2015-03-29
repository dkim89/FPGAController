package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.accelerometer.ControllerInterfaceListener;
import spring15.ec551.fpgacontroller.accelerometer.ControllerObject;
import spring15.ec551.fpgacontroller.resources.CustomTextView;

/**
 * Created by davidkim on 3/28/15.
 */
public class ControllerSettingsFragment extends Fragment implements ControllerInterfaceListener{

    private final int X = 0;
    private final int Y = 1;
    private final int Z = 2;

    Context mContext;
    private FragmentActionListener mListener;

    CustomTextView mInstructionText, mNotificationText;
    View mBaseLine, mDeviceImage;

    private ControllerObject mController;

    private float netChange[];

    public static ControllerSettingsFragment newInstance() {
        ControllerSettingsFragment fragment = new ControllerSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControllerSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // TODO: GET UserConfigurationObject
        }

        mController = new ControllerObject(mContext, this);
        netChange = new float[]{0.0f, 0.0f, 0.0f};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller_settings, container, false);

        mInstructionText = (CustomTextView) view.findViewById(R.id.controller_instruc_textview);
        mNotificationText = (CustomTextView) view.findViewById(R.id.controller_notify_textview);
        mBaseLine = view.findViewById(R.id.controller_instruc_baseline);
        mDeviceImage = view.findViewById(R.id.device_image);

        // TODO
        mInstructionText.setText("INSTRUCTION HERE");
        mNotificationText.setText("NOTIFICATIONS HERE");

        mListener.initializeControllerSettingsBackButton();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mListener = (FragmentActionListener) mContext;
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBaseChangedListener(float valueX, float valueY, float valueZ) {

    }

    @Override
    public void onFilterChangedListener(float valueX, float valueY, float valueZ) {

    }
}
