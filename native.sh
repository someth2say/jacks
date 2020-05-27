./target/yq-1.0.0-SNAPSHOT-runner -f /Users/jordisola/Projects/yq/src/test/resources/dco.yml -q "$.chapters[?(@.chapter_word=='mesh')]" -o json
