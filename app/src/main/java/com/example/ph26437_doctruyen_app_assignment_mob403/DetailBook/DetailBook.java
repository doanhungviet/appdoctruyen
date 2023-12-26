package com.example.ph26437_doctruyen_app_assignment_mob403.DetailBook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ph26437_doctruyen_app_assignment_mob403.Comment.Comments;
import com.example.ph26437_doctruyen_app_assignment_mob403.Comment.PostComments;
import com.example.ph26437_doctruyen_app_assignment_mob403.DetailBook.CommentsAdapter.CommentsAdapter;
import com.example.ph26437_doctruyen_app_assignment_mob403.Home.API.ApiService;
import com.example.ph26437_doctruyen_app_assignment_mob403.Home.Model.Book;
import com.example.ph26437_doctruyen_app_assignment_mob403.MainActivity;
import com.example.ph26437_doctruyen_app_assignment_mob403.R;
import com.example.ph26437_doctruyen_app_assignment_mob403.ReadBook.ReadBook;
import com.example.ph26437_doctruyen_app_assignment_mob403.SharedPreferences.MySharedPreferences;
import com.example.ph26437_doctruyen_app_assignment_mob403.User.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBook extends Fragment {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private CommentsAdapter commentsAdapter;
    private List<Comments> commentsList = new ArrayList<>();
    private Button btn_read, btn_comment;
    private ImageView img_imgbook;
    private TextView tv_namebook_detail, tv_author_detail, tv_publishyear_detail, tv_description_detail;
    private EditText edt_comment;
    private Book book;
    private String idGetBook;
    private User myUser = new User();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private Socket mSocket;
    {
        try {
//            mSocket = IO.socket("http://192.168.56.102:5000");
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {
        }
    }

    public DetailBook() {
        // Required empty public constructor
    }

    public static DetailBook newInstance(String idBook) {
        DetailBook fragment = new DetailBook();
        Bundle bundle = new Bundle();
        bundle.putString("idBook", idBook);
        fragment.setArguments(bundle);
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
        return inflater.inflate(R.layout.fragment_detail_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewFromXml(view);
        getDataBundle();
        getDetailBook();
        getListComments();
        mSocket.connect();

        myUser = MySharedPreferences.getAccount(getActivity());

        commentsAdapter = new CommentsAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentsAdapter);

        directBottomNavigationAndToolBar();

        btn_read.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.layout_content, ReadBook.newInstance(idGetBook)).addToBackStack(null).commit();
        });
        btn_comment.setOnClickListener(view1 -> {
            String content = edt_comment.getText().toString();
            Date date = new Date();
            PostComments postComments = new PostComments(idGetBook, myUser.get_id(), content, dateFormat.format(date));
            ApiService.apiService.sendComment(postComments).enqueue(new Callback<PostComments>() {
                @Override
                public void onResponse(Call<PostComments> call, Response<PostComments> response) {
                    if (response.isSuccessful()){
                        mSocket.emit("msgCmt", "{\"bookname\": \""+book.getBookname()+"\", \"content\": \""+content+"\"}");
                        getListComments();
                        Toast.makeText(getActivity(), "Binh luan thanh cong", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Binh luan that bai", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PostComments> call, Throwable t) {
                    Toast.makeText(getActivity(), "Binh luan that bai", Toast.LENGTH_SHORT).show();
                }
            });
            edt_comment.setText("");
        });

    }

    private void getViewFromXml(View view) {
        recyclerView = view.findViewById(R.id.recycler_comments);
        toolbar = view.findViewById(R.id.toolbar_detailbook);
        btn_read = view.findViewById(R.id.btn_read);
        img_imgbook = view.findViewById(R.id.img_imgbook);
        tv_namebook_detail = view.findViewById(R.id.tv_namebook_detail);
        tv_author_detail = view.findViewById(R.id.tv_author_detail);
        tv_publishyear_detail = view.findViewById(R.id.tv_publishyear_detail);
        tv_description_detail = view.findViewById(R.id.tv_description_detail);
        edt_comment = view.findViewById(R.id.edt_comment);
        btn_comment = view.findViewById(R.id.btn_comment);
    }

    private void setViewForXml() {
        tv_namebook_detail.setText(book.getBookname());
        tv_publishyear_detail.setText(book.getPublishyear() + "");
        tv_author_detail.setText(book.getAuthor());
        tv_description_detail.setText(book.getDescription());
        //load image book
        Picasso.get().load(book.getImgbook()).placeholder(R.drawable.imageload).error(R.drawable.imageload).into(img_imgbook);
    }

    private void getDataBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idGetBook = bundle.getString("idBook");
        }
    }

    private void getDetailBook() {
        ApiService.apiService.getDetailBook(idGetBook).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                book = response.body();
                setViewForXml();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(getActivity(), "Loi khong the tai truyen", Toast.LENGTH_SHORT).show();
            }
        });
        //        Log.d("TAGcomment", "getDataBook: " + commentsList.toArray().length);
    }

    private void getListComments() {
        ApiService.apiService.getListComments(idGetBook).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                commentsList = response.body();
                commentsAdapter.setDataComments(commentsList);
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {

            }
        });
    }

    private void directBottomNavigationAndToolBar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            bottomNavigationView.setVisibility(View.GONE);
            mainActivity.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(view1 -> {
                getActivity().onBackPressed();
            });
        }
    }
}