package spring15.ec551.fpgacontroller.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
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
import java.util.Set;
import java.util.UUID;

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.activities.MainActivity;
import spring15.ec551.fpgacontroller.bluetooth.ConnectThread;

/** Sets up the BluetoothController Object for connectivity with Controller */
public class VehicleSettingsFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 100;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private Button On,Off,Visible, listbutton;
    BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView mListedDevice;
    Context mContext;
    ArrayAdapter adapter;
    List<String> list;
    ConnectThread mConnectThread;

    private FragmentActionListener mListener;

    public static VehicleSettingsFragment newInstance() {
        VehicleSettingsFragment fragment = new VehicleSettingsFragment();
        Bundle args = new Bundle();

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

        }
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        list = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vehicle_settings, container, false);

        On = (Button) v.findViewById(R.id.button1);
        Visible = (Button) v.findViewById(R.id.button2);
        listbutton = (Button) v.findViewById(R.id.button3);
        Off = (Button) v.findViewById(R.id.button4);
        On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on();
            }
        });

        Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                off();
            }
        });

        Visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
            }
        });

        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list();
            }
        });


        pairedDevices = mBTAdapter.getBondedDevices();
        mListedDevice = (ListView)v.findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(mContext, R.layout.list_item, list);
        mListedDevice.setAdapter(adapter);
        mListedDevice.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String s = list.get(position);
                 for(BluetoothDevice bt : pairedDevices) {
                     if (s.equals(bt.getName())) {

                         mListener.connectToBTSocket(bt);
                     }
                 }
             }
         });
        mListener.adjustActivityForSettings();

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

    public void checkEnabled() {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void on(){
        if (!mBTAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(mContext,"Turned on"
                    ,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(mContext,"Already on",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void list(){
        pairedDevices = mBTAdapter.getBondedDevices();

        for(BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }

        adapter.notifyDataSetChanged();

        Toast.makeText(mContext, "Showing Paired Devices",
                Toast.LENGTH_SHORT).show();
    }

    public void off(){
        mBTAdapter.disable();
        Toast.makeText(mContext,"Turned off" ,
                Toast.LENGTH_LONG).show();
    }

    public void visible(){
        Intent getVisible = new Intent(BluetoothAdapter.
                ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

}
