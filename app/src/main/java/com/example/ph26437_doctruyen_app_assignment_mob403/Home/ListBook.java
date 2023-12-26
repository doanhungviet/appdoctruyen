package com.example.ph26437_doctruyen_app_assignment_mob403.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ph26437_doctruyen_app_assignment_mob403.Home.API.ApiService;
import com.example.ph26437_doctruyen_app_assignment_mob403.Home.Adapter.BookAdapter;
import com.example.ph26437_doctruyen_app_assignment_mob403.Home.Model.Book;
import com.example.ph26437_doctruyen_app_assignment_mob403.MainActivity;
import com.example.ph26437_doctruyen_app_assignment_mob403.R;
import com.example.ph26437_doctruyen_app_assignment_mob403.TransitionAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListBook#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ListBook extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private List<Book> bookList = new ArrayList<>();
    private BookAdapter bookAdapter;
    private MainActivity mainActivity;
    private Toolbar toolbar;
    private TextView tv_search_toolbar;
    private SearchView searchViewCustom;

    private Button btn_addbook;
    private String idGetBook;

    public ListBook() {
    }

    public static ListBook newInstance() {
        ListBook fragment = new ListBook();
        return fragment;
    }

    public static ListBook newInstance(String idBook) {
        ListBook fragment = new ListBook();
        Bundle bundle = new Bundle();
        bundle.putString("idBook", idBook);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_listbook);
        btn_addbook = view.findViewById(R.id.btn_addbook);
        toolbar = view.findViewById(R.id.toolbar);
        getDataBundle();
//        searchView = view.findViewById(R.id.search_view);


        btn_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_content, AddBookFragment.newInstance(idGetBook))
                        .addToBackStack(null)
                        .commit();
            }
        });


        layoutManager = new GridLayoutManager(getContext(), 2);

        bookAdapter = new BookAdapter(getActivity());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookAdapter);

        callApiGetDataBook();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            recyclerView.setOnTouchListener(new TransitionAnimation(getActivity(), bottomNavigationView));
            mainActivity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.search_toolbar);
        View searchView = LayoutInflater.from(getContext()).inflate(R.layout.custom_searchview, null);
        searchItem.setActionView(searchView);
        tv_search_toolbar = searchView.findViewById(R.id.tv_search_toolbar);
        searchViewCustom = searchView.findViewById(R.id.searchViewCustom);
        tv_search_toolbar.setOnClickListener(view -> {
            ApiService.apiService.getListBooksFilter(searchViewCustom.getQuery().toString()).enqueue(new Callback<List<Book>>() {
                @Override
                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                    if (response.isSuccessful()) {
                        bookList = response.body();
                        bookAdapter.setDataBook(bookList);
                    } else {
                        Toast.makeText(getActivity(), "Lỗi không lấy được danh sách truyện", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Book>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi không lấy được danh sách truyện", Toast.LENGTH_SHORT).show();
                }
            });
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void callApiGetDataBook() {
        ApiService.apiService.getListBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    mainActivity.setLoading(false);
                    bookList = response.body();
                    bookAdapter.setDataBook(bookList);
                } else {
                    Toast.makeText(getActivity(), "Lỗi không lấy được danh sách truyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                mainActivity.setLoading(true);
                Toast.makeText(getActivity(), "Lỗi không lấy được danh sách truyện", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idGetBook = bundle.getString("idBook");
        }
    }

}
