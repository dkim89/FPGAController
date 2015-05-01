package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.activities.MainActivity;

/** Sets up the BluetoothController Object for connectivity with Controller */
public class VehicleSettingsFragment extends Fragment {
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    Context mContext;
    Button On,Off,Visible, listPairedDevices;
    ListView mListView;
    ArrayAdapter mListAdapter;
    List<String> mListOfDevices;
    ArrayList<BluetoothDevice> pairedDevices;

    private FragmentActionListener mListener;

    public static VehicleSettingsFragment newInstance() {
        VehicleSettingsFragment fragment = new VehicleSettingsFragment();
        Bundle args = new Bundle();
        // Add arguments here
        fragment.setArguments(args);
        return fragment;
    }

    public VehicleSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Get arguments
        }

        mListOfDevices = new ArrayList<>();
        MainActivity.BluetoothStaticObject.registerReceivers();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vehicle_settings, container, false);

        On = (Button) v.findViewById(R.id.button1);
        Visible = (Button) v.findViewById(R.id.button2);
        listPairedDevices = (Button) v.findViewById(R.id.button3);
        Off = (Button) v.findViewById(R.id.button4);
        mListView = (ListView)v.findViewById(R.id.listView1);
        mListAdapter = new ArrayAdapter<>(mContext, R.layout.list_item, mListOfDevices);
        mListView.setAdapter(mListAdapter);
        mListener.adjustActivityForSettings();

        On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.BluetoothStaticObject.checkEnabled();
            }
        });

        Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.BluetoothStaticObject.disableBluetooth();
            }
        });

        Visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.BluetoothStaticObject.becomeDiscoverable();
            }
        });

        listPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevices =
                        MainActivity.BluetoothStaticObject.queryPairedDevices();

                for(BluetoothDevice bt : pairedDevices) {
                    mListOfDevices.add(bt.getName());
                }

                mListAdapter.notifyDataSetChanged();

                Toast.makeText(mContext, "Showing Paired Devices",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s = mListOfDevices.get(position);
                        for (BluetoothDevice bt : pairedDevices) {
                            if (s.equals(bt.getName())) {
                                MainActivity.BluetoothStaticObject.connectToBTAsClient(bt, uuid);
                            }
                        }
                    }
                });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mListener = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.BluetoothStaticObject.unregisterReceivers();
    }

}
