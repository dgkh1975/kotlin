/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package templates

import templates.Family.*
import templates.SequenceClass.*

object Mapping : TemplateGroupBase() {

    init {
        val terminalOperationPattern = Regex("^\\w+To")
        defaultBuilder {
            if (sequenceClassification.isEmpty()) {
                if (terminalOperationPattern in signature)
                    sequenceClassification(terminal)
                else
                    sequenceClassification(intermediate, stateless)
            }
            specialFor(ArraysOfUnsigned) {
                sinceAtLeast("1.3")
                annotation("@ExperimentalUnsignedTypes")
            }
        }
    }

    val f_withIndex = fn("withIndex()") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        doc {
            """
            Returns a ${if (f == Sequences) f.mapResult else "lazy [Iterable]"} that wraps each ${f.element} of the original ${f.collection}
            into an [IndexedValue] containing the index of that ${f.element} and the ${f.element} itself.
            """
        }
        returns("Iterable<IndexedValue<T>>")
        body {
            """
            return IndexingIterable { iterator() }
            """
        }

        specialFor(Sequences) { returns("Sequence<IndexedValue<T>>") }
        body(Sequences) { """return IndexingSequence(this)""" }
    }

    val f_mapIndexed = fn("mapIndexed(transform: (index: Int, T) -> R)") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc {
            """
            Returns a ${f.mapResult} containing the results of applying the given [transform] function
            to each ${f.element} and its index in the original ${f.collection}.
            @param [transform] function that takes the index of ${f.element.prefixWithArticle()} and the ${f.element} itself
            and returns the result of the transform applied to the ${f.element}.
            """
        }
        typeParam("R")
        returns("List<R>")
        body(Iterables) {
            "return mapIndexedTo(ArrayList<R>(collectionSizeOrDefault(10)), transform)"
        }
        body(ArraysOfObjects, ArraysOfPrimitives, ArraysOfUnsigned) {
            "return mapIndexedTo(ArrayList<R>(size), transform)"
        }
        body(CharSequences) {
            "return mapIndexedTo(ArrayList<R>(length), transform)"
        }
        specialFor(Sequences) {
            inline(Inline.No)
            returns("Sequence<R>")
        }
        body(Sequences) {
            "return TransformingIndexedSequence(this, transform)"
        }
    }

    val f_map = fn("map(transform: (T) -> R)") {
        includeDefault()
        include(Maps, CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc {
            """
            Returns a ${f.mapResult} containing the results of applying the given [transform] function
            to each ${f.element} in the original ${f.collection}.
            """
        }

        specialFor(Iterables, Sequences, ArraysOfObjects, ArraysOfPrimitives, ArraysOfUnsigned) {
            sample("samples.collections.Collections.Transformations.map")
        }

        specialFor(Maps) {
            sample("samples.collections.Maps.Transformations.mapToList")
        }

        specialFor(CharSequences) {
            sample("samples.text.Strings.map")
        }

        typeParam("R")
        returns("List<R>")
        body(Iterables) {
            "return mapTo(ArrayList<R>(collectionSizeOrDefault(10)), transform)"
        }
        body(ArraysOfObjects, ArraysOfPrimitives, ArraysOfUnsigned, Maps) {
            "return mapTo(ArrayList<R>(size), transform)"
        }
        body(CharSequences) {
            "return mapTo(ArrayList<R>(length), transform)"
        }

        specialFor(Sequences) {
            inline(Inline.No)
            returns("Sequence<R>")
        }
        body(Sequences) {
            "return TransformingSequence(this, transform)"
        }
    }

    val f_mapNotNull = fn("mapNotNull(transform: (T) -> R?)") {
        include(Iterables, ArraysOfObjects, Sequences, Maps, CharSequences)
    } builder {
        inline()
        typeParam("R : Any")
        returns("List<R>")
        doc {
            """
            Returns a ${f.mapResult} containing only the non-null results of applying the given [transform] function
            to each ${f.element} in the original ${f.collection}.
            """
        }

        fun sampleClass(f: Family): String = when (f) {
            Maps -> "samples.collections.Maps.Transformations"
            else -> "samples.collections.Collections.Transformations"
        }
        sample("${sampleClass(f)}.mapNotNull")

        body {
            "return mapNotNullTo(ArrayList<R>(), transform)"
        }

        specialFor(Sequences) {
            inline(Inline.No)
            returns("Sequence<R>")
        }
        body(Sequences) {
            "return TransformingSequence(this, transform).filterNotNull()"
        }

    }

    val f_mapIndexedNotNull = fn("mapIndexedNotNull(transform: (index: Int, T) -> R?)") {
        include(Iterables, ArraysOfObjects, Sequences, CharSequences)
    } builder {
        inline()
        typeParam("R : Any")
        returns("List<R>")
        doc {
            """
            Returns a ${f.mapResult} containing only the non-null results of applying the given [transform] function
            to each ${f.element} and its index in the original ${f.collection}.
            @param [transform] function that takes the index of ${f.element.prefixWithArticle()} and the ${f.element} itself
            and returns the result of the transform applied to the ${f.element}.
            """
        }
        body {
            "return mapIndexedNotNullTo(ArrayList<R>(), transform)"
        }

        specialFor(Sequences) {
            inline(Inline.No)
            returns("Sequence<R>")
        }
        body(Sequences) {
            "return TransformingIndexedSequence(this, transform).filterNotNull()"
        }
    }

    val f_mapTo = fn("mapTo(destination: C, transform: (T) -> R)") {
        includeDefault()
        include(Maps, CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc {
            """
            Applies the given [transform] function to each ${f.element} of the original ${f.collection}
            and appends the results to the given [destination].
            """
        }
        annotation("@IgnorableReturnValue")
        typeParam("R")
        typeParam("C : MutableCollection<in R>")
        returns("C")

        body {
            """
            for (item in this)
                destination.add(transform(item))
            return destination
            """
        }
    }

    val f_mapIndexedTo = fn("mapIndexedTo(destination: C, transform: (index: Int, T) -> R)") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc {
            """
            Applies the given [transform] function to each ${f.element} and its index in the original ${f.collection}
            and appends the results to the given [destination].
            @param [transform] function that takes the index of ${f.element.prefixWithArticle()} and the ${f.element} itself
            and returns the result of the transform applied to the ${f.element}.
            """
        }
        annotation("@IgnorableReturnValue")
        typeParam("R")
        typeParam("C : MutableCollection<in R>")
        returns("C")

        body {
            fun checkOverflow(value: String) = if (f == Sequences || f == Iterables) "checkIndexOverflow($value)" else value
            """
            var index = 0
            for (item in this)
                destination.add(transform(${checkOverflow("index++")}, item))
            return destination
            """
        }
    }

    val f_mapNotNullTo = fn("mapNotNullTo(destination: C, transform: (T) -> R?)") {
        include(Iterables, ArraysOfObjects, Sequences, Maps, CharSequences)
    } builder {
        inline()
        typeParam("R : Any")
        typeParam("C : MutableCollection<in R>")
        returns("C")
        doc {
            """
            Applies the given [transform] function to each ${f.element} in the original ${f.collection}
            and appends only the non-null results to the given [destination].
            """
        }
        annotation("@IgnorableReturnValue")
        body {
            """
            forEach { element -> transform(element)?.let { destination.add(it) } }
            return destination
            """
        }
    }

    val f_mapIndexedNotNullTo = fn("mapIndexedNotNullTo(destination: C, transform: (index: Int, T) -> R?)") {
        include(Iterables, ArraysOfObjects, Sequences, CharSequences)
    } builder {
        inline()
        typeParam("R : Any")
        typeParam("C : MutableCollection<in R>")
        returns("C")
        doc {
            """
            Applies the given [transform] function to each ${f.element} and its index in the original ${f.collection}
            and appends only the non-null results to the given [destination].
            @param [transform] function that takes the index of ${f.element.prefixWithArticle()} and the ${f.element} itself
            and returns the result of the transform applied to the ${f.element}.
            """
        }
        annotation("@IgnorableReturnValue")
        body {
            """
            forEachIndexed { index, element -> transform(index, element)?.let { destination.add(it) } }
            return destination
            """
        }
    }

    val f_flatMap = fn("flatMap(transform: (T) -> Iterable<R>)") {
        includeDefault()
        include(Maps, CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc { "Returns a single list of all elements yielded from results of [transform] function being invoked on each ${f.element} of original ${f.collection}." }

        fun sampleClass(f: Family): String = when (f) {
            Maps -> "samples.collections.Maps.Transformations"
            else -> "samples.collections.Collections.Transformations"
        }

        sample("${sampleClass(f)}.flatMap")
        typeParam("R")
        returns("List<R>")
        body {
            "return flatMapTo(ArrayList<R>(), transform)"
        }
        specialFor(Sequences) {
            inline(Inline.No)
            since("1.4")
            annotation("@OptIn(kotlin.experimental.ExperimentalTypeInference::class)")
            annotation("@OverloadResolutionByLambdaReturnType")
            annotation("""@kotlin.jvm.JvmName("flatMapIterable")""")
            doc { "Returns a single sequence of all elements from results of [transform] function being invoked on each element of original sequence." }
            returns("Sequence<R>")
            body {
                "return FlatteningSequence(this, transform, Iterable<R>::iterator)"
            }
        }
    }

    val f_flatMapSequence = fn("flatMap(transform: (T) -> Sequence<R>)") {
        include(Sequences, Iterables, ArraysOfObjects, Maps)
    } builder {
        inline()
        doc { "Returns a single list of all elements yielded from results of [transform] function being invoked on each ${f.element} of original ${f.collection}." }
        sample("samples.collections.Collections.Transformations.flatMap")
        typeParam("R")
        returns("List<R>")
        body {
            "return flatMapTo(ArrayList<R>(), transform)"
        }
        if (f != Sequences) {
            since("1.4")
            annotation("@OptIn(kotlin.experimental.ExperimentalTypeInference::class)")
            annotation("@OverloadResolutionByLambdaReturnType")
            annotation("""@kotlin.jvm.JvmName("flatMapSequence")""")
        }
        specialFor(Sequences) {
            inline(Inline.No)
            doc { "Returns a single sequence of all elements from results of [transform] function being invoked on each element of original sequence." }
            returns("Sequence<R>")
            body {
                "return FlatteningSequence(this, transform, Sequence<R>::iterator)"
            }
        }
    }

    val f_flatMapTo = fn("flatMapTo(destination: C, transform: (T) -> Iterable<R>)") {
        includeDefault()
        include(Maps, CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc { "Appends all elements yielded from results of [transform] function being invoked on each ${f.element} of original ${f.collection}, to the given [destination]." }
        specialFor(Sequences) {
            since("1.4")
            annotation("@OptIn(kotlin.experimental.ExperimentalTypeInference::class)")
            annotation("@OverloadResolutionByLambdaReturnType")
            annotation("""@kotlin.jvm.JvmName("flatMapIterableTo")""")
        }
        annotation("@IgnorableReturnValue")
        typeParam("R")
        typeParam("C : MutableCollection<in R>")
        returns("C")
        body {
            """
            for (element in this) {
                val list = transform(element)
                destination.addAll(list)
            }
            return destination
            """
        }
    }

    val f_flatMapToSequence = fn("flatMapTo(destination: C, transform: (T) -> Sequence<R>)") {
        include(Sequences, Iterables, ArraysOfObjects, Maps)
    } builder {
        inline()
        doc { "Appends all elements yielded from results of [transform] function being invoked on each ${f.element} of original ${f.collection}, to the given [destination]." }
        if (f != Sequences) {
            since("1.4")
            annotation("@OptIn(kotlin.experimental.ExperimentalTypeInference::class)")
            annotation("@OverloadResolutionByLambdaReturnType")
            annotation("""@kotlin.jvm.JvmName("flatMapSequenceTo")""")
        }
        annotation("@IgnorableReturnValue")
        typeParam("R")
        typeParam("C : MutableCollection<in R>")
        returns("C")
        body {
            """
            for (element in this) {
                val list = transform(element)
                destination.addAll(list)
            }
            return destination
            """
        }
    }

    val f_flatMapIndexed = listOf(Iterables, Sequences).map { containerFamily ->
        val containerClass = containerFamily.name.dropLast(1)
        fn("flatMapIndexed(transform: (index: Int, T) -> $containerClass<R>)") {
            when (containerFamily) {
                Iterables -> include(Iterables, Sequences, ArraysOfObjects, ArraysOfPrimitives, ArraysOfUnsigned, CharSequences)
                Sequences -> include(Sequences, Iterables, ArraysOfObjects)
                else -> {}
            }
        } builder {
            inlineOnly()

            since("1.4")
            doc {
                """
                Returns a single ${f.mapResult} of all elements yielded from results of [transform] function being invoked on each ${f.element}  
                and its index in the original ${f.collection}.
                """
            }
            annotation("@OptIn(kotlin.experimental.ExperimentalTypeInference::class)")
            annotation("@OverloadResolutionByLambdaReturnType")
            if (family != ArraysOfUnsigned)
                annotation("""@kotlin.jvm.JvmName("flatMapIndexed$containerClass")""")
            sample("samples.collections.Collections.Transformations.flatMapIndexed")
            typeParam("R")
            returns("List<R>")
            body {
                "return flatMapIndexedTo(ArrayList<R>(), transform)"
            }
            specialFor(Sequences) {
                inline(Inline.No)
                returns("Sequence<R>")
                body {
                    "return flatMapIndexed(this, transform, $containerClass<R>::iterator)"
                }
            }
        }
    }


    val f_flatMapIndexedTo = listOf(Iterables, Sequences).map { containerFamily ->
        val containerClass = containerFamily.name.dropLast(1)
        fn("flatMapIndexedTo(destination: C, transform: (index: Int, T) -> $containerClass<R>)") {
            when (containerFamily) {
                Iterables -> include(Iterables, Sequences, ArraysOfObjects, ArraysOfPrimitives, ArraysOfUnsigned, CharSequences)
                Sequences -> include(Sequences, Iterables, ArraysOfObjects)
                else -> {}
            }
        } builder {
            inlineOnly()

            since("1.4")
            doc {
                """
                Appends all elements yielded from results of [transform] function being invoked on each ${f.element} 
                and its index in the original ${f.collection}, to the given [destination].
                """
            }
            annotation("@OptIn(kotlin.experimental.ExperimentalTypeInference::class)")
            annotation("@OverloadResolutionByLambdaReturnType")
            if (family != ArraysOfUnsigned)
                annotation("""@kotlin.jvm.JvmName("flatMapIndexed${containerClass}To")""")
            annotation("@IgnorableReturnValue")
            typeParam("R")
            typeParam("C : MutableCollection<in R>")
            returns("C")
            body {
                fun checkOverflow(value: String) = if (f == Sequences || f == Iterables) "checkIndexOverflow($value)" else value
                """
                var index = 0
                for (element in this) {
                    val list = transform(${checkOverflow("index++")}, element)
                    destination.addAll(list)
                }
                return destination
                """
            }
        }
    }

    val f_groupBy_key = fn("groupBy(keySelector: (T) -> K)") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc {
            """
            Groups ${f.element.pluralize()} of the original ${f.collection} by the key returned by the given [keySelector] function
            applied to each ${f.element} and returns a map where each group key is associated with a list of corresponding ${f.element.pluralize()}.

            The returned map preserves the entry iteration order of the keys produced from the original ${f.collection}.
            """
        }
        sample("samples.collections.Collections.Transformations.groupBy")
        sequenceClassification(terminal)
        typeParam("K")
        returns("Map<K, List<T>>")
        body { "return groupByTo(LinkedHashMap<K, MutableList<T>>(), keySelector)" }
    }

    val f_groupByTo_key = fn("groupByTo(destination: M, keySelector: (T) -> K)") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        typeParam("K")
        typeParam("M : MutableMap<in K, MutableList<T>>")
        doc {
            """
            Groups ${f.element.pluralize()} of the original ${f.collection} by the key returned by the given [keySelector] function
            applied to each ${f.element} and puts to the [destination] map each group key associated with a list of corresponding ${f.element.pluralize()}.

            @return The [destination] map.
            """
        }
        sample("samples.collections.Collections.Transformations.groupBy")
        annotation("@IgnorableReturnValue")
        sequenceClassification(terminal)
        returns("M")
        body {
            """
            for (element in this) {
                val key = keySelector(element)
                val list = destination.getOrPut(key) { ArrayList<T>() }
                list.add(element)
            }
            return destination
            """
        }
    }

    val f_groupBy_key_value = fn("groupBy(keySelector: (T) -> K, valueTransform: (T) -> V)") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        doc {
            """
            Groups values returned by the [valueTransform] function applied to each ${f.element} of the original ${f.collection}
            by the key returned by the given [keySelector] function applied to the ${f.element}
            and returns a map where each group key is associated with a list of corresponding values.

            The returned map preserves the entry iteration order of the keys produced from the original ${f.collection}.
            """
        }
        sample("samples.collections.Collections.Transformations.groupByKeysAndValues")
        sequenceClassification(terminal)
        typeParam("K")
        typeParam("V")
        returns("Map<K, List<V>>")
        body { "return groupByTo(LinkedHashMap<K, MutableList<V>>(), keySelector, valueTransform)" }
    }


    val f_groupByTo_key_value = fn("groupByTo(destination: M, keySelector: (T) -> K, valueTransform: (T) -> V)") {
        includeDefault()
        include(CharSequences, ArraysOfUnsigned)
    } builder {
        inline()
        specialFor(ArraysOfUnsigned) { inlineOnly() }

        typeParam("K")
        typeParam("V")
        typeParam("M : MutableMap<in K, MutableList<V>>")

        doc {
            """
            Groups values returned by the [valueTransform] function applied to each ${f.element} of the original ${f.collection}
            by the key returned by the given [keySelector] function applied to the ${f.element}
            and puts to the [destination] map each group key associated with a list of corresponding values.

            @return The [destination] map.
            """
        }
        sample("samples.collections.Collections.Transformations.groupByKeysAndValues")
        annotation("@IgnorableReturnValue")
        sequenceClassification(terminal)
        returns("M")
        body {
            """
            for (element in this) {
                val key = keySelector(element)
                val list = destination.getOrPut(key) { ArrayList<V>() }
                list.add(valueTransform(element))
            }
            return destination
            """
        }
    }

    val f_groupingBy = fn("groupingBy(crossinline keySelector: (T) -> K)") {
        include(Iterables, Sequences, ArraysOfObjects, CharSequences)
    } builder {
        since("1.1")
        inline()

        typeParam("T")
        typeParam("K")

        returns("Grouping<T, K>")

        doc {
            """
            Creates a [Grouping] source from ${f.collection.prefixWithArticle()} to be used later with one of group-and-fold operations
            using the specified [keySelector] function to extract a key from each ${f.element}.
            """
        }
        sample("samples.collections.Grouping.groupingByEachCount")

        body {
            """
            return object : Grouping<T, K> {
                override fun sourceIterator(): Iterator<T> = this@groupingBy.iterator()
                override fun keyOf(element: T): K = keySelector(element)
            }
            """
        }
    }

}
