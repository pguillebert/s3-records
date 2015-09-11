(defproject pravda "0.1.0-SNAPSHOT"
  :description "Stores streams of events in s3"
  :url "http://github.com/pguillebert/pravda"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [factual/s3-journal "0.1.3" :exclusions [com.fasterxml.jackson.core/jackson-core
                                                          com.fasterxml.jackson.core/jackson-annotations
                                                          com.fasterxml.jackson.core/jackson-databind]]
                 [clj-aws-s3 "0.3.10" :exclusions [com.amazonaws/aws-java-sdk]]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [org.slf4j/slf4j-log4j12 "1.7.12"]
                 [log4j "1.2.17"]]
  :profiles {:dev {:aot [pravda.spark pravda.spark-test]
                   :dependencies [[yieldbot/flambo "0.6.0"]]}
             :provided {:dependencies [[org.apache.spark/spark-core_2.10 "1.4.1"]]}}
  :global-vars {*warn-on-reflection* true})
