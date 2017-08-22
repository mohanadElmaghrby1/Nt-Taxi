package android.example.com.nttaxi.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.com.nttaxi.R;
import android.example.com.nttaxi.controller.DataParser;
import android.example.com.nttaxi.controller.PlaceAutocompleteAdapter;
import android.example.com.nttaxi.controller.networkController.RequestCallBack;
import android.example.com.nttaxi.controller.networkController.Service;
import android.example.com.nttaxi.model.DriversLocations;
import android.example.com.nttaxi.model.Location;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapFragment extends BaseFragment implements OnMapReadyCallback , LocationListener  {

    /**the LOG tag*/
    private static String LOG_TAG=MapFragment.class.getName();

    /***/
    private PlaceAutocompleteAdapter mAdapter;

    /** list of drivers (name , location, email ..)*/
    private ArrayList<Location> driversList;

    ArrayList<LatLng> MarkerPoints;



    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    public GoogleApiClient mGoogleApiClient;

    private AutoCompleteTextView mAutocompleteFromView;

    private AutoCompleteTextView mAutocompleteToView;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(22.00000, 24.00000), new LatLng(33.000000, 36.000000));

    private GoogleMap mMap;

    SupportMapFragment supportMapFragment;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteFromView = (AutoCompleteTextView)getView().findViewById(R.id.autxt_from);
        mAutocompleteToView  = (AutoCompleteTextView)getView().findViewById(R.id.autxt_destination);

        view.findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);


                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        });


        mAdapter = new PlaceAutocompleteAdapter(getContext() ,mGoogleApiClient,BOUNDS_GREATER_SYDNEY,null);
        mAutocompleteFromView.setAdapter(mAdapter);

        mAutocompleteToView.setAdapter(mAdapter);

        // Initializing
        MarkerPoints = new ArrayList<>();


        mAutocompleteFromView.setOnItemClickListener(mAutocompleteClickListener);
        mAutocompleteToView.setOnItemClickListener(mAutocompleteClickListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager= (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        //
        Criteria criteria = new Criteria();

        String provider =locationManager.getBestProvider(criteria,true);

        //check permission

        //show drivers markers on the map
        drawDriversOnMap();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                String a=marker.getTitle();
                Toast.makeText(getContext(),a,Toast.LENGTH_SHORT).show();
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               int index= (int) marker.getTag();

                                Location driverlocation =driversList.get(index);
                                String driver_name=driverlocation.getName();
                                String driver_email = driverlocation.getEmail();
                                String driver_id=driverlocation.getId();

                                LatLng from =MarkerPoints.get(0);
                                LatLng to = MarkerPoints.get(1);

                                String location = mAutocompleteFromView.getText().toString();
                                String droplocation = mAutocompleteToView.getText().toString();

                                Log.v("fromto",location+" >"+droplocation);

                                String toLat=to.latitude+"";
                                String tolong=to.longitude+"";

                                String fromLat=from.latitude+"";
                                String fromLong=from.longitude+"";

                                Service.requestRide(getContext(), driver_id, driver_name,
                                        driver_email, "mohanad", "01014152062", location,
                                        droplocation, "24.000", "24.0000", fromLat, toLat,
                                        fromLong, tolong, new RequestCallBack() {
                                    @Override
                                    public void success(String response) {
                                        if (response.contains("Taxi request is succcessful")){
                                            Toast.makeText(getContext(),"done y basha",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(getContext(),"bl7a y basha",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void error(Exception exc) {
                                        Log.e("error reuqest ride",exc.toString());
                                    }
                                });

                            }
                        };

                showMarkerDataDialog(discardButtonClickListener ,marker);
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Checks, whether start and end locations are captured
        android.location.Location location =locationManager.getLastKnownLocation(provider);

        if (location!=null){

        }


        //request every 2 seconds
        locationManager.requestLocationUpdates(provider, 2000, 0, this);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocationManager locationManager= (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        locationManager.removeUpdates(this);

    }

    /**
     * move camera focus on the given LatLang
     * @param current
     */
    public void moveCamerTo( LatLng current ){
        float zoom=10;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, zoom));
    }


    /**
     * show popup dialog when driver marker is clicked
     * @param discardButtonClickListener
     * @param marker
     */
    private void showMarkerDataDialog(
            DialogInterface.OnClickListener discardButtonClickListener , Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(marker.getTitle());
        builder.setPositiveButton("ride", discardButtonClickListener);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private  void drawDriversOnMap( ){

        //getting the drivers locations
        getDriversLocations();

    }

    private void getDriversLocations(){

        Service.getLocations(getContext(), new RequestCallBack() {
            @Override
            public void success(String response) {
                Log.v(LOG_TAG, response+"  123");
                //this Gson parse and return base object contains all json response data
                DriversLocations driversLocations = new Gson().fromJson(response , DriversLocations.class);

                //get the success result
                int loginResult =driversLocations.getSuccess();

                //check if result =1 , successfully return locations
                if (loginResult==1){
                    //get drivers locations
                    driversList= driversLocations.getLocation();

                    Log.v(LOG_TAG,String.valueOf(loginResult));

                    for (int i = 0 ; i<driversList.size() ; ++i){
                        Location currentDriver=driversList.get(i);
                        LatLng currentLocation = new LatLng(Double.parseDouble(currentDriver.getLatitude())
                                , Double.parseDouble(currentDriver.getLongitude()));

                        Marker marker =mMap.addMarker(new MarkerOptions().position(currentLocation).title(currentDriver.getName()));
                        marker.setTag(i);
                    }
                }
                else{
                    //the result = 0 , token changed in the server so close tha pp and open loginActivity
                    //may be the user change email or password from another device
                    startActivity(new Intent(getContext() , LoginActivity.class));

                }

            }

            @Override
            public void error(Exception exc) {
                Log.e(LOG_TAG, "Error loading driver locations"+exc);
            }
        });
    }


    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }



    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("", "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i("", "Called getPlaceById to get Place details for " + placeId);


        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            MarkerPoints.add(place.getLatLng());


            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {

            } else {

            }

            Log.i("", "Place details received: " + place.getName());



            places.release();
        }
    };

    @Override
    public void onLocationChanged(android.location.Location location) {
//        Toast.makeText(getContext() , location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }



}

