package com.example.mybike.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class SuffixTransformer(private val suffix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val result = text + AnnotatedString(suffix)

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val transformedOffsets = result
                    .mapIndexedNotNull { index, c ->
                        index
                            .takeIf { c.isDigit() }
                            // convert index to an offset
                            ?.plus(1)
                    }
                    // We want to support an offset of 0 and shift everything to the right,
                    // so we prepend that index by default
                    .let { offsetList ->
                        listOf(0) + offsetList
                    }

                return transformedOffsets[offset]
            }

            override fun transformedToOriginal(offset: Int): Int =
                result
                    // This creates a list of all separator offsets
                    .mapIndexedNotNull { index, c ->
                        index.takeIf { !c.isDigit() }
                    }
                    // We want to count how many separators precede the transformed offset
                    .count { separatorIndex ->
                        separatorIndex < offset
                    }
                    // We find the original offset by subtracting the number of separators
                    .let { separatorCount ->
                        offset - separatorCount
                    }
        }
        return TransformedText(result, offsetTranslator)
    }

    private fun calculateOutputOffsets(output: String): List<Int> {
        val digitOffsets = output.mapIndexedNotNull { index, char ->
            // +1 because we're looking for offsets, not indices
            index.takeIf { char.isDigit() }?.plus(1)
        }

        return listOf(0) + digitOffsets.dropLast(1) + output.length
    }


    private fun calculateSeparatorOffsets(output: String): List<Int> {
        return output.mapIndexedNotNull { index, c ->
            index.takeUnless { c.isDigit() }
        }
    }
}