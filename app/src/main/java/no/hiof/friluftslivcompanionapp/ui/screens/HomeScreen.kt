package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.friluftslivcompanionapp.ui.components.Carousel
import no.hiof.friluftslivcompanionapp.ui.components.Carousel2
import no.hiof.friluftslivcompanionapp.ui.components.items.cardItems
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun HomeScreen(userViewModel: UserViewModel = viewModel(), modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

/*

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(text = "Happening now")

        Row{
            Text(text = "Berries in Season!")
        }

        Text(text= "Trips in your Area" )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Carousel(cards = cardItems)
        }

        Text(text = "Sightings in your Area")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Carousel(cards = cardItems)

        item {
            Text(
                text = "Sightings in your area:",
                style = MaterialTheme.typography.titleSmall,

                )
            Carousel(cards = cardItems)
            Spacer(modifier = Modifier.height(16.dp))
        }



    }

    */
    Carousel2(cards = cardItems)




}




