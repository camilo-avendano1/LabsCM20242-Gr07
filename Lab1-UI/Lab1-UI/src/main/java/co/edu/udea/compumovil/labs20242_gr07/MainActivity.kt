package co.edu.udea.compumovil.labs20242_gr07

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.labs20242_gr07.ui.theme.Labs20242Gr07Theme
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20242Gr07Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PersonalData()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PersonalData() {

    /*TODO Scrollable*/
    var nameText by remember { mutableStateOf("") }

    var mRadioButtonSelected by remember { mutableStateOf(false) }
    var fRadioButtonSelected by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var dateLong by remember {
        mutableStateOf<Long?>(null)
    }
    var dateSelected = dateLong?.let {
        convertMillisToDate(it)
    } ?: "..."

    val educationStrings = arrayOf<String>(R.array.escolaridades.toString())

    var dropDownSelected by remember { mutableStateOf("") }
    var expandedDropDown by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, bottom = 42.dp, start = 4.dp, end = 4.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "Nombres", modifier = Modifier.padding(4.dp))
            Row {
                TextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            }
            Text(text = "Apellidos", modifier = Modifier.padding(4.dp))
            Row {
                TextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            }
            Row {
                Text(text = "Sexo: ", modifier = Modifier
                    .padding(4.dp, end = 8.dp)
                    .align(Alignment.CenterVertically))
                Text(
                    text = "Masculino",
                    modifier = Modifier
                        .padding(4.dp, end = 1.dp)
                        .align(Alignment.CenterVertically)
                )
                RadioButton(
                    selected = mRadioButtonSelected,
                    onClick = {
                        fRadioButtonSelected = false; mRadioButtonSelected = true
                    },
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "Femenino",
                    modifier = Modifier
                        .padding(4.dp, end = 1.dp)
                        .align(Alignment.CenterVertically)
                )
                RadioButton(
                    selected = fRadioButtonSelected,
                    onClick = {
                        fRadioButtonSelected = true; mRadioButtonSelected = false
                    },
                    modifier = Modifier.padding(4.dp)
                )
            }
            Row {
                Text(text = "Fecha de nacimiento: ", 
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = {
                        showDatePicker = true
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select a date")
                }
                Text(text = dateSelected, modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically))
            }
            Text(text = "Nivel de escolaridad", modifier = Modifier.padding(4.dp))                        

            // Spinner
            /* TODO
            Box (modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .padding(4.dp)){
                DropdownMenu(
                    expanded = expandedDropDown,
                    onDismissRequest = { expandedDropDown = false },
                    modifier = Modifier.padding(4.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Hola") },
                        onClick = { dropDownSelected = "Hola" },
                        modifier = Modifier.padding(4.dp)
                    )
                    repeat(educationStrings.size) {
                        DropdownMenuItem(
                            text = { Text(educationStrings[it]) },
                            onClick = { dropDownSelected = educationStrings[it] },
                            modifier = Modifier.padding(4.dp)
                        )

                    }
                }
            }
             */
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "Siguiente")
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = {
                dateLong = it
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format((millis))
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Labs20242Gr07Theme {
        PersonalData()
    }
}