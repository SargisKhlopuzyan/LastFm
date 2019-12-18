package app.sargis.khlopuzyan.lastfm.di.annotation

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelKey(
    val value: KClass<out ViewModel>
)