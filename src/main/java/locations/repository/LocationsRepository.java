package locations.repository;

import locations.entity.LocationsEntity;
import util.repository.BaseRepository;

public class LocationsRepository extends BaseRepository<LocationsEntity> {
    public LocationsRepository() {
        super(LocationsEntity.class);
    }
}
