package com.example.covid_19app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_19app.R;
import com.example.covid_19app.api.ApiService;
import com.example.covid_19app.api.RetrofitClient;
import com.example.covid_19app.model.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private TextView totalConfirm , totalActive , totalRecovered , totalDeath , totalTests;
    private TextView todayConfirm , todayRecovered , todayDeath , dateTV , countryTV;
    private PieChart pieChart;
    private List<CountryData> list;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        totalTests = findViewById(R.id.totalTests);
        todayConfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);
        pieChart = findViewById(R.id.piechart);
        dateTV = findViewById(R.id.date);
        countryTV = findViewById(R.id.country);

        list = new ArrayList<>();
        apiService = RetrofitClient.getClient();

        apiService.getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for (int i=0; i<list.size(); i++){
                    if (list.get(i).getCountry().equals("Egypt")){
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int deaths = Integer.parseInt(list.get(i).getDeaths());

                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                        totalDeath.setText(NumberFormat.getInstance().format(deaths));

                        todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel("Confirm" , confirm , getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Active" , active , getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel("Recovered" , recovered , getResources().getColor(R.color.green_pie)));
                        pieChart.addPieSlice(new PieModel("Death" , deaths , getResources().getColor(R.color.red_pie)));

                        pieChart.startAnimation();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        countryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this , CountryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setText(String update){
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        Long milliseconds = Long.parseLong(update);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        dateTV.setText("Updated at " + format.format(calendar.getTime()));
    }
}