package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.accelerometer.ControllerInterfaceListener;
import spring15.ec551.fpgacontroller.activities.MainActivity;
import spring15.ec551.fpgacontroller.resources.CustomTextView;
import spring15.ec551.fpgacontroller.resources.ThrottleSlider;

/**
 * Created by davidkim on 4/9/15.
 * This fragment provides a platform where both vehicle and controller will
 * be able to communicate with each other.  This is used for Free Roam, but should
 * be extended for New Game mode where opponent information and game settings need to be initialized.
 */
public class PlayFragment extends Fragment implements ControllerInterfaceListener{
    final int MAX_AMMO = 100;
    final int RELOAD_DELAY = 3;
    final int HALF_SEC = 500;     // 0.50 second
    final int TENTH_SECOND = 100; // 0.10 second

    Context mContext;
    FragmentActionListener mListener;

    // Throttle
    ThrottleSlider mThrottleSlider;
    CustomTextView mThrottleSpeed;

    // "La-serz"
    LinearLayout mFireHud;
    Button mFireButton;
    CustomTextView mAmmoTextView;
    Timer fireTimer;
    boolean isFireHeldDown;
    int mMaxAmmo;
    int mCurrentAmmo;
    int mReloadDelay;

    DecimalFormat speedDecimalFormat;
    DecimalFormat ammoDecimalFormat;

    public static PlayFragment newInstance() {
        return new PlayFragment();
    }

    public PlayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.mControllerObject.setInterface(this);
        speedDecimalFormat = new DecimalFormat("+###;-###");
        ammoDecimalFormat = new DecimalFormat("000");

        fireTimer = new Timer();
        initializeFireZeLazors(MAX_AMMO, RELOAD_DELAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_roam, container, false);

        mListener.adjustActivityForPlay();

        mThrottleSpeed = (CustomTextView) view.findViewById(R.id.throttle_speed);
        mThrottleSlider = (ThrottleSlider) view.findViewById(R.id.throttle_slider);
        mThrottleSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mThrottleSpeed.setText(speedDecimalFormat.format(progress - ThrottleSlider.MID_PROGRESS) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mFireButton = (Button) view.findViewById(R.id.fire_button);
        mAmmoTextView = (CustomTextView) view.findViewById(R.id.current_ammo);
        mFireHud = (LinearLayout) view.findViewById(R.id.fire_hud);
        mAmmoTextView.setText(
                ammoDecimalFormat.format(mCurrentAmmo) + "/"
                        + ammoDecimalFormat.format(mMaxAmmo));
        mFireButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        startRamboMode();
                        mFireHud.setBackgroundColor(getResources().getColor(R.color.hud_red));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        isFireHeldDown = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        mFireHud.setBackgroundColor(getResources().getColor(R.color.hud_blue));
                        isFireHeldDown = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });


        return view;
    }

    /** Initialize ammo capacity of vehicle */
    private void initializeFireZeLazors(int maxAmmo, int reloadDelay) {
        this.mMaxAmmo = maxAmmo;
        this.mCurrentAmmo = maxAmmo;
        this.mReloadDelay = reloadDelay;
    }

    /** Allows for fire button to be held down */
    private void startRamboMode() {
        shootLaser();
        isFireHeldDown = true;
        fireTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFireHeldDown) {
                            // Returns false if ammo is depleted
                            if (!shootLaser()) {
                                isFireHeldDown = false;
                            };
                        } else if (!isFireHeldDown) {
                            cancel();
                        }
                    }
                });
            }
        }, HALF_SEC, TENTH_SECOND);
    }

    /** Will shoot laser and deplete ammo */
    private boolean shootLaser() {
        if (mCurrentAmmo > 0) {
            mCurrentAmmo -= 1;
            mAmmoTextView.setText(
                    ammoDecimalFormat.format(mCurrentAmmo) + "/"
                            + ammoDecimalFormat.format(mMaxAmmo));
            return true;
        } else {
            return false;
        }
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
