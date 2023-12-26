package com.example.ph26437_doctruyen_app_assignment_mob403;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ph26437_doctruyen_app_assignment_mob403.Home.API.ApiService;
import com.example.ph26437_doctruyen_app_assignment_mob403.SharedPreferences.MySharedPreferences;
import com.example.ph26437_doctruyen_app_assignment_mob403.User.Model.User;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private CheckBox myCheckbox;
    private EditText edt_username, edt_passwd;
    private Button btn_login;
    private TextView tv_register;
    private String[] myData;
    private Dialog dialog1, dialog2;
    private Socket mSocket;
    {
        try {
//            mSocket = IO.socket("http://192.168.56.102:5000");
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showBottomDialogLogin();
        mSocket.connect();
        mSocket.on("msgRes", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("NOTIFY", "run: "+args[0]);
                        String data = (String) args[0];
                        postNotify("Booking flexin has a new user",data);
                    }
                });
            }
        });
    }

    private void showBottomDialogLogin() {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.bottom_dialog);

        myData = MySharedPreferences.getStrings(LoginActivity.this);

        edt_username = dialog1.findViewById(R.id.edt_username);
        edt_passwd = dialog1.findViewById(R.id.edt_passwd);
        btn_login = dialog1.findViewById(R.id.btn_login);
        tv_register = dialog1.findViewById(R.id.tv_register);
        myCheckbox = dialog1.findViewById(R.id.myCheckbox);
        myCheckbox.setChecked(Boolean.parseBoolean(myData[2]));
        if (Boolean.parseBoolean(myData[2])) {
            edt_username.setText(myData[0]);
            edt_passwd.setText(myData[1]);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_username.getText().toString().isEmpty() || edt_passwd.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    ApiService.apiService.getUserLogin(edt_username.getText().toString(), edt_passwd.getText().toString()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body().getErr() != null) {
                                Toast.makeText(LoginActivity.this, "" + response.body().getErr(), Toast.LENGTH_SHORT).show();
                            } else {
                                MySharedPreferences.saveStrings(LoginActivity.this, response.body().getUsername(), edt_passwd.getText().toString(), myCheckbox.isChecked() + "");
                                MySharedPreferences.saveAccount(LoginActivity.this, response.body());
                                dialog1.dismiss();
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Lỗi không thể đăng nhập", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        tv_register.setOnClickListener(view -> {
            dialog1.dismiss();
            showBottomDialogRegister();
        });
        dialog1.show();
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showBottomDialogRegister() {
        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.bottom_dialog_register);
        EditText edt_username_res = dialog2.findViewById(R.id.edt_username_res);
        EditText edt_fullname_res = dialog2.findViewById(R.id.edt_fullname_res);
        EditText edt_passwd_res = dialog2.findViewById(R.id.edt_passwd_res);
        EditText edt_passwd_res_again = dialog2.findViewById(R.id.edt_passwd_res_again);
        EditText edt_email_res = dialog2.findViewById(R.id.edt_email_res);
        Button btn_res = dialog2.findViewById(R.id.btn_res);
        TextView tv_login = dialog2.findViewById(R.id.tv_login);
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username_res.getText().toString();
                String fullname = edt_fullname_res.getText().toString();
                String passwd = edt_passwd_res.getText().toString();
                String pass_again = edt_passwd_res_again.getText().toString();
                String email = edt_email_res.getText().toString();
                if (username.isEmpty() || fullname.isEmpty() || passwd.isEmpty() || pass_again.isEmpty() || email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                } else if (!passwd.equals(pass_again)) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(username, passwd, email, fullname);
                    ApiService.apiService.registerUser(user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body().getErr() != null) {
                                Toast.makeText(LoginActivity.this, "" + response.body().getErr(), Toast.LENGTH_SHORT).show();
                            } else {
                                mSocket.emit("msgRes", edt_username_res.getText().toString());
                                Toast.makeText(LoginActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
                                showBottomDialogLogin();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Lỗi đăng ký", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        tv_login.setOnClickListener(view -> {
            dialog2.dismiss();
            showBottomDialogLogin();
        });
        dialog2.show();
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
    }
    void postNotify(String title, String content){
        Notification customNotification = new NotificationCompat.Builder(LoginActivity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(R.drawable.iconbookapp)
                .setContentTitle( title )
                .setContentText(content)
                .setAutoCancel(true)

                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(LoginActivity.this);

        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(LoginActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        notificationManagerCompat.notify(id_notiy , customNotification);

    }
}