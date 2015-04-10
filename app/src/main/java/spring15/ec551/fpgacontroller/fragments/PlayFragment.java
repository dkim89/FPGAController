package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.accelerometer.ControllerInterfaceListener;
import spring15.ec551.fpgacontroller.activities.MainActivity;
import spring15.ec551.fpgacontroller.resources.CustomTextView;
import spring15.ec551.fpgacontroller.resources.ThrottleSlider;

/**
 * Created by davidkim on 4/9/15.
 */
public class PlayFragment extends Fragment implements ControllerInterfaceListener{
    Context mContext;
    FragmentActionListener mListener;
    ThrottleSlider mThrottleSlider;
    CustomTextView mThrottleSpeed;

    public static PlayFragment newInstance() {
        return new PlayFragment();
    }

    public PlayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.mControllerObject.setInterface(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_roam, container, false);

        mListener.adjustActivityForPlay();
        mThrottleSpeed = (CustomTextView) view.findViewById(R.id.throttle_speed);

        mThrottleSlider = (ThrottleSlider) view.findViewById(R.id.throttle_slider);
        mThrottleSlider.setMax(200);
        mThrottleSlider.post(new Runnable() {
            @Override
            public void run() {
                mThrottleSlider.setProgress(100);
            }
        });

        mThrottleSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mThrottleSpeed.setText("" + mThrottleSlider.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mListener = (FragmentActionListener) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.mControllerObject != null)
            MainActivity.mControllerObject.registerSensor();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (MainActivity.mControllerObject != null)
            MainActivity.mControllerObject.unregisterSensor();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /** Steering */
    @Override
    public void onBaseChangedListener(float[] baseValues) {

    }

    @Override
    public void onFilterChangedListener(float[] filterValues, float[] netValues) {

    }

    @Override
    public void onAngleChangeListener(int angleValue) {

    }
}
