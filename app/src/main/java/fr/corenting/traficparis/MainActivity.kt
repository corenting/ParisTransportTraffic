package fr.corenting.traficparis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.traffic.TrafficUiState
import fr.corenting.traficparis.traffic.TrafficViewModel
import fr.corenting.traficparis.ui.components.AboutDialog
import fr.corenting.traficparis.ui.components.FilterMenu
import fr.corenting.traficparis.ui.components.TrafficList
import fr.corenting.traficparis.ui.theme.ParisTransportTrafficTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParisTransportTrafficTheme {
                val viewModel: TrafficViewModel = viewModel(
                    factory = TrafficViewModel.factory(applicationContext),
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
                val filters by viewModel.filters.collectAsStateWithLifecycle()

                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
                val snackbarHostState = remember { SnackbarHostState() }
                val downloadErrorMessage = stringResource(R.string.download_error)
                var showAboutDialog by remember { mutableStateOf(false) }

                LaunchedEffect(uiState) {
                    if (uiState is TrafficUiState.Error) {
                        snackbarHostState.showSnackbar(downloadErrorMessage)
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        MainAppBar(
                            title = stringResource(R.string.app_name),
                            filters = filters,
                            onFilterToggle = viewModel::toggleFilter,
                            onAboutClick = { showAboutDialog = true },
                            scrollBehavior = scrollBehavior,
                        )
                    }
                ) { innerPadding ->
                    TrafficList(
                        uiState = uiState,
                        isRefreshing = isRefreshing,
                        filters = filters,
                        onRefresh = viewModel::refresh,
                        modifier = Modifier.padding(innerPadding),
                    )
                }

                if (showAboutDialog) {
                    AboutDialog(onDismiss = { showAboutDialog = false })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    filters: Map<LineType, Boolean>,
    onFilterToggle: (LineType) -> Unit,
    onAboutClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    var filterMenuExpanded by remember { mutableStateOf(false) }

    ParisTransportTrafficTheme(darkTheme = true) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            title = { Text(title) },
            actions = {
                Box {
                    IconButton(onClick = { filterMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = stringResource(R.string.filter),
                        )
                    }
                    FilterMenu(
                        filters = filters,
                        onToggle = onFilterToggle,
                        expanded = filterMenuExpanded,
                        onDismiss = { filterMenuExpanded = false },
                    )
                }
                IconButton(onClick = onAboutClick) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(R.string.about),
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}
