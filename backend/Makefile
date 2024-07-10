# Makefile

GRADLE = ./gradlew
MYSQL_LOG_STRING=Ready for start up
MYSQL_CONTAINER=mysql-1
CASSANDRA_LOG_STRING=Startup complete
CASSANDRA_CONTAINER=cassandra-1

.PHONY: all
all: build load run

.PHONY: init
init: run_container wait build load run

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

.PHONY: wait_cassandra
wait_cassandra:
	./scripts/wait_docker.sh $(CASSANDRA_CONTAINER) "$(CASSANDRA_LOG_STRING)"

.PHONY: run_container
run_container:
	docker-compose up -d
# .PHONY: seed
# seed: seed_customer seed_item
#
# .PHONY: seed_customer
# seed_customer:
#
# .PHONY: seed_item:
# seed_item:
#
# ランタスク
.PHONY: run
run:
	$(GRADLE) run
