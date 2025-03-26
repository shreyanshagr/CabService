package com.cab.nearbydrivers.services.impl;

import com.cab.nearbydrivers.entity.Driver;
import com.cab.nearbydrivers.services.IndexingFunctions;
import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexingFunctionImpl implements IndexingFunctions {


    private final H3Core h3;

    public IndexingFunctionImpl(H3Core h3) {
        this.h3 = h3;
    }

//    H3Core getH3Core() {
//        try{
//            return H3Core.newInstance();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public IndexingFunctionImpl() {
//       this.h3 = getH3Core();
//    }

    @Override
    public long latLngToCell(double lat, double lng, int res) {
//        H3Core h3 = getH3Core();
        return h3.latLngToCell(lat, lng, res);
    }

    @Override
    public String latLngToCellAddress(double lat, double lng, int res) {
//        H3Core h3 = getH3Core();
        return h3.latLngToCellAddress(lat, lng, res);
    }

    @Override
    public List<Driver> findNearbyDrivers(double lat, double lng, int radius) {
        return List.of();
    }

    public List<Long> gridDisk(long h3Index, int k) {
        return h3.gridDisk(h3Index, k);
    }

    public int calculateMaxRingSize(int maxSearchRadiusKm, int currResolution) {
        double hexSize = h3.edgeLength(currResolution, LengthUnit.km);
        return (int) Math.ceil(maxSearchRadiusKm/hexSize);
    }
}
