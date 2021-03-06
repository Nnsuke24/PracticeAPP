package com.example.dlv4119.practiceapp;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private WifiManager wifiManager = null;
    private List<ScanResult> scanResults = null;
    private ArrayAdapter<String> adapter = null;
    private String aps = null;
    private List<String> apList = null;

    private Timer mTimer = null;
    private Handler mHandler = null;
    private UpdateTimerTask mUpdateTimerTask = null;

    WifiApScan wifiApScan;
    private final boolean ISDIRECT = true;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Action", "Direct_onCreate()");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Action", "Direct_onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment1, null);

        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        // 文字列コレクションの取得
        wifiApScan = new WifiApScan(getActivity());
        apList = wifiApScan.getScanList(ISDIRECT);
        // 文字列型アダプターの作成 (文字列コレクションを設定)
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, apList);
        // リストビューオブジェクトを作成する
        android.widget.ListView listView = (android.widget.ListView) view.findViewById(R.id.listView);
        // アダプターを設定します
        listView.setAdapter(adapter);

        mHandler = new Handler();
        mTimer = new Timer();
        mUpdateTimerTask = new UpdateTimerTask();
        // taskオブジェクトのrunメソッドを現在時刻からdelay（ミリ秒）後を開始時点としてperiod（ミリ秒）間隔で実行する
        mTimer.schedule(mUpdateTimerTask, 3000, 3000);
//        mTimer.schedule(mUpdateTimerTask, 3000, 3000);

        return view;
    }

    /**
     * 繰り返しタスクを設定しているクラス
     */
    public class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    // リストの更新
                    apList = wifiApScan.getScanList(ISDIRECT);
                    adapter.notifyDataSetChanged();
                    Log.d(MainActivity.getTag(), "Directリストを更新しました。");
                    Log.d("size", "Direct apList.size : " + String.valueOf(apList.size()));
                    Log.d("size", "Direct adapter.count : " + String.valueOf(adapter.getCount()));
                }
            });
        }
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
        Log.d("Action", "Direct_onDetach()");
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

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Action", "WiFi_onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Action", "WiFi_onStop()");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Action", "WiFi_onDestroyView()");
    }
}
