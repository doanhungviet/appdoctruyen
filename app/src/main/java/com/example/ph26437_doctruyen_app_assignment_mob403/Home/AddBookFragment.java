package com.example.ph26437_doctruyen_app_assignment_mob403.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ph26437_doctruyen_app_assignment_mob403.Home.API.ApiService;
import com.example.ph26437_doctruyen_app_assignment_mob403.Home.Adapter.BookAdapter;
import com.example.ph26437_doctruyen_app_assignment_mob403.Home.Model.Book;
import com.example.ph26437_doctruyen_app_assignment_mob403.MainActivity;
import com.example.ph26437_doctruyen_app_assignment_mob403.R;
import com.example.ph26437_doctruyen_app_assignment_mob403.SharedPreferences.MySharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AddBookFragment extends Fragment {

    private Socket mSocket;

    {
        try {
//            mSocket = IO.socket("http://192.168.56.102:5000");
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {
        }
    }

    private Toolbar toolbar;

    private EditText edtName, edtDescription, edtAuthor, edtYear, edtImgCover, edtImgContent;
    private Button btnAddSubmit;
    private List<Book> booksList = new ArrayList<>();

    private BookAdapter booksAdapter;
    private String idAddBook;

    private RecyclerView recyclerView;

    public AddBookFragment() {

    }

    public static AddBookFragment newInstance(String idBook) {
        AddBookFragment fragment = new AddBookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idBook", idBook);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_truyen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewFromXml(view);
        getDataBundle();
        directToolBar();

        mSocket.connect();
        directBottomNavigationAndToolBar();
        btnAddSubmit.setOnClickListener(view1 -> {
            String bookname = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String author = edtAuthor.getText().toString().trim();
            int publishyear = Integer.parseInt(edtYear.getText().toString());
            String imgbook = edtImgCover.getText().toString().trim();
            String contentString = edtImgContent.getText().toString().trim();
            String[] contentArray = contentString.split(",");
            List<String> contentList = Arrays.asList(contentArray);
            if (bookname.isEmpty() || description.isEmpty() || author.isEmpty() || imgbook.isEmpty() || contentList.isEmpty()) {
                Toast.makeText(getActivity(), "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Book book = new Book(bookname, description, author, publishyear, imgbook, contentList);
                ApiService.apiService.postBook(book).enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        Log.d("addbook", "Mã phản hồi: " + response.code());
                        if (response.isSuccessful()) {
                            mSocket.emit("msgAddBook", "{\"bookname\": \""+book.getBookname()+"\", \"author\": \""+author+"\"}");
                            getListBooks();
                            Toast.makeText(getActivity(), "Thêm truyện thanh cong", Toast.LENGTH_SHORT).show();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToListBookFragment();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    edtName.setText("");
                                    edtDescription.setText("");
                                    edtAuthor.setText("");
                                    edtYear.setText("");
                                    edtImgCover.setText("");
                                    edtImgContent.setText("");
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(getActivity(), "Thêm truyện that bai. Mã lỗi: "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        Toast.makeText(getActivity(), "Lỗi api", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void getListBooks(){
        ApiService.apiService.getListBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                booksList = response.body();
                booksAdapter.setDataBook(booksList);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("listsauadd","lôi",t);
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

    private void navigateToListBookFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_content, ListBook.newInstance(idAddBook))
                .addToBackStack(null)
                .commit();
    }
    private void directToolBar(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(view1 -> {
                getActivity().onBackPressed();
            });
        }
    }

    private void getDataBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idAddBook = bundle.getString("idBook");
        }
    }

    private void getViewFromXml(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar_addbook);
        edtName = view.findViewById(R.id.edtName);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtAuthor = view.findViewById(R.id.edtAuthor);
        edtYear = view.findViewById(R.id.edtYear);
        edtImgCover = view.findViewById(R.id.edtImgCover);
        edtImgContent = view.findViewById(R.id.edtImgContent);
        btnAddSubmit = view.findViewById(R.id.btnAddSubmit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        booksAdapter = new BookAdapter(getActivity());
        recyclerView.setAdapter(booksAdapter);
    }
}