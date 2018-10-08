(ns duct.scheduler.simple
  (:require [integrant.core :as ig])
  (:import [java.util.concurrent Executors ExecutorService TimeUnit]))

(defmethod ig/init-key :duct.scheduler/simple
  [_ {:keys [thread-pool-size jobs] :or {thread-pool-size 32}}]
  (let [executor (Executors/newScheduledThreadPool thread-pool-size)]
    (doseq [{:keys [delay interval run]} jobs]
      (let [delay-ms    (long (* 1000 (or delay interval)))
            interval-ms (long (* 1000 interval))]
        (.scheduleAtFixedRate executor
                              ^Runnable run
                              delay-ms
                              interval-ms
                              TimeUnit/MILLISECONDS)))
    executor))

(defmethod ig/halt-key! :duct.scheduler/simple [_ ^ExecutorService service]
  (.shutdownNow service))
