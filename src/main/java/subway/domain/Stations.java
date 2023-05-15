package subway.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Stations {

    private final Set<Station> stations;

    public Stations(Set<Station> stations) {
        this.stations = new HashSet<>(stations);
    }

    public Optional<Station> getStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findAny();
    }
}
