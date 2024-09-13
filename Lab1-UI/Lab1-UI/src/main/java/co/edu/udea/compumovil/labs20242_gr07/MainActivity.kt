package co.edu.udea.compumovil.labs20242_gr07

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

        val educationStrings : Array<String> = resources.getStringArray(R.array.escolaridades)

        val countryStrings : Array<String> = arrayOf("A","B","C")
        val cityStrings : Array<String> = arrayOf("a","b","c")

        setContent {
            Labs20242Gr07Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SelectView(educationStrings = educationStrings, countryStrings = countryStrings, cityStrings = cityStrings)
                }
            }
        }
    }
}

@Composable
fun SelectView(educationStrings: Array<String>, countryStrings: Array<String>, cityStrings: Array<String>){
    var view by remember { mutableIntStateOf(0) }
    when(view){
        0 -> PersonalData(educationStrings, nextButton =  { view = 1 })
        1 -> ContactInformation(countryStrings,cityStrings, nextButton = { view = 2 })
        else -> Debug( firstButton = {view=1} , secondButton = {view=2} )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PersonalData(arrayItems: Array<String>, nextButton: () -> Unit) {
    
    /*TODO Scrollable*/
    var nameText by rememberSaveable { mutableStateOf("") }
    var surnameText by rememberSaveable { mutableStateOf("") }
    var mRadioButtonSelected by rememberSaveable { mutableStateOf(false) }
    var fRadioButtonSelected by rememberSaveable { mutableStateOf(false) }



    var sex by remember {mutableStateOf("")}
    if(mRadioButtonSelected){
        sex = "Male"
    }
    if (fRadioButtonSelected){
        sex = "Female"
    }
    else{
        sex = "Unknown"
    }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var dateLong by rememberSaveable {
        mutableStateOf<Long?>(null)
    }
    val dateSelected = dateLong?.let {
        convertMillisToDate(it)
    } ?: "..."
    
    var dropDownSelected by rememberSaveable { mutableStateOf("") }
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
                    value = surnameText,
                    onValueChange = { surnameText = it },
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
                Text(text = dateSelected, modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically))
            }

// Spinner en Jetpack Compose usando DropdownMenu
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Nivel de escolaridad", modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    Text(
                        text = dropDownSelected.ifEmpty { "Seleccione una opción" },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.CenterStart)
                            .clickable { expandedDropDown = !expandedDropDown }
                    )
                    DropdownMenu(
                        expanded = expandedDropDown,
                        onDismissRequest = { expandedDropDown = false }
                    ) {
                        arrayItems.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    dropDownSelected = item
                                    expandedDropDown = false
                                }
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                Log.d("PersonalData",
                    "Name: $nameText, \n" +
                            "Surname: $surnameText,\n" +
                            "Sex: $sex, \n" +
                            "DoB: $dateSelected, \n" +
                            "Education degree: $dropDownSelected")
                nextButton()
            },
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

@Composable
fun ContactInformation(countryArray: Array<String>, cityArray: Array<String>, nextButton: () -> Unit) {

    var phoneNum by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    var countrySelected by rememberSaveable { mutableStateOf("") }
    var citySelected by rememberSaveable { mutableStateOf("") }

    var countryExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, bottom = 42.dp, start = 4.dp, end = 4.dp)
    ) {
        Column{
            Row {
                Text(text = "Telefono: ", modifier = Modifier.padding(4.dp))
                TextField(
                    value = phoneNum,
                    onValueChange = { phoneNum = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                )
            }
            Row {
                Text(text = "Direccion: ", modifier = Modifier.padding(4.dp))
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            }
            Row {
                Text(text = "Email: ", modifier = Modifier.padding(4.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            }
            Row(modifier = Modifier.wrapContentSize()) {
                Text(text = "Pais", modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = countrySelected.ifEmpty { "Seleccione una opción" },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                            .clickable { countryExpanded = !countryExpanded }
                    )
                    DropdownMenu(
                        expanded = countryExpanded,
                        onDismissRequest = { countryExpanded = false }
                    ) {
                        countryArray.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    countrySelected = item
                                    countryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Row(modifier = Modifier.wrapContentSize()) {
                Text(text = "Ciudad", modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = citySelected.ifEmpty { "Seleccione una opción" },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                            .clickable { cityExpanded = !cityExpanded }
                    )
                    DropdownMenu(
                        expanded = cityExpanded,
                        onDismissRequest = { cityExpanded = false }
                    ) {
                        cityArray.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    citySelected = item
                                    cityExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                Log.d("Contact Information",
                    "Phone: $phoneNum, \n" +
                            "Address: $address,\n" +
                            "Email: $email, \n" +
                            "Country: $countrySelected, \n" +
                            "City: $citySelected")
                nextButton()
            },
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "Siguiente")
        }
    }
}

@Composable
fun Debug(firstButton: () -> Unit, secondButton: ()-> Unit){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 28.dp, bottom = 42.dp, start = 4.dp, end = 4.dp)
    ){
        Column (modifier = Modifier
            .padding(4.dp)
            .align(Alignment.Center)){
            Button(onClick = { firstButton() }) {
                Text(text = "PersonalData")
            }
            Button(onClick = { secondButton() }) {
                Text(text = "ContactInformation")
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    Labs20242Gr07Theme {

    }
}