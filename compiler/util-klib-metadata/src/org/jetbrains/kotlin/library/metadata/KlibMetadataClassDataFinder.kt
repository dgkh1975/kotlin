/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.library.metadata

import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.library.metadataVersion
import org.jetbrains.kotlin.metadata.ProtoBuf.PackageFragment
import org.jetbrains.kotlin.metadata.deserialization.MetadataVersion
import org.jetbrains.kotlin.metadata.deserialization.NameResolver
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.serialization.deserialization.ClassData
import org.jetbrains.kotlin.serialization.deserialization.ClassDataFinder
import org.jetbrains.kotlin.serialization.deserialization.getClassId

class KlibMetadataClassDataFinder(
    private val fragment: PackageFragment,
    private val nameResolver: NameResolver,
    private val containerSource: KlibDeserializedContainerSource? = null
) : ClassDataFinder {
    val nameList = fragment.getExtension(KlibMetadataProtoBuf.className).orEmpty()

    override fun findClassData(classId: ClassId): ClassData? {

        val index = nameList.indexOfFirst { nameResolver.getClassId(it) == classId }
        if (index == -1) {
            return null
        }

        val foundClass = fragment.getClass_(index) ?: error("Could not find data for serialized class $classId")

        return ClassData(
            nameResolver,
            foundClass,
            containerSource?.klib?.metadataVersion ?: MetadataVersion.INVALID_VERSION,
            containerSource ?: SourceElement.NO_SOURCE
        )
    }
}
