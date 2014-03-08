(ns async-watch.core
  (:require [clojure.core.async :as async :refer [go chan put! <!]]
            [clojure-watch.core :as watch]))

(def ^:private changes-watchers (atom '()))

(defn changes-in
  "Given a path or collection of paths as strings, sets up watchers in individual
threads for each path and then returns a channel that receives all filesystem
modifications within those paths."
  [paths]
  (let [paths   (if (coll? paths) paths (vector paths))
        channel (chan)]
    (swap! changes-watchers conj
           (future
             (watch/start-watch
              (mapv (fn [path] {:path        path
                                :event-types [:create :modify :delete]
                                :bootstrap   nil
                                :callback    (fn [op file] (put! channel [op file]))
                                :options     {:recursive true}})
                    paths))))
    channel))

(defn cancel-changes
  "Stops all watches and closes their channels"
  []
  (swap! changes-watchers (fn [threads]
                            (doseq [thread threads]
                              (when-not (future-cancelled? thread)
                                (future-cancel thread))))))






