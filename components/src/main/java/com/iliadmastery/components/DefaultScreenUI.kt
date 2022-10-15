package com.iliadmastery.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.Queue
import com.iliadmastery.core.domain.UIComponent

/**
 * @param queue: Dialogs
 * @param content: The content of the UI.
 */
@Composable
fun DefaultScreenUI(
    queue: Queue<UIComponent> = Queue(mutableListOf()),
    onRemoveHeadFromQueue: () -> Unit,
    progressBarState: ProgressBarState = ProgressBarState.Idle,
    content: @Composable () -> Unit,
){
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ){
            content()
            // process the queue
            if(!queue.isEmpty()){
                queue.peek()?.let { uiComponent ->
                    if(uiComponent is UIComponent.Dialog){
                        GenericDialog(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                            ,
                            title = uiComponent.title,
                            description = uiComponent.description,
                            onRemoveHeadFromQueue = onRemoveHeadFromQueue
                        )
                    }
                }
            }

            if(progressBarState is ProgressBarState.Loading){
                CircularIndeterminateProgressBar()
            }
        }
    }
}











