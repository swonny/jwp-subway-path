package subway.domain;

import subway.exception.DuplicateLineNameException;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void addNewLine(Line newLine) {
        validateLineDuplicate(newLine);
        lines.add(newLine);
    }

    private void validateLineDuplicate(Line line) {
        if (hasLine(line)) {
            throw new DuplicateLineNameException("이미 존재하는 노선입니다");
        }
    }

    private boolean hasLine(Line newLine) {
        return lines.stream()
                .anyMatch(line -> line.getName().equals(newLine.getName()));
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }
}
