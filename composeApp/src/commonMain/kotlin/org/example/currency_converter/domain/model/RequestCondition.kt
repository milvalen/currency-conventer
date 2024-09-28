package org.example.currency_converter.domain.model

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.util.reflect.typeInfo
import io.realm.kotlin.internal.platform.returnType
import kotlin.reflect.typeOf

sealed class RequestCondition<out T> {
    data object IdleCondition: RequestCondition<Nothing>()
    data object LoadingCondition: RequestCondition<Nothing>()

    data class SuccessCondition<out T>(val data: T): RequestCondition<T>()
    data class ErrorCondition(val message: String): RequestCondition<Nothing>()

    fun isLoading() = this is LoadingCondition
    fun isError() = this is ErrorCondition
    fun isSuccess() = this is SuccessCondition
    fun getSuccessInfo() = (this as SuccessCondition).data
    fun getErrorMessageInfo() = (this as ErrorCondition).message
}

@Composable
fun <T> RequestCondition<T>.ShowResult(
    animationTransitionSpecification: ContentTransform =
        scaleIn(tween(500)) + fadeIn(tween(900))
                togetherWith scaleOut(tween(500)) + fadeOut(tween(800)),
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable (String) -> Unit)? = null,
    onSuccess: @Composable (T) -> Unit
) {
    AnimatedContent(
        this,
        transitionSpec = { animationTransitionSpecification },
        label = "Animation"
    ) {
        state ->
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            when (state) {
                is RequestCondition.ErrorCondition -> onError?.invoke(state.getErrorMessageInfo())
                is RequestCondition.SuccessCondition -> onSuccess.invoke(state.getSuccessInfo())
                is RequestCondition.LoadingCondition -> onLoading?.invoke()
                is RequestCondition.IdleCondition -> onIdle?.invoke()
            }
        }
    }
}
