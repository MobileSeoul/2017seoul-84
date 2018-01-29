package com.example.kakyunglee.smokingproject.activity.activity.model;

/**
 * Created by chakh on 2017-10-25.
 */

public class SelectedLocation {

    double selectedLocationLatitude;
    double selectedLocationLongitude;

    public SelectedLocation(){
        selectedLocationLatitude = 37.566677;
        selectedLocationLongitude = 126.978407;
    }

    public double getSelectedLocationLatitude() {
        return selectedLocationLatitude;
    }

    public void setSelectedLocationLatitude(double selectedLocationLatitude) {
        this.selectedLocationLatitude = selectedLocationLatitude;
    }

    public double getSelectedLocationLongitude() {
        return selectedLocationLongitude;
    }

    public void setSelectedLocationLongitude(double selectedLocationLongitude) {
        this.selectedLocationLongitude = selectedLocationLongitude;
    }
}
