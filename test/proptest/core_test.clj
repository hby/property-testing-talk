(ns proptest.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [proptest.core :refer :all]))


;;; Open in Light Table and eval these in turn

gen/int

(gen/sample gen/int)

(gen/sample gen/int 20)

(gen/sample gen/nat)

(gen/sample (gen/choose 1 6))

(gen/sample gen/string)

(gen/sample gen/char-alphanumeric)

(gen/sample gen/char-ascii)

(gen/sample gen/string-ascii)

(drop 9 (gen/sample gen/any))

(gen/sample (gen/frequency [[5 gen/int] [3 gen/char-ascii] [2 gen/boolean]]))

(def mostly-ints (gen/frequency [[9 gen/int] [1 (gen/return nil)]]))
(gen/sample mostly-ints)

(gen/sample (gen/vector gen/int))

(gen/sample (gen/vector (gen/choose 1 6) 2))

(gen/sample (gen/map gen/keyword (gen/vector (gen/choose 0 10))))

(gen/sample (gen/fmap str gen/int))

(gen/sample (gen/fmap keyword gen/string-alphanumeric))

(gen/sample gen/keyword)

(gen/sample (gen/elements [:opt1 :opt2 :opt3]))

(def joescii (gen/elements [\j \J \o \O \e \E]))
(gen/sample joescii)

(gen/sample (gen/bind (gen/such-that not-empty (gen/vector gen/int))
                      #(gen/tuple (gen/return %) (gen/elements %))))

;; Shrinking

(defn ascending?
  [coll]
  (every? (fn [[a b]] (<= a b))
          (partition 2 1 coll)))

(def bad-property
  (prop/for-all [v (gen/vector gen/int)]
    (ascending? v)))

(tc/quick-check 100 bad-property)


;; Famous find from Clojure 1.5

(-> #{}
    (conj 109)
    (conj -110)
    transient
    (disj! -110)
    persistent!
    (conj -110))

(-> #{}
    (conj 109)
    (conj -110)
    (disj -110)
    (conj -110))

