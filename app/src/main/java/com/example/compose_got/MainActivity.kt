package com.example.compose_got

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.FrameManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.state
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.ContextAmbient
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Image
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.compose_got.data.Episode
import com.example.compose_got.data.seriesService
import com.example.compose_got.ui.theme.Compose_GOTTheme
import androidx.compose.ui.graphics.Color as Color

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(seriesService) as T
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_GOTTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Episodes(episodesData: LiveData<List<Episode>>){
val episodes by episodesData.observeAsState(emptyList())
AdapterList(
    data = episodes
) { episode ->
    EpisodeItem(episode = episode)

}
}

@Composable
fun EpisodeItem(episode: Episode){
    Row {
        Column(
            modifier = Modifier.padding(8.dp, 16.dp)
        ) {
            val imageModifier = Modifier
                .heightIn(20.dp, 30.dp)
                .clip(shape = RoundedCornerShape(4.dp))

            GlideImage(url = episode.posterUrl, imageModifier = imageModifier)
        }
       Column (
           modifier = Modifier.padding(0.dp, 8.dp)
               ){
           Text(text = episode.title)
       }
    }
}

@Composable
fun GlideImage(url: String?, imageModifier: Modifier = Modifier) {
    val image = state<ImageAsset?>{null}
    
    val target = object : CustomTarget<Bitmap>(){
        override fun onLoadCleared(placeholder: Drawable?) {
            image.value = null 
        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            FrameManager.framed { 
                image.value = resource.asImageAsset()
            }
        }
    }
    
    Glide.with(ContextAmbient.current)
        .asBitmap()
        .load(url)
        .into(target)
    
    val theImage = image.value
    if (theImage !=null) {
        Image(
            asset = theImage,
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
            alignment = Alignment.CenterStart,
        )
    }
    }


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Compose_GOTTheme {
        Greeting("Android")
    }
}

