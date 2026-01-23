package com.yolbertdev.coffeeplatform.di

import com.yolbertdev.coffeeplatform.data.database.dao.CustomerDao
import com.yolbertdev.coffeeplatform.data.database.repository.CustomerRepositoryImpl
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository
import com.yolbertdev.coffeeplatform.domain.usecase.InsertCustomerUseCase
import com.yolbertdev.coffeeplatform.domain.usecase.SelectAllCustomerUseCase
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.CustomerScreenModel
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeScreenModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    single<CoffeeDatabase> { CoffeeDatabase(get()) }
    single<CustomerDao> { CustomerDao(get()) }
    single<HomeScreenModel> { HomeScreenModel() }
    single<CustomerScreenModel>{CustomerScreenModel(get(), get())}
    single<InsertCustomerUseCase>{InsertCustomerUseCase(get())}
    single<SelectAllCustomerUseCase>{SelectAllCustomerUseCase(get())}
    singleOf(::CustomerRepositoryImpl) bind CustomerRepository::class
}