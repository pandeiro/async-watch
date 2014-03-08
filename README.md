# async-watch

Yet another filesystem watcher for Clojure. This one uses
[Clojure-Watch](https://github.com/derekchiang/Clojure-Watch/)
(a filesystem watcher that uses Java 7's WatchEvent API) +
[Clojure.core.async](https://github.com/clojure/core.async/)'s
channel abstraction for CPU efficiency and a dead-simple API.

## Clojars

    [async-watch "0.1.0"]

## Usage

The `changes-in` function takes a single path or a collection of paths:

```clojure
(require '[async-watch.core :refer [changes-in cancel-changes]])

(let [changes (changes-in ["src" "resources"])]
  (go (while true
        (let [[op filename] (<! changes)]
          ;; op will be one of :create, :modify or :delete
          (println filename "just experienced a" op)))))
```

Internally, each changes channel is fed by a running Clojure-Watch in a separate future.
To cancel these futures, run `(cancel-changes)`.

## Tests

    lein test

## License

Copyright © 2014 Murphy McMahon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
