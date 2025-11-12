package com.example.duka.ui.family


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
// ADD THESE LINES


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.duka.R
import com.example.duka.ui.theme.DukaTheme


@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.duka_background),
            contentDescription = "Duka Logo",
            modifier = Modifier
                .size(500.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to Duka",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 28.sp
                ),
                textAlign = TextAlign.Companion.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your one-stop shop for everything you need. \nShop smart, shop easy!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Companion.Center
            )
        }


        Button(
            onClick = {navController.navigate("create_family")},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = "Get Started", fontSize = 18.sp, fontWeight = FontWeight.Companion.SemiBold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    DukaTheme {
        val navController = rememberNavController()
        WelcomeScreen(navController)
    }
}