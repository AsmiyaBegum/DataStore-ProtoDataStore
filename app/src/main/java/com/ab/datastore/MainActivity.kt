package com.ab.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.ab.datastore.data.Detail
import com.ab.datastore.ui.theme.DataStoreTheme


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            DataStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    contentColor = Color.Black
                ) {
                    DataStoreHome(viewModel)
                }
            }
        }
    }
}

@Composable
fun DataStoreHome(viewModel : MainViewModel){
    val tabState = remember {mutableStateOf(Pair(0,"DataStore"))}
    Box(modifier = Modifier
        .fillMaxSize()) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp).background(androidx.compose.material.MaterialTheme.colors.primary), contentAlignment = Alignment.CenterStart){
                Text(text = "DataStore And ProtoDataStore", color = Color.White, modifier = Modifier.padding(start = 15.dp))
            }
            TabRow(selectedTabIndex = tabState.value.first) {
                Tab(selected = tabState.value.first == 0, onClick = {
                    tabState.value = Pair(0,"DataStore")
                },modifier = Modifier.padding(10.dp)) {
                    Text(text = "DataStore")
                }

                Tab(selected = tabState.value.first == 1, onClick = {
                    tabState.value = Pair(1,"ProtoDataStore")
                },modifier = Modifier.padding(8.dp)) {
                    Text(text = "ProtoDataStore")
                }
            }

            TabsContent(tabState = tabState.value.second,viewModel)
        }

    }
}

@Composable
fun TabsContent(tabState : String,viewModel: MainViewModel) {
    when (tabState) {
        "DataStore" -> DataStore(viewModel = viewModel)
        "ProtoDataStore" -> ProtoDataStore(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStore(viewModel: MainViewModel,modifier: Modifier = Modifier) {


    val state = viewModel.readFromDataStore.collectAsState(initial = "")

    var text by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            TextField(
                value = text,
                onValueChange = { text = it }, modifier = Modifier.align(CenterHorizontally),

               )
            Text(
                text = "${state.value}",
                modifier = modifier
                    .align(CenterHorizontally)
                    .padding(20.dp),
                maxLines = 5
            )

            Button(content = {
                       Text(text = "Submit")
            },onClick = {
                viewModel.saveToDataStore(text)
            },modifier = modifier.align(CenterHorizontally))


        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtoDataStore(viewModel: MainViewModel,modifier: Modifier = Modifier) {


    val state = viewModel.readFromProtoDataStore.collectAsState(initial = Detail.getDefaultInstance())

    var name by remember {
        mutableStateOf("")
    }

    var age by remember {
        mutableStateOf("")
    }

    var isCompleted by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            androidx.compose.material.OutlinedTextField(
                value = name,
                onValueChange = { name = it }, modifier = Modifier.align(CenterHorizontally), placeholder = { Text(
                    text = "Name"
                )})

            androidx.compose.material.OutlinedTextField(
                value = age,
                onValueChange = { age = it  }, modifier = Modifier.align(CenterHorizontally).padding(top = 15.dp), placeholder = { Text(
                    text = "Age"
                )})

            Checkbox(
                checked = isCompleted,
                onCheckedChange = { isCompleted = it }, modifier = Modifier.align(CenterHorizontally))

            Text(
                text = "${state.value.name} - ${state.value.age} - ${state.value.isCompleted}",
                modifier = modifier
                    .align(CenterHorizontally)
                    .padding(20.dp),
                maxLines = 5
            )

            Button(content = {
                Text(text = "Submit")
            },onClick = {
                viewModel.saveToProtoDataStore(name, age.toInt(),isCompleted)
            },modifier = modifier.align(CenterHorizontally))


        }

    }


}