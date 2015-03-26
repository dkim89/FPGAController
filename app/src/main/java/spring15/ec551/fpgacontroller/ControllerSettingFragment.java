package spring15.ec551.fpgacontroller;


import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControllerSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerSettingFragment extends Fragment implements KeyboardView.OnKeyboardActionListener {
    public static final int JOYSTICK_UP = 0;
    public static final int JOYSTICK_DOWN = 1;
    public static final int JOYSTICK_LEFT = 2;
    public static final int JOYSTICK_RIGHT = 3;

    public static final int BUMPER_LEFT = 4;
    public static final int BUMPER_RIGHT = 5;
    public static final int BUTTON_A = 6;

    Context mContext;
    CustomTextView mSubtitle;
    CustomTextView mButton;
    InputMethodService mInputMethodService;
    Map<Integer,Integer> mKeyMapping;
    int keyValue = 0;


    // TODO: Rename and change types and number of parameters
    public static ControllerSettingFragment newInstance() {
        ControllerSettingFragment fragment = new ControllerSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControllerSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInputMethodService = new InputMethodService();
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

//        try {
//            mListener = (ExampleFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    public void startMapping() {
        mSubtitle.setText("Please press button for A");
    }


    @Override
    public void onPress(int primaryCode) {
        mKeyMapping.put(keyValue,primaryCode);
        mButton.setText("Binded " + primaryCode);
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        mKeyMapping.put(keyValue,primaryCode);
        mButton.setText("Binded " + primaryCode);
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
