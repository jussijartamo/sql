(defproject sql "1.0.0"
  :dependencies [[org.clojure/clojure        "1.9.0"]
                 [org.clojure/core.match "0.3.0-alpha5"]

                                  ;[org.danielsz/system "0.4.1"]
                                  ;[com.stuartsierra/component "0.3.2"]
                                  ;[reloaded.repl "0.2.4"]
                 [ring "1.7.0"]

                 [org.eclipse.jetty.websocket/websocket-server "9.2.24.v20180105"]]

  :auto-clean false

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel  "0.5.14"]
            [lein-cljfmt "0.6.1"]
            [lein-less "1.7.5"]
            [lein-ancient "0.6.15"]
            [lein-kibit "0.1.6"]]

  :main sql.core

  :less {:source-paths ["resources/less"]
         :target-path  "resources/public/css/compiled"}

  :aot [com.stuartsierra.dependency com.stuartsierra.component sql.ws.listener]

  :jvm-opts ^:replace ["-XX:MaxJavaStackTraceDepth=0"]

  :profiles {:generate-test-data {:main generate-test-data
                                  :target-path "target-example-datasource"
                                  :source-paths   ["example-datasource/clj"]
                                  :resource-paths ["example-datasource/resources"]
                                  :dependencies   [[org.postgresql/postgresql "42.2.5"]
                                                   [org.clojure/data.json "0.2.6"]]}
             :example-datasource            {:main example-datasource
                                             :dependencies   [[org.clojure/core.async "0.4.474"]
                                                              [org.clojure/data.json "0.2.6"]
                                                              [org.postgresql/postgresql "42.2.5"]
                                                              [ru.yandex.qatools.embed/postgresql-embedded "2.9"]]
                                             :target-path "target-example-datasource"
                                             :source-paths   ["example-datasource/clj"]
                                             :resource-paths ["example-datasource/resources"]}
             :frontend {:dependencies [[org.clojure/clojurescript  "1.10.339"]
                                       [reagent  "0.8.1"]
                                       [re-frisk "0.5.3"]
                                       [fentontravers/websocket-client "0.4.8"]
                                       [re-frame "0.10.6"]]
                        :hooks [leiningen.cljsbuild]
                        :cljsbuild
                        {:builds {:client {:figwheel     {:on-jsload "sql.core/run"}
                                           :compiler     {:main "sql.core"
                                                          :preloads [re-frisk.preload]
                                                          :asset-path "js"
                                                          :optimizations :none
                                                          :source-map true
                                                          :source-map-timestamp true}}}}}
            
             :server {:dependencies [[org.clojure/core.async "0.4.474"]
                                     [org.clojure/data.json "0.2.6"]
                                     [com.clojure-goes-fast/clj-memory-meter "0.1.2"]
                                     [org.postgresql/postgresql "42.2.5"]]}
             :prod {:cljsbuild
                    {:builds {:client {:compiler    {:optimizations :advanced
                                                     :elide-asserts true
                                                     :pretty-print false}}}}}}

  :source-paths ["src/clj"]

  :figwheel {:repl false
             :css-dirs ["resources/public/css"]
             :server-port 4450}

  :clean-targets ^{:protect false} ["resources/public/js"]

  :aliases {"server" ["with-profile" "server" "run"]
            "frontend" ["with-profile" "frontend" "figwheel"]
            "example-datasource" ["with-profile" "example-datasource" "run"]
            "generate-test-data" ["with-profile" "generate-test-data" "run"]}

  :cljsbuild {:builds {:client {:source-paths ["src/cljs"]
                                :compiler     {:output-dir "resources/public/js"
                                               :output-to  "resources/public/js/client.js"}}}})
