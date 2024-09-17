package co.edu.udea.compumovil.labs20242_gr07

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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

        val educationStrings : Array<String> = resources.getStringArray(R.array.educationDegrees)
        val resources = resources


        val countryApi = CountryApi

        setContent {
            Labs20242Gr07Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    countryApi.getCountries()
                    val countryStrings = getCountriesName(countryApi.getCountries())
                    SelectView(resources = resources, educationStrings = educationStrings, countryStrings = countryStrings)
                }
            }
        }
    }
}


fun getCountriesName(countries: List<Country>): Array<String>{
    var aux:Array<String> = emptyArray()
    for (country in countries){
        aux += country.name
    }
    aux.sort()
    return aux
}

fun getCitiesName(cities: List<City>): Array<String>{
    var aux:Array<String> = emptyArray()
    for (city in cities){
        aux += city.name
    }
    aux.sort()
    return aux
}


@Composable
fun SelectView(resources: Resources, educationStrings: Array<String>, countryStrings: Array<String>){
    var view by rememberSaveable { mutableIntStateOf(0) }
    when(view){
        0 -> PersonalData(resources,educationStrings, nextButton =  { view = 1 })
        1 -> ContactInformation(resources,countryStrings, nextButton = { view = 2 })
        else -> Debug( firstButton = {view=0} , secondButton = {view=1} )
    }
}


@Composable
fun PersonalData(resources: Resources, arrayItems: Array<String>, nextButton: () -> Unit) {
    val configuration = LocalConfiguration.current // Detectar la orientación
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val scrollState = rememberScrollState()

    var nameText by rememberSaveable { mutableStateOf("") }
    var surnameText by rememberSaveable { mutableStateOf("") }
    var mRadioButtonSelected by rememberSaveable { mutableStateOf(false) }
    var fRadioButtonSelected by rememberSaveable { mutableStateOf(false) }

    var sex by remember { mutableStateOf("") }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var dateLong by rememberSaveable { mutableStateOf<Long?>(null) }
    val dateSelected = dateLong?.let { convertMillisToDate(it) } ?: ""

    var dropDownSelected by rememberSaveable { mutableStateOf("") }
    var expandedDropDown by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var surnameError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var futureDateError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, bottom = 42.dp, start = 4.dp, end = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .verticalScroll(scrollState)
        ) {
            // Campos de Nombre y Apellido
            if (isLandscape) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Campo Nombre
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)) {
                        Text(text = resources.getString(R.string.name), modifier = Modifier.padding(4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = resources.getString(R.string.name),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            TextField(
                                value = nameText,
                                onValueChange = { nameText = it },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next,
                                    capitalization = KeyboardCapitalization.Words
                                ),
                                isError = nameError
                            )
                        }
                        if (nameError) {
                            Text(resources.getString(R.string.nameError), color = MaterialTheme.colorScheme.error)
                        }
                    }

                    // Campo Apellido
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)) {
                        Text(text = resources.getString(R.string.surname), modifier = Modifier.padding(4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AccountBox,
                                contentDescription = resources.getString(R.string.name),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            TextField(
                                value = surnameText,
                                onValueChange = { surnameText = it },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done,
                                    capitalization = KeyboardCapitalization.Words
                                ),
                                isError = surnameError
                            )
                        }
                        if (surnameError) {
                            Text(resources.getString(R.string.surnameError), color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            } else {
                // Vertical (uno debajo del otro)
                Text(text = resources.getString(R.string.name), modifier = Modifier.padding(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = resources.getString(R.string.name),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    TextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        isError = nameError
                    )
                }
                if (nameError) {
                    Text(resources.getString(R.string.nameError), color = MaterialTheme.colorScheme.error)
                }

                Text(text = resources.getString(R.string.surname), modifier = Modifier.padding(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = resources.getString(R.string.name),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    TextField(
                        value = surnameText,
                        onValueChange = { surnameText = it },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        isError = surnameError
                    )
                }
                if (surnameError) {
                    Text(resources.getString(R.string.surnameError), color = MaterialTheme.colorScheme.error)
                }
            }

            // Campos adicionales como Sexo, Fecha de Nacimiento, etc.
            Row {
                // Icono para la palabra "Sexo"
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder, // Puedes cambiarlo por otro ícono que represente "sexo"
                    contentDescription = resources.getString(R.string.sex),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically)
                )

                // Texto para "Sexo"
                Text(
                    text = resources.getString(R.string.sex),
                    modifier = Modifier
                        .padding(4.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Row {
                // Texto para género masculino
                Text(
                    text = resources.getString(R.string.male),
                    modifier = Modifier
                        .padding(4.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)
                )

                // RadioButton para masculino
                RadioButton(
                    selected = mRadioButtonSelected,
                    onClick = {
                        fRadioButtonSelected = false
                        mRadioButtonSelected = true
                    },
                    modifier = Modifier.padding(4.dp)
                )

                // Texto para género femenino
                Text(
                    text = resources.getString(R.string.female),
                    modifier = Modifier
                        .padding(4.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)
                )

                // RadioButton para femenino
                RadioButton(
                    selected = fRadioButtonSelected,
                    onClick = {
                        fRadioButtonSelected = true
                        mRadioButtonSelected = false
                    },
                    modifier = Modifier.padding(4.dp)
                )
            }




            Row {
                Text(text = resources.getString(R.string.dob),
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = resources.getString(R.string.selectDate))
                }
                Text(text = dateSelected, modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically))
            }

            if (dateError) {
                Text(resources.getString(R.string.dateError), color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
            } else if (futureDateError) {
                Text(resources.getString(R.string.dateValidError), color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = resources.getString(R.string.education),
                    modifier = Modifier.padding(4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    Text(
                        text = dropDownSelected.ifEmpty { resources.getString(R.string.selectOption) },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.CenterStart)
                            .clickable { expandedDropDown = !expandedDropDown }
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp)) // Añadir borde
                            .background(Color.White) // Fondo blanco para el texto
                            .padding(8.dp) // Añadir relleno para un área clicable más grande
                    )
                    DropdownMenu(
                        expanded = expandedDropDown,
                        onDismissRequest = { expandedDropDown = false },
                        modifier = Modifier.heightIn(max = 200.dp)
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
                nameError = nameText.isEmpty()
                surnameError = surnameText.isEmpty()
                dateError = dateSelected.isEmpty()
                futureDateError = if (dateError) {
                    true
                } else {
                    dateLong!! > System.currentTimeMillis()
                }
                if (!nameError && !surnameError && !dateError && !futureDateError) {
                    sex = if (mRadioButtonSelected)
                        (resources.getString(R.string.male))
                    else if (fRadioButtonSelected)
                        (resources.getString(R.string.female))
                    else
                        (resources.getString(R.string.sexNotGiven))

                    Log.d(
                        "PersonalData",
                        "Name: $nameText, \n" +
                                "Surname: $surnameText,\n" +
                                "Sex: $sex, \n" +
                                "DoB: $dateSelected, \n" +
                                "Education degree: $dropDownSelected"
                    )
                    nextButton()
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = resources.getString(R.string.next))
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            resources = resources,
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
    resources: Resources,
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
                Text(resources.getString(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(resources.getString(R.string.cancel))
            }
        }
    )
    {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format((millis))
}


@Composable
fun ContactInformation(resources: Resources, countryArray: Array<String>, nextButton: () -> Unit) {

    var cityStrings by remember {mutableStateOf(arrayOf<String>())}

    val scrollState = rememberScrollState()
    val countryScroll = rememberScrollState()
    val cityScroll = rememberScrollState()


    var phoneNum by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    var countrySelected by rememberSaveable { mutableStateOf("") }
    var citySelected by rememberSaveable { mutableStateOf("") }

    var countryExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }


    var phoneNumError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var countryError by remember { mutableStateOf(false) }
    var emailValidError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, bottom = 42.dp, start = 4.dp, end = 4.dp)
    ) {
        Column(modifier = Modifier
            .padding(4.dp)
            .verticalScroll(scrollState)){
            Row {
                Text(text = resources.getString(R.string.phone),
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically)
                )
                TextField(
                    value = phoneNum,
                    onValueChange = { phoneNum = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )
            }
            if (phoneNumError){
                Text(resources.getString(R.string.phoneError), color = MaterialTheme.colorScheme.error)
            }
            Row {
                Text(text = resources.getString(R.string.address),
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically)
                )
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
            }
            Row {
                Text(text = resources.getString(R.string.email),
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically)
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    )
                )
            }
            if (emailError){
                Text(resources.getString(R.string.emailError), color = MaterialTheme.colorScheme.error)
            } else if(emailValidError){
                Text(resources.getString(R.string.emailValidError), color = MaterialTheme.colorScheme.error)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {
                Text(text = resources.getString(R.string.country), modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = countrySelected.ifEmpty { resources.getString(R.string.selectOption) },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                            .clickable { countryExpanded = !countryExpanded }
                    )
                    DropdownMenu(
                        expanded = countryExpanded,
                        onDismissRequest = { countryExpanded = false },
                        modifier = Modifier.heightIn(max=200.dp),
                        scrollState = countryScroll
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
            if (countryError){
                Text(resources.getString(R.string.countryError), color = MaterialTheme.colorScheme.error)
            }
            if (countrySelected.isNotEmpty()){
                val countryApi = CountryApi
                var selCountry = countryApi.getCca2(country = countrySelected)
                Log.d("pedro", selCountry.cca2)
                val cityApi = CityApi
                cityStrings = getCitiesName(cityApi.getCities(cca2 = selCountry.cca2))
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {
                Text(text = resources.getString(R.string.city), modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = citySelected.ifEmpty { resources.getString(R.string.selectOption) },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                            .clickable { cityExpanded = !cityExpanded }
                    )
                    DropdownMenu(
                        expanded = cityExpanded,
                        onDismissRequest = { cityExpanded = false },
                        modifier = Modifier.heightIn(max=200.dp),
                        scrollState = cityScroll
                    ) {
                        cityStrings.forEach { item ->
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
                phoneNumError = phoneNum.isEmpty()
                emailValidError = !isValidEmail(email)
                emailError = email.isEmpty()
                countryError = countrySelected.isEmpty()
                if (!phoneNumError && !emailError && !countryError && !emailValidError){
                    Log.d("Contact Information",
                        "Phone: $phoneNum, \n" +
                                "Address: $address,\n" +
                                "Email: $email, \n" +
                                "Country: $countrySelected, \n" +
                                "City: $citySelected")

                    nextButton()
                }
            },
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = resources.getString(R.string.next))
        }
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailRegex)
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