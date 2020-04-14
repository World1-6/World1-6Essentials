package com.andrew121410.mc.world16.objects;

import org.bukkit.Location;

public class LocationObject {

    private String playerName = "null";

    private String locationName1 = "null";
    private Integer locationID1;
    private Location location1;

    private String locationName2 = "null";
    private Integer locationID2;
    private Location location2;

    private String locationName3 = "null";
    private Integer locationID3;
    private Location location3;

    private String locationName4 = "null";
    private Integer locationID4;
    private Location location4;

    private String locationName5 = "null";
    private Integer locationID5;
    private Location location5;

    //Constructors

    public LocationObject() {
    }

    public LocationObject(String playerName) {
        this.playerName = playerName;
    }

    public LocationObject(Integer locationID1, Location location1) {
        this("UNKNOWN", "default", locationID1, location1);
    }

    public LocationObject(String locationName1, Location location1) {
        this("UNKNOWN", locationName1, 0, location1);
    }

    public LocationObject(String playerName, Integer locationID1, Location location1) {
        this(playerName, "default", locationID1, location1);
    }

    public LocationObject(String playerName, String locationName1, Location location1) {
        this(playerName, locationName1, 0, location1);
    }

    public LocationObject(String playerName, String locationName1, Integer locationID1, Location location1) {
        this.playerName = playerName;
        this.locationName1 = locationName1;
        this.locationID1 = locationID1;
        this.location1 = location1;
    }
//...

    @Override
    public String toString() {
        return "LocationObject{" +
                "playerName='" + playerName + '\'' +
                ", locationName1='" + locationName1 + '\'' +
                ", locationID1=" + locationID1 +
                ", location1=" + location1 +
                ", locationName2='" + locationName2 + '\'' +
                ", locationID2=" + locationID2 +
                ", location2=" + location2 +
                ", locationName3='" + locationName3 + '\'' +
                ", locationID3=" + locationID3 +
                ", location3=" + location3 +
                ", locationName4='" + locationName4 + '\'' +
                ", locationID4=" + locationID4 +
                ", location4=" + location4 +
                ", locationName5='" + locationName5 + '\'' +
                ", locationID5=" + locationID5 +
                ", location5=" + location5 +
                '}';
    }

    //Custom Functions:
    public Location getLocation(Integer LocationID) {
        switch (LocationID) {
            case 0:
                return getLocation1();
            case 1:
                return getLocation1();
            case 2:
                return getLocation2();
            case 3:
                return getLocation3();
            case 4:
                return getLocation4();
            case 5:
                return getLocation5();
            default:
                return null;
        }
    }

    public Location getLocation(String LocationName) {
        if (LocationName.equalsIgnoreCase(locationName1)) {
            return getLocation1();
        } else if (LocationName.equalsIgnoreCase(locationName2)) {
            return getLocation2();
        } else if (LocationName.equalsIgnoreCase(locationName3)) {
            return getLocation3();
        } else if (LocationName.equalsIgnoreCase(locationName4)) {
            return getLocation4();
        } else if (LocationName.equalsIgnoreCase(locationName5)) {
            return getLocation5();
        } else {
            return null;
        }
    }

    public void setLocation(String locationName, Integer locationID, Location location) {
        switch (locationID) {
            case 0:
                locationName1 = locationName;
                location1 = location;
                break;
            case 1:
                locationName1 = locationName;
                location1 = location;
                break;
            case 2:
                locationName2 = locationName;
                location2 = location;
                break;
            case 3:
                locationName3 = locationName;
                location3 = location;
                break;
            case 4:
                locationName4 = locationName;
                location4 = location;
                break;
            case 5:
                locationName5 = locationName;
                location5 = location;
                break;
            default:
                break;
        }
    }
//...

//Getters And Setters:

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getLocationName1() {
        return locationName1;
    }

    public void setLocationName1(String locationName1) {
        this.locationName1 = locationName1;
    }

    public Integer getLocationID1() {
        return locationID1;
    }

    public void setLocationID1(Integer locationID1) {
        this.locationID1 = locationID1;
    }

    public Location getLocation1() {
        return location1;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public String getLocationName2() {
        return locationName2;
    }

    public void setLocationName2(String locationName2) {
        this.locationName2 = locationName2;
    }

    public Integer getLocationID2() {
        return locationID2;
    }

    public void setLocationID2(Integer locationID2) {
        this.locationID2 = locationID2;
    }

    public Location getLocation2() {
        return location2;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public String getLocationName3() {
        return locationName3;
    }

    public void setLocationName3(String locationName3) {
        this.locationName3 = locationName3;
    }

    public Integer getLocationID3() {
        return locationID3;
    }

    public void setLocationID3(Integer locationID3) {
        this.locationID3 = locationID3;
    }

    public Location getLocation3() {
        return location3;
    }

    public void setLocation3(Location location3) {
        this.location3 = location3;
    }

    public String getLocationName4() {
        return locationName4;
    }

    public void setLocationName4(String locationName4) {
        this.locationName4 = locationName4;
    }

    public Integer getLocationID4() {
        return locationID4;
    }

    public void setLocationID4(Integer locationID4) {
        this.locationID4 = locationID4;
    }

    public Location getLocation4() {
        return location4;
    }

    public void setLocation4(Location location4) {
        this.location4 = location4;
    }

    public String getLocationName5() {
        return locationName5;
    }

    public void setLocationName5(String locationName5) {
        this.locationName5 = locationName5;
    }

    public Integer getLocationID5() {
        return locationID5;
    }

    public void setLocationID5(Integer locationID5) {
        this.locationID5 = locationID5;
    }

    public Location getLocation5() {
        return location5;
    }

    public void setLocation5(Location location5) {
        this.location5 = location5;
    }
}