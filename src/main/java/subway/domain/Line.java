package subway.domain;

import subway.exception.NameLengthException;

import java.util.LinkedList;
import java.util.List;

public class Line {

    public static final int MINIMUM_NAME_LENGTH = 2;
    public static final int MAXIMUM_NAME_LENGTH = 15;

    private String name;
    private LinkedList<Section> sections;

    public Line(String name, List<Section> sections) {
        String stripped = name.strip();
        validateNameLength(stripped);
        this.name = stripped;
        this.sections = new LinkedList<>(sections);
    }

    private void validateNameLength(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new NameLengthException("이름 길이는 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public Line(Line otherLine) {
        this(otherLine.getName(), otherLine.getSections());
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return new LinkedList<>(sections);
    }

    public List<Section> addStation(Station newStation, Station upstream, Station downstream) {
        return null;
    }
}
