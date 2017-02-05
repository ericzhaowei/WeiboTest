package com.eric.test.weibo.weibo.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.eric.test.weibo.weibo.AccessTokenKeeper;
import com.eric.test.weibo.weibo.Constant;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;

/**
 * Created by Eric on 2017-02-04.
 */

public class UserApi {

    private Context context;
    private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersApi;
    private static UserApi INSTANCE;

    private UserApi(Context context) {
        this.context = context;
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
        mUsersApi = new UsersAPI(context, Constant.APP_KEY, mAccessToken);
    }

    public static UserApi getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new UserApi(context);
        }
        return INSTANCE;
    }

    public void getUser(Oauth2AccessToken token, RequestListener listener) {
//        mUsersApi = new UsersAPI(context, Constant.APP_KEY, token);
        Long uid = Long.parseLong(mAccessToken.getUid());
        mUsersApi.show(uid, listener);
    }



}
