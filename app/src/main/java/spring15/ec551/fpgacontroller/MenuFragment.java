package spring15.ec551.fpgacontroller;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/** TODO
 */
public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView mListView;
    MenuAdapter mListAdapter;
    ArrayList<String> mList;

    Context mContext;

    private static final String NEW_GAME = "NEW GAME";
    private static final String FREE_ROAM = "FREE ROAM";
    private static final String SETTINGS = "SETTINGS";

    private static final String CONTROLLER_SETTINGS = "CONTROLLER SETTINGS";
    private static final String VEHICLE_SETTINGS = "VEHICLE SETTINGS";
    private static final String BACK_TO_MAIN = "BACK";

    private MenuInterfaceListener mListener;

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mList == null) {
            initializeMainMenu();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mListView = (ListView) view.findViewById(R.id.menu_list);
        mListAdapter = new MenuAdapter(mContext, mList);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }

    /** Initializes Main Menu */
    private void initializeMainMenu() {
        mList = new ArrayList<>();
        mList.add(NEW_GAME);
        mList.add(FREE_ROAM);
        mList.add(SETTINGS);
    }

    private void initializeSettingsMenu() {
        mList = new ArrayList<>();
        mList.add(CONTROLLER_SETTINGS);
        mList.add(VEHICLE_SETTINGS);
        mList.add(BACK_TO_MAIN);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

        try {
            mListener = (MenuInterfaceListener) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String menuString = mListAdapter.getItem(position);
        switch (menuString) {
//            case NEW_GAME:
//                // TODO
//                break;
//            case FREE_ROAM:
//                // TODO
//                break;
            case SETTINGS:
                initializeSettingsMenu();
                mListAdapter.updateList(mList);
                break;
            case BACK_TO_MAIN:
                initializeMainMenu();
                mListAdapter.updateList(mList);
                break;
//            default:
//                // TODO
        }
    }
}
