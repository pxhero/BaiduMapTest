package com.pxhero.baidumaptest;

import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MapView mMapView;
	private LocationManager locManager;
	private BaiduMap baiduMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.activity_main);
		mMapView = (MapView) findViewById(R.id.map_view);

		baiduMap = mMapView.getMap(); // ��ȡ��ͼ������
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // ���õ�ͼ����
		baiduMap.setMyLocationEnabled(true);
		baiduMap.setMaxAndMinZoomLevel(4, 18); // ���õ�ͼ����Լ���С���ż��𣬵�ͼ֧�ֵ������С����ֱ�Ϊ[3-19]
		// baiduMap.setMapType(BaiduMap. MAP_TYPE_SATELLITE );

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList = locManager.getProviders(true);
		String providerName = null;
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			providerName = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			providerName = LocationManager.NETWORK_PROVIDER;
		} else {
			Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
			return;
		}

		Location location = locManager.getLastKnownLocation(providerName);

		navigateTo(location);

		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Marker �������", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}

	private void  navigateTo(Location loc) {
		if(loc == null)
			return;
		
        // ���춨λ����  
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(loc.getLatitude())  
                .longitude(loc.getLongitude()).build();  
        
		baiduMap.setMyLocationData(locData);
		
		  LatLng ll = new LatLng(loc.getLatitude(),  
				  loc.getLongitude());  
		  MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
          baiduMap.animateMapStatus(u); 
          
          //  ��ע��Marker���ǿ������ʹ�õĵ�ͼ������֮һ modify
          //׼�� marker ��ͼƬ  
          BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.location_overlay);
          //׼�� marker option ��� marker ʹ��  
          MarkerOptions markerOptions = new MarkerOptions().icon(bitmapDescriptor).position(ll);
        //��ȡ��ӵ� marker �������ں����Ĳ��� 
         baiduMap.addOverlay(markerOptions);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// activity �ָ�ʱͬʱ�ָ���ͼ�ؼ�
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// activity ��ͣʱͬʱ��ͣ��ͼ�ؼ�
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// activity ����ʱͬʱ���ٵ�ͼ�ؼ�
		mMapView.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
