java -jar target/yq-1.0.0-SNAPSHOT-runner.jar -f /Users/jordisola/Projects/yq/src/test/resources/dco.yml -q "$.chapters[?(@.chapter_word=='mesh')]" -o json
