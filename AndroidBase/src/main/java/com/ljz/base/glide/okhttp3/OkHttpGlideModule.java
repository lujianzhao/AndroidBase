package com.ljz.base.glide.okhttp3;

import android.content.Context;

import com.ljz.base.common.utils.FileUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * A {@link GlideModule} implementation to replace Glide's default
 * {@link java.net.HttpURLConnection} based {@link com.bumptech.glide.load.model.ModelLoader}
 * with an OkHttp based {@link com.bumptech.glide.load.model.ModelLoader}.
 *
 * <p> If you're using gradle, you can include this module simply by depending on the aar, the
 * module will be merged in by manifest merger. For other build systems or for more more
 * information, see {@link GlideModule}. </p>
 */
public class OkHttpGlideModule implements GlideModule {
  @Override
  public void applyOptions(Context context, GlideBuilder builder) {

//    int deskacheize = 1024 * 1024 * 30;
    int maxMemory = (int)Runtime.getRuntime().maxMemory();
    int memoryCheSize = maxMemory / 8;
//    builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", deskacheize));
    builder.setDiskCache(new DiskLruCacheFactory(FileUtil.getCacheDir(),"glide", DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
    builder.setMemoryCache(new LruResourceCache(memoryCheSize));
    builder.setBitmapPool(new LruBitmapPool(memoryCheSize));
    builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
  }

  @Override
  public void registerComponents(Context context, Glide glide) {
     glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
  }

}
