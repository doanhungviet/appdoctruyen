package com.example.ph26437_doctruyen_app_assignment_mob403;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ph26437_doctruyen_app_assignment_mob403.Home.ListBook;
import com.example.ph26437_doctruyen_app_assignment_mob403.User.ProfileUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private GifImageView gifImageView;
    private Socket mSocket;
    {
        try {
//            mSocket = IO.socket("http://192.168.56.102:5000");
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifImageView = findViewById(R.id.gif_loading);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        mSocket.connect();
        mSocket.on("msgCmt", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        postNotify("Booking flexin has a new comment",data);
                    }
                });
            }
        });

        replaceFragment(ListBook.newInstance());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_listbook) {
                    replaceFragment(ListBook.newInstance());
                    return true;
                } else if (item.getItemId() == R.id.bottom_user) {
                    replaceFragment(ProfileUser.newInstance());
                    return true;
                }
                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setLoading(boolean check) {
        if (!check) {
            gifImageView.setVisibility(View.GONE);
        } else {
            gifImageView.setVisibility(View.VISIBLE);
        }

    }
    void postNotify(String title, String content){
        Notification customNotification = new NotificationCompat.Builder(MainActivity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(R.drawable.iconbookapp)
                .setContentTitle( title )
                .setContentText(content)
                .setAutoCancel(true)

                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(MainActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        notificationManagerCompat.notify(id_notiy , customNotification);

    }
}