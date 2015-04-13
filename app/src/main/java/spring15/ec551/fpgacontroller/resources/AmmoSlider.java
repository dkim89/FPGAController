package spring15.ec551.fpgacontroller.resources;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import java.util.Timer;
import java.util.TimerTask;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.activities.MainActivity;
import spring15.ec551.fpgacontroller.fragments.PlayFragment;

/**
 * Created by davidkim on 4/12/15.
 */
public class AmmoSlider extends ProgressBar{
    // Default reload time is set to 3 seconds
    final int MAX_PROGRESS = 60;
    final int INCREMENT = 1;
    final int TIME = 50;       // 0.05 second

    Context mContext;
    Timer mTimer;
    
    public AmmoSlider(Context context) {
        this(context, null);
    }

    public AmmoSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmmoSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTimer = new Timer();
        initializeProgressBar();
    }

    private void initializeProgressBar() {
        setProgressDrawable(getResources().getDrawable(R.drawable.ammo_seekbar));
        setMax(MAX_PROGRESS);
        setProgress(0);
    }

    public void startReloadCountdown() {
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((MainActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getProgress() < MAX_PROGRESS) {
                            setProgress(getProgress() + INCREMENT);
                        } else {
                            cancel();
                            setProgress(0);
                            finishReload();
                        }

                    }
                });
            }
        }, 0, TIME);
    }

    /** This must be modified if using another Fragment other than PlayFragment */
    private void finishReload() {
        PlayFragment fragment = (PlayFragment)((MainActivity) mContext).getFragmentManager().findFragmentByTag(MainActivity.FREE_ROAM_FRAGMENT);
        fragment.finishReloading();
    }


}
