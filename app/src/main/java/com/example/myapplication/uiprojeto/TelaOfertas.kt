package com.example.myapplication.uiprojeto




import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri


data class CashbackItemData(
    val percentage: String,
    val description: String,
    val imageResId: Int,
    val url: String
)




data class ProductItemData(
    val title: String,
    val originalPrice: String,
    val currentPrice: String,
    val imageResId: Int,
    val url: String
)




@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun TelaOfertas() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SecaoCentralO()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview(showBackground = true)
@Composable
fun TelaOfertasPreview() {
    TelaOfertas()
}


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun SecaoCentralO() {
    // Variáveis de estado para o texto do campo de busca e o histórico
    var searchText by remember { mutableStateOf("") }
    val recentSearches = remember { mutableStateListOf<String>() }
    val keyboardController = LocalSoftwareKeyboardController.current


    val cashbackItems = listOf(
        CashbackItemData("11%", "de cashback", R.drawable.vivara, "https://www.vivara.com.br"),
        CashbackItemData("10%", "de cashback", R.drawable.life, "https://www.vivara.com.br"),
        CashbackItemData("9%", "de cashback", R.drawable.kipling, "https://www.kipling.com.br"),
        CashbackItemData("12%", "de cashback", R.drawable.lor, "https://www.cafelor.com.br/?srsltid=AfmBOooO4eiVk2EyPlSrVAD6m01KlqVUnotNRGpYB6Ts_c5Z4hCf5iZe"),
        CashbackItemData("15%", "de cashback", R.drawable.evino, "https://www.evino.com.br"),
        CashbackItemData("8%", "de cashback", R.drawable.dolce, "https://www.dolcegusto.com.br"),
        CashbackItemData("20%", "de cashback", R.drawable.fotoregistro, "https://www.fotoregistro.com.br")
    )
    val productItems = listOf(
        ProductItemData(
            "Curso de Finances com Julius",
            "R$ 1.099,00",
            "R$ 599,00 à vista",
            R.drawable.julius,
            "https://www.ev.org.br/trilhas-de-conhecimento/financas"
        ),
        ProductItemData(
            "E-Book de Economia Avançado - Julius",
            "R$ 159,90",
            "R$ 91,90 à vista",
            R.drawable.economia,
            "https://www.amazon.com.br/Economia-Avan%C3%A7ada-Tomislav-R-Femenick/dp/6556058149?source=ps-sl-shoppingads-lpcontext&ref_=fplfs&psc=1&smid=A1ZZFT5FULY4LN"
        ),
        ProductItemData(
            "Uniforme Julius",
            "R$ 200,00",
            "R$ 150,00",
            R.drawable.unifrome,
            "https://www.amazon.com.br/Fantasia-Macac%C3%A3o-Julius-Mundo-Cosplay/dp/B0C1HBQ6TM"
        )
    )


    // Variável de estado para controlar a visibilidade das ofertas especiais
    var showSpecialOffers by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // ... (Código da barra de pesquisa e carrosséis) ...
        var isSearchFocused by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("O que você procura?") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de busca"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isSearchFocused = focusState.isFocused
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchText.isNotBlank()) {
                        if (!recentSearches.contains(searchText)) {
                            recentSearches.add(0, searchText)
                        }
                        if (recentSearches.size > 5) {
                            recentSearches.removeLast()
                        }
                        searchText = ""
                        keyboardController?.hide()
                    }
                }
            )
        )

// Seção para mostrar as pesquisas recentes
        if (recentSearches.isNotEmpty()) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Pesquisas Recentes",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.height(70.dp)
            ) {
                items(recentSearches) { searchItem ->
                    Text(
                        text = searchItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                searchText = searchItem
                            }
                            .padding(vertical = 4.dp),
                        color = Color.Gray
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        // --- Carrossel de Cashback ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            cashbackItems.forEach { item ->
                CashbackItem(item)
            }
        }


        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Julius Shop indica! ❤️",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            productItems.forEach { item ->
                ProductCard(item)
            }
        }


        Spacer(modifier = Modifier.height(24.dp))


        // Botão para mostrar/esconder o conteúdo
        Button(
            onClick = { showSpecialOffers = !showSpecialOffers }, // Inverte o valor da variável de estado
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(
                text = if (showSpecialOffers) "Mostrar menos" else "Mostrar mais", // Muda o texto do botão
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.height(24.dp))


        // Conteúdo condicional que só será exibido se showSpecialOffers for true
        if (showSpecialOffers) {
            Text(
                text = "Ofertas Especiais",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.meme),
                contentDescription = "Banner de Oferta Especial",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://www.condor.com.br/".toUri() // Link para a oferta
                        )
                        context.startActivity(intent)
                    },
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CashbackItem(data: CashbackItemData) {
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .clickable {
                // Ação ao clicar no item inteiro
                val intent = Intent(Intent.ACTION_VIEW, data.url.toUri())
                context.startActivity(intent)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = data.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
        )
        Text(text = "SUPER", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(text = "CASHBACK", color = Color.Gray, fontSize = 10.sp)
        Text(text = "Até ${data.percentage}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = data.description, fontSize = 12.sp, color = Color.Gray)
    }
}




@Composable
fun ProductCard(data: ProductItemData) {
    val context = LocalContext.current


    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable {
                // Ação: Abrir o link no navegador
                val intent = Intent(Intent.ACTION_VIEW, data.url.toUri())
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = data.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = data.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = data.originalPrice, color = Color.Gray, fontSize = 12.sp)
            Text(text = data.currentPrice, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}


