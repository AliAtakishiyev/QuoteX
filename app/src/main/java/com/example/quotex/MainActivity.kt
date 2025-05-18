package com.example.quotex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.quotex.ui.theme.QuoteXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()


        //Create database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "quote-db"
        ).build()

        // DAO
        val dao = db.quoteDao()
        //Repository
        val repository = QuotesRepository(dao)

        val factory = QuoteViewModel.QuoteViewModelFactory(repository)
        val viewmodel = ViewModelProvider(this,factory)[QuoteViewModel::class.java]

        setContent {
            QuoteXTheme {
                MainQuotes(viewmodel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainQuotes(viewModel: QuoteViewModel) {

    val quotes by viewModel.quotes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDialogEdit by remember { mutableStateOf(false) }

    var quoteText by remember { mutableStateOf("") }
    var quoteTextEdit by remember  { mutableStateOf("") }

    var quoteID by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp, start = 4.dp, end = 4.dp)
    ) {
        LazyColumn {
            items(quotes){ quote ->
                Card (
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    ),
                    border = BorderStroke(1.dp, color = Color.Black),
                    modifier = Modifier.fillMaxWidth().height(150.dp).padding(4.dp)
                ){
                    Box(
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    ){

                    Text(
                        text = quote.quote,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            //.padding(end = 48.dp)
                            .fillMaxWidth(0.85f)
                            .wrapContentHeight()
                    )


                        IconButton(
                            onClick = {
                                viewModel.deleteQuote(quote)

                            },
                            modifier = Modifier.align(BottomEnd)
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Button")
                        }

                        IconButton(
                            onClick = {
                                showDialogEdit = true
                                quoteID = quote.id
                            },
                            modifier = Modifier.align(TopEnd)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Button")
                        }





                    }

                }

            }
        }

        //----------------------------------------------------------------------------- (Create fun for this block)
        if(showDialogEdit){
            BasicAlertDialog(
                onDismissRequest = { showDialogEdit = false },
            ){
                Surface (shape = RoundedCornerShape(5.dp)){
                    Column (
                        Modifier.padding(16.dp)
                    ){
                        OutlinedTextField(
                            value = quoteTextEdit,
                            onValueChange = {quoteTextEdit = it},
                            label = {Text("Edit Quote")}
                        )

                        Spacer(Modifier.height(8.dp))

                        TextButton(onClick = {
                            if(quoteTextEdit.isNotBlank()){
                                viewModel.updateQuote(Quote(quote = quoteTextEdit,id = quoteID))
                                quoteTextEdit = ""
                                showDialogEdit = false
                            }
                        },
                            modifier = Modifier.fillMaxWidth()
                            ) {
                            Text("Edit", color = Color.Black)
                        }
                    }
                }
            }


        }
//---------------------------------------------------------------------------------------------------------------------





       Button(
            onClick = {
                showDialog = true
            },
            shape = CircleShape,
            modifier = Modifier
                .align(alignment = BottomEnd)
                .padding(16.dp)
                .size(66.dp)
                .border(1.dp,Color.Black, RoundedCornerShape(32.dp)),
           colors = ButtonDefaults.buttonColors(
               contentColor = Color.Black,
               containerColor = Color.White
           )

        ) {
            Text(
                text = "+",
            )
        }

        if(showDialog){
            BasicAlertDialog(
                onDismissRequest = { showDialog = false },
            ){
                Surface (shape = RoundedCornerShape(5.dp)){
                    Column (
                        Modifier.padding(16.dp)
                    ){
                        OutlinedTextField(
                            value = quoteText,
                            onValueChange = {quoteText = it},
                            label = {Text("Enter Quote")}
                        )

                        Spacer(Modifier.height(8.dp))

                        TextButton(onClick = {
                            if(quoteText.isNotBlank()){
                                viewModel.addQuote(Quote(quote = quoteText))
                                quoteText = ""
                                showDialog = false
                            }
                        },
                            modifier = Modifier.fillMaxWidth()
                            ) {
                            Text("Add", color = Color.Black)
                        }
                    }
                }
            }


        }

    }


}
