(ns pravda.spark
  (:require [pravda.core :as core])
  (:import [org.apache.spark Partition SparkContext TaskContext]))

(defrecord FilePartition
    [index file]
  ;; implement the spark Partition interface
  Partition
  (index [this] index))

(defn make-rdd
  [^org.apache.spark.api.java.JavaSparkContext sc s3 files]
  (let [sc (.sc sc)
        scala-nil scala.collection.immutable.Nil$/MODULE$
        classtag (scala.reflect.ClassTag$/MODULE$)
        ^scala.reflect.ClassTag clojure-classtag (.apply classtag clojure.lang.APersistentMap)
        ;; create a Spark RDD to source our events
        rdd (proxy [org.apache.spark.rdd.RDD] [sc scala-nil clojure-classtag]

              (compute [^Partition split-in ^TaskContext _]
                (let [^FilePartition split-in split-in
                      file (.file split-in)
                      ^java.util.Map part (core/build-partition s3 file)
                      java-conversions (scala.collection.JavaConversions$/MODULE$)]
                  (.iterator (.asScalaIterable java-conversions part))))

              (getPartitions []
                (->> files
                     (map-indexed (fn [index file] (->FilePartition index file)))
                     (into-array))))]
    ;; convert to a JavaRDD
    (org.apache.spark.api.java.JavaRDD. rdd clojure-classtag)))