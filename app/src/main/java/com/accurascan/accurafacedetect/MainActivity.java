package com.accurascan.accurafacedetect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.accurascan.facedetection.LivenessCustomization;
import com.accurascan.facedetection.SelfieCameraActivity;
import com.accurascan.facedetection.model.AccuraVerificationResult;
import com.accurascan.facedetection.utils.AccuraLivenessLog;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private TextView tv_liveness,btn_liveness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.im_pick_photo);
        tv_liveness = findViewById(R.id.liveness_text);
        btn_liveness = findViewById(R.id.btn_liveness);

        AccuraLivenessLog.setDEBUG(true);
        AccuraLivenessLog.refreshLogfile(this);

        new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("accurascan.com", session);
            }
        };
    }
    public void openCamera(View view) {

        LivenessCustomization livenessCustomization = new LivenessCustomization();

        livenessCustomization.backGroundColor = 0xFFC4C4C5;
        livenessCustomization.closeIconColor = 0xFF000000;
        livenessCustomization.feedbackBackGroundColor = Color.TRANSPARENT;
        livenessCustomization.feedbackTextColor = Color.BLACK;
        livenessCustomization.feedbackTextSize = 18;
        livenessCustomization.feedBackTopMessage = "Keep Face In Frame \n Face Must Be Near To Camera ";
        livenessCustomization.feedBackframeMessage = "Frame Your Face";
        livenessCustomization.feedBackAwayMessage = "Move Phone Away";
        livenessCustomization.feedBackOpenEyesMessage = "Keep Your Eyes Open";
        livenessCustomization.feedBackCloserMessage = "Move Phone Closer";
        livenessCustomization.feedBackCenterMessage = "Move Phone Center";
        livenessCustomization.feedBackMultipleFaceMessage = "Multiple Face Detected";
        livenessCustomization.feedBackHeadStraightMessage = "Keep Your Head Straight";
        livenessCustomization.feedBackLowLightMessage = "Low light Detected";
        livenessCustomization.feedBackBlurFaceMessage = "Blur Detected Over Face";
        livenessCustomization.feedBackGlareFaceMessage = "Glare Detected over face";
        livenessCustomization.setApiKey("add your api Key");

        livenessCustomization.setLowLightTolerence(-1);
        livenessCustomization.setBlurPercentage(80);
        livenessCustomization.setGlarePercentage(6, 98);

        Intent intent = SelfieCameraActivity.getCustomIntent(this, livenessCustomization, "https://your liveness url");
        startActivityForResult(intent, 100);
        tv_liveness.setText("");
        imageView.setImageBitmap(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            btn_liveness.setText("Retry Liveness");
            if (data != null) {
                AccuraVerificationResult result = data.getParcelableExtra("Accura.liveness");
                if (result == null){
                    return;
                }
                if (result.getStatus().equals("1")) {
                    if (result.getFaceBiometrics() != null) {
                        imageView.setImageBitmap(result.getFaceBiometrics());
                        if (result.getLivenessResult() == null){
                            return;
                        }
                        if(result.getLivenessResult().getLivenessStatus()){
                            String livenessScore = String.format(getString(R.string.score_formate), result.getLivenessResult().getLivenessScore());
                            tv_liveness.setText(String.format(getResources().getString(R.string.score_msg), result.getLivenessResult().getLivenessScore() * 100.0));
//                            Toast.makeText(this, result.getLivenessResult().getLivenessScore()+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, " " + result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
