# GameTest Example

This mod is an example on how to use the GameTest framework as seen in this [video](https://youtu.be/vXaWOJTCYNg) with Quilt and QSL/QFAPI (if you're interested in how to do it with Fabric, there's some resemblance, but I won't cover it)

## Structure of my code

So I wanted to create a template that could be used in a clean way, so I have my main mod in `src/main` with the mod id `example`, and my tests in `src/testmod` with the mod id `example-testing`,
however you could have the two in a same sourceSet, but the rest of this explanation is going to focus on two.

### Gradle configuration

I added 
```groovy
sourceSets {
    testmod {
        java {
            srcDir 'src/testmod/java'
            compileClasspath += main.compileClasspath
            runtimeClasspath += main.runtimeClasspath
        }
    }
}
```
to register my working directory and also to link all my imported classpath (`net.minecraft`, `org.quiltmc`... things like that)

Added a new entry to my dependencies
```groovy
dependencies {
    // Other dependencies...
    
    testmodImplementation sourceSets.main.output
}
```

then I added also some entries to my loom configuration, so I can launch a Test Client and a Test Server (`gradlew runTestmodClient` and `gradlew runGameTestServer`)

```groovy
loom {
    // Other loom configuration
    // ...
    
    runs {
        testmodClient {
            client()
            configName = "Game test Client"
            vmArg "-Dquilt.game_test=true"
            source(sourceSets.testmod)
            runDir("run/game_test_client")
        }

        testmodServer {
            server()
            source(sourceSets.testmod)
        }
        gameTestServer {
            inherit testmodServer
            configName = "Game test Server"

            // Enable the game test runner.
            property("quilt.game_test", "true")
            property("quilt.game_test.report_file", "${project.buildDir}/game_test/report.xml")
            runDir("run/game_test")
        }
    }
}
```

and finally 
```groovy
afterEvaluate {
    tasks.test.dependsOn tasks.runGameTestServer
}
```
and that's it for my `build.gradle` nothing was changed for the other gradle files

Now you can use two sourceSets and got new toys to play with.

## Quilt configuration

We're gonna head to `src/testmod/resources/quild.mod.json`

Here in the `entrypoints` object I added my GameTests class at the `quilt:game_test` key and also made sure to not use the same mod id for the `main` mod and the `testmod` one 
(they're going to be different mods, I suggest doing `mainid` and `maindid-testing` for example)\
and that's the end of the Quilt configuration.

## Actual code explanation

Now let's head to [`GameTests.java`](/src/testmod/java/dev/renoux/example/GameTests.java), it's an implementation of the `QuiltGameTest` interface.

In it, I created multiple method with the decorator `@GameTest` and with `QuiltTestContext` as a parameter, it's all my tests: `checkForDiamond` and `checkForPistonWorking`.\
Tests use structure template, you can pass on in parameter of the decorator, like with `template = QuiltGameTest.EMPTY_STRUCTURE` (but that's the most bugged thing I ever tried so... if you got it working tell me, please I need this) 
but if no parameter is set it will search in the `testmod` resource folder at `data/<mod id>/structures/<name of this class>/<name of the method>.nbt` (all names being put in lowercase).

## In Game Uses

Now that I coded all my tests, I need to try them, if you remember from my Gradle configuration I created two tasks: `runTestmodClient` and `runGameTestServer`:\
- The Game Test Server, when launched, will start a server, generate a map, do all the tests by its own and terminate itself with the number of errors/success printed in the Terminal;
- The Testmod Client will look like a normal client but when generating a map, you will have access to the `/test` command.\
My first thing was to do the command `/test runall` to generate all the templates, if there were errors, I rebooted the game and launched the same map again,
powering the command block in front of a failed test to re-run it.

---
This is every thing you need to know to do basic testings, if you need more I invite you to read these two files: 
- [QSL:core/testing/api/package-info.java](https://github.com/QuiltMC/quilt-standard-libraries/blob/1.20/library/core/testing/src/main/java/org/quiltmc/qsl/testing/api/package-info.java) 
- [QSL:core/testing/api/game/package-info.java](https://github.com/QuiltMC/quilt-standard-libraries/blob/1.20/library/core/testing/src/main/java/org/quiltmc/qsl/testing/api/game/package-info.java)

They give further explanations and that's on them that I based my mod and research

And also look at all the methods on the `QuiltTestContext` class there's a lot as it extend the very good vanilla `GameTestHelper` class