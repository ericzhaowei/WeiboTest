package com.eric.test.weibo.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.eric.test.weibo.weibo.api.UserApi;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.User;

import org.w3c.dom.Text;

/**
 * Created by Eric on 2017-02-04.
 */

public class LoginPresenter {

    private static final String TAG = "LoginPresenter";

    private Activity activity;
    private SsoHandler mSsoHandler;


    public LoginPresenter(Activity activity) {
        this.activity = activity;
        // 微博授权类对象，保存应用信息
        AuthInfo mAuthInfo = new AuthInfo(activity, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
        this.mSsoHandler = new SsoHandler(activity, mAuthInfo);
    }

    // 检查本地保存的AccessToken
    public void checkExistsAccessToken() {
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(activity);
        Log.i(TAG, "checkExistsAccessToken: token.ExpiresTime = " + token.getExpiresTime());
        Log.i(TAG, "checkExistsAccessToken: currentTimeMills = " + System.currentTimeMillis());

        if(!token.getUid().isEmpty() && token.getExpiresTime() > System.currentTimeMillis()) {
            UserApi.getInstance(activity.getApplicationContext()).getUser(token, userListener);
        }
    }

    public void login() {
        this.mSsoHandler.authorize(new AuthListener());
    }


    public void onAuthorizeCallback(int requestCode, int resultCode, Intent data) {
        if(mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            if(activity instanceof ILoginView) {
                Toast.makeText(activity, "登陆成功", Toast.LENGTH_LONG).show();
                Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if(mAccessToken.isSessionValid()) {
                    Log.i(TAG, "onComplete: uid = " + mAccessToken.getUid());
                    AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
                    UserApi.getInstance(activity.getApplicationContext()).getUser(mAccessToken, userListener);

                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if(activity instanceof ILoginView) {

            }
        }

        @Override
        public void onCancel() {
            if(activity instanceof ILoginView) {

            }
        }
    }

    private RequestListener userListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.i(TAG, "onComplete: " + response);
            User user = User.parse(response);
            if (user != null) {
                ((ILoginView) activity).showUser(user.screen_name, user.avatar_large, user.statuses_count, user.friends_count, user.followers_count);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.i(TAG, "onWeiboException: " + e.getMessage());
        }
    };

}
