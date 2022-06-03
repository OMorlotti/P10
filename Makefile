.PHONY: api
api:
	mvn clean package
	cd WebAPI/target ; java -jar VirtualBookcaseWebAPI-*.jar

.PHONY: api-test-mode
api-test-mode:
	mvn clean package
	cd WebAPI/target ; java  -Dspring.datasource.url=jdbc:mysql://localhost:8889/virtualbookcase_test?serverTimezone=UTC -jar VirtualBookcaseWebAPI-*.jar

.PHONY: site
site:
	mvn clean package
	cd UserWebSite/target ; java -jar VirtualBookcaseUserWebSite-*.jar

.PHONY: batch
batch:
	mvn clean package
	cd Batch/target ; java -jar VirtualBookcaseBatch-*.jar

api-test:
	mvn test

api-sonar:
	cd WebAPI && mvn test sonar:sonar \
      -Dsonar.projectKey=P10 \
      -Dsonar.host.url=https://sonar.odier.xyz \
      -Dsonar.login=9cc9fb408176cc68a69f63a1fe0e97d6e15bc00b
