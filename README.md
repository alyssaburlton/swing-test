# swing-test

[![Build Status](https://github.com/alyssaburlton/swing-test/workflows/build/badge.svg)](https://github.com/alyssaburlton/swing-test/actions)
[<img src="https://img.shields.io/maven-central/v/com.github.alexburlton/swing-test.svg?label=latest%20release"/>](http://search.maven.org/#search|ga|1|g:com.github.alexburlton)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

swing-test is an idiomatic testing library for Java Swing components, developed in [Kotlin](https://kotlinlang.org/)

Test components with ease
-------------------------

Write simple assertions for Swing components, using the `StringSpec` style.

```kotlin
@Test
fun `Should enable Ok button once terms have been read`() {
    val form = MyForm()
    form.getChild<JButton>(text = "Ok").shouldBeDisabled()
    form.clickChild<JCheckBox>("I have read the terms and conditions")
    form.getChild<JButton>(text = "Ok").shouldBeEnabled()
}
```

Easily interact with whole layouts
----------------------------------

Use in-built finders to interact with child components without having to expose them directly. Out-the-box support for
narrowing by `class`, `name` (an unused property on Component that can be used as a testId) and `text`, plus the ability to specify your own lambda for more complex cases:

```kotlin
val myContainer = MyContainer()
myContainer.findChild<JButton>(text = "Cancel").shouldBeNull()
myContainer.getChild<JLabel>(name = "AvatarLabel").shouldBeVisible()

// Custom example
myContainer.clickChild<JRadioButton> { it.text.contains("foo") }
```

Simulate common interactions once you have a reference to the component you're after:

```kotlin
val label = myContainer.getChild<JLabel>()
label.doHover() //fires mouseEntered on listeners
label.doHoverAway() //fires mouseExited
label.doubleClick() //simulates mouseClicked/mouseReleased, with clickCount = 2

val table = myContainer.getChild<JTable>()
table.simulateKeyPress(KeyEvent.VK_ENTER) //Simulate the enter key being pressed
```

Easy debugging with component trees :evergreen_tree:
----------------------------------------------------

Many failing assertions will provide a useful error message that includes the component tree, for example:

```
com.github.alyssaburlton.swingtest.NoSuchComponentException: Found 0 JButtons. Text [null], name [null]. 

Component tree:

JPanel - FlowLayout
|- JRadioButton - "A"
```

These can also be generated manually via the extension method `Container.generateComponentTree()`

Snapshot Testing :camera_flash:
-------------------------------

swing-test provides a simple one-line approach for verifying that components match a generated `png` snapshot file. This
is particularly useful for testing components with custom painting logic, which can otherwise be hard to verify:

```kotlin
@Test
fun `Should match snapshot - locked`() {
    val achievement = AchievementMedal(AchievementStatus.LOCKED)
    achievement.shouldMatchImage("locked")
}

@Test
fun `Should match snapshot - red`() {
    val achievement = AchievementMedal(AchievementStatus.RED)
    medal.shouldMatchImage("red")
}
```

Snapshot images are automatically written
to `src/test/resources/__snapshots__/your/package/structure/test-class/imageName.png`, for example:

![image](https://user-images.githubusercontent.com/5732536/81931594-43270680-95e2-11ea-8a3f-aef01b91ab31.png)

- Running with the system property `updateSnapshots=true` allows the image files to be created for the first time, or
  updated locally in the event of a deliberate change.
- When a snapshot comparison fails, the failed image file is written out with the same name and a `failed.png`
  extension, to allow easy manual inspection.
- Due to pixel differences caused by running on different operating systems, you may optionally specify a `screenshotOs`
  system property, e.g. `screenshotOs=linux`. This will cause any screenshot tests to be skipped when run on a different
  operating system.

Fully interoperable with Java
-----------------------------

Although swing-test is developed with Kotlin in mind, it fully supports raw Java projects too:

```
Component myComponent = new CustomComponent();
SwingSnapshotsKt.shouldMatchImage(myComponent, "Default");

List<JButton> buttons=ComponentFindersKt.findAll(myComponent, JButton.class);

ComponentInteractionsKt.doHover(myComponent);

SwingDebug.dumpComponentTree(myComponent);
```