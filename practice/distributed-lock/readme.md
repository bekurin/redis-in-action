### 분산 락
여러 개의 팟이 있는 경우 또는 다수의 프로세스에서 동일한 자원을 접근하는 경우
동시성 문제를 해결하기 위해 분산 락을 구현할 수 있다.

락 획득 cli 코드
![distribute-lock.png](images%2Fdistribute-lock.png)

NX 옵션은 distribute-lock 키가 존재하지 않은 경우 락을 획득할 수 있도록 도움을 줍니다.

EX 옵션은 락을 획득한 팟 또는 프로세스가 의도치 못하게 종료되는 경우 자동으로 락이 반납될 수 있도록 하기 위함입니다.

TTL 시간이 처리 시간 보다 짧은 경우 동시성 문제가 발생할 수 있기 때문에 TTL은 락을 잡아 처리하는 로직의 수행 시간보다 충분히 크게 잡는 것이 좋습니다.
(성능 테스트 또는 운영 환경 데이터를 기반으로 적절한 시간을 추측하는 것이 좋습니다)