package com.eric.test.weibo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eric.test.weibo.weibo.ILoginView;
import com.eric.test.weibo.weibo.LoginPresenter;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements ILoginView {

    private LoginPresenter presenter;
    private TextView name;
    private ImageView image;
    private TextView statuses, friends, fans;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new LoginPresenter(this);

        name = (TextView) findViewById(R.id.textView_name);
        image = (ImageView) findViewById(R.id.imageView);
        statuses = (TextView) findViewById(R.id.statues_count);
        friends = (TextView) findViewById(R.id.friends_count);
        fans = (TextView) findViewById(R.id.fans_count);
        login = (Button) findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        checkExistUser();

    }


    public void checkExistUser() {
        presenter.checkExistsAccessToken();
    }

    @Override
    public void login() {
        presenter.login();
    }

    @Override
    public void showUser(String name, String imageUrl, int statuses_count, int friends_count, int followers_count) {
        this.name.setText(name);
        Glide.with(this).load(imageUrl).into(image);
        this.statuses.setText(String.valueOf(statuses_count));
        this.friends.setText(String.valueOf(friends_count));
        this.fans.setText(String.valueOf(followers_count));
        this.login.setVisibility(View.GONE);
        findViewById(R.id.textview_weibo).setVisibility(View.VISIBLE);
        findViewById(R.id.textview_fav).setVisibility(View.VISIBLE);
        findViewById(R.id.textview_fans).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(presenter != null) {
            presenter.onAuthorizeCallback(requestCode, resultCode, data);
        }
    }
}
