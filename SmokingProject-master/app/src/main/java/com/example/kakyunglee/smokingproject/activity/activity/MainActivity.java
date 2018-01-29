package com.example.kakyunglee.smokingproject.activity.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.activity.model.SelectedLocation;
import com.example.kakyunglee.smokingproject.activity.dto.AreaNoneSmokingDTO;
import com.example.kakyunglee.smokingproject.activity.dto.AreaSmokingDTO;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeListDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.AddressComponent;
import com.example.kakyunglee.smokingproject.activity.dto.response.GeoCodeResult;
import com.example.kakyunglee.smokingproject.activity.dto.response.ReportResultDTO;
import com.example.kakyunglee.smokingproject.activity.geointerface.AddressInfo;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.GetNoticeInfo;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.NonSmokingArea;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.PostReport;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.SmokingArea;
import com.example.kakyunglee.smokingproject.activity.util.GeoRetrofit;
import com.example.kakyunglee.smokingproject.activity.util.ServiceRetrofit;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.kakyunglee.smokingproject.R.layout.report_dialog;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {

    private GoogleApiClient mGoogleApiClient;
    String currentAddress = "";
    GoogleMap mGoogleMap;
    DrawerLayout drawer;
    private FusedLocationProviderClient mFusedLocationClient;
    SelectedLocation infoLocation = new SelectedLocation();
    MarkerOptions markerOptions = new MarkerOptions();
    String currentMapCenterLatitude = "";
    String currentMapCenterLongitude = "";
    ArrayList<Marker> markerNonSmokingList = null;
    ArrayList<Marker> markerSmokingList = null;
    Marker userPinned = null;
    /////////////////////////////////////
    private ImageButton fab_no_smoking; // 금연 구역 필터 버튼
    private ImageButton fab_smoking; //흡연 구역 필터 버튼
    private Button reportBtn; // 신고하기 버튼
    private NavigationView navigationView; // 내비게이션 뷰
    private TextView tv_address;
    private EditText et_userAddressInput;
    ImageView btn_search;
    ///////////////////////////////////
    private boolean no_smoking_clicked = true; // 금연 구역 필터 버튼 눌림 유지
    private boolean smoking_clicked = true;  // 흡연 구역 필터 버튼 눌림 유지

    ////////////////////////////////////////

    // 움직이는 마커

    //위치정보 제공자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("");

        //init Api Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        markerNonSmokingList = new ArrayList<>();
        markerSmokingList = new ArrayList<>();
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ///////////////////////////////////////
        fab_no_smoking = (ImageButton) findViewById(R.id.none_smoking_area);
        fab_smoking = (ImageButton) findViewById(R.id.smoking_area);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        reportBtn = (Button) findViewById(R.id.report);
        tv_address = (TextView) findViewById(R.id.address);
        et_userAddressInput = (EditText) findViewById(R.id.et_search_Loc);
        btn_search = (ImageView) findViewById(R.id.search_button);
        ////////////////////////////////////////

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,et_userAddressInput.getText().toString(),Toast.LENGTH_SHORT).show();
                String userSearchAddress = et_userAddressInput.getText().toString();
                AddressInfo getLatLng = GeoRetrofit.getInstance().getRetrofit().create(AddressInfo.class);
                Call<GeoCodeResult> callGeoCodeResult = getLatLng.geoResult(userSearchAddress, "ko", "AIzaSyA8lmYR7nzLGTmbPd1KSl4R-B__-bNOr9k");
                new NetWorkGeoAddressInfo().execute(callGeoCodeResult);
            }
        });
        et_userAddressInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        String userSearchAddress = et_userAddressInput.getText().toString();
                        AddressInfo getLatLng = GeoRetrofit.getInstance().getRetrofit().create(AddressInfo.class);
                        Call<GeoCodeResult> callGeoCodeResult = getLatLng.geoResult(userSearchAddress, "ko", "AIzaSyA8lmYR7nzLGTmbPd1KSl4R-B__-bNOr9k");
                        new NetWorkGeoAddressInfo().execute(callGeoCodeResult);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        // 금연구역 필터 버튼
        fab_no_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaNonSmokingButtonClicked();
            }
        });

        // 흡연구역 필터 버튼
        fab_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaSmokingButtonClicked();
            }
        });

        // 내비게이션 불러오기
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // 내비게이션에서 선택된 리스트로 이동
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();   // 선택된 menu
                selectedNavigationItem(id);       // 선택된 menu로 이동
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // 신고하기 버튼
        reportBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(report_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                String fixedAddress = currentAddress;

                //final double fixedLat = infoLocation.getSelectedLocationLatitude();
                //final double fixedLng = infoLocation.getSelectedLocationLongitude();
                DecimalFormat formatterLat = new DecimalFormat("##.######");
                DecimalFormat formatterLng = new DecimalFormat("###.######");

                final String fixedLat = formatterLat.format(infoLocation.getSelectedLocationLatitude());
                final String fixedLng = formatterLng.format(infoLocation.getSelectedLocationLongitude());
                /*DecimalFormat formLat = new DecimalFormat("##.######");
                DecimalFormat formLng = new DecimalFormat("###.######");
                String fixedLat = formLat.format(infoLocation.getSelectedLocationLatitude());
                String fixedLng = formLng.format(infoLocation.getSelectedLocationLongitude());
*/
                TextView textView = (TextView) view.findViewById(R.id.report_dialog_address);
                //사용자가 설정한 마커 또는 사용자 위치의 주소 입력 ""
                textView.setText(fixedAddress);

                final AlertDialog dialog = builder.create();
                dialog.show();

                // 신고하지 않을 경우
                Button noBtn = (Button) view.findViewById(R.id.no);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // 신고할 경우
                Button yesBtn = (Button) view.findViewById(R.id.yes);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        PostReport postReport = ServiceRetrofit.getInstance().getRetrofit().create(PostReport.class);
                        //Toast.makeText(MainActivity.this, "" + fixedLat + " / " + fixedLng, Toast.LENGTH_SHORT).show();
                        Call<ReportResultDTO> call = postReport.postSimpleReport(fixedLat, fixedLng);
                        new NetworkReport().execute(call);
                    }
                });
            }
        });
    }

    // 금연 구역 필터 on off 확인 함수
    private void areaNonSmokingButtonClicked() {

        if (!no_smoking_clicked) {
            Toast.makeText(getApplicationContext(), "금연 구역 필터 on", Toast.LENGTH_LONG).show();
            fab_no_smoking.setBackgroundResource(R.drawable.filter_pressed_button);
            if (markerNonSmokingList != null)
                for (int i = 0; i < markerNonSmokingList.size(); i++)
                    markerNonSmokingList.get(i).setVisible(true);
            no_smoking_clicked = true;

        } else {
            Toast.makeText(getApplicationContext(), "금연 구역 필터 off", Toast.LENGTH_LONG).show();
            fab_no_smoking.setBackgroundResource(R.drawable.filter_button);
            if (markerNonSmokingList != null)
                for (int i = 0; i < markerNonSmokingList.size(); i++)
                    markerNonSmokingList.get(i).setVisible(false);
            no_smoking_clicked = false;
        }
        return;
    }

    // 흡연 구역 필터 on off 확인 함수
    private void areaSmokingButtonClicked() {
        if (!smoking_clicked) {

            Toast.makeText(getApplicationContext(), "흡연 구역 필터 on", Toast.LENGTH_LONG).show();
            fab_smoking.setBackgroundResource(R.drawable.filter_pressed_button_red);
            if (markerSmokingList != null)
                for (int i = 0; i < markerSmokingList.size(); i++)
                    markerSmokingList.get(i).setVisible(true);
            smoking_clicked = true;

        } else {
            Toast.makeText(getApplicationContext(), "흡연 구역 필터 off", Toast.LENGTH_LONG).show();
            fab_smoking.setBackgroundResource(R.drawable.filter_button);
            if (markerSmokingList != null)
                for (int i = 0; i < markerSmokingList.size(); i++)
                    markerSmokingList.get(i).setVisible(false);
            smoking_clicked = false;
        }
        return;
    }

    //내비게이션에서 선택된 리스트로 이동하는 함수
    private void selectedNavigationItem(int id) {
        if (id == R.id.nav_notice) { // 공지사항으로 이동

            GetNoticeInfo getNoticeInfo = ServiceRetrofit.getInstance().getRetrofit().create(GetNoticeInfo.class);

            final Call<NoticeListDTO> call = getNoticeInfo.noticeInfo();
            new GetNoticeList().execute(call);

        } else if (id == R.id.nav_law) { //법률 정보로 이동
            Intent intent = new Intent(MainActivity.this, LawListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_question) { // 정부 문의로 이동
            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
            startActivityForResult(intent,0);

        } else if (id == R.id.nav_info) { // 앱 정보로 이동
            Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
            startActivity(intent);
        }
    }

    //상세신고를 작성하는 경우
    private void doDetailNotice(AlertDialog dialog2, int reportId, String address){
        dialog2.cancel();
        Intent intent = new Intent(MainActivity.this, ReportDetailActivity.class);
        intent.putExtra("report_id", reportId);
        intent.putExtra("address", address);
        startActivityForResult(intent,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(resultCode){
            case 0:
                break;
            case 1:
                break;
        }

    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    ////////////////////// retrofit 삽입 : NoticeList/////////////////////////
    private class GetNoticeList extends AsyncTask<Call, Void, NoticeListDTO> {
        @Override
        protected NoticeListDTO doInBackground(Call... params) {
            try {
                Call<NoticeListDTO> call = params[0];
                Response<NoticeListDTO> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NoticeListDTO result) {
            Bundle extras = new Bundle();
            extras.putSerializable("notice_list", result);
            Intent intent = new Intent(MainActivity.this, NoticeListActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMinZoomPreference(14.0f);
        mGoogleMap.setMaxZoomPreference(21.0f);
        //markerOptions.position(new LatLng(infoLocation.getSelectedLocationLatitude(),infoLocation.getSelectedLocationLongitude()));

        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);

        int userLocPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (userLocPermissionCheck == PackageManager.PERMISSION_DENIED) {
            //다이얼로그 -> 퍼미션 non
            //default 서울시청 위치
            //Snackbar -> 퍼미션 허용 하시겠습니까?
            // 네트워크 작업이기 때문에 asyncTask 필요?
        } else {
            mGoogleMap.setMyLocationEnabled(true);
            /*LatLng userLocation = new LatLng(37.566673, 126.978412);
            markerOptions.position(userLocation);
            googleMap.addMarker(markerOptions);*/

        }
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                renewPinnedLocation(null, latLng);
            }
        });
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location mLastLocation = requestUserLastLocation();
                renewPinnedLocation(mLastLocation, null);
                return false;
            }
        });
    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Log.d("camera_user gestured" ,"The user gestured on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {

        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {

        }
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraIdle() {

        DecimalFormat formatterLat = new DecimalFormat("##.######");
        DecimalFormat formatterLng = new DecimalFormat("###.######");
        currentMapCenterLatitude = formatterLat.format(mGoogleMap.getCameraPosition().target.latitude);
        currentMapCenterLongitude = formatterLng.format(mGoogleMap.getCameraPosition().target.longitude);

        NonSmokingArea nonSmokingArea = ServiceRetrofit.getInstance().getRetrofit().create(NonSmokingArea.class);
        Call<List<AreaNoneSmokingDTO>> callNonSmokingArea = nonSmokingArea.getNonSmokingArea(currentMapCenterLatitude, currentMapCenterLongitude);
        new NetworkNonSmoking().execute(callNonSmokingArea);

        SmokingArea smokingArea = ServiceRetrofit.getInstance().getRetrofit().create(SmokingArea.class);
        Call<List<AreaSmokingDTO>> callSmokingArea = smokingArea.getSmokingArea(currentMapCenterLatitude, currentMapCenterLongitude);
        new NetworkSmoking().execute(callSmokingArea);
    }

    // Permission check
    public Location requestUserLastLocation() {
        int userLocPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (userLocPermissionCheck != PackageManager.PERMISSION_DENIED)
            return LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        return null;
    }

    // selected Location renewing
    public void renewPinnedLocation(Location newLocation, LatLng newLatlng) {
        int selectedLogin = 0;
        final int LOCATION_FLAG = 0;
        final int LATLNG_FLAG = 1;
        String refinedLatLng = "";

        if (newLocation == null && newLatlng == null) {
            Log.d("ckh_null","위치정보 가져오기 에러");
            return;
        }
        if (newLatlng != null) selectedLogin = 1;
        //set pin Login
        if(userPinned!=null) userPinned.remove();
        switch (selectedLogin) {
            case LOCATION_FLAG:
                infoLocation.setSelectedLocationLatitude(newLocation.getLatitude());
                infoLocation.setSelectedLocationLongitude(newLocation.getLongitude());
                LatLng target = new LatLng(infoLocation.getSelectedLocationLatitude(), infoLocation.getSelectedLocationLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                markerOptions.position(target);
                userPinned = mGoogleMap.addMarker(markerOptions);
                break;
            case LATLNG_FLAG:
                infoLocation.setSelectedLocationLatitude(newLatlng.latitude);
                infoLocation.setSelectedLocationLongitude(newLatlng.longitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newLatlng));
                markerOptions.position(newLatlng);
                userPinned = mGoogleMap.addMarker(markerOptions);
                break;
            default:
                break;
        }
        DecimalFormat formLat = new DecimalFormat("##.########");
        DecimalFormat formLng = new DecimalFormat("###.########");
        refinedLatLng = formLat.format(infoLocation.getSelectedLocationLatitude()) + "," + formLng.format(infoLocation.getSelectedLocationLongitude());
        /*Toast.makeText(
                MainActivity.this,
                refinedLatLng,
                Toast.LENGTH_SHORT
        ).show();*/
        AddressInfo getAddress = GeoRetrofit.getInstance().getRetrofit().create(AddressInfo.class);
        Call<GeoCodeResult> callReverseGeoCodeResult = getAddress.reverseGeoResult(refinedLatLng, "ko", "AIzaSyA8lmYR7nzLGTmbPd1KSl4R-B__-bNOr9k");
        new NetWorkGeoInfo().execute(callReverseGeoCodeResult);
        //get address
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = requestUserLastLocation();
        renewPinnedLocation(mLastLocation, null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("api client error", connectionResult.getErrorMessage());
    }

    private class NetWorkGeoInfo extends AsyncTask<Call, Void, GeoCodeResult> {

        @Override
        protected GeoCodeResult doInBackground(Call... params) {
            try {
                Call<GeoCodeResult> call = params[0];
                Response<GeoCodeResult> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GeoCodeResult result) {
            if (result == null) {
                Log.d("network_reverse_geo","요청 failed");
            } else {
                Log.d("ckhlogging", result.getStatus());
                currentAddress = result.getResults().get(0).getFormattedAddress();
                tv_address.setText(currentAddress);
                //Toast.makeText(MainActivity.this, currentAddress, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class NetWorkGeoAddressInfo extends AsyncTask<Call, Void, GeoCodeResult> {

        @Override
        protected GeoCodeResult doInBackground(Call... params) {
            try {
                Call<GeoCodeResult> call = params[0];
                Response<GeoCodeResult> response = call.execute();
                Log.d("ckh_logging", response.toString());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GeoCodeResult result) {
            if (result == null || result.getResults() == null) {
                Toast.makeText(MainActivity.this, "주소를 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("ckhlogging", result.getStatus());
                if (!result.getStatus().equals("ZERO_RESULTS")) {
                    boolean flag = false;
                    //서울이 아닌경우
                    List<AddressComponent> refined = result.getResults().get(0).getAddressComponents();
                    for (int i = 0; i < refined.size(); i++) {
                        if (refined.get(i).getLongName().equals("서울특별시")) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        Toast.makeText(MainActivity.this, "유효한 주소가 아닙니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //성공 케이스
                    currentAddress = result.getResults().get(0).getFormattedAddress();

                    result.getResults().get(0).getGeometry().getLocation().getLat();
                    result.getResults().get(0).getGeometry().getLocation().getLng();
                    renewPinnedLocation(
                            null,
                            new LatLng(
                                    result.getResults().get(0).getGeometry().getLocation().getLat(),
                                    result.getResults().get(0).getGeometry().getLocation().getLng()
                            )
                    );
                } else
                    Toast.makeText(MainActivity.this, "유효한 주소가 아닙니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class NetworkReport extends AsyncTask<Call, Void, ReportResultDTO> {
        @Override
        protected ReportResultDTO doInBackground(Call... params) {
            try {
                Call<ReportResultDTO> call = params[0];
                Response<ReportResultDTO> response = call.execute();
                Log.d("ckh_report", response.toString());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override

        protected void onPostExecute(ReportResultDTO result){
            if(result==null){
                Toast.makeText(MainActivity.this,"간편신고 failed",Toast.LENGTH_SHORT).show();
            }else{

                Log.d("test",result.getCount()+"");

                final int reportId = result.getId();
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.report_detail_dialog,null);
                TextView count = (TextView) view.findViewById(R.id.report_count);
                count.setText(result.getCount()+"");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);

                //// 신고 횟수 입력 하는 곳

                final AlertDialog  dialog2 = builder.create();
                dialog2.show();

                //상세 신고를 하지 않는 경우
                Button skipBtn = (Button) view.findViewById(R.id.skip);
                skipBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.cancel();
                    }
                });

                //상세신고를 하는 경우
                Button writeBtn = (Button) view.findViewById(R.id.write_detail);
                writeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDetailNotice(dialog2, reportId, currentAddress);
                    }
                });
            }
        }
    }
    private class NetworkNonSmoking extends AsyncTask<Call, Void, List<AreaNoneSmokingDTO>> {
        @Override
        protected List<AreaNoneSmokingDTO> doInBackground(Call... params) {
            try {
                Call<List<AreaNoneSmokingDTO>> call = params[0];
                Response<List<AreaNoneSmokingDTO>> response = call.execute();
                Log.d("ckh_report", response.toString());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AreaNoneSmokingDTO> result) {
            if (result == null) {

                Log.d("network_NonSmoking","금연구역 쿼리 failed");
            } else {
                Log.d("network_NonSmoking","금연구역 쿼리 success");

            }
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.custom_pin_smoking);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 96, 96, false);

            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    AreaNoneSmokingDTO area = result.get(i);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(area.getLatitude()), Double.parseDouble(area.getLongitude())))
                            .title("금연구역 ["+area.getName()+"]")
                            .snippet("범위 :" + area.getRange())
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

                    if (no_smoking_clicked && checkDuplicateArea(markerNonSmokingList, markerOptions)) {
                        markerNonSmokingList.add(mGoogleMap.addMarker(markerOptions));
                    }
                }
            }

        }

    }
    private class NetworkSmoking extends AsyncTask<Call, Void, List<AreaSmokingDTO>> {
        @Override
        protected List<AreaSmokingDTO> doInBackground(Call... params) {
            try {
                Call<List<AreaSmokingDTO>> call = params[0];
                Response<List<AreaSmokingDTO>> response = call.execute();
                Log.d("ckh_report", response.toString());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AreaSmokingDTO> result) {
            if (result == null) {
                Log.d("network_Smoking","흡연구역 쿼리 failed");
            } else {
                Log.d("network_Smoking","흡연구역 쿼리 success");

            }
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.custom_pin_non_smoking);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 96, 96, false);
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    AreaSmokingDTO area = result.get(i);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(area.getLatitude()), Double.parseDouble(area.getLogitude())))
                            .title("흡연구역 ["+area.getDetail_address()+"]")
                            .snippet("분류 :" + area.getClassification())
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

                    if (smoking_clicked && checkDuplicateArea(markerSmokingList, markerOptions))
                        markerSmokingList.add(mGoogleMap.addMarker(markerOptions));
                }
            }
        }
    }

    boolean checkDuplicateArea(ArrayList<Marker> markerList, MarkerOptions markerOptions) {
        if (markerList == null) return true;
        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getTitle().equals(markerOptions.getTitle())) return false;
        }
        return true;
    }
}
