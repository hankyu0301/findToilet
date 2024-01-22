package com.findToilet.global.util;

public class UserLocationCalculator {

    private static final double EARTH_RADIUS = 6371.0;

    private UserLocationCalculator() {
    }

    public static double calculateDistance(double userLatitude, double userLongitude, double targetLatitude, double targetLongitude) {
        double dLat = Math.toRadians(targetLatitude - userLatitude);
        double dLon = Math.toRadians(targetLongitude - userLongitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(targetLatitude)) * Math.cos(Math.toRadians(userLatitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public static double getLatitudeMinus(double userLatitude, double distance) {
        return userLatitude - 0.0091 * distance;
    }

    public static double getLatitudePlus(double userLatitude, double distance) {
        return userLatitude + 0.0091 * distance;
    }

    public static double getLongitudeMinus(double userLongitude, double distance) {
        return userLongitude - 0.011269 * distance;
    }

    public static double getLongitudePlus(double userLongitude, double distance) {
        return userLongitude + 0.011269 * distance;
    }
}
