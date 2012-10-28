(ns ^{:doc "A compendium is 'a collection of concise but detailed information
            about a particular subject. The Midje compendium contains
            the currently relevant facts about your program."}
  midje.ideas.compendium)

(def ^:dynamic *parse-time-fact-level* 0)

(defmacro given-possible-fact-nesting [& forms]
  `(binding [*parse-time-fact-level* (inc *parse-time-fact-level*)]
     ~@forms))

(defmacro working-on-nested-facts [& forms]
  ;; Make sure we don't treat this as a top-level fact
  `(binding [*parse-time-fact-level* (+ 2 *parse-time-fact-level*)]
     ~@forms))


(def fact-check-history (atom (constantly true)))

(defn dereference-history []
  @(ns-resolve 'midje.ideas.compendium @fact-check-history))
  

(defn wrap-with-check-time-fact-recording [true-name form]
  (if (= *parse-time-fact-level* 1)
    `(do (record-fact-check '~true-name)
         ~form)
    form))

(def compendium (atom {}))

(defn record-fact-existence [function]
  (intern 'midje.ideas.compendium (:midje/true-name (meta function)) function))

(defn record-fact-check [true-name]
  (reset! fact-check-history true-name))

(defn reset-compendium []
  (reset! compendium {}))

(defn compendium-contents
  []
  (map vals (vals compendium)))
  
