package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.accelerometer.ControllerInterfaceListener;
import spring15.ec551.fpgacontroller.accelerometer.ControllerObject;
import spring15.ec551.fpgacontroller.resources.CustomTextView;
import spring15.ec551.fpgacontroller.resources.UserConfigurationObject;

/**
 * Created by davidkim on 3/28/15.
 */
public class CalibrateControllerFragment extends Fragment implements ControllerInterfaceListener{

    Context mContext;
    private FragmentActionListener mListener;

    CustomTextView mInstructionText, mNotificationText, mCalibrationButton, mSaveButton;
    ImageView mBaseLine;
    ImageView mDeviceImage;
    ImageView mExampleImage;

    float mFilteredValues[];
    float mNetValues[];
    boolean mTimerCheck;
    float mAngle;
    private static final int FIVE_SECONDS = 5000; // Milliseconds

    private ControllerObject mController;
    UserConfigurationObject mConfigObject = null;

    public static CalibrateControllerFragment newInstance(UserConfigurationObject object) {
        CalibrateControllerFragment fragment = new CalibrateControllerFragment();
        Bundle args = new Bundle();
        args.putParcelable(UserConfigurationObject.USER_SAVED_CONFIG, object);
        fragment.setArguments(args);
        return fragment;
    }

    public CalibrateControllerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFilteredValues = new float[]{0.0f,0.0f,0.0f};
        mNetValues = new float[]{0.0f, 0.0f, 0.0f};
        mAngle = 0.0f;
        if (getArguments() != null) {
            mConfigObject = getArguments().getParcelable(UserConfigurationObject.USER_SAVED_CONFIG);
        }

        if (mConfigObject != null) {
            mController = new ControllerObject(mContext, this, mConfigObject);
        } else {

            mController = new ControllerObject(mContext, this);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_config_controller, container, false);

        mInstructionText = (CustomTextView) view.findViewById(R.id.controller_instruc_textview);
        mNotificationText = (CustomTextView) view.findViewById(R.id.controller_notify_textview);
        mBaseLine = (ImageView) view.findViewById(R.id.controller_instruc_baseline);
        mDeviceImage = (ImageView) view.findViewById(R.id.device_image);
        mCalibrationButton = (CustomTextView) view.findViewById(R.id.controller_button);

        mListener.exitMainMenuFragment();

        /* If configured, display current settings; otherwise initialize config procedure */
        if (mConfigObject == null) {
            beginConfiguration();
        }

        return view;
    }

    /* Begin Configuration */
    private void beginConfiguration() {
        mInstructionText.setText("Please position your device as shown in the picture.\n" +
                "The screen should be facing you with the device in landscape position.");
        mNotificationText.setText("Once in position, press begin.");
        mCalibrationButton.setText("Begin");

        mCalibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.resetNetValues();
//                measureValue(MEASURE_BASE, false);
            }
        });
    }

    private void completeConfiguration() {
        mInstructionText.setText("Device has been configured.");
        mNotificationText.setText("");
        mCalibrationButton.setText("Redo");
        mCalibrationButton.setVisibility(View.VISIBLE);
    }

    private void measureValue(String tag, boolean retry) {
//        mCalibrationButton.setVisibility(View.INVISIBLE);
        /**
        mTimerCheck = true;
        boolean isSuccessful = true;

        // Runs for 3 seconds, then expires the timer
        final Handler handler = new Handler(mContext.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTimerCheck = false;
            }
        }, FIVE_SECONDS);

        switch (tag) {
            // Runs a time elapsed check of filtered X,Y values. Filtered values should be between -.5 to .5.
            case MEASURE_BASE:
                mDeviceImage.setRotation(0.0f);
                mInstructionText.setText("Continue to hold position");
                if (!retry) {
                    mNotificationText.setText("Measuring...");
                } else {
                    mNotificationText.setText("Not steady enough.  Hold steady...");
                }
                while (mTimerCheck) {
                    if (mFilteredValues[X] > 0.5f || mFilteredValues[Y] > 0.5f
                            || mFilteredValues[X] < -0.5f || mFilteredValues[Y] < -0.5f) {
                        isSuccessful = false;
                    }
                }

                if (isSuccessful) {
                    mController.resetNetValues();
                    measureValue(MEASURE_LEFT, false);
                } else {
                    measureValue(MEASURE_BASE, true);
                }
                break;
            case MEASURE_LEFT:
                mDeviceImage.setRotation(90.0f);
                mInstructionText.setText("Tilt your device to the left as shown");
                mNotificationText.setText("Calibrating left bound...");
                while(mTimerCheck) {}
                mController.setLeftBound(new int[]{(int)mNetValues[X], (int)mNetValues[Y]});
                measureValue(MEASURE_RIGHT, false);
                break;
            case MEASURE_RIGHT:
                mDeviceImage.setRotation(-90.0f);
                mInstructionText.setText("Tilt your device to the right as shown");
                mNotificationText.setText("Calibrating right bound...");
                while(mTimerCheck) {}
                mController.setRightRound(new int[]{(int)mNetValues[X], (int)mNetValues[Y]});
                completeConfiguration();
                break;
        }
        /* Runs a time elapsed check of filtered X,Y values. Filtered values should be between -.5 to .5. */

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
        // Not relevant for this fragment as we only need net value
    }

    @Override
    public void onFilterChangedListener(float[] filterValues, float[] netValues) {
        mFilteredValues = filterValues;
        mNetValues = netValues;
    }

    @Override
    public void onAngleChangeListener(int angleValue) {
        mDeviceImage.setRotation(angleValue);
        mNotificationText.setText("" + angleValue);
    }
}
