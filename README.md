# Duct scheduler.simple [![Build Status](https://github.com/duct-framework/scheduler.simple/actions/workflows/test.yml/badge.svg)](https://github.com/duct-framework/scheduler.simple/actions/workflows/test.yml)

[Integrant][] methods for running functions periodically in a thread
pool. Part of the [Duct][] framework, but can be used in any
application that uses Integrant.

[integrant]: https://github.com/weavejester/integrant
[duct]:      https://github.com/duct-framework/duct

## Installation

Add the following dependency to your deps.edn file:

    duct/scheduler.simple {:mvn/version "0.1.0"}

Or to your Leiningen project file:

    [duct/server.http.jetty "0.1.0"]

## Usage

The scheduler runs jobs, which are zero-argument functions, at
periodic intervals. A configuration might look something like this:

```edn
{:duct.scheduler/simple
 {:jobs [{:interval 60   :run #ig/ref :example.job/every-minute}
         {:interval 3600 :run #ig/ref :example.job/every-hour]}

 :example.job/every-minute {}
 :example.job/every-hour   {}}
```

Where the example jobs are defined:

```clojure
(require '[integrant.core :as ig])

(defmethod ig/init-key :example.job/every-minute [_ _]
  #(println "A minute passed."))

(defmethod ig/init-key :example.job/every-hour [_ _]
  #(println "An hour passed."))
```

The `:duct.schedule/simple` key takes a collection of `:jobs`, and
optionally the `:thread-pool-size`, which defaults to 32.

Jobs are maps that have three keys:

* `:delay` (optional) - how long in seconds to delay before the first job
* `:interval`         - how long in seconds between the start of each job
* `:run`              - a zero-argument function run at each interval

This scheduler isn't suitable for more complex cron-like scheduling,
but is useful if you just want to periodically run cleanup, indexing
or other processing scripts at regular intervals.

## License

Copyright © 2025 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
