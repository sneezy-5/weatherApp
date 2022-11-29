// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.Repository
import ui.FF076470

@Composable
@Preview
fun App() {
  //  var text by remember { mutableStateOf("Hello, World!") }
    val API_KEY = "19f7eab5025caaaf3df40a93b8d8a3e9"
    val repository = Repository(API_KEY)

    MaterialTheme {
        MainScreen(repository)

    }
}

fun main() = application {
    Window(
        title = "Weather",
        onCloseRequest = ::exitApplication) {
        App()
    }
}
