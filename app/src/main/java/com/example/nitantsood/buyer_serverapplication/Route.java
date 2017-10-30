package com.example.nitantsood.buyer_serverapplication;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class Route implements Serializable {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

    /**
     * Created by NITANT SOOD on 27-10-2017.
     */

    public static class OnePoolItem {
    }
}
