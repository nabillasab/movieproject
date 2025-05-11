package com.example.moviesproject.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviesproject.ui.theme.MoviesProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesApp() {
    MoviesProjectTheme {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination?.route

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Movies Project") },
                    navigationIcon = {
                        if (currentDestination?.startsWith("detail") == true) {
                            IconButton(onClick = { navController.popBackStack()}) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "back")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            MovieNavHost(
                navHostController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        }
    }
}