# 좌표 계산

redis에서는 많은 자료형을 제공해주는데 이 중에서 Geospatial을 사용하면 위, 경도, 반경을 사용하여 이에 포함되는 값을 검색할 수 있습니다.

사용할 수 있는 명령어는 아래와 같습니다.
- GEOADD: 키, 경도, 위도, 이름을 파라미터로 받아 값을 생성합니다.
- GEOSEARCH: 키, 검색 타입, 반경, 경도, 위도를 파라미터로 받아 해당하는 값을 반환합니다.
  두 명령어의 실행 시간은 모두 O(n) 보다 작습니다.

![geosearch-result.png](images%2Fgeosearch-result.png)

1가지 유의할 점은 위도, 경도 순으로 입력하지 않고, 경도, 위도 순으로 값을 입력합니다.