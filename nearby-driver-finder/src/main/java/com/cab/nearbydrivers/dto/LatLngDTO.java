package com.cab.nearbydrivers.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class LatLngDTO {
    private double lat;
    private double lng;
    private int res;
}
