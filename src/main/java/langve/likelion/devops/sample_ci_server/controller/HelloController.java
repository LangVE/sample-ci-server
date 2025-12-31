package langve.likelion.devops.sample_ci_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    // 메모리 누수 시뮬레이션을 위한 리스트 (static으로 선언하여 GC되지 않도록 함)
    private static final List<byte[]> memoryLeakStorage = new ArrayList<>();

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, YH! container running in EC2!! deploy by layer caching haha";
    }

    // 메모리 누수 시뮬레이션 API
    @GetMapping("/memory-leak")
    public String simulateMemoryLeak() {
        // 약 100MB 크기의 바이트 배열 생성 (100 * 1024 * 1024 bytes)
        byte[] data = new byte[100 * 1024 * 1024];
        memoryLeakStorage.add(data);

        long currentSizeMb = memoryLeakStorage.size() * 100L;
        return String.format("{\"message\": \"메모리 누수 시뮬레이션 실행됨\", \"stored_items\": %d, \"approximate_size_mb\": \"%dMB\"}",
                memoryLeakStorage.size(), currentSizeMb);
    }

    // CPU 집약적 작업 시뮬레이션 API
    @GetMapping("/cpu-intensive")
    public String cpuIntensiveTask() {
        long startTime = System.currentTimeMillis();
        long result = 0;

        // 10^6 (100만) 번 반복
        for (int i = 0; i < 1_000_000; i++) {
            // 소수 판별 로직 (비효율적 구현)
            if (i > 1) {
                boolean isPrime = true;
                for (int j = 2; j <= Math.sqrt(i); j++) {
                    if (i % j == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    result += i;
                }
            }
            // 간단한 수학 연산 추가
            result += (long) (Math.pow(i, 2) % 1000);
        }

        long endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime) / 1000.0;

        return String.format("{\"message\": \"CPU 집약적 작업 완료\", \"execution_time\": \"%.2f초\", \"result\": %d}",
                executionTime, result);
    }
}
