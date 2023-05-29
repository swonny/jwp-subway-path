package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;
import subway.domain.fare.FareCalculator;
import subway.domain.path.ShortestPath;
import subway.domain.path.ShortestPathFinder;
import subway.dto.AddLineRequest;
import subway.dto.AddStationRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.LineResponse;
import subway.dto.ShortestPathResponse;
import subway.dto.StationResponse;
import subway.dto.SubwayPathRequest;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.SubwayRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class SubwayService {

    private final SubwayRepository subwayRepository;

    public SubwayService(SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public long addStation(AddStationRequest addStationRequest) {
        Line line = findLine(addStationRequest.getLineName());
        Station stationToAdd = findStationByName(addStationRequest.getAddStationName()).orElseGet(() -> createNewStation(addStationRequest.getAddStationName()));
        Station upstream = findStationByName(addStationRequest.getUpstreamName()).orElseThrow(() -> new StationNotFoundException("추가하려는 역과 연결되는 상행역이 존재하지 않습니다."));
        Station downstream = findStationByName(addStationRequest.getDownstreamName()).orElseThrow(() -> new StationNotFoundException("추가하려는 역과 연결되는 하행역이 존재하지 않습니다."));
        line.addStation(stationToAdd, upstream, downstream, addStationRequest.getDistanceToUpstream());

        subwayRepository.updateLine(line);
        return subwayRepository.findStationIdByName(stationToAdd.getName())
                .orElseThrow(() -> new NoSuchElementException("디버깅: 역이 추가되어야 하는데 안됐습니다"));
    }

    private Line findLine(String lineNameInput) {
        LineName lineName = new LineName(lineNameInput);
        return subwayRepository.getLineByName(lineName);
    }

    private Optional<Station> findStationByName(String stationName) {
        Stations stations = subwayRepository.getStations();
        return stations.getStationByName(stationName);
    }

    private Station createNewStation(String stationName) {
        Station newStation = new Station(stationName);
        subwayRepository.addStation(newStation);
        return newStation;
    }

    public void deleteStation(DeleteStationRequest deleteStationRequest) {
        Line line = findLine(deleteStationRequest.getLineName());
        Station stationToDelete = findStationByName(deleteStationRequest.getStationName()).orElseThrow(() -> new StationNotFoundException("삭제하고자 하는 역이 존재하지 않습니다."));
        line.deleteStation(stationToDelete);

        subwayRepository.updateLine(line);
    }

    public long addNewLine(AddLineRequest addLineRequest) {
        LineName lineNameToAdd = new LineName(addLineRequest.getLineName());
        Station upstream = findStationByName(addLineRequest.getUpstreamName()).orElseGet(() -> createNewStation(addLineRequest.getUpstreamName()));
        Station downstream = findStationByName(addLineRequest.getDownstreamName()).orElseGet(() -> createNewStation(addLineRequest.getDownstreamName()));
        Section section = new Section(upstream, downstream, addLineRequest.getDistance());
        Line newLine = new Line(lineNameToAdd, section);
        Lines lines = subwayRepository.getLines();
        lines.addNewLine(newLine);
        return subwayRepository.addNewLine(newLine);
    }

    public LineResponse findLineById(long id) {
        Line line = subwayRepository.getLineById(id)
                .orElseThrow(() -> new LineNotFoundException("조회하고자 하는 노선이 없습니다"));
        return toLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return subwayRepository.getLines().getLines()
                .stream()
                .map(this::toLineResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private LineResponse toLineResponse(Line line) {
        return new LineResponse(line.getStationNamesInOrder(), line.getName().getName());
    }

    public ShortestPathResponse findShortestPath(SubwayPathRequest subwayPathRequest) {
        Stations stations = subwayRepository.getStations();
        Sections sections = subwayRepository.getSections();
        Station departure = subwayRepository.findStation(subwayPathRequest.getDepartureId());
        Station destination = subwayRepository.findStation(subwayPathRequest.getDestinationId());

        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        ShortestPath shortestPath = shortestPathFinder.findShortestPath(sections, stations, departure, destination);

        FareCalculator fareCalculator = new FareCalculator();
        int fare = fareCalculator.calculate(shortestPath.getDistance());
        return toShortestPathResponse(shortestPath, fare);
    }

    private ShortestPathResponse toShortestPathResponse(ShortestPath shortestPath, int fare) {
        return shortestPath.getStations()
                .stream()
                .map(Station::getName)
                .collect(collectingAndThen(
                        toList(),
                        stations -> new ShortestPathResponse(stations, shortestPath.getDistance(), fare))
                );
    }

    public StationResponse findStationById(long stationId) {
        Station station = subwayRepository.findStation(stationId);
        return new StationResponse(station.getName());
    }

    @Override
    public String toString() {
        return "SubwayService{" +
                "subwayRepository=" + subwayRepository +
                '}';
    }
}
