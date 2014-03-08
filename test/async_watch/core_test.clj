(ns async-watch.core-test
  (:require [clojure.test :refer :all]
            [async-watch.core :refer :all]
            [clojure.java.io :as io]
            [clojure.core.async :as async :refer [go <! <!!]]))

;; Basic file operations
(defn create-file! [path] (.createNewFile (io/file path)))

(defn remove-file! [path] (.delete (io/file path)))

(defn touch-file! [path] (.setLastModified (io/file path)
                                           (System/currentTimeMillis)))

;; Test directory filepath helper
(defn filepath [path] (str "test-dir/" path))


(def DEREF_WAIT 20)


;; Tests
(deftest touch-test
  (let [f (filepath "foo/example.txt")
        c (changes-in (filepath "foo"))
        _ (do
            (Thread/sleep 1000)
            (touch-file! f))
        r (future (<!! c))]
    (is (= @r [:modify f]))))

(deftest create-delete-test
  (let [f (filepath "foo/temp.txt")
        c (changes-in (filepath "foo"))
        _ (do
            (Thread/sleep 1000)
            (create-file! f)
            (remove-file! f))
        r (atom #{})]
    (go (while true
          (let [item (<! c)]
            (swap! r conj (first item)))))
    (Thread/sleep 1000)
    (is (= @r #{:create :delete}))))

(deftest directory-test
  (let [f (filepath "foo/example.txt")
        c (changes-in "bar")
        _ (do
            (Thread/sleep 1000)
            (touch-file! f))
        r (future (<!! c))]
    (is (= (deref r DEREF_WAIT :blocked) :blocked))))

(deftest cancel-changes-test
  (let [f (filepath "foo/example.txt")
        c (changes-in "foo")
        _ (do
            (Thread/sleep 1000)
            (cancel-changes)
            (touch-file! f))
        r (future (<!! c))]
    (is (= (deref r DEREF_WAIT :blocked) :blocked))))
