package com.gystry.pjetpack.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gystry.libnetworkkt.ApiResponse;
import com.gystry.libnetworkkt.ApiService;
import com.gystry.libnetworkkt.JsonCallback;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.model.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private View actionClose;
    private View actionLogin;
    private Tencent tencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionClose = findViewById(R.id.action_close);
        actionLogin = findViewById(R.id.action_login);

        actionClose.setOnClickListener(this);
        actionLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_close:
                finish();
                break;
            case R.id.action_login:
                login();
                break;
        }
    }

    private void login() {
        if (tencent==null) {
            tencent = Tencent.createInstance("appkey", getApplicationContext());
        }
        tencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //这一步是登陆成功
                JSONObject response = (JSONObject) o;
                try {
                    String openid = response.getString("openid");
                    String access_token = response.getString("access_token");
                    String expires_in = response.getString("expires_in");
                    long expires_time = response.getLong("expires_time");
                    tencent.setAccessToken(access_token,expires_in);
                    tencent.setOpenId(openid);
                    //登陆成功，获取到相应的信息之后继续获取用户信息
                    QQToken qqToken = tencent.getQQToken();
                    getUserInfo(qqToken,expires_time,openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(LoginActivity.this, "登录失败："+uiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "登录取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo(QQToken qqToken,long expires_time,String openid) {
        UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;
                try {
                    String nickname = response.getString("nickname");
                    String figureurl_2 = response.getString("figureurl_2");

                    save(nickname,figureurl_2,openid,expires_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(LoginActivity.this, "登录失败："+uiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "登录取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save(String nickname, String avatar, String openid, long expires_time) {
        ApiService.INSTANCE.<User>get("/user/insert")
                .addParams("name",nickname)
                .addParams("avatar",avatar)
                .addParams("qqOpenId",openid)
                .addParams("expires_time",expires_time)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if (response.getBody()!=null) {
                            UserManager.getInstance().save(response.getBody());
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(ApiResponse<User> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                });
    }
}
