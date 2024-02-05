package model.locations.repository;

import model.locations.entity.LocationsEntity;
import util.repository.BaseRepository;

public class LocationsRepository extends BaseRepository<LocationsEntity,Long> {
    public LocationsRepository() {
        super(LocationsEntity.class);
    }

}
