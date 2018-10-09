package com.example.nobody.weather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.nobody.weather.weather.OpenWeather;
import com.example.nobody.weather.weather.WeatherRequest;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TableLayout table_layout;

    private OpenWeather openWeather;

    Set<String> cities = new HashSet<String>();

    int days_forecast = 1;

    private Button button_add_city;
    private EditText editText_city;

    private Button button_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        loadPreferences(sharedPref);

        addTableHeader(days_forecast);

        if (cities != null) {
            for(String s : cities) {
                addCity(s, days_forecast);
                setWeather(s);
            }
        }

        button_add_city = findViewById(R.id.button2);
        editText_city = findViewById(R.id.editText2);

        button_add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText_city.getText().toString();
                String city = "";

                if (s.length() > 1) {
                    city = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
                }

                if (s.length() > 1 && ! cities.contains(city)) {
                    addCity(city, days_forecast);
                    setWeather(city);
                }
            }
        });

        button_refresh = findViewById(R.id.button);

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String s: cities) {
                    setWeather(s);
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        savePreferences(sharedPref);
    }


    protected void addTableHeader(int days) {
        TableLayout tl = (TableLayout) findViewById(R.id.table_layout);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView city = new TextView(this);
        city.setText("city");
        city.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tr.addView(city);

        for(int i = 0; i < days; i++) {
            TextView tmp1 = new TextView(this);
            tmp1.setText("temp");
            tmp1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tmp2 = new TextView(this);
            tmp2.setText("weather");
            tmp2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            tr.addView(tmp1);
            tr.addView(tmp2);
        }

        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }


    protected void setWeather(String city) {
        String key_api = getResources().getString(R.string.api_key);

        openWeather.loadWeather(city, key_api)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        String call_city = call.request().url().queryParameter("q");
                        TableRow tr = getCityRow(call_city);
                        float[] temp = new float[1];
                        int[] cod = new int[1];

                        if (response.body() != null) {
                            Float temp0 = response.body().getMain().getTemp() - 273;
                            int weather_cod = response.body().getWeather()[0].getId();

                            temp[0] = temp0;
                            cod[0] = weather_cod;

                            WeatherData wd = new WeatherData(temp, cod);
                            wd.setWeatherRow(tr);
                        } else {
                            WeatherData wd = new WeatherData(1);
                            wd.setWeatherRow(tr);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        String call_city = call.request().url().queryParameter("q");
                        TableRow tr = getCityRow(call_city);
                        WeatherData wd = new WeatherData(1);
                        wd.setWeatherRow(tr);
                    }
                });
    }

    protected TableRow getCityRow(String city) {
        TableLayout tl = (TableLayout) findViewById(R.id.table_layout);
        int rows = tl.getChildCount();
        for(int i = 1; i < rows; i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            CheckBox cb = (CheckBox) tr.getChildAt(0);
            String s = (String) cb.getText();
            if (city.equals(s)) {
                return tr;
            }
        }
        return null;
    }

    protected void addCity(String city, int days) {
        cities.add(city);

        TableLayout tl = (TableLayout) findViewById(R.id.table_layout);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        CheckBox cb = new CheckBox(this);
        cb.setChecked(true);
        cb.setText(city);
        cb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("lala", "checkbox = " + buttonView.getText());
                if (! isChecked) {
                    String city = buttonView.getText().toString();
                    deleteCity(city);
                }
            }
        }
        );

        tr.addView(cb);

        for(int i = 0; i < days; i++) {
            ImageView im_day = new ImageView(this);
            im_day.setImageResource(R.drawable.w01d);
            im_day.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView temp_day = new TextView(this);
            temp_day.setText("?");
            temp_day.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            tr.addView(temp_day);
            tr.addView(im_day);
        }

        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }


    protected void deleteCity(String city) {
        TableLayout tl = (TableLayout) findViewById(R.id.table_layout);
        int rows = tl.getChildCount();

        for(int i = 1; i < rows; i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            CheckBox cb = (CheckBox) tr.getChildAt(0);
            String s = (String) cb.getText();
            if (city.equals(s)) {
                tl.removeViewAt(i);
                i--;
                rows = tl.getChildCount();
            }
        }

        cities.remove(city);
    }



    protected void savePreferences(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("cities", cities);
        editor.commit();
    }

    protected void loadPreferences(SharedPreferences sharedPref) {
        Set<String> tmp = sharedPref.getStringSet("cities", null);

        if (tmp != null) {
            cities = tmp;
        }
    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

}
