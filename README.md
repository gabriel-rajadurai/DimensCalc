# DimenCalcs

This repo contains code to automatically generate dimen values based on the screen resolution. It generates dimens for the following resolutions,

- 320dp
- 400dp
- 480dp
- 600dp
- 720dp

**Usage:**

- Firstly, locate the "buildSrc" folder in the project root folder in this repo. Copy that into your own project's root directory. This folder contains the code for dimen generation.
- Next write a simple gradle task in the app gradle file as shown below,

```groovy
task dimenCalc() {
    DimensCalc.calcDimens(project.projectDir.absolutePath)
}
```

**Note:**

- This task needs a base *dimens.xml* file created in the values folder. Also make sure, the base dimens has values for a *360dp screen size.* The formula used is based around that.
- If you want to calculate dimens for a module inside your project, see the following code block,

```groovy
task dimenCalc() {
    DimensCalc.calcDimens(project(':module-name').projectDir.absolutePath)
}
```

Make sure to write the proper "module-name" in the same format as shown above. Note that the single quotes and colon symbol are mandatory.


That's all. Just run the gradle script and see all the required folders, values resource files and the values themselves generated automatiaclly.


**What exactly is happening when you run this task?:**

- The *'dimens.xml'* inside the values folder is considered as dimens for a shortest width screen size of *360dp*.
- The dimens for other screen sizes are calculated as shown below,

For a particular screen size of ``` X dp ```, its dimen values are calculated using the following formula

```java

dimenValue /*X dp resolution*/ = dimenValue /*360 dp resolution*/ * X/360

```
