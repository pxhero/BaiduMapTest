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

		baiduMap = mMapView.getMap(); // 获取地图控制器
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // 设置地图类型
		baiduMap.setMyLocationEnabled(true);
		baiduMap.setMaxAndMinZoomLevel(4, 18); // 设置地图最大以及最小缩放级别，地图支持的最大最小级别分别为[3-19]
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
				Toast.makeText(getApplicationContext(), "Marker 被点击了", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}

	private void  navigateTo(Location loc) {
		if(loc == null)
			return;
		
        // 构造定位数据  
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(loc.getLatitude())  
                .longitude(loc.getLongitude()).build();  
        
		baiduMap.setMyLocationData(locData);
		
		  LatLng ll = new LatLng(loc.getLatitude(),  
				  loc.getLongitude());  
		  MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
          baiduMap.animateMapStatus(u); 
          
          //  标注（Marker）是开发者最常使用的地图覆盖物之一 modify
          //准备 marker 的图片  
          BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.location_overlay);
          //准备 marker option 添加 marker 使用  
          MarkerOptions markerOptions = new MarkerOptions().icon(bitmapDescriptor).position(ll);
        //获取添加的 marker 这样便于后续的操作 
         baiduMap.addOverlay(markerOptions);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// activity 销毁时同时销毁地图控件
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
