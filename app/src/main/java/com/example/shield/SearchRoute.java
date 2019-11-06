package com.example.shield;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.RouteOverlay;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

/**
 * Created by effy on 2018/11/11.
 */

public class SearchRoute implements RouteSearch.OnRouteSearchListener {


    LatLng mStartPoint;
    LatLng mEndPoint;


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }
    RouteOverlay routeOverlay;
//    public WalkRouteOverlay walkRouteOverlay;

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {




    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
