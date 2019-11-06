package com.example.shield;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by effy on 2018/11/15.
 */

public class UserLocation {
    String phone;
    String name;

    boolean start = false;

    LatLng startPoint;
    LatLng endPoint;

    boolean first_time;

    User u;
    ArrayList<LatLng> postions = new ArrayList<>();
    LatLng oldPosition;
    LatLng newPosition;

    String destination;
    String departure;

    public UserLocation(User u){
        this.u=u;
        this.phone=u.getPhoneNo();
        this.name = u.getUsername();
    }
//
    public void addNewPosition1st( LatLng l){

        oldPosition = l;
        newPosition = l;

        postions.add(oldPosition);
    }

    public void addNewPosition( LatLng l){

        oldPosition = postions.get(postions.size()-1);
        newPosition =l;
        postions.add(newPosition);
    }
}
