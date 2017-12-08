package com.neisha.zhimaexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String appId;
    private String params, sign;
    private String userName;//真实姓名
    private String idNumber;//身份证号
    private Map<String, Object> userMap = new HashMap<>();
    private Map<String, String> extParams = new HashMap<>();
    private CreditApp creditApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //1.请求服务端，获取发起芝麻信用授权参数：params，sign
    //appsdk  移动端传appsdk  否则授权页不适配屏幕
    private void chechkIsAuthorizate() {
        userMap.put("identity_name", userName);
        userMap.put("identity_id_card", idNumber);
        userMap.put("channelType", "appsdk");
        RequestNetData(1, userMap, "");
    }

    //2.开始芝麻信用认证，一定要配置,还有权限
//     <activity
//          android:name="com.android.moblie.zmxy.antgroup.creditsdk.app.SDKActivity"
//          android:label="芝麻信用"
//          android:screenOrientation="portrait" />
    private void startAuthorizate() {
        creditApp = CreditApp.getOrCreateInstance(getApplicationContext());
        creditApp.authenticate(this, appId, null, params, sign, extParams, new ICreditListener() {
            @Override
            public void onComplete(Bundle bundle) {
                creditApp = null;
                CreditApp.destroy();
                //4.提交成功授权信息给服务端
                commitInfo(bundle);
            }

            @Override
            public void onError(Bundle bundle) {
                creditApp = null;
                CreditApp.destroy();
            }

            @Override
            public void onCancel() {
                creditApp = null;
                CreditApp.destroy();
            }
        });
    }

    //3.注册授权结果回调，否则ICreditListener不回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CreditApp.onActivityResult(requestCode, resultCode, data);
    }

    //4.提交成功授权信息给服务端
    private void commitInfo(Bundle bundle) {
        String params = bundle.getString("params");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("params", params);
        paramsMap.put("app", "neisha");
        RequestNetData(4, paramsMap, "");
    }


    /**
     * 请求数据
     *
     * @param what
     * @param params
     * @param url
     */
    private void RequestNetData(int what, Map<String, Object> params, String url) {
        onSuccess(what, new JSONObject());
    }

    /**
     * 请求成功回调
     *
     * @param what
     * @param jsonObject
     */
    private void onSuccess(int what, JSONObject jsonObject) {
        switch (what) {
            case 1:
                //获取params，sign
                sign = jsonObject.optString("sign");
                params = jsonObject.optString("params");
                appId = jsonObject.optString("appId");
                //开始信用授权
                startAuthorizate();
                break;
            case 4:
                //提交成功授权信息给服务端
                //授权完成
                finish();
                break;
        }
    }
}
