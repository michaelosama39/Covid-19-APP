package com.example.covid_19app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.covid_19app.R;
import com.example.covid_19app.adapter.CountryAdapter;
import com.example.covid_19app.api.ApiService;
import com.example.covid_19app.api.RetrofitClient;
import com.example.covid_19app.model.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CountryData> list;
    private ProgressDialog dialog;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        recyclerView = findViewById(R.id.countries);
        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        CountryAdapter adapter = new CountryAdapter(this , list);
        recyclerView.setAdapter(adapter);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        apiService = RetrofitClient.getClient();

        apiService.getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                dialog.dismiss();

                Toast.makeText(CountryActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}