<p align="center"> 
   <img height="200" style="float: center;" src=".graphics/cng_icon_512px.png"/> 
</p>

<h1 align="center"> 
    Compose/Navigation/Generator
</h1>

> :warning: This library is still under development and not considered stable!

### Content  <!-- omit in toc -->
- [Introduction](#introduction)
- [Usage](#usage)
    - [Example: Single destination without parameters](#example-single-destination-without-parameters)
    - [Example: Multiple destinations without parameters](#example-multiple-destinations-without-parameters)
    - [Example: Multiple destinations with parameters](#example-multiple-destinations-with-parameters)
- [Features](#features)
- [Setup](#setup)
- [Bugs](#bugs)
- [Used Libraries](#used-libraries)


## Introduction
***Compose/Navigation/Generator*** | ***C/N/G*** is a KSP library which automatically generates navigation support for [Jetpack Compose for Android](https://https://developer.android.com/jetpack/compose). Unlike the *old* XML-based [navigation graphs](https://developer.android.com/guide/navigation/navigation-getting-started#create-nav-graph), the *new* [Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) does not automatically generate naviagtion functions for the destination within and between graphs. Generally, navigation for Jetpack Compose is controlled only by code, which currently has to be written from scratch by the developer for each project.

***C/N/G*** uses Annotations and [Kotlin Symbol Processing (KSP)](https://kotlinlang.org/docs/ksp-overview.html) to generate as `SetupNavHost` function, as well as extension functions to the `NavHostController`, which trigger the navigation to a declared destination.

## Usage

- All composable functions annotated with `@Destination` will be reachable via navigation
- The *Home-Destination*, reuired by compose-navigation, can be annotated with `@Home`
- Within the setup of the composable scope (usually within the ComposeActivity), call the generated `SetupNavHost` function
- Use the generated extension functions `navHostController.navigateTo<<Destination>>(...)` to navigate to the Destinations

```kotlin
// Annotate a destination you want to be able to navigate to with @Destination
@Destination
@Home
@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Hello World")
        /*...*/
    }
}

/***** GENERATED *****/
// A setup function for compose-navigaiton is generated, using your destinations
@Composable
public fun SetupNavHost(navController: NavHostController): Unit {
  NavHost(navController = navController, startDestination = "HomeScreen")
  {
    composable("HomeScreen") {
      Log.d(TAG, "Navigating to HomeScreen") // If enabled, a logging call is inserted
      HomeScreen()
    }
  }
}

/***** GENERATED *****/
// A navigation function (extending NavHostController) is generated
public fun NavHostController.navigateToHomeScreen(): Unit {
  navigate("HomeScreen")
}

// The destination can be navigated to from every composable function, using the NavHostController
@Composable
fun SomeComposableFunction(navController: NavHostController) {
    /*...*/
    navController.navigateToHomeScreen()
}
```

### Example: Single destination without parameters

A single destination without parameters. The function is annotated with `@Destination` which all navigable compose functions must be. Also, compose expects one destination (per nav-graph), to be the **home** destination which is the default when the nav-graph is initialized. The `@Home` destination must be used on a **single** `@Destination` function to inform ***C/N/G*** to treat it as the home-destination.

<details>
  <summary>Source</summary>

  ```kotlin
  @Destination
  @Home
  @Composable
  fun HomeScreen() {
      Column(modifier = Modifier.fillMaxSize()) {
          Text("Hello World")
          /*...*/
      }
  }
  ```
</details>

The `SetupNavHost` function itself is a composable function and should be called during initialization of the composeable scope. Usualy this is done during setup of the parent compose activity. The function takes a `NavHostController` as parameter, which can also be passed on to composables, if required.

The `navHostController.navigateTo<<Destination>>()` function is created for every destination and extends the existing `NavHostController` class. It can therefore be used from everywhere, a `NavHostController` is available.

<details>
  <summary>Generated</summary>

```kotlin
// Generated
@Composable
public fun SetupNavHost(navController: NavHostController): Unit {
  NavHost(navController = navController, startDestination = "HomeScreen")
  {
    composable("HomeScreen") {
      Log.d(TAG, "Navigating to HomeScreen") // If enabled, a logging call is inserted
      HomeScreen()
    }
  }
}

private const val TAG: String = "NavHost" // If enabled, a logging tag is inserted
``` 

```kotlin
// Generated
public fun NavHostController.navigateToHomeScreen(): Unit {
  navigate("HomeScreen")
}
```
</details>

<details>
  <summary>Usage</summary>

```kotlin
@Composable
fun SomeComposableFunction(navController: NavHostController) {
    /*...*/
    navController.navigateToHomeScreen()
}
```
</details>

### Example: Multiple destinations without parameters

<details>
  <summary>Source</summary>

```kotlin
@Destination
@Home
@Composable
fun HomeScreen() { 
  /*...*/
}

@Destination
@Composable
fun DetailScreen() {
  /*...*/

}
```
</details>

<details>
  <summary>Generated</summary>

```kotlin
// Generated
@Composable
public fun SetupNavHost(navController: NavHostController): Unit {
  NavHost(navController = navController, startDestination = "HomeScreen")
  {
    composable("HomeScreen") {
      Log.d(TAG, "Navigating to HomeScreen")
      HomeScreen()
    }
    composable("DetailScreen") {
      Log.d(TAG, "Navigating to DetailScreen")
      DetailScreen()
    }
  }
}

private const val TAG: String = "NavHost"
``` 

```kotlin
// Generated
public fun NavHostController.navigateToHomeScreen(): Unit {
  navigate("HomeScreen")
}
```
</details>

<details>
  <summary>Usage</summary>

```kotlin
@Composable
fun SomeComposableFunction(navController: NavHostController) {
    navController.navigateToHomeScreen()
    navController.navigateToDetailScreen()
}
```
</details>

### Example: Multiple destinations with parameters

***C/N/G*** supports nullable- and non-nullable parameters as navigation arguments.

If a destination only useses non-nullable parameters, a "non-nullable" navigation path will be generated:
```kotlin
val navigationPath = "Destination/{arg1}/{arg2}/{arg3}" // ...
```

If one or more of the arguments are nullable, a "nullable" navigation path will be generated:
```kotlin
val navigationPath = "Destination?arg1&arg2&arg3" // ...
```

<details>
  <summary>Source</summary>

  ```kotlin
  @Destination
  @Home
  @Composable
  fun HomeScreen() { 
    /*...*/
  }

  @Destination
  @Composable
  fun DetailScreen(name: String, age: Int) {
    /*...*/
  }

  @Destination
  @Composable
  fun UltraDetailScreen(name: String, age: Int, height: Double? = 1.90) {
    /*...*/
  }
  ```
</details>

<details>
  <summary>Generated</summary>

  ```kotlin
  // Generated
  @Composable
  public fun SetupNavHost(navController: NavHostController): Unit {
    NavHost(navController = navController, startDestination = "HomeScreen")
    {
      // Simple, no-argument destination
      composable("HomeScreen") {
        Log.d(TAG, "Navigating to HomeScreen")
        HomeScreen()
      }
      // Destination with exclusivly non-nullable arguments
      composable("DetailScreen/argName/argAge", arguments = listOf(
        // Type and properties of navArgs is automatically determined
        navArgument("argName"){
          nullable = false 
          type = NavType.fromArgType("String")
        },
        navArgument("argAge"){
          nullable = false 
          type = NavType.fromArgType("Int")   
        },
      )) { backStackEntry ->
        // Read arguments from backstack
        val argName = backStackEntry.arguments?.getString("argName")  
        val argAge = backStackEntry.arguments?.getInt("argAge")       

        // Non-null is required for such parameters
        requireNotNull(argName)   
        requireNotNull(argAge)    

        Log.d(TAG, "Navigating to DetailScreen")

        // Destination is called with provided parameters
        DetailScreen(name=argName, age=argAge) 
      }
      // Destination with nullable and non-nullable arguments
      composable("UltraDetailScreen?argName={name}&argAge={age}&argHeight={height}", arguments = listOf(
        // Type and properties of navArgs is automatically determined
        navArgument("argName"){
          nullable = false
          type = NavType.fromArgType("String")
        },
        navArgument("argAge"){
          nullable = false
          type = NavType.fromArgType("Int")
        },
        navArgument("argHeight"){
          nullable = true
          type = NavType.fromArgType("Double")
        },
      )) { backStackEntry ->
          // Read arguments from backstack
        val argName = backStackEntry.arguments?.getString("argName")
        val argAge = backStackEntry.arguments?.getInt("argAge")
        val argHeight = backStackEntry.arguments?.getDouble("argHeight")

        // Non-null is required for such parameters
        requireNotNull(argName) 
        requireNotNull(argAge)  

        Log.d(TAG, "Navigating to UltraDetailScreen")

        // Destination is called with provided parameters
        UltraDetailScreen(name=argName, age=argAge, height=argHeight)
      }
    }
  }

  private const val TAG: String = "NavHost"
  ```

  ```kotlin
  // Generated
  public fun NavHostController.navigateToHomeScreen(): Unit {
    navigate("HomeScreen")
  }

  // Generated
  public fun NavHostController.navigateToDetailScreen(name: String, age: Int): Unit {
    navigate("DetailScreen/$name/$age")
  }

  // Generated
  public fun NavHostController.navigateToUltraDetailScreen(name: String,  age: Int,  height: Double?): Unit {
    navigate("UltraDetailScreen?arg_name=$name&arg_age=$age&arg_height=$height")
  }
  ```

</details>

<details>
  <summary>Usage</summary>

  ```kotlin
  @Composable
  fun SomeComposableFunction(navController: NavHostController) {
      navController.navigateToHomeScreen()
      navController.navigateToDetailScreen(name = "Steffen", age = 27)
      navController.navigateToUltraDetailScreen(name = "Steffen", age = 27, height = null)
  }
  ```
</details>


## Features
- Generates the `SetupNavHost` function
- Generates navigation functions for each destination
- Parses and wraps all parameters in the appropriate navigation paths
- *more coming soon!*

## Setup
*TBD...*

## Bugs
*Lots...*

## Used Libraries
- [KSP](https://github.com/google/ksp)
- [Auto-Service-KSP](https://github.com/ZacSweers/auto-service-ksp)
- [KotlinPoet](https://square.github.io/kotlinpoet/) :heart:
- [Kotlin Compile Testing](https://github.com/tschuchortdev/kotlin-compile-testing)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) 