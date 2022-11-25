package com.lyb.googlesmsretriever;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lyb.googlesmsretriever.opt.BaseOptActivity;

public class MainActivity extends BaseOptActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void sendSMs(){

        //发送短信的代码
        //.....

        /**
         * 在发送短信后发起监听
         */
        smsRetriever();
    }

    @Override
    public void onOtpReceived(String otp) {
        if(otp.contains("xxxx")){

            //按照短信格式截取短信内的验证码
        }

    }
}