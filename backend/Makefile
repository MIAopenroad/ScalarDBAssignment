GRADLE = ./gradlew
MYSQL_LOG_STRING=Ready for start up
MYSQL_CONTAINER=mysql
MYSQL_USER=root
MYSQL_PASSWORD=mysql
SEED_CUSTOMER_SQL=./sql/seed_customer.sql
SEED_ITEM_SQL=./sql/seed_item.sql
CASSANDRA_LOG_STRING=Startup complete
CASSANDRA_CONTAINER=cassandra

.PHONY: all
all: run

.PHONY: init
init: down_with_volume run_container wait build load seed run

.PHONY: down_with_volume
down_with_volume:
	docker-compose down -v $(MYSQL_CONTAINER) $(CASSANDRA_CONTAINER)

.PHONY: run_container
run_container:
	docker-compose up -d $(MYSQL_CONTAINER) $(CASSANDRA_CONTAINER)

.PHONY: build
build:
	$(GRADLE) build

.PHONY: load
load:
	$(GRADLE) load

.PHONY: wait
wait: wait_mysql wait_cassandra

.PHONY: wait_mysql
wait_mysql:
	./scripts/wait_docker.sh $(MYSQL_CONTAINER) "$(MYSQL_LOG_STRING)"
	@echo "MySQL container get ready!!"

.PHONY: wait_cassandra
wait_cassandra:
	./scripts/wait_docker.sh $(CASSANDRA_CONTAINER) "$(CASSANDRA_LOG_STRING)"
	@echo "Cassandra container get ready!!"

.PHONY: seed
seed: seed_item

.PHONY: seed_item
seed_item:
	@docker exec -i $(MYSQL_CONTAINER) mysql -u $(MYSQL_USER) -p$(MYSQL_PASSWORD) < $(SEED_ITEM_SQL)
	@echo "Success to seed items!"

.PHONY: run
run:
	$(GRADLE) run
