(ns duct.scheduler.simple-test
  (:require [clojure.test :refer :all]
            [duct.scheduler.simple]
            [integrant.core :as ig]))

(deftest test-scheduler
  (let [a (atom 1)
        s (ig/init {:duct.scheduler/simple
                    {:jobs [{:delay 0.10, :run #(swap! a inc)}
                            {:delay 0.15, :run #(swap! a (partial * 2))}]}})]
    (try
      (Thread/sleep 10)
      (is (= 1 @a))
      (Thread/sleep 100)
      (is (= 2 @a))
      (Thread/sleep 50)
      (is (= 4 @a))
      (Thread/sleep 50)
      (is (= 5 @a))
      (finally
        (ig/halt! s)))

    (is (= @a 5))))
