package com.example.first_app.di

import android.app.Application
import androidx.room.Room
import com.example.first_app.data.local.NewsDao
import com.example.first_app.data.local.NewsDatabase
import com.example.first_app.data.local.NewsTypeConvertor
import com.example.first_app.data.manager.LocalUserManagerImpl
import com.example.first_app.data.remote.NewsApi
import com.example.first_app.data.repository.NewsRepositoryImp
import com.example.first_app.domain.manager.LocalUserManager
import com.example.first_app.domain.repository.NewsRepository
import com.example.first_app.domain.usecase.app_entry.AppEntryUseCases
import com.example.first_app.domain.usecase.app_entry.ReadAppEntry
import com.example.first_app.domain.usecase.app_entry.SaveAppEntry
import com.example.first_app.domain.usecase.news.DeleteArticle
import com.example.first_app.domain.usecase.news.GetArticle
import com.example.first_app.domain.usecase.news.GetArticles
import com.example.first_app.domain.usecase.news.GetNews
import com.example.first_app.domain.usecase.news.NewsUseCases
import com.example.first_app.domain.usecase.news.SearchNews
import com.example.first_app.domain.usecase.news.UpsertArticle
import com.example.first_app.util.Constants.BASE_URL
import com.example.first_app.util.Constants.NEWS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class) // will be live till application is open
object AppModule {
//    @Provides
//    @Singleton
//    fun provideLocalUserManager(
//        application: Application  // takes context as parameter from Application class
//    ): LocalUserManager=LocalUserManagerImpl(context = application)
//
//    @Provides
//    @Singleton
//    fun provideAppEntryUseCases(
//        localUserManager: LocalUserManager
//    ): AppEntryUseCases = AppEntryUseCases(
//        readAppEntry = ReadAppEntry(localUserManager),
//        saveAppEntry = SaveAppEntry(localUserManager)
//    )


    @Provides
    @Singleton
    fun provideApiInstance(): NewsApi {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideNewsRepository(
//        newsApi: NewsApi
//    ): NewsRepository {
//        return NewsRepositoryImp(newsApi)
//    }

//    @Provides
//    @Singleton
//    fun provideNewsUseCases(
//        newsRepository: NewsRepository,
//        newsDao: NewsDao
//    ): NewsUseCases {
//        return NewsUseCases(
//            getNews = GetNews(newsRepository),
//            searchNews = SearchNews(newsRepository),
//            upsertArticle = UpsertArticle(newsDao),
//            deleteArticle = DeleteArticle(newsDao),
//            getArticles = GetArticles(newsDao),
//            getArticle = GetArticle(newsDao)
//        )
//    }

    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application
    ):NewsDatabase{
        return Room.databaseBuilder(
            context =  application,
            klass = NewsDatabase::class.java,
            name = NEWS_DATABASE_NAME
        ).addTypeConverter(NewsTypeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(
        newsDatabase: NewsDatabase
    ) = newsDatabase.newsDao

}