# Makefile

# Gradleコマンドの定義
GRADLE = ./gradlew

# デフォルトのターゲットを定義
.PHONY: all
all: build load run

# ビルドタスク
.PHONY: build
build:
	$(GRADLE) build

# ロードタスク
.PHONY: load
load:
	$(GRADLE) load

# ランタスク
.PHONY: run
run:
	$(GRADLE) run