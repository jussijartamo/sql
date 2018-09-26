(defproject simple "0.10.5"
  :dependencies [[org.clojure/clojure        "1.8.0"]
                 [org.clojure/clojurescript  "1.9.908"]
                 [reagent  "0.7.0"]
                 [re-frame "0.10.5"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel  "0.5.14"]
            [lein-cljfmt "0.6.1"]
            [lein-less "1.7.5"]]

  :hooks [leiningen.cljsbuild]
  
  :less {:source-paths ["resources/less"]
         :target-path  "resources/public/css/compiled"}

  :profiles {:dev {:cljsbuild
                   {:builds {:client {:figwheel     {:on-jsload "sql.core/run"}
                                      :compiler     {:main "sql.core"
                                                     :asset-path "js"
                                                     :optimizations :none
                                                     :source-map true
                                                     :source-map-timestamp true}}}}}

             :prod {:cljsbuild
                    {:builds {:client {:compiler    {:optimizations :advanced
                                                     :elide-asserts true
                                                     :pretty-print false}}}}}}

  :figwheel {:repl false
      :css-dirs ["resources/public/css"]
	     :server-port 4450}

  :clean-targets ^{:protect false} ["resources/public/js"]

  :cljsbuild {:builds {:client {:source-paths ["src"]
                                :compiler     {:output-dir "resources/public/js"
                                               :output-to  "resources/public/js/client.js"}}}})
