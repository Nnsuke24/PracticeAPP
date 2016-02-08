package com.example.dlv4119.practiceapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    final String TAG = "Result";

    private ListView mListView;
    private ArrayList<String> mScanList;
    private ArrayAdapter<String> mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private String mResult = "";

    // ブロードキャストレシーバーの操作
    private BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                // Bluetoothデバイスがインテントを受け取ったときの操作
                // Bluetoothオブジェクトを取得
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // RSSI値読み出し
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                mScanList = new ArrayList<String>();
                if (mResult != null) {
                    mScanList.add(mResult);
                }
                mResult = "";
                mResult += "Name : " + device.getName() + "\n"
                        + "Device Class : " + device.getBluetoothClass().getDeviceClass() + "\n"
                        + "MAC Address : " + device.getAddress() + "\n"
                        + "State : " + getBondState(device.getBondState()) + "\n"
                        + "RSSI : " + String.valueOf(rssi);
                Log.d(TAG, "デバイス名を取得しました。");
                Log.d(TAG, "デバイスクラスを取得しました。");
                Log.d(TAG, "MACアドレスを取得しました。");
                Log.d(TAG, "接続状態を取得しました。");
                Log.d(TAG, "電波強度(RSSI)を取得しました。");
                mScanList.add(mResult);

                mAdapter.clear();
                mAdapter.addAll(mScanList);
                mAdapter.notifyDataSetChanged();
//                mListView.invalidateViews();
            }
        }
    };

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment2, null);

        // BLE�ɑΉ����Ă��邩�ǂ����𔻒f���Ă���
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // BLE�����p�o���Ȃ��[���������ꍇ�̏������L�q
            Log.d("Available ble", "���̒[����BLE�ɑΉ����Ă��܂���B");
        }

//        mListView = (ListView) getActivity().findViewById(R.id.BtListView);
        //リストビューオブジェクトを作成する
        android.widget.ListView mListView = (android.widget.ListView) view.findViewById(R.id.BtListView);
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mBluetoothSearchReceiver, bluetoothFilter);
        mBluetoothAdapter = mBluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery(); //開始

        return view;
    }

    /**
     * 接続状態を判断し、返す
     *
     * @param state
     * @return 接続状態
     */
    String getBondState(int state) {
        String strState;
        switch (state) {
            case BluetoothDevice.BOND_BONDED:
                strState = "接続履歴あり";
                break;
            case BluetoothDevice.BOND_BONDING:
                strState = "接続中";
                break;
            case BluetoothDevice.BOND_NONE:
                strState = "接続履歴なし";
                break;
            default:
                strState = "エラー";
        }
        return strState;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
