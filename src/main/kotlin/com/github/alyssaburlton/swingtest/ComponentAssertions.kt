package com.github.alyssaburlton.swingtest

import io.kotest.matchers.shouldBe
import java.awt.Component

fun Component.shouldBeEnabled() {
    isEnabled shouldBe true
}

fun Component.shouldBeDisabled() {
    isEnabled shouldBe false
}

fun Component.shouldBeVisible() {
    isVisible shouldBe true
}

fun Component.shouldNotBeVisible() {
    isVisible shouldBe false
}
