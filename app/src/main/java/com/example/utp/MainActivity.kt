package com.example.utp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

val DarkNavy = Color(0xFF0D3B45)
val LightBg = Color(0xFFF4F7F7)
val CreamBg = Color(0xFFFCF7F1)
val SuccessGreen = Color(0xFF4CAF50)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                AppNavigation(navController)
            }
        }
    }
}

data class Barang(
    val harga: String,
    val nama: String,
    val deskripsi: String,
    val kategori: String,
    val imageRes: Int,
    val lokasi: String = "Sleman, Yogyakarta"
)

val dummyBarang = listOf(
    Barang("Rp 55.000.000", "Honda CBR 250RR", "Tahun 2022, KM Rendah, Surat Lengkap", "Motor", R.drawable.motor1),
    Barang("Rp 165.000.000", "Toyota Avanza G", "Tahun 2018, Mesin Halus, Pajak Hidup", "Mobil", R.drawable.mobil1),
    Barang("Rp 25.000.000", "Yamaha R15 V3", "Tahun 2020, Body Mulus, Siap Pakai", "Motor", R.drawable.motor2),
    Barang("Rp 130.000.000", "Honda Brio Satya", "Tahun 2019, AC Dingin, Ban Baru", "Mobil", R.drawable.mobil2),
    Barang("Rp 2.500.000", "Polygon Cascade", "Kondisi 95%, Rem Pakem, Lecet Pemakaian", "Sepeda", R.drawable.sepeda1)
)

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { BahasaApp(navController) }
        composable("detail/{nama}") { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            val barang = dummyBarang.find { it.nama == nama }
            if (barang != null) DetailScreen(barang, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BahasaApp(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BARKAS", fontWeight = FontWeight.ExtraBold, color = DarkNavy) },
                modifier = Modifier.shadow(4.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(LightBg),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Rekomendasi Pilihan", fontWeight = FontWeight.Bold, color = DarkNavy)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(dummyBarang) { item -> ItemRekomendasi(item, navController) }
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Seluruh Barang Bekas", fontWeight = FontWeight.Bold, color = DarkNavy)
            }
            items(dummyBarang) { item -> ItemListHorizontal(item, navController) }
        }
    }
}

@Composable
fun ItemRekomendasi(barang: Barang, navController: NavController) {
    Card(
        modifier = Modifier.width(180.dp).clickable { navController.navigate("detail/${barang.nama}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                Image(painterResource(barang.imageRes), null, Modifier.fillMaxWidth().height(110.dp), contentScale = ContentScale.Crop)
                Icon(Icons.Outlined.FavoriteBorder, null, tint = Color.Red, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(20.dp))
            }
            Column(Modifier.padding(10.dp)) {
                Text(barang.harga, fontWeight = FontWeight.ExtraBold, color = DarkNavy, fontSize = 14.sp)
                Text(barang.nama, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ItemListHorizontal(barang: Barang, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("detail/${barang.nama}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(barang.imageRes), null, Modifier.size(90.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(barang.harga, fontWeight = FontWeight.ExtraBold, color = DarkNavy, fontSize = 16.sp)
                Text(barang.nama, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, Modifier.size(14.dp), tint = Color.LightGray)
                    Text(barang.lokasi, fontSize = 11.sp, color = Color.LightGray)
                }
            }
            Icon(imageVector = if (barang.nama.contains("CBR")) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, null, tint = if (barang.nama.contains("CBR")) Color.Red else Color.LightGray, modifier = Modifier.align(Alignment.Top).padding(top = 4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(barang: Barang, navController: NavController) {
    var isFavorite by remember { mutableStateOf(false) }
    var isBought by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detail Barang", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp).navigationBarsPadding()) {
                Button(
                    onClick = {
                        if (!isBought) {
                            isBought = true
                            scope.launch {
                                snackbarHostState.showSnackbar("Saya tertarik membeli ${barang.nama}")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBought) SuccessGreen else DarkNavy
                    )
                ) {
                    Text(
                        text = if (isBought) "DIBELI" else "Beli",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(
                    painter = painterResource(barang.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else DarkNavy,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamBg)
                    .padding(24.dp)
            ) {
                Text(
                    text = barang.harga,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Text(
                    text = barang.nama,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Text("Deskripsi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(barang.deskripsi, fontSize = 15.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Lokasi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(barang.lokasi, fontSize = 15.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
