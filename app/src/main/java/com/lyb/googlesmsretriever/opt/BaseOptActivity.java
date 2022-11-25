package com.lyb.googlesmsretriever.opt;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lyb.googlesmsretriever.DeviceUtils;

public class BaseOptActivity extends Activity implements OtpReceivedInterface{

    protected SmsBroadcastReceiver mSmsBroadcastReceiver;
    protected boolean isSmsReceiverRegistered;

    private static final int SMS_CONSENT_REQUEST = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSmsReceiver();

    }

    private void initSmsReceiver(){
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        mSmsBroadcastReceiver.setOnOtpListeners(BaseOptActivity.this);
    }

    private void registerSmsReceiver(){
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(mSmsBroadcastReceiver, intentFilter,  SmsRetriever.SEND_PERMISSION,null ); //SmsRetriever.SEND_PERMISSION
        isSmsReceiverRegistered = true;
    }

    private void unRegisterSmsReceiver(){
        if(mSmsBroadcastReceiver != null && isSmsReceiverRegistered){
            unregisterReceiver(mSmsBroadcastReceiver);
            isSmsReceiverRegistered = false;
        }
    }

    /**
     * 监听短信，需要在发送短信后发起
     */
    protected void smsRetriever(){
        //没有google服务的手机，不执行监听
        if(!DeviceUtils.checkPlayServices(this)){
            return;
        }
        Task<Void> task = SmsRetriever.getClient(this).startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                //如果为空，初始化
                if(mSmsBroadcastReceiver == null){
                    initSmsReceiver();
                }
                //先反注册
                unRegisterSmsReceiver();
                //广播注册
                registerSmsReceiver();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterSmsReceiver();

    }

//    @Override
//    public void onIntentStart(Bundle extras) {
//        // Get consent intent
//        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
//        try {
//            // Start activity to show consent dialog to user, activity must be started in
//            // 5 minutes, otherwise you'll receive another TIMEOUT intent
//            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
//        } catch (ActivityNotFoundException e) {
//            // Handle the exception ...
//        }
//    }

    @Override
    public void onOtpReceived(String otp) {
        if(otp.contains("xxxx")){
            int startIndx = otp.indexOf("[");
            int endIndex = otp.indexOf("]");
            if(startIndx < 0 || endIndex < 0){
                startIndx = otp.indexOf("<");
                endIndex = otp.indexOf(">");
            }
            if(startIndx > 0 && endIndex > 0) {
                String code = otp.substring(startIndx + 1, endIndex);
                onReceiveMsgCode(code);
            }
        }

    }

    @Override
    public void onOtpTimeout() {
        unRegisterSmsReceiver();
    }

    protected  void onReceiveMsgCode(String msgCode){ }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case SMS_CONSENT_REQUEST:
//                if (resultCode == RESULT_OK) {
//                    // Get SMS message content
//                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                    Log.v("opt", "message = " + message);
//                    // Extract one-time code from the message and complete verification
//                    // `sms` contains the entire text of the SMS message, so you will need
//                    // to parse the string.
//
//                    // send one time code to the server
//                } else {
//                    // Consent canceled, handle the error ...
//                }
//                break;
//        }
//    }
}