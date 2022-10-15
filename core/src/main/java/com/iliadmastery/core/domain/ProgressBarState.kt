package com.iliadmastery.core.domain

/**
 * States available of Progress Bar
 */
sealed class ProgressBarState{
    
    object Loading: ProgressBarState()
    
    object Idle: ProgressBarState()
}