package subway.domain;

import subway.exception.NameLengthException;

import java.util.Objects;

public class Station {

    public static final int MINIMUM_NAME_LENGTH = 2;
    public static final int MAXIMUM_NAME_LENGTH = 15;

    private static final String INPUT_FOR_END_POINT = "";
    private static final Station EMPTY_ENDPOINT_STATION = Station.from("종점역에 연결된 더미 데이터");

    private final String name;

    private Station(String name) {
        String stripped = name.strip();
        validateNameLength(stripped);
        this.name = stripped;
    }

    public static Station from(String stationName) {
        if (Objects.equals(stationName, INPUT_FOR_END_POINT)) {
            return EMPTY_ENDPOINT_STATION;
        }
        return new Station(stationName);
    }

    private void validateNameLength(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new NameLengthException("이름 길이는 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public static Station getEmptyEndpoint() {
        return EMPTY_ENDPOINT_STATION;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
