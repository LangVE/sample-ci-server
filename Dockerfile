# 1. Builder Stage: 레이어 추출
FROM eclipse-temurin:17-jre-alpine AS builder
WORKDIR /builder
# 빌드된 JAR 파일 복사
COPY build/libs/sample-ci-server-*.jar application.jar
# layertools 모드로 실행하여 레이어 추출
RUN java -Djarmode=layertools -jar application.jar extract

# 2. Final Stage: 실행 이미지 생성
FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="LangVE"
LABEL description="Sample CI Server Spring Boot Application"
WORKDIR /app

# 변경 빈도가 낮은 순서대로 레이어 복사 (캐시 효율 증대)
COPY --from=builder /builder/dependencies/ ./
COPY --from=builder /builder/spring-boot-loader/ ./
COPY --from=builder /builder/snapshot-dependencies/ ./
# 가장 자주 변경되는 애플리케이션 코드
COPY --from=builder /builder/application/ ./

EXPOSE 8080

# JarLauncher를 사용하여 실행
# Spring Boot 3.2 이상: org.springframework.boot.loader.launch.JarLauncher
# Spring Boot 3.2 미만: org.springframework.boot.loader.JarLauncher
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]