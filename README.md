# ZhiMaExample
Android芝麻信用接入（旧版本）

## 1.添加jar包、权限、manifest配置activity
* zmxySDKALCmini_V1.01450366738902_release.jar放到libs目录下
* 权限
```
<uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
```
* activity配置
```
 <!-- 芝麻信用 -->
        <activity
            android:name="com.android.moblie.zmxy.antgroup.creditsdk.app.SDKActivity"
            android:label="芝麻信用"
            android:screenOrientation="portrait" />
```
>不配置，不会出现H5授权页面

## 2.请求自己的服务端获取sign、params、appId

## 3.开始授权认证
```
private CreditApp creditApp;
private Map<String, String> extParams = new HashMap<>();
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
```
> extParams参数可以放置一些额外的参数，例如当biz_params参数忘记组织auth_code参数时，可以通过extParams参数带入auth_code。
  不过建议auth_code参数组织到biz_params里面进行加密加签。
  extParams.put("auth_code", "M_FACE");没有的请无视这句话，可以传空对象
>> creditApp = null;
   CreditApp.destroy();记得关闭，否则无法重新调用

## 4.注册授权回调
```
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CreditApp.onActivityResult(requestCode, resultCode, data);
    }
```
> 如果不注册 3.ICreditListener无效

## 5.提交成功授权信息给服务端
```
    private void commitInfo(Bundle bundle) {
        String params = bundle.getString("params");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("params", params);
        paramsMap.put("app", "neisha");
        RequestNetData(4, paramsMap, "");
    }
```
## 6.接入完成

# 献上官方的文档地址
* [旧版本](https://b.zmxy.com.cn/technology/openDoc.htm?relInfo=zhima.auth.info.authorize@1.0@1.3) Android部分已经找不到了
* [新版本](https://b.zmxy.com.cn/technology/openDoc.htm?relInfo=CERTIFICATION_QUICK_START)