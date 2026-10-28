.DEFAULT_GOAL := build-run

clean:
	./gradlew clean

build:
	./gradlew clean build

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest spotlessCheck

lint-fix:
	./gradlew spotlessApply

update-deps:
	./gradlew dependencyUpdates

build-run: build

.PHONY: build
