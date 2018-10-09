package com.example.nobody.weather;

import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class WeatherData {
    String[] temp;
    int[] cod;

    public WeatherData(int l) {
        this.temp = new String[l];
        this.cod = new int[l];

        for(int i = 0; i < l; i++) {
            this.temp[i] = "?";
            this.cod[i] = 0;
        }
    }

    public WeatherData(float[] temp, int[] cod) {
        int l = temp.length;

        this.temp = new String[l];
        this.cod = new int[l];

        for(int i = 0; i < l; i++) {
            if (temp[i] < 0) {
                this.temp[i] = String.valueOf(Math.round(temp[i]));
            } else if (temp[i] > 0) {
                this.temp[i] = "+" + String.valueOf(Math.round(temp[i]));
            } else {
                this.temp[i] = "0";
            }

            this.cod[i] = cod[i];
        }
    }


    public void setWeatherRow(TableRow tr) {
        TextView tv = (TextView) tr.getChildAt(1);
        ImageView im = (ImageView) tr.getChildAt(2);

        tv.setText(String.valueOf(temp[0]));
        setWeatherImage(im, cod[0], true);
    }


    protected void setWeatherImage(ImageView im, int cod, boolean is_day) {
        if (cod >= 200 && cod < 300) {
            if (is_day) {
                im.setImageResource(R.drawable.w11d);
            } else {
                im.setImageResource(R.drawable.w11n);
            }
        } else if (cod >= 300 && cod < 400) {
            if (is_day) {
                im.setImageResource(R.drawable.w09d);
            } else {
                im.setImageResource(R.drawable.w09n);
            }
        } else if (cod >= 500 && cod < 510) {
            if (is_day) {
                im.setImageResource(R.drawable.w10d);
            } else {
                im.setImageResource(R.drawable.w10n);
            }
        } else if (cod >= 510 && cod < 520) {
            if (is_day) {
                im.setImageResource(R.drawable.w13d);
            } else {
                im.setImageResource(R.drawable.w13n);
            }
        } else if (cod >= 520 && cod < 600) {
            if (is_day) {
                im.setImageResource(R.drawable.w09d);
            } else {
                im.setImageResource(R.drawable.w09n);
            }
        } else if (cod >= 600 && cod < 700) {
            if (is_day) {
                im.setImageResource(R.drawable.w13d);
            } else {
                im.setImageResource(R.drawable.w13n);
            }
        } else if (cod >= 700 && cod < 800) {
            if (is_day) {
                im.setImageResource(R.drawable.w50d);
            } else {
                im.setImageResource(R.drawable.w50n);
            }
        } else if (cod == 800) {
            if (is_day) {
                im.setImageResource(R.drawable.w01d);
            } else {
                im.setImageResource(R.drawable.w01n);
            }
        } else if (cod == 801) {
            if (is_day) {
                im.setImageResource(R.drawable.w02d);
            } else {
                im.setImageResource(R.drawable.w02n);
            }
        } else if (cod == 802) {
            if (is_day) {
                im.setImageResource(R.drawable.w03d);
            } else {
                im.setImageResource(R.drawable.w03n);
            }
        } else if (cod >= 803 && cod <= 804) {
            if (is_day) {
                im.setImageResource(R.drawable.w04d);
            } else {
                im.setImageResource(R.drawable.w04n);
            }
        } else {
            if (is_day) {
                im.setImageResource(R.drawable.w50d);
            } else {
                im.setImageResource(R.drawable.w50n);
            }
        }
    }


}
