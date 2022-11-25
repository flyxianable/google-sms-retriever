package com.lyb.googlesmsretriever;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * 设备信息工具类
 */
public class DeviceUtils {

    /**
     * 获取设备唯一识别码
     */
    public static String getDeviceId(Context context) {
        String id = getImei(context);
        if (TextUtils.isEmpty(id) || "000000000000000".equals(id)) {
            id = getMacAddress(context);
        }
        if (TextUtils.isEmpty(id)) {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if (TextUtils.isEmpty(id)) {
            id = Build.SERIAL;
        }
        return id;
    }

    /**
     * 获取imei
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.getImei();
            } else {
                return tm.getDeviceId();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取wifi网络下的的MAC地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        if (TextUtils.isEmpty(macAddress) || "02:00:00:00:00:00".equals(macAddress)) {
            return "";
        } else {
            return macAddress;
        }
    }

    /**
     * 获取sdk版本（int）
     * @return
     */
    public static int getSdkVersion(){
        int versionInt = Build.VERSION.SDK_INT;
        return versionInt;
    }

    /**
     * android sdk是否是14
     * @return
     */
    public static boolean isSdk14(){
        int versionInt = Build.VERSION.SDK_INT;
        if(versionInt == Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            return  true;
        }
        return false;
    }

    /**
     * SDK 19以下
     * @return
     */
    public static boolean isBelowSdk19(){
        int versionInt = Build.VERSION.SDK_INT;
        if(versionInt < Build.VERSION_CODES.KITKAT){
            return  true;
        }
        return false;
    }

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        } else {
            return true;
        }
    }


}
