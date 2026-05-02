package com.indianheritage.app.service;

import com.indianheritage.app.dto.PlaceDetailResponse;
import com.indianheritage.app.dto.PlaceSummaryResponse;
import com.indianheritage.app.dto.PlaceUpsertRequest;
import com.indianheritage.app.entity.Place;
import com.indianheritage.app.exception.NotFoundException;
import com.indianheritage.app.repository.PlaceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<PlaceSummaryResponse> getPlaceSummaries() {
        return placeRepository.findAllByOrderByIdAsc().stream()
            .map(this::toSummaryResponse)
            .toList();
    }

    public List<PlaceDetailResponse> getAllPlaceDetailsForAdmin() {
        return placeRepository.findAllByOrderByIdAsc().stream()
            .map(this::toDetailResponse)
            .toList();
    }

    public PlaceDetailResponse getPlaceDetails(Long placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("Place not found"));

        return toDetailResponse(place);
    }

    public PlaceDetailResponse createPlace(PlaceUpsertRequest request) {
        Place place = new Place(
            request.getName().trim(),
            request.getCity().trim(),
            request.getState().trim(),
            request.getDescription().trim(),
            request.getLocation().trim(),
            request.getTimings().trim(),
            request.getEntryFee().trim(),
            request.getImageUrl().trim()
        );

        Place saved = placeRepository.save(place);
        return toDetailResponse(saved);
    }

    public PlaceDetailResponse updatePlace(Long placeId, PlaceUpsertRequest request) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("Place not found"));

        place.setName(request.getName().trim());
        place.setCity(request.getCity().trim());
        place.setState(request.getState().trim());
        place.setDescription(request.getDescription().trim());
        place.setLocation(request.getLocation().trim());
        place.setTimings(request.getTimings().trim());
        place.setEntryFee(request.getEntryFee().trim());
        place.setImageUrl(request.getImageUrl().trim());

        Place saved = placeRepository.save(place);
        return toDetailResponse(saved);
    }

    public void deletePlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("Place not found"));
        placeRepository.delete(place);
    }

    private PlaceSummaryResponse toSummaryResponse(Place place) {
        return new PlaceSummaryResponse(
            place.getId(),
            place.getName(),
            place.getCity(),
            place.getState(),
            place.getImageUrl(),
            place.getDescription()
        );
    }

    private PlaceDetailResponse toDetailResponse(Place place) {
        return new PlaceDetailResponse(
            place.getId(),
            place.getName(),
            place.getCity(),
            place.getState(),
            place.getDescription(),
            place.getLocation(),
            place.getTimings(),
            place.getEntryFee(),
            place.getImageUrl()
        );
    }
}
