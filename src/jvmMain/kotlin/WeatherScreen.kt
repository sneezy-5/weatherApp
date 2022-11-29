import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ui.FF076470
import ui.FFD8B753

@Composable
fun MainScreen(repository:Repository){

    var weatherState by remember { mutableStateOf<Lce<WeatherResults>?>(null) }
    val scope = rememberCoroutineScope()
    val searchViewModel = remember { SearchVewModel() }
    Column(modifier = Modifier.fillMaxSize().background(FF076470), horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Box(modifier = Modifier.padding(7.dp)){
                ConstomTextField(
                    leadingIcon = {

                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "",
                            tint = LocalContentColor.current.copy(alpha = 0.3f)
                        )
                    },
                    trailingIcon = {
                        IconButton(

                            onClick = {
                                searchViewModel.onSearchChanged("")
                            }){
                            Icon(imageVector = Icons.Filled.Close, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    searchViewModel = searchViewModel
                )
            }
            IconButton(

                onClick = {
                     weatherState = Lce.Loading
                    scope.launch {
                         weatherState = repository.weatherForCity(searchViewModel.search.value)
                    }
                }){
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, tint = Color.Green)
            }
        }

        when (val state = weatherState) {
            is Lce.Loading -> LoadingUI()
            is Lce.Error -> ErrorUI()
            is Lce.Content -> ContentUI(state.data)
        }
    }
}


@Composable
fun ConstomTextField(
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    searchViewModel:SearchVewModel
){
    val searchvalue by searchViewModel.search.collectAsState()
    BasicTextField(
        modifier=Modifier
            .width(700.dp)
            .height(30.dp),
        value = searchvalue ,
        onValueChange = {
            searchViewModel.onSearchChanged(it)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.secondary),
        decorationBox = {innerTextField ->

            Row(
                modifier=Modifier
                    .background(Color.LightGray, RoundedCornerShape(30)),
                    //.padding(top=16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (searchvalue.isEmpty()){
                    Text("Search")
                }
                    innerTextField()
                }
                if (searchvalue.isNotEmpty()){
                    if (trailingIcon != null) trailingIcon()
                }

            }

        }
    )
}


@Composable
fun WeatherImageAndDescription(){
    Box {
        Image(
            painter = painterResource("images/01d.png"),
            contentDescription = "sun logo",
            modifier = Modifier
                .size(300.dp)
                .padding(start=0.dp),
        )

    }

    Column(modifier = Modifier.padding(top=100.dp)) {
        Box(modifier = Modifier.padding(top=10.dp)
            //.background(Color.Yellow)
            .size(width = 180.dp, height = 30.dp)){
            Text(text = "25 C°", color = Color.White)
        }

        Box(modifier = Modifier.padding(top=5.dp)
            //.background(Color.Yellow)
            .size(width = 180.dp, height = 30.dp)){
            Text(text = "Nuageux", color = Color.White)
        }

    }
}



@Composable
fun ContentUI(data: WeatherResults) {
    var imageState by remember { mutableStateOf<ImageBitmap?>(null) }

  LaunchedEffect(data.currentWeather.iconUrl) {
        imageState = data.currentWeather.iconUrl?.let { ImageDownloader.downloadImage(it) }
    }

    Text(
        text = "Temperature actuelle",
        modifier = Modifier.padding(all = 16.dp),
        style = MaterialTheme.typography.h6,
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 72.dp),
        backgroundColor = Color.LightGray
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = data.currentWeather.condition,
                style = MaterialTheme.typography.h6,
            )

            imageState?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 128.dp, minHeight = 128.dp)
                        .padding(top = 8.dp)
                )
            }

            Text(
                text = "Temperature en°C: ${data.currentWeather.temperature}",
                modifier = Modifier.padding(all = 8.dp),
            )
            Text(
                text = "Feels like: ${data.currentWeather.feelsLike}",
                style = MaterialTheme.typography.caption,
            )
        }
    }

    Divider(
        color = Color.Yellow,
        modifier = Modifier.padding(all = 16.dp),
    )

    Text(
        text = "Prevusion",
        modifier = Modifier.padding(all = 16.dp),
        style = MaterialTheme.typography.h6,
    )
    LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        items(data.forecast.take(5)) { weatherCard ->
            ForecastUI(weatherCard)
        }
    }
}
@Composable
fun ForecastUI(weatherCard: WeatherCard) {
    var imageState by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(weatherCard.iconUrl) {
        imageState = weatherCard.iconUrl?.let { ImageDownloader.downloadImage(it) }
    }

    Card(
        modifier = Modifier
            .padding(all = 4.dp),
        backgroundColor = Color.LightGray
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = weatherCard.condition,
                style = MaterialTheme.typography.h6
            )

            imageState?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 64.dp, minHeight = 64.dp)
                        .padding(top = 8.dp)
                )
            }


            Text(
                text =  "Temperature: ${weatherCard.temperature}",
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

@Composable
fun ErrorUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Something went wrong, try again in a few minutes $. ¯\\_(ツ)_/¯",
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 72.dp, vertical = 72.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.error,
        )
    }
}

@Composable
fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color=Color.Green,
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .defaultMinSize(minWidth = 96.dp, minHeight = 96.dp)
        )
    }
}
class SearchVewModel {

    private val _search= MutableStateFlow<String>("")

    val search: StateFlow<String> = _search

    fun onSearchChanged(text: String) {
        _search.value = text
    }

}




