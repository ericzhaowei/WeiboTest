package com.eric.test.weibo.weibo;

import android.app.Activity;

import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by Eric on 2017-02-04.
 */

public interface ILoginView {

    void login();

    /**
     *
     * @param name 用户名
     * @param imageUrl 用户头像地址
     * @param statuses_count 微博数
     * @param friends_count 关注数
     * @param followers_count 粉丝数
     */
    void showUser(String name, String imageUrl, int statuses_count, int friends_count, int followers_count);

}
