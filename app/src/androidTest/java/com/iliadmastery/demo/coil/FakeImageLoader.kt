package com.iliadmastery.demo.coil

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.ComponentRegistry
import coil.ImageLoader
import coil.decode.DataSource
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.*
import kotlinx.coroutines.Deferred


class FakeImageLoader {
    companion object Factory {
        fun build(context: Context): ImageLoader {
            return object : ImageLoader {

                private val disposable = object : Disposable {
                    override val isDisposed: Boolean
                        get() = true
                    override val job: Deferred<ImageResult>
                        get() = throw UnsupportedOperationException()

                    override fun dispose() {}

                }

                override val components: ComponentRegistry
                    get() = throw UnsupportedOperationException()
                override val defaults: DefaultRequestOptions
                    get() = DefaultRequestOptions()
                override val diskCache: DiskCache
                    get() = throw UnsupportedOperationException()

                // Optionally, you can add a custom fake memory cache implementation.
                override val memoryCache: MemoryCache
                    get() = throw UnsupportedOperationException()


                override fun enqueue(request: ImageRequest): Disposable {
                    // Always call onStart before onSuccess.
                    request.target?.onStart(placeholder = ColorDrawable(Color.BLACK))
                    request.target?.onSuccess(result = ColorDrawable(Color.BLACK))
                    return disposable
                }

                override suspend fun execute(request: ImageRequest) = SuccessResult(
                    drawable = ColorDrawable(Color.BLACK),
                    request = request,
                    memoryCacheKey = MemoryCache.Key(""),
                    isSampled = false,
                    dataSource = DataSource.MEMORY_CACHE,
                    isPlaceholderCached = false
                )

                override fun newBuilder() = ImageLoader.Builder(context)

                override fun shutdown() = Unit

            }
        }
    }
}