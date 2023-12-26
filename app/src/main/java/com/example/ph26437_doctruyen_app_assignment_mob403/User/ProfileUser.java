package com.example.ph26437_doctruyen_app_assignment_mob403.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ph26437_doctruyen_app_assignment_mob403.LoginActivity;
import com.example.ph26437_doctruyen_app_assignment_mob403.MainActivity;
import com.example.ph26437_doctruyen_app_assignment_mob403.R;
import com.example.ph26437_doctruyen_app_assignment_mob403.SharedPreferences.MySharedPreferences;
import com.example.ph26437_doctruyen_app_assignment_mob403.User.FragmentProfile.ProfileUserDetail;
import com.example.ph26437_doctruyen_app_assignment_mob403.User.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileUser extends Fragment {
    private TextView tv_name_profile;
    private LinearLayout tv_profile, tv_logout;
    private User myUser;

    public ProfileUser() {
        // Required empty public constructor
    }

    public static ProfileUser newInstance() {
        ProfileUser fragment = new ProfileUser();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_name_profile = view.findViewById(R.id.tv_name_profile);
        tv_profile = view.findViewById(R.id.tv_profile);
        tv_logout = view.findViewById(R.id.tv_logout);
        myUser = MySharedPreferences.getAccount(getActivity());
        tv_name_profile.setText(myUser.getFullname());
        tv_profile.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.layout_content, ProfileUserDetail.newInstance()).addToBackStack(null).commit();
        });
        tv_logout.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc muốn đăng xuất không?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        ShowBottomNavigation();
    }

    private void ShowBottomNavigation() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            if (bottomNavigationView.getVisibility() == View.GONE){
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }
}