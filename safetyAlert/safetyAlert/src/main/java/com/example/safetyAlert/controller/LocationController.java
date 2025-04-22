package com.example.safetyAlert.controller;


import com.example.safetyAlert.model.LocationUpdate;
import com.example.safetyAlert.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/update")
    public ResponseEntity<LocationUpdate> updateLocation(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        double latitude = (double) request.get("latitude");
        double longitude = (double) request.get("longitude");

        LocationUpdate locationUpdate = locationService.saveLocation(username, latitude, longitude);
        return ResponseEntity.ok(locationUpdate);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<LocationUpdate>> getLocationHistory(@PathVariable String username) {
        return ResponseEntity.ok(locationService.getUserLocations(username));
    }
}
