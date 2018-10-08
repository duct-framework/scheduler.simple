(ns duct.scheduler.simple-test
  (:require [clojure.test :refer :all]
            [duct.scheduler.simple]
            [integrant.core :as ig]))

(deftest test-scheduler
  (let [a (atom 1)
        s (ig/init
           {:duct.scheduler/simple
            {:jobs [{:interval 0.10, :run #(swap! a (partial + 3))}
                    {:interval 0.15, :run #(swap! a (partial * 2))}
                    {:interval 0.05, :delay 0.25, :run #(swap! a inc)}]}})]
    (try
      (Thread/sleep 10)   ;; fudge factor
      (is (= 1 @a))
      (Thread/sleep 100)  ;; 0.10s [+ 3]
      (is (= 4 @a))
      (Thread/sleep 50)   ;; 0.15s [* 2]
      (is (= 8 @a))
      (Thread/sleep 50)   ;; 0.20s [+ 3]
      (is (= 11 @a))
      (Thread/sleep 50)   ;; 0.25s [inc]
      (is (= 12 @a))
      (finally
        (ig/halt! s)))

    (Thread/sleep 200)
    (is (= @a 12))))
