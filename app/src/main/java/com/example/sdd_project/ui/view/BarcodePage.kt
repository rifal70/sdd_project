package com.example.sdd_project.ui.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdd_project.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BarcodePage(qrString: String) {
    var saldo by remember { mutableIntStateOf(0) }
    val barcodeBitmap = generateBarcodeBitmap(qrString)
    val dis = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tampilkan barcode
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.White)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.White)
            )
        }

        Text(
            text = "Rp $saldo",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Tombol untuk melakukan refresh saldo
        Button(
            onClick = {
                Log.d("TAG", "BarcodePage: " + barcodeBitmap.toString())
                saldo = readSaldoFromQRCode(qrString, dis)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Refresh Saldo")
        }
    }
}


@Composable
fun generateBarcodeBitmap(qrString: String): ImageBitmap? {
    return try {
        val writer = MultiFormatWriter()
        val bitMatrix: BitMatrix = writer.encode(qrString, BarcodeFormat.QR_CODE, 400, 400)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
            }
        }
        ImageBitmap(width, height, ImageBitmapConfig.Argb8888, hasAlpha = true)
    } catch (e: Exception) {
        // Handle exception
        null
    }
}

fun readSaldoFromQRCode(qrString: String, dis: Context): Int {
    val saldoRegex = Regex("""\b(\d+)\b""")
    val matchResult = saldoRegex.find(qrString)
    if (matchResult?.value != null){
        Toast.makeText(dis, "Berhasil update saldo. ${matchResult.value}", Toast.LENGTH_SHORT).show()
    }
    return matchResult?.value?.toIntOrNull() ?: 0
}
@Preview
@Composable
fun PreviewBarcodePage() {
    BarcodePage("BNI.ID12345678.MERCHANT MOCK TEST.50000")
}
