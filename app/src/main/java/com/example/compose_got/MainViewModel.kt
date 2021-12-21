package com.example.compose_got

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_got.data.Episode
import com.example.compose_got.data.SeriesService
import kotlinx.coroutines.launch

class MainViewModel(
    private val seriesService: SeriesService
): ViewModel() {

    private val _episodesData = MutableLiveData<List<Episode>>()
    val episodesData: LiveData<List<Episode>>
    get() = _episodesData

    fun getSeries() {
        viewModelScope.launch {
            try{
                val series = seriesService.getGotSeasonOne()
                for (episode in series.episodes){
                    val poster = seriesService.getPoster(episode.imdbID)
                    episode.posterUrl = poster.url
                }
                _episodesData.value = series.episodes
            } catch (e: Exception){
                Log.d("Service Error:", e.toString())
            }
        }
    }

}