# 기능 목록

## API 설계
- 역 추가
- POST /line/stations 
  - Request
    - station-name
    - line-name
    - upstream-name
    - downstream-name
    - distance-to-upstream

  - Response
    - 400 BAD REQUEST
      - (station-name < 2 || station-name > 15) ->
      - (line-name < 2 || line-name > 15) ->
      - (line-name이 존재하지 않는 경우)
      - (upstream-name, downstream-name이 Section으로 존재하지 않는 경우)
      - (distance-to-upstream >= distance(upstream, downstream)인 경우)
      - (종점이 아닌 station에 종점을 추가하는 경우)
      - (station-name이 이미 Line에 존재하는 경우)
    - 201 CREATED

- POST /lines
  - Request
    - line-name 
    - upstream-name
    - downstream-name
    - distance

  - Response
    - 400 BAD REQUEST
      - (upstream-name < 2 || upstream-name > 15) ->
      - (downstream-name < 2 || downstream-name > 15) ->
      - (line-name < 2 || line-name > 15) ->
      - (distance < 1인 경우)
    - 201 CREATED

- DELETE /line/stations
  - Request
    - line-name
    - station-name

  - Response
    - 400 BAD REQUEST
      - (line-name이 존재하지 않는 경우)
      - (station-name이 Line에 존재하지 않는 경우)
    - 204 NO-CONTENT

## 도메인

- [ ] Station
  - [ ] 역 이름을 갖는다.
    - [ ] 역 이름이 2글자 이상 15글자 이하가 아닌 경우 예외를 던진다.

- [ ] Section
  - [ ] 하행역, 상행역에 해당하는 Station을 갖는다.
  - [ ] 구간의 거리를 갖는다.
    - [ ] 구간의 거리가 양의 정수가 아닌 경우 예외를 던진다.
  - [ ] upstream-name, downstream-name이 일치하는지 확인한다.
  - [ ] Station을 중간에 추가해서 새로운 Section들을 반환한다.
    - [ ] 추가하려는 Station이 종점인데 종점에 연결이 되지 않는다면 예외를 던진다.
  - [ ] Section을 삭제할 수 있다.
 
- [ ] Line
  - [ ] 이름을 갖는다.
    - [ ] 이름이 2자 이상 15자 이하가 아니면 예외를 던진다.
  - [ ] Line에 해당하는 Section들을 갖는다.
  - [ ] 추가하려는 Station이 이미 존재하는 경우 예외를 던진다.
  - [ ] Line에 존재하지 않는 Station을 삭제할 경우 예외를 던진다.
  
  
-[ ] Lines
  - [ ] Line들을 갖는다.
  - [ ] 찾는 Line이 있는지 확인한다.