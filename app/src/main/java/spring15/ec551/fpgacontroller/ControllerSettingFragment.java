package spring15.ec551.fpgacontroller;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControllerSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerSettingFragment extends Fragment {
    Context mContext;
    CustomTextView mSubtitle;
    CustomTextView mButton;

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


}
