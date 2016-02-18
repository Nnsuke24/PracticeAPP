package com.example.dlv4119.practiceapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment0.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment0#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment0 extends Fragment {
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
    private List<String> apList = new ArrayList<String>();
    // 通知する基準の信号レベル(dBm)
    private int notifyLevel = -30;
    // 開始時刻からdelay(ミリ秒後)に1回目実行
    private int delay = 3000;
    // スキャン間隔(ミリ秒)
    private int period = 3000;

    private Timer mTimer = null;
    private Handler mHandler = null;
    private UpdateTimerTask mUpdateTimerTask = null;

    public Fragment0() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment0.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment0 newInstance(String param1, String param2) {
        Fragment0 fragment = new Fragment0();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Action", "WiFi_onCreate()");
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Action", "WiFi_onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment0, null);

        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        // 文字列コレクションの取得
        apList = getScanList();
        // 文字列型アダプターの作成 (文字列コレクションを設定)
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, apList);
        // リストビューオブジェクトを作成する
        android.widget.ListView listView = (android.widget.ListView) view.findViewById(R.id.listView);
        // アダプターを設定します
        listView.setAdapter(adapter);

        Log.d("size", "adapterの初期サイズ :" + String.valueOf(adapter.getCount()));

        mHandler = new Handler();
        mTimer = new Timer();
        mUpdateTimerTask = new UpdateTimerTask();
        // taskオブジェクトのrunメソッドを現在時刻からdelay（ミリ秒）後を開始時点としてperiod（ミリ秒）間隔で実行する
        mTimer.schedule(mUpdateTimerTask, delay, period);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * リストの更新
     */
    public void updateList() {
        // リストの更新
        apList = getScanList();
//        adapter.clear();
//        adapter.addAll(apList);
        adapter.notifyDataSetChanged();
        Log.d(MainActivity.getTag(), "WiFi リストを更新しました。");
        Log.d("size", "apList.size : " + String.valueOf(apList.size()));
        Log.d("size", "adapter.count : " + String.valueOf(adapter.getCount()));
    }

    /**
     * スキャン結果からWiFi DirectのみのAPリストを作成
     *
     * @return 接続可能なWiFi Direct APリスト　(文字列コレクション)
     */
    public List getScanList() {
        apList.clear();
        scanResults = wifiManager.getScanResults();
        Log.d(MainActivity.getTag(), "WiFi接続可能数：" + String.valueOf(scanResults.size()));
        for (int i = 0; i < scanResults.size(); i++) {
            String ssid = scanResults.get(i).SSID;
            int frequency = scanResults.get(i).frequency;
            int level = scanResults.get(i).level;
            // listViewに表示する内容をapListに格納する
            aps = "SSID:" + ssid + "\n"
                    + "チャンネル周波数：" + frequency + "MHz " + "\n"
                    + "信号レベル：" + level + "dBm";
            apList.add(aps);
            // 電波強度が0～-10dBmになった時に通知を送る
            if (level <= 0 && level >= notifyLevel) {
                wifiConnectNotify(ssid, level);
            }
        }
        return apList;
    }

    /**
     * 繰り返しタスクを設定しているクラス
     */
    public class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    updateList();
                }
            });
        }
    }

    public void wifiConnectNotify(String ssid, int level) {
        // 通知選択時のインテントの作成
        Intent notificationIntent = new Intent(getActivity(), com.example.dlv4119.practiceapp.MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
        // ビルダー作成
        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("インフォメーション");
        builder.setContentText(ssid + "との電波強度が" + level + "dBmです。");
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(contentIntent);
        // マネージャー作成
        NotificationManager manager= (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // 通知
        manager.notify(1, builder.build());

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
        Log.d("Action", "WiFi_onDetach()");
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
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Action", "WiFi_onDestroyView()");
    }
}
