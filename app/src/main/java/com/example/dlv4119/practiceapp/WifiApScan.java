package com.example.dlv4119.practiceapp;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dlv4119 on 2016/02/19.
 */
public class WifiApScan {

    Context context;
    private List<String> apList = new ArrayList<String>();
    private List<ScanResult> scanResults = null;

    /**
     * コンストラクタ
     * @param context
     */
    public WifiApScan(Context context) {
        this.context = context;
    }

    /**
     * リストの更新
     */
    public void updateList() {
        // リストの更新
//        apList = getScanList();
//        adapter.notifyDataSetChanged();
        Log.d(MainActivity.getTag(), "WiFi リストを更新しました。");
        Log.d("size", "apList.size : " + String.valueOf(apList.size()));
//        Log.d("size", "adapter.count : " + String.valueOf(adapter.getCount()));
    }

    /**
     * スキャン結果からAPリストを作成
     *
     * @return 接続可能なWiFi Direct APリスト　(文字列コレクション)
     */
    public List getScanList(boolean isDirect) {
        String aps;
        apList.clear();
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            apList.add("WiFi  OFF");
            return apList;
        }
        scanResults = wifiManager.getScanResults();
        if (scanResults != null) {
            Log.d(MainActivity.getTag(), "WiFi AP 接続可能数：" + String.valueOf(scanResults.size()));
            for (int i = 0; i < scanResults.size(); i++) {
                String ssid = scanResults.get(i).SSID;
                int frequency = scanResults.get(i).frequency;
                int level = scanResults.get(i).level;
                if (isDirect) {
                    if (scanResults.get(i).SSID.startsWith("DIRECT")){
                        aps = "SSID:" + scanResults.get(i).SSID + "\n"
                                + "チャンネル周波数：" + scanResults.get(i).frequency + "MHz " + "\n"
                                + "信号レベル：" + scanResults.get(i).level + "dBm";
                        apList.add(aps);
                    }
                    if (apList.size() == 0) {
                        apList.add("N/A");
                    }
                } else {
                    // listViewに表示する内容をapListに格納する
                    aps = "SSID:" + ssid + "\n"
                            + "チャンネル周波数：" + frequency + "MHz " + "\n"
                            + "信号レベル：" + level + "dBm";
                    apList.add(aps);
                }
//                // 電波強度が0～-10dBmになった時に通知を送る
//                if (level <= 0 && level >= notifyLevel) {
//                    wifiConnectNotify(ssid, level);
//                }
            }
        }
        else {
            apList.add("N/A");
        }
        return apList;
    }
}
