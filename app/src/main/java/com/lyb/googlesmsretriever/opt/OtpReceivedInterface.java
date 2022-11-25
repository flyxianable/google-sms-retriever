package com.lyb.googlesmsretriever.opt;

import android.os.Bundle;

public interface OtpReceivedInterface {

//    void onIntentStart(Bundle extras);
    void onOtpReceived(String otp);
    void onOtpTimeout();
}
