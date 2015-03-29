package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.accelerometer.ControllerInterfaceListener;
import spring15.ec551.fpgacontroller.accelerometer.ControllerObject;
import spring15.ec551.fpgacontroller.resources.CustomTextView;
import spring15.ec551.fpgacontroller.resources.UserConfigurationObject;

/**
 * Created by davidkim on 3/28/15.
 */
public class ControllerSettingsFragment extends Fragment implements ControllerInterfaceListener{
    private final int X = 0;
    private final int Y = 1;
    private final int Z = 2;

    private final String BEGIN_BUTTON = "BEGIN";

    Context mContext;
    private FragmentActionListener mListener;
    float mBaseValues[];

    Button mResetCalibration;
    CustomTextView mInstructionText, mNotificationText, mCalibrationButton;
    ImageView mBaseLine;
    ImageView mDeviceImage;
    Matrix mMatrix;

    private ControllerObject mController;
    UserConfigurationObject mConfigObject = null;



    public static ControllerSettingsFragment newInstance(UserConfigurationObject object) {
        ControllerSettingsFragment fragment = new ControllerSettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable(UserConfigurationObject.USER_SAVED_CONFIG, object);
        fragment.setArguments(args);
        return fragment;
    }

    public ControllerSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mConfigObject = savedInstanceState.getParcelable(UserConfigurationObject.USER_SAVED_CONFIG);
        }

        if (mConfigObject != null) {
            mController = new ControllerObject(mContext, this, mConfigObject);
        } else {
            mBaseValues = new float[]{0.0f,0.0f,0.0f};
            mController = new ControllerObject(mContext, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller_settings, container, false);

        mInstructionText = (CustomTextView) view.findViewById(R.id.controller_instruc_textview);
        mNotificationText = (CustomTextView) view.findViewById(R.id.controller_notify_textview);
        mBaseLine = (ImageView) view.findViewById(R.id.controller_instruc_baseline);
        mDeviceImage = (ImageView) view.findViewById(R.id.device_image);
        mCalibrationButton = (CustomTextView) view.findViewById(R.id.controller_button);

//        mMatrix = new Matrix();
//        mDeviceImage.setScaleType(ImageView.ScaleType.MATRIX);
//        mMatrix.postRotate()

        mListener.initializeControllerSettingsBackButton();

        /* If configured, display current settings; otherwise initialize config procedure */
        if (mConfigObject == null) {
            mInstructionText.setText("Please position your device as shown in the picture.\n" +
            "The screen should be facing you with the device in landscape position.");
            mNotificationText.setText("Once in position, press begin.");
        }

        return view;
    }

    private void beginConfiguration() {
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
        // TODO save values of user config object
    }

    @Override
    public void onBaseChangedListener(float[] baseValues) {

    }

    @Override
    public void onFilterChangedListener(float[] filterValues, float[] netValues) {

    }

    @Override
    public void onAngleChangeListener(float angleValue) {

    }
}
