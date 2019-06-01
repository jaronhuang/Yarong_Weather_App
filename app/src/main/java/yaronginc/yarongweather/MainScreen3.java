package yaronginc.yarongweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Random;

import yaronginc.yarongweather.Common.Common;
import yaronginc.yarongweather.Helper.Helper;
import yaronginc.yarongweather.Model.OpenWeatherMap;
import yaronginc.yarongweather.Model.Sys;

public class MainScreen3 extends AppCompatActivity implements LocationListener {

    private Button normal;
    private Button details;

    //    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtTime, txtCelsius;
    TextView txtHead, txtTop, txtBottom, txtShoes, txtCelsius;
    ImageView imageView;

    LocationManager locationManager;
    String provider;
    static double lat, lng;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    int MY_PERMISSION = 0;
    String[] hatW = {"Baseball Cap", "Snap Back Cap", "Bucket Cap"};
    String[] hatC = {"Beanie", "Face Mask", "Ear flap Hat"};

    String[] topW = {"Short Sleeve", "Tank Top", "Hawaiian Shirt"};
    String[] topN = {"Hoodie", "Vest", "Turtleneck", "Short Sleeve", "Long Sleeve", "Windbreaker"};
    String[] topC = {"Fur Coat", "Long Sleeve", "Parka", "Heavy Jacket"};

    String[] bottomW = {"Shorts", "Swim Trunks"};
    String[] bottomN = {"Joggers", "Jeans", "Long Pants"};
    String[] bottomC = {"Uniqlo Heattech", "Skiing Pants"};

    String[] shoesW = {"Sneakers", "Flip Flops", "Sandals", "Slides"};
    String[] shoesN = {"Sneakers", "Dress Shoes"};
    String[] shoesC = {"Winter Boots", "Sneakers"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen3);

        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        txtHead = (TextView) findViewById(R.id.txtHead);
        txtTop = (TextView) findViewById(R.id.txtTop);
        txtBottom = (TextView) findViewById(R.id.txtBottom);
        txtShoes = (TextView) findViewById(R.id.txtShoes);
        imageView = (ImageView) findViewById(R.id.imageView);

        normal = (Button)findViewById(R.id.button1);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreen3.this,MainScreen.class);
                startActivity(i);
            }
        });

        details = (Button)findViewById(R.id.button3);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreen3.this,MainScreen2.class);
                startActivity(i);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainScreen3.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null)
            Log.e("TAG", "No Location");

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        if((hour >= 0 && hour < 6) || (hour >= 20)){
            linearLayout.setBackgroundResource(R.drawable.img1);
        }else if(hour >= 6 && hour < 8){
            linearLayout.setBackgroundResource(R.drawable.img2);
        }else if(hour >= 8 && hour < 17 ){
            linearLayout.setBackgroundResource(R.drawable.img3);
        }else if(hour >= 17 && hour < 20){
            linearLayout.setBackgroundResource(R.drawable.img4);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainScreen3.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainScreen3.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = (location.getLongitude()) * -1;

        new MainScreen3.GetWeather().execute(Common.apiRequest(String.valueOf(lat),String.valueOf(lng)));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private class GetWeather extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainScreen3.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error: Not found city")) {
                pd.dismiss();
                return;
            }
            Gson gson = new Gson();
            Type mtype = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s, mtype);
            pd.dismiss();

            txtCelsius.setText(String.format("%.2f °C", openWeatherMap.getMain().getTemp()));

            if (openWeatherMap.getMain().getTemp() > 30) {
                txtHead.setText(String.format("Head Wear: " + getRandom(hatW)));
                txtTop.setText(String.format("Top: " + getRandom(topW)));
                txtBottom.setText(String.format("Bottom: " + getRandom(bottomW)));
                if((openWeatherMap.getWeather().get(0).getDescription().contains("rain"))){
                    txtShoes.setText(String.format("Shoes: Boots"));
                }
                else {
                    txtShoes.setText(String.format("Shoes: " + getRandom(shoesW)));
                }
            }

            else if (openWeatherMap.getMain().getTemp() > 15) {
                txtHead.setText(String.format("Head Wear: None"));
                txtTop.setText(String.format("Top: " + getRandom(topN)));
                txtBottom.setText(String.format("Bottom: " + getRandom(bottomN)));
                if((openWeatherMap.getWeather().get(0).getDescription()).contains("rain")){
                    txtShoes.setText(String.format("Shoes: Boots"));
                }
                else {
                    txtShoes.setText(String.format("Shoes: " + getRandom(shoesN)));
                }
            }

            else {
                txtHead.setText(String.format("Head Wear: " + getRandom(hatC)));
                txtTop.setText(String.format("Top: " + getRandom(topC)));
                txtBottom.setText(String.format("Bottom: " + getRandom(bottomC)));
                if((openWeatherMap.getWeather().get(0).getDescription().contains("rain"))){
                    txtShoes.setText(String.format("Shoes: Boots"));
                }
                else {
                    txtShoes.setText(String.format("Shoes: " + getRandom(shoesC)));
                }
            }

            Picasso.with(MainScreen3.this).load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon())).into(imageView);
        }
    }
}
